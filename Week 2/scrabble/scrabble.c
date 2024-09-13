#include <cs50.h>
#include <ctype.h>
#include <stdio.h>
#include <string.h>

int calculateScore(string word);
int letterToInt(char letter);

int main(void)
{

    int playerOneScore, playerTwoScore;
    string playerOneAnswer, playerTwoAnswer;

    playerOneAnswer = (get_string("Player 1: "));
    playerTwoAnswer = (get_string("Player 2: "));

    playerOneScore = calculateScore(playerOneAnswer);
    playerTwoScore = calculateScore(playerTwoAnswer);

    if (playerOneScore > playerTwoScore)
        printf("Player 1 wins!");
    else if (playerOneScore < playerTwoScore)
        printf("Player 2 wins!");
    else
        printf("Tie!");
}

int calculateScore(string word)
{
    int score = 0;
    const int letterScores[26] = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};

    for (int i = 0; i < strlen(word); i++)
    {
        if (isalpha(word[i]))
        {
            score += letterScores[letterToInt(tolower(word[i]))];
        }
    }
    return score;
}

int letterToInt(char letter)
{
    return letter - 97;
}
