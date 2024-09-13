#include <cs50.h>
#include <stdio.h>

int main(void)
{
    int starting_population, ending_population, n, calculated_population;

    do
    {
        starting_population = get_int("Start size: ");
    }
    while (starting_population < 9);

    do
    {
        ending_population = get_int("End size: ");
    }
    while (ending_population < starting_population);

    n = 0, calculated_population = starting_population;

    while (calculated_population < ending_population)
    {
        calculated_population += (calculated_population / 3) - (calculated_population / 4);
        n++;
    }

    printf("Years: %d\n", n);
}
