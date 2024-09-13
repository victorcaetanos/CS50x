// Modifies the volume of an audio file

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

// Number of bytes in .wav header
const int HEADER_SIZE = 44;

int main(int argc, char *argv[])
{
    // Check command-line arguments
    if (argc != 4)
    {
        printf("Usage: ./volume input.wav output.wav factor\n");
        return 1;
    }

    // Open files and determine scaling factor
    FILE *input = fopen(argv[1], "r");
    if (input == NULL)
    {
        printf("Could not open file.\n");
        return 1;
    }

    FILE *output = fopen(argv[2], "w");
    if (output == NULL)
    {
        printf("Could not open file.\n");
        return 1;
    }

    float factor = atof(argv[3]);
    typedef uint8_t BYTE;
    BYTE byte[2];
    int counter = 0;

    // TODO: Copy header from input file to output file
    while (counter < HEADER_SIZE)
    {
        fread(&byte, sizeof(byte), 1, input);
        fwrite(&byte, sizeof(byte), 1, output);
        counter += 2;
    }

    typedef int16_t SAMPLE;
    SAMPLE sample;

    // TODO: Read samples from input file and write updated data to output file
    while (fread(&sample, sizeof(sample), 1, input) != 0)
    {
        sample *= factor;
        fwrite(&sample, sizeof(sample), 1, output);
    }

    // Close files
    fclose(input);
    fclose(output);
}
