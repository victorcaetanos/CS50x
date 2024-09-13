#include "helpers.h"
#include <math.h>

typedef uint8_t BYTE;

void blurPixel(int i, int j, int height, int width, RGBTRIPLE origBMP[height][width], RGBTRIPLE modBMP[height][width]);
void mixPixel(int i, int j, int height, int width, RGBTRIPLE origBMP[height][width], RGBTRIPLE modBMP[height][width]);
int smallest(int x, int y);

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

// Detect edges
void edges(int height, int width, RGBTRIPLE image[height][width])
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
            mixPixel(i, j, height, width, copy, image);
        }
    }

    return;
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

int smallest(int x, int y)
{
    return (x < y) ? x : y;
}

void mixPixel(int i, int j, int height, int width, RGBTRIPLE origBMP[height][width], RGBTRIPLE modBMP[height][width])
{
    int sumRedGx = 0, sumRedGy = 0, sumGreenGx = 0, sumGreenGy = 0, sumBlueGx = 0, sumBlueGy = 0;
    int Gx[3][3] = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    int Gy[3][3] = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

    for (int innerI = -1; innerI <= 1; innerI++)
    {
        for (int innerJ = -1; innerJ <= 1; innerJ++)
        {
            int borderI = i + innerI, borderJ = j + innerJ;
            if ((borderI >= 0 && borderI < height) && (borderJ >= 0 && borderJ < width))
            {
                // vertical Gx
                // sum += (original BMP pixel) * (kernel constant (1 or 2) * (-1 or 1))
                sumRedGx += origBMP[borderI][borderJ].rgbtRed * Gx[innerI + 1][innerJ + 1];
                sumGreenGx += origBMP[borderI][borderJ].rgbtGreen * Gx[innerI + 1][innerJ + 1];
                sumBlueGx += origBMP[borderI][borderJ].rgbtBlue * Gx[innerI + 1][innerJ + 1];
                // vertical Gy
                sumRedGy += origBMP[borderI][borderJ].rgbtRed * Gy[innerI + 1][innerJ + 1];
                sumGreenGy += origBMP[borderI][borderJ].rgbtGreen * Gy[innerI + 1][innerJ + 1];
                sumBlueGy += origBMP[borderI][borderJ].rgbtBlue * Gy[innerI + 1][innerJ + 1];
            }
        }
    }
    // finalBMP = (sqrt(Gx^2 + Gy^2) or 255)
    modBMP[i][j].rgbtRed = smallest(round(sqrt(pow(sumRedGx, 2) + pow(sumRedGy, 2))), 255);
    modBMP[i][j].rgbtGreen = smallest(round(sqrt(pow(sumGreenGx, 2) + pow(sumGreenGy, 2))), 255);
    modBMP[i][j].rgbtBlue = smallest(round(sqrt(pow(sumBlueGx, 2) + pow(sumBlueGy, 2))), 255);
}
