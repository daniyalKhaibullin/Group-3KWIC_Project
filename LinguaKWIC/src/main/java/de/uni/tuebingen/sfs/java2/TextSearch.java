/***
 *V1.1 a search class which uses hashMap for searching
 */


package de.uni.tuebingen.sfs.java2;

import java.util.*;


public class TextSearch {
    // Map to store tokens, posTag and lemma as keys and lists of indices as values
    // map a string to a list of integers which are the index position of the sentence that has that string
    private Map<String, List<Integer>> tokenIndex = new HashMap<>();
    private Map<String, List<Integer>> posTagIndex = new HashMap<>();
    private Map<String, List<Integer>> lemmaIndex = new HashMap<>();


    /**
     * Indexes tokens, POS tags, and lemmas from input lists.
     *
     * @param tokens  List of tokenized sentences
     * @param posTags List of POS tags for tokens
     * @param lemmas  List of lemmatized tokens
     */
    public void indexText(List<List<String>> tokens, List<List<String>> posTags, List<List<String>> lemmas) {
        indexData(tokens, tokenIndex); // Index tokens
        indexData(posTags, posTagIndex); // Index POS tags
        indexData(lemmas, lemmaIndex); // Index lemmas
    }

    /**
     * Helper method to index data from a list into a map of indices.
     *
     * @param data     List of lists containing data to index
     * @param indexMap Map to store indices of data elements
     */
    private void indexData(List<List<String>> data, Map<String, List<Integer>> indexMap) {
        for (int i = 0; i < data.size(); i++) {
            List<String> sentenceData = data.get(i);
            for (String item : sentenceData) {
                // Compute if absent: If key (item) is not present, initialize with new ArrayList
                // and add current index (i). If present, add current index to existing list.
                indexMap.computeIfAbsent(item, k -> new ArrayList<>()).add(i);
            }
        }
    }

    // Method to search for indices of a token
    public List<Integer> searchByToken(String token) {
        // Return the list of indices for the given token, or an empty list if token not found
        return tokenIndex.getOrDefault(token, new ArrayList<>());
    }

    // Method to search for indices of a tag
    public List<Integer> searchByTag(String tag) {
        // Return the list of indices for the given tag, or an empty list if tag not found
        return tokenIndex.getOrDefault(tag, new ArrayList<>());
    }

    // Method to search for indices of a lemm
    public List<Integer> searchByLemm(String lemm) {
        // Return the list of indices for the given lemm, or an empty list if lemm not found
        return tokenIndex.getOrDefault(lemm, new ArrayList<>());
    }


}

