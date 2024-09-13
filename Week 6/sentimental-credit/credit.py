from cs50 import get_string


def main():
    cardNumber = get_string("Number: ")

    if (cardIsValid(cardNumber)):
        printCardBrand(cardNumber)
    else:
        print("INVALID")


def cardIsValid(cardNumber):
    numberSize = len(cardNumber)
    totalSum = 0

    for i in range(numberSize):
        currentDigit = int(cardNumber[numberSize - 1 - i])
        if i % 2 == 0:
            totalSum += currentDigit
        else:
            nTimesTwo = currentDigit * 2
            totalSum += ((nTimesTwo // 10) + (nTimesTwo % 10))

    if totalSum % 10 == 0:
        return True
    return False


def printCardBrand(cardNumber):

    cardLenght = len(cardNumber)
    firstTwoDigits = cardNumber[0:2]

    if (cardLenght == 15 and (firstTwoDigits in ["34", "37"])):
        print("AMEX")
    elif (cardLenght == 16 and (firstTwoDigits in ["51", "52", "53", "54", "55"])):
        print("MASTERCARD")
    elif (cardLenght in [13, 16] and (firstTwoDigits[0] == "4")):
        print("VISA")
    else:
        print("INVALID")


main()
