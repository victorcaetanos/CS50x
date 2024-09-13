import os
from datetime import datetime
import sys
from cs50 import SQL
from flask import Flask, flash, redirect, render_template, request, session
from flask_session import Session
from werkzeug.security import check_password_hash, generate_password_hash

from helpers import apology, login_required, lookup, usd

# Configure application
app = Flask(__name__)

# Custom filter
app.jinja_env.filters["usd"] = usd

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///finance.db")


@app.after_request
def after_request(response):
    """Ensure responses aren't cached"""
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response


def filtering(symbol):
    return symbol["stock_symbol"]


@app.route("/", methods=["GET", "POST"])
@login_required
def index():

    if request.method == "POST":
        symbols = db.execute(
            "SELECT DISTINCT stock_symbol FROM transactions WHERE user_id = ?", session["user_id"])
        # A really weird line of code that I searched around to make it smaller and ended up making it harder to read
        if not request.form.get("symbol") in list(map(lambda x: x["stock_symbol"], symbols)):
            return apology("shares not owned", 400)
        elif request.form.get("action") == "buy":
            return buyFunc()
        elif request.form.get("action") == "sell":
            return sellFunc()
    else:
        cash = db.execute("SELECT * FROM users WHERE id = ?", session["user_id"])[0]["cash"]

        stocks = db.execute("SELECT stock_symbol, SUM(shares) as shares FROM transactions WHERE user_id = ? " +
                            "GROUP BY stock_symbol HAVING SUM(shares) > 0", session["user_id"])
        i = 0
        stock_sum = 0
        for stock in stocks:
            cur_price = lookup(stock["stock_symbol"])["price"]
            stocks[i]["price"] = cur_price
            stocks[i]["total"] = stocks[i]["shares"] * cur_price
            stock_sum += stocks[i]["total"]
            i += 1

        total = stock_sum + cash
        return render_template("index.html", stocks=stocks, cash=cash, total=total)


@app.route("/buy", methods=["GET", "POST"])
@login_required
def buy():
    if request.method == "POST":
        return buyFunc()
    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("buy.html")


@app.route("/history")
@login_required
def history():
    """Show history of transactions"""
    stocks = db.execute(
        "SELECT stock_symbol, shares, price, time FROM transactions WHERE user_id = ? ", session["user_id"])

    return render_template("history.html", stocks=stocks)


@app.route("/login", methods=["GET", "POST"])
def login():
    """Log user in"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":
        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        # Query database for username
        rows = db.execute(
            "SELECT * FROM users WHERE username = ?", request.form.get("username")
        )

        # Ensure username exists and password is correct
        if len(rows) != 1 or not check_password_hash(
            rows[0]["hash"], request.form.get("password")
        ):
            return apology("invalid username and/or password", 403)

        # Remember which user has logged in
        session["user_id"] = rows[0]["id"]

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("login.html")


@app.route("/logout")
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    # Redirect user to login form
    return redirect("/")


@app.route("/quote", methods=["GET", "POST"])
@login_required
def quote():
    if request.method == "POST":
        symbol = request.form.get("symbol")
        stock = lookup(symbol)

        # checking user input
        if not symbol:
            return apology("missing symbol", 400)
        elif not stock:
            return apology("invalid symbol", 400)

        return render_template("quoted.html", stock=stock)

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("quote.html")


@app.route("/register", methods=["GET", "POST"])
def register():
    if request.method == "POST":
        username = request.form.get("username")
        password = request.form.get("password")
        conf_password = request.form.get("confirmation")
        userCount = db.execute("SELECT COUNT(*) FROM users WHERE username = ?",
                               username)[0]["COUNT(*)"]

        print(f'{password}, {type(password)}', file=sys.stderr)
        print(f'{conf_password}, {type(conf_password)}', file=sys.stderr)

        # checking user input
        if not username:
            return apology("missing username", 400)
        elif userCount == 1:
            return apology("username already exists", 400)
        elif not password:
            return apology("missing password", 400)
        elif not password == conf_password:
            return apology("passwords don't match", 400)

        # Inserts username and hash into the database
        db.execute("INSERT INTO users (username, hash) VALUES (?, ?)",
                   username, generate_password_hash(password))

        # saves user's id in session
        id = db.execute("SELECT id FROM users WHERE username = ?", username)[0]["id"]
        session["user_id"] = id

        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("register.html")


@app.route("/sell", methods=["GET", "POST"])
@login_required
def sell():
    """Sell shares of stock"""

    if request.method == "POST":
        return sellFunc()

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        symbols = db.execute("SELECT stock_symbol, SUM(shares) as shares FROM transactions WHERE user_id = ? " +
                             "GROUP BY stock_symbol HAVING SUM(shares) > 0", session["user_id"])
        return render_template("sell.html", symbols=symbols)


def buyFunc():
    symbol = request.form.get("symbol")
    shares = request.form.get("shares")

    # checking user input
    if not symbol:
        return apology("missing symbol", 400)
    elif not (stock := lookup(symbol)):
        return apology("invalid symbol", 400)
    elif not shares:
        return apology("missing shares", 400)
    elif not shares.isdigit():
        return apology("invalid shares", 400)
    elif (shares := int(shares)) < 1:
        return apology("shares must be positive", 400)

    # preparing and checking user balance
    user = db.execute("SELECT * FROM users WHERE id = ?", session["user_id"])[0]
    total = stock["price"] * shares

    if total > user["cash"]:
        return apology("can't afford", 400)

    # completing the transaction
    moneyLeft = float(user["cash"]) - total
    db.execute("INSERT INTO transactions VALUES (?, ?, ?, ?, ?, ?)",
               session["user_id"], stock["symbol"], shares, stock["price"], total, datetime.now())
    db.execute("UPDATE users SET cash = ? WHERE id = ?", moneyLeft, session["user_id"])
    flash("Bought!")

    return redirect("/")


def sellFunc():
    symbol = request.form.get("symbol")
    shares = request.form.get("shares")

    # checking user input
    if not symbol:
        return apology("missing symbol", 400)
    elif not shares:
        return apology("missing shares", 400)
    elif not shares.isdigit():
        return apology("invalid shares", 400)
    elif not (stock := lookup(symbol)):
        return apology("invalid symbol", 400)

    shareAmount = db.execute("SELECT SUM(shares) as shares from transactions WHERE user_id = ? and stock_symbol = ?",
                             session["user_id"], stock["symbol"])[0]["shares"]

    if not shareAmount:
        return apology("shares not owned", 400)
    elif (shares := int(shares)) < 1:
        return apology("shares must be positive", 400)
    elif shareAmount < shares:
        return apology("too many shares", 400)

    # preparing and completing the transaction
    user = db.execute("SELECT * FROM users WHERE id = ?", session["user_id"])[0]
    total = stock["price"] * shares
    moneyLeft = float(user["cash"]) + total
    db.execute("INSERT INTO transactions VALUES (?, ?, ?, ?, ?, ?)",
               session["user_id"], stock["symbol"], -shares, stock["price"], total, datetime.now())
    db.execute("UPDATE users SET cash = ? WHERE id = ?", moneyLeft, session["user_id"])
    flash("Sold!")

    return redirect("/")
