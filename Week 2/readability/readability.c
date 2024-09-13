
#include <cs50.h>
#include <ctype.h>
#include <math.h>
#include <stdio.h>
#include <string.h>

int count_letters(string text);
int count_words(string text);
int count_sentences(string text);

int main(void)
{
    string userInput = "";
    int letters = 0, words = 0, sentences = 0, grade = 0;
    float l = 0.0, s = 0.0;

    userInput = get_string("Text: ");
    letters = count_letters(userInput);
    words = count_words(userInput);
    sentences = count_sentences(userInput);
    l = ((float) letters / words) * 100;
    s = ((float) sentences / words) * 100;

    grade = round(0.0588 * l - 0.296 * s - 15.8);

    if (grade >= 16)
        printf("Grade 16+\n");
    else if (grade < 1)
        printf("Before Grade 1\n");
    else
        printf("Grade %d\n", grade);
}

int count_letters(string text)
{
    int letterAmount = 0;

    for (int i = 0, n = strlen(text); i < n; i++)
        if (isalpha(text[i]))
            letterAmount += 1;

    return letterAmount;
}

int count_words(string text)
{
    int wordAmount = 1; // starts with 1 because there is no space before the first word

    for (int i = 0, n = strlen(text); i < n; i++)
        if (text[i] == ' ')
            wordAmount += 1;
    return wordAmount;
}

int count_sentences(string text)
{
    int sentenceAmount = 0;

    for (int i = 0, n = strlen(text); i < n; i++)
        if (text[i] == '.' || text[i] == '?' || text[i] == '!')
            sentenceAmount += 1;

    return sentenceAmount;
}
