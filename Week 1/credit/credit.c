#include <cs50.h>
#include <stdio.h>

int main(void)
{
    int validateCardCheckSum(long cardNumber);
    int getLenghtOfCardNumber(long cardNumber);
    int getFirstTwoDigits(long cardNumber);
    long inputCardNumber, cardLenght, firstTwoDigits;

    inputCardNumber = get_long("Number: ");

    cardLenght = getLenghtOfCardNumber(inputCardNumber);
    firstTwoDigits = getFirstTwoDigits(inputCardNumber);

    if (validateCardCheckSum(inputCardNumber) == 0)
        printf("INVALID\n");
    else if (cardLenght == 15 && (firstTwoDigits == 34 || firstTwoDigits == 37))
        printf("AMEX\n");
    else if (cardLenght == 16 &&
             (firstTwoDigits == 51 || firstTwoDigits == 52 || firstTwoDigits == 53 || firstTwoDigits == 54 || firstTwoDigits == 55))
        printf("MASTERCARD\n");
    else if ((cardLenght == 13 || cardLenght == 16) && (firstTwoDigits / 10 == 4))
        printf("VISA\n");
    else
        printf("INVALID\n");
}

int validateCardCheckSum(long cardNumber)
{
    int totalAddition = 0, b = 0;

    for (int i = 0; i < 16; i++)
    {
        b = cardNumber % 10;

        if (i % 2 == 0)
        {
            totalAddition += b;
        }
        else
        {
            int nTimesTwo = b * 2;
            totalAddition += (((nTimesTwo) / 10) + ((nTimesTwo) % 10));
        }

        cardNumber /= 10;
    }

    if (totalAddition % 10 == 0)
    {
        return 1;
    }
    return 0;
}

int getLenghtOfCardNumber(long cardNumber)
{
    long size = 0;

    for (int i = 0; i < 16; i++)
    {
        if (cardNumber != 0)
        {
            size += 1;
        }
        cardNumber /= 10;
    }
    return size;
}

int getFirstTwoDigits(long cardNumber)
{
    long firstDigit = 0, secondDigit = 0;

    while (cardNumber != 0)
    {
        secondDigit = firstDigit;
        firstDigit = cardNumber % 10;
        cardNumber /= 10;
    }
    return (firstDigit * 10) + secondDigit;
}
