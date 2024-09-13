from cs50 import get_string


def main():
    userInput = get_string("Text: ")
    letters = count_letters(userInput)
    words = count_words(userInput)
    sentences = count_sentences(userInput)

    grade = calulate_grade(letters, words, sentences)

    if (grade >= 16):
        print(f"Grade 16+")
    elif (grade < 1):
        print(f"Before Grade 1")
    else:
        print(f"Grade {grade}")


def calulate_grade(letters, words, sentences):
    CONST1 = 0.0588
    CONST2 = 0.296
    CONST3 = 15.8
    l = (letters / words) * 100
    s = (sentences / words) * 100

    return round(CONST1 * l - CONST2 * s - CONST3)


def count_letters(text):
    letter_amount = 0

    for char in text:
        if char.isalpha():
            letter_amount += 1

    return letter_amount


def count_words(text):
    word_amount = 1  # starts with 1 because there is no space before the first word

    for char in text:
        if char == ' ':
            word_amount += 1
    return word_amount


def count_sentences(text):
    sentence_amount = 0

    for char in text:
        if char in ['.', '?', '!']:
            sentence_amount += 1

    return sentence_amount


main()
