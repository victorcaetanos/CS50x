import csv
from sys import argv, exit


def main():
    # Check for command-line usage
    if len(argv) != 3:
        print("Usage: python dna.py file.csv file.txt")
        exit(0)

    # Read database file into a variable
    field_names = []
    rows = []
    with open(argv[1]) as csv_file:
        reader = csv.DictReader(csv_file)
        field_names = reader.fieldnames
        for row in reader:
            rows.append(row)
    field_names.pop(0)

    # Read DNA sequence file into a variable
    dna_sequence = ''
    with open(argv[2]) as txt_file:
        dna_sequence = txt_file.read().strip()

    # Find longest match of each STR in DNA sequence
    str_quant = {}
    for name in field_names:
        str_quant[name] = longest_match(dna_sequence, name)

    # Check database for matching profiles
    for dict in rows:
        str_count = 0
        for name in field_names:
            if str(str_quant[name]) == str(dict[name]):
                str_count += 1
                if str_count == len(field_names):
                    print(f"{dict['name']}")
                    return
            else:
                break

    print("No match")
    return


def longest_match(sequence, subsequence):
    """Returns length of longest run of subsequence in sequence."""

    # Initialize variables
    longest_run = 0
    subsequence_length = len(subsequence)
    sequence_length = len(sequence)

    # Check each character in sequence for most consecutive runs of subsequence
    for i in range(sequence_length):

        # Initialize count of consecutive runs
        count = 0

        # Check for a subsequence match in a "substring" (a subset of characters) within sequence
        # If a match, move substring to next potential match in sequence
        # Continue moving substring and checking for matches until out of consecutive matches
        while True:

            # Adjust substring start and end
            start = i + count * subsequence_length
            end = start + subsequence_length

            # If there is a match in the substring
            if sequence[start:end] == subsequence:
                count += 1

            # If there is no match in the substring
            else:
                break

        # Update most consecutive matches found
        longest_run = max(longest_run, count)

    # After checking for runs at each character in seqeuence, return longest run found
    return longest_run


main()
