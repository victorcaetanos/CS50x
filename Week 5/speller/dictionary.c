// Implements a dictionary's functionality

#include <ctype.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>

#include "dictionary.h"

// Represents a node in a hash table
typedef struct node
{
    char word[LENGTH + 1];
    struct node *next;
} node;

// TODO: Choose number of buckets in hash table
const unsigned int N = 26 * 26;

// Hash table
node *table[N];

// Returns true if word is in dictionary, else false
bool check(const char *word)
{
    // Hash the word to obtain its hash value
    unsigned int hash_value = hash(word);

    // Search the hash table at the location specified by the word’s hash value
    node *temp_node = table[hash_value];
    while (temp_node != NULL)
    {
        // Return true if the word is found
        if (strcasecmp(temp_node->word, word) == 0)
        {
            return true;
        }
        temp_node = temp_node->next;
    }
    // Return false if no word is found
    return false;
}

// Hashes word to a number
unsigned int hash(const char *word)
{
    // TODO: Improve this hash function
    if (word[1] == '\0')
    {
        return (toupper(word[0]) - 'A') * 26;
    }
    return ((toupper(word[0]) - 'A') * 26) + (toupper(word[1]) - 'A');
}

int node_count = 0;

// Loads dictionary into memory, returning true if successful, else false
bool load(const char *dictionary)
{
    FILE *open_dict = fopen(dictionary, "r");
    if (open_dict == NULL)
    {
        return false;
    }

    char word_buffer[LENGTH + 1];

    while (fscanf(open_dict, "%s", word_buffer) != EOF)
    {
        // Create space for a new hash table node
        node *new_node = malloc(sizeof(node));
        if (new_node == NULL)
            return false;

        // Copy the word into the new node
        strcpy(new_node->word, word_buffer);

        // Hash the word to obtain its hash value
        unsigned int hash_value = hash(new_node->word);

        // Insert the new node into the hash table
        new_node->next = table[hash_value];
        table[hash_value] = new_node;
        node_count++;
    }
    fclose(open_dict);
    return true;
}

// Returns number of words in dictionary if loaded, else 0 if not yet loaded
unsigned int size(void)
{
    return node_count;
}

// Unloads dictionary from memory, returning true if successful, else false
bool unload(void)
{
    // while table has something end is in range
    for (int i = 0; i < N; i++)
    {
        node *current_node = table[i];
        // while current hash key section has items
        while (current_node != NULL)
        {
            node *temp_node = current_node;
            current_node = current_node->next;
            free(temp_node);
        }
    }
    return true;
}
