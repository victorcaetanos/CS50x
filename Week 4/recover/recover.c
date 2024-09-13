#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

typedef uint8_t BYTE;

int main(int argc, char *argv[])
{
    const int MEM_BLOCK = 512;

    if (argc != 2)
    {
        printf("Usage: ./recover card.raw");
        return 1;
    }

    FILE *infile = fopen(argv[1], "r");
    if (infile == NULL)
    {
        printf("Could not open file.\n");
        return 1;
    }

    BYTE buffer[MEM_BLOCK];
    FILE *outfile = NULL;
    int fileCounter = 0, found = 0;
    char fileName[8];

    while (fread(buffer, 1, MEM_BLOCK, infile) == MEM_BLOCK)
    {
        // if start of new JPEG
        if (buffer[0] == 0xff && buffer[1] == 0xd8 && buffer[2] == 0xff && (buffer[3] >= 0xe0 && buffer[3] <= 0xef))
        {
            if (fileCounter != 0)
            {
                found = 0;
                fclose(outfile);
            }

            sprintf(fileName, "%03i.jpg", fileCounter);
            outfile = fopen(fileName, "w");
            fwrite(buffer, 1, MEM_BLOCK, outfile);
            fileCounter++;
            found = 1;
        }
        else if (found == 1)
        {
            fwrite(buffer, 1, MEM_BLOCK, outfile);
        }
    }
    fclose(infile);
    fclose(outfile);
    return 0;
}
