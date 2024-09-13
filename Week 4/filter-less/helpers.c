#include "helpers.h"
#include <math.h>

typedef uint8_t BYTE;

int smallest(int x, int y);
void blurPixel(int i, int j, int height, int width, RGBTRIPLE origBMP[height][width], RGBTRIPLE modBMP[height][width]);

// Convert image to grayscale
void grayscale(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            BYTE temp = round((image[i][j].rgbtBlue + image[i][j].rgbtRed + image[i][j].rgbtGreen) / 3.0);
            image[i][j].rgbtRed = temp;
            image[i][j].rgbtGreen = temp;
            image[i][j].rgbtBlue = temp;
        }
    }
    return;
}

// Convert image to sepia
void sepia(int height, int width, RGBTRIPLE image[height][width])
{
    const int MAX_VALUE = 255;
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            int tempRed = round(.393 * image[i][j].rgbtRed + .769 * image[i][j].rgbtGreen + .189 * image[i][j].rgbtBlue);
            int tempGreen = round(.349 * image[i][j].rgbtRed + .686 * image[i][j].rgbtGreen + .168 * image[i][j].rgbtBlue);
            int tempBlue = round(.272 * image[i][j].rgbtRed + .534 * image[i][j].rgbtGreen + .131 * image[i][j].rgbtBlue);
            image[i][j].rgbtRed = smallest(tempRed, MAX_VALUE);
            image[i][j].rgbtGreen = smallest(tempGreen, MAX_VALUE);
            image[i][j].rgbtBlue = smallest(tempBlue, MAX_VALUE);
        }
    }
    return;
}

// Reflect image horizontally
void reflect(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width / 2; j++)
        {
            RGBTRIPLE temp;

            temp = image[i][j];
            image[i][j] = image[i][width - j - 1];
            image[i][width - j - 1] = temp;
        }
    }
    return;
}

// Blur image
void blur(int height, int width, RGBTRIPLE image[height][width])
{
    RGBTRIPLE copy[height][width];
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            copy[i][j] = image[i][j];
        }
    }

    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            blurPixel(i, j, height, width, copy, image);
        }
    }

    return;
}

int smallest(int x, int y)
{
    if (x < y)
        return x;
    return y;
}

void blurPixel(int i, int j, int height, int width, RGBTRIPLE origBMP[height][width], RGBTRIPLE modBMP[height][width])
{
    float counter = 0, sumRed = 0, sumGreen = 0, sumBlue = 0;
    for (int blurI = -1; blurI <= 1; blurI++)
    {
        for (int blurJ = -1; blurJ <= 1; blurJ++)
        {
            int borderI = i + blurI, borderJ = j + blurJ;
            if ((borderI >= 0 && borderI < height) && (borderJ >= 0 && borderJ < width))
            {
                sumRed += origBMP[borderI][borderJ].rgbtRed;
                sumGreen += origBMP[borderI][borderJ].rgbtGreen;
                sumBlue += origBMP[borderI][borderJ].rgbtBlue;
                counter++;
            }
        }
    }

    modBMP[i][j].rgbtRed = round(sumRed / counter);
    modBMP[i][j].rgbtGreen = round(sumGreen / counter);
    modBMP[i][j].rgbtBlue = round(sumBlue / counter);
}
