/***
 *V1.1 a search class which uses hashMap for searching
 *V1.2 adding more search methods and cleaning the code
 */


package de.uni.tuebingen.sfs.java2;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;


public class TextSearch implements Serializable {
    private static final long serialVersionUID = 42L;
    // Map to store tokens, posTag and lemma as keys and lists of indices as values
    // map a string to a list of integers which are the index position of the sentence that has that string
    private final Map<String, List<Pair>> tokenIndex = new HashMap<>();
    private final Map<String, List<Pair>> posTagIndex = new HashMap<>();
    private final Map<String, List<Pair>> lemmaIndex = new HashMap<>();

    /**
     * constructor
     */
    public TextSearch(List<List<String>> tokens, List<List<String>> posTags, List<List<String>> lemmas) {
        indexText(tokens, posTags, lemmas);
    }

    /**
     * Indexes tokens, POS tags, and lemmas from input lists.
     *
     * @param tokens  List of tokenized sentences
     * @param posTags List of POS tags for tokens
     * @param lemmas  List of lemmatized tokens
     */
    public void indexText(List<List<String>> tokens, List<List<String>> posTags, List<List<String>> lemmas) {
        for (int i = 0; i < tokens.size(); i++) {
            List<String> tokenList = tokens.get(i);
            List<String> posTagList = posTags.get(i);
            List<String> lemmaList = lemmas.get(i);

            for (int j = 0; j < tokenList.size(); j++) {
                String token = tokenList.get(j);
                String posTag = posTagList.get(j);
                String lemma = lemmaList.get(j);

                Pair pair = new Pair(i, j);

                tokenIndex.computeIfAbsent(token, k -> new ArrayList<>()).add(pair);
                posTagIndex.computeIfAbsent(posTag, k -> new ArrayList<>()).add(pair);
                lemmaIndex.computeIfAbsent(lemma, k -> new ArrayList<>()).add(pair);
            }
        }
    }

    // Method to search for indices of a token
    public List<Pair> searchByToken(String token) {
        // Return the list of indices for the given token, or an empty list if token not found
        return tokenIndex.getOrDefault(token, new ArrayList<>());
    }

    // Method to search for indices of a tag
    public List<Pair> searchByTag(String tag) {
        // Return the list of indices for the given tag, or an empty list if tag not found
        return posTagIndex.getOrDefault(tag, new ArrayList<>());
    }

    // Method to search for indices of a lemm
    public List<Pair> searchByLemm(String lemm) {
        // Return the list of indices for the given lemm, or an empty list if lemm not found
        return lemmaIndex.getOrDefault(lemm, new ArrayList<>());
    }

    // Method to search for indices of a lemm and  token
    public List<Pair> searchByTokenAndLemm(String token, String lemm) {
        List<Pair> result = new ArrayList<>();
        List<Pair> tokenPairs = tokenIndex.getOrDefault(token, new ArrayList<>());
        List<Pair> lemmaPairs = lemmaIndex.getOrDefault(lemm, new ArrayList<>());

        // Find intersection of pairs
        for (Pair tokenPair : tokenPairs) {
            if (lemmaPairs.contains(tokenPair)) {
                result.add(tokenPair);
            }
        }
        return result;

    }

    // Method to search for indices of a posTag and  token
    public List<Pair> searchByTokenAndTag(String token, String tag) {
        List<Pair> result = new ArrayList<>();
        List<Pair> tokenPairs = tokenIndex.getOrDefault(token, new ArrayList<>());
        List<Pair> tagPairs = posTagIndex.getOrDefault(tag, new ArrayList<>());
        for (Pair tokenPair : tokenPairs) {
            if (tagPairs.contains(tokenPair)) {
                result.add(tokenPair);
            }
        }
        return result;

    }

    // Method to search for indices of a lemm and  tag
    public List<Pair> searchByTagAndLemm(String tag, String lemm) {
        List<Pair> result = new ArrayList<>();
        List<Pair> lemmaPairs = lemmaIndex.getOrDefault(lemm, new ArrayList<>());
        List<Pair> tagPairs = posTagIndex.getOrDefault(tag, new ArrayList<>());
        for (Pair lemmaPair : lemmaPairs) {
            if (tagPairs.contains(lemmaPair)) {
                result.add(lemmaPair);
            }
        }
        return result;

    }

    // Method to search for indices of a lemm and  token and tag
    public List<Pair> searchByTokenAndLemmAndTag(String token, String lemm, String tag) {
        List<Pair> result = new ArrayList<>();
        List<Pair> lemmaPairs = lemmaIndex.getOrDefault(lemm, new ArrayList<>());
        List<Pair> tagPairs = posTagIndex.getOrDefault(tag, new ArrayList<>());
        List<Pair> tokenPairs = tokenIndex.getOrDefault(token, new ArrayList<>());
        for (Pair tokenPair : tokenPairs) {
            if (lemmaPairs.contains(tokenPair) && tagPairs.contains(tokenPair)) {
                result.add(tokenPair);
            }
        }

        return result;

    }


    /**
     * A helper class to store a pair of indices: sentence index and token index.
     */
    @Getter
    public static class Pair implements Serializable {
        private static final long serialVersionUID = 42L;
        /**
         * -- GETTER --
         * Gets the sentence index.
         *
         * @return The index of the sentence
         */
        private final int sentenceIndex;
        /**
         * -- GETTER --
         * Gets the token index.
         *
         * @return The index of the token within the sentence
         */
        private final int tokenIndex;

        /**
         * Constructs a Pair with the given sentence and token indices.
         *
         * @param sentenceIndex The index of the sentence
         * @param tokenIndex    The index of the token within the sentence
         */
        public Pair(int sentenceIndex, int tokenIndex) {
            this.sentenceIndex = sentenceIndex;
            this.tokenIndex = tokenIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return sentenceIndex == pair.sentenceIndex && tokenIndex == pair.tokenIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sentenceIndex, tokenIndex);
        }

        @Override
        public String toString() {
            return "(" + sentenceIndex + ", " + tokenIndex + ")";
        }
    }


}

