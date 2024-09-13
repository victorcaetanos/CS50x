#include <cs50.h>
#include <ctype.h>
#include <stdio.h>
#include <string.h>

bool hasOnlyAlpha(string text);
bool hasDuplicateLetter(string text);
string encode(string plaintext, string key);

int main(int argc, string argv[])
{
    const int ALPHABET_SIZE = 26;
    string plaintext = "";

    if (argc != 2)
    {
        printf("Usage: ./substitution key\n");
        return 1;
    }
    else if (strlen(argv[1]) != ALPHABET_SIZE)
    {
        printf("Key must contain 26 characters.\n");
        return 1;
    }
    else if (!hasOnlyAlpha(argv[1]))
    {
        printf("Key must have only alphabetic characters.\n");
        return 1;
    }
    else if (hasDuplicateLetter(argv[1]))
    {
        printf("Key must contain all 26 alphabetic characters.\n");
        return 1;
    }

    plaintext = get_string("plaintext:  ");
    printf("ciphertext: %s\n", encode(plaintext, argv[1]));

    return 0;
}

bool hasOnlyAlpha(string text)
{
    for (int i = 0, n = strlen(text); i < n; i++)
        if (!isalpha(text[i]))
            return false;
    return true;
}

bool hasDuplicateLetter(string text)
{
    for (int i = 0, n = strlen(text); i < n; i++)
        for (int j = i + 1; j < n; j++)
            if (tolower(text[i]) == tolower(text[j]))
                return true;
    return false;
}

string encode(string plaintext, string key)
{
    string ciphertext = plaintext;
    const int ASCII_LOWERCASE_DISTANCE_FROM_0 = 97, ASCII_UPPERCASE_DISTANCE_FROM_0 = 65;

    for (int i = 0, n = strlen(plaintext); i < n; i++)
        if (isalpha(plaintext[i]))
            if (islower(plaintext[i]))
                ciphertext[i] = tolower(key[plaintext[i] - ASCII_LOWERCASE_DISTANCE_FROM_0]);
            else
                ciphertext[i] = toupper(key[plaintext[i] - ASCII_UPPERCASE_DISTANCE_FROM_0]);
        else
            ciphertext[i] = plaintext[i];
    return ciphertext;
}
