package de.uni.tuebingen.sfs.java2;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

public class TextSearch implements Serializable {
    private static final long serialVersionUID = 42L;

    private final Map<String, List<Pair>> tokenIndex = new HashMap<>();
    private final Map<String, List<Pair>> tokenIndexLowerCase = new HashMap<>();
    private final Map<String, List<Pair>> posTagIndex = new HashMap<>();
    private final Map<String, List<Pair>> posTagIndexLowerCase = new HashMap<>();
    private final Map<String, List<Pair>> lemmaIndex = new HashMap<>();
    private final Map<String, List<Pair>> lemmaIndexLowerCase = new HashMap<>();

    public TextSearch(List<List<String>> tokens, List<List<String>> posTags, List<List<String>> lemmas) {
        indexText(tokens, posTags, lemmas);
    }

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

                // Populate tokenIndex
                tokenIndex.computeIfAbsent(token, k -> new ArrayList<>()).add(pair);

                // Populate tokenIndexLowerCase
                if (token != null) {
                    String tokenLower = token.toLowerCase();
                    tokenIndexLowerCase.computeIfAbsent(tokenLower, k -> new ArrayList<>()).add(pair);
                }

                // Populate posTagIndex
                posTagIndex.computeIfAbsent(posTag, k -> new ArrayList<>()).add(pair);

                // Populate posTagIndexLowerCase
                if (posTag != null) {
                    String posTagLower = posTag.toLowerCase();
                    posTagIndexLowerCase.computeIfAbsent(posTagLower, k -> new ArrayList<>()).add(pair);
                }

                // Populate lemmaIndex
                lemmaIndex.computeIfAbsent(lemma, k -> new ArrayList<>()).add(pair);

                // Populate lemmaIndexLowerCase
                if (lemma != null) {
                    String lemmaLower = lemma.toLowerCase();
                    lemmaIndexLowerCase.computeIfAbsent(lemmaLower, k -> new ArrayList<>()).add(pair);
                }
            }
        }
    }

    public List<Pair> searchByToken(String token, boolean caseSensitive) {
        if (!caseSensitive) {
            return tokenIndexLowerCase.getOrDefault(token.toLowerCase(), Collections.emptyList());
        }
        return tokenIndex.getOrDefault(token, Collections.emptyList());
    }

    public List<Pair> searchByTag(String tag, boolean caseSensitive) {
        if (!caseSensitive) {
            return posTagIndexLowerCase.getOrDefault(tag.toLowerCase(), Collections.emptyList());
        }
        return posTagIndex.getOrDefault(tag, Collections.emptyList());
    }

    public List<Pair> searchByLemm(String lemm, boolean caseSensitive) {
        if (!caseSensitive) {
            return lemmaIndexLowerCase.getOrDefault(lemm.toLowerCase(), Collections.emptyList());
        }
        return lemmaIndex.getOrDefault(lemm, Collections.emptyList());
    }

    public List<Pair> searchByTokenAndLemm(String token, String lemm, boolean caseSensitive) {
        List<Pair> result = new ArrayList<>();
        List<Pair> tokenPairs = caseSensitive ? tokenIndex.get(token) : tokenIndexLowerCase.get(token.toLowerCase());
        List<Pair> lemmaPairs = caseSensitive ? lemmaIndex.get(lemm) : lemmaIndexLowerCase.get(lemm.toLowerCase());

        if (tokenPairs != null && lemmaPairs != null) {
            for (Pair tokenPair : tokenPairs) {
                if (lemmaPairs.contains(tokenPair)) {
                    result.add(tokenPair);
                }
            }
        }
        return result;
    }

    public List<Pair> searchByTokenAndTag(String token, String tag, boolean caseSensitive) {
        List<Pair> result = new ArrayList<>();
        List<Pair> tokenPairs = caseSensitive ? tokenIndex.get(token) : tokenIndexLowerCase.get(token.toLowerCase());
        List<Pair> tagPairs = caseSensitive ? posTagIndex.get(tag) : posTagIndexLowerCase.get(tag.toLowerCase());

        if (tokenPairs != null && tagPairs != null) {
            for (Pair tokenPair : tokenPairs) {
                if (tagPairs.contains(tokenPair)) {
                    result.add(tokenPair);
                }
            }
        }
        return result;
    }

    public List<Pair> searchByTagAndLemm(String tag, String lemm, boolean caseSensitive) {
        List<Pair> result = new ArrayList<>();
        List<Pair> tagPairs = caseSensitive ? posTagIndex.get(tag) : posTagIndexLowerCase.get(tag.toLowerCase());
        List<Pair> lemmaPairs = caseSensitive ? lemmaIndex.get(lemm) : lemmaIndexLowerCase.get(lemm.toLowerCase());

        if (tagPairs != null && lemmaPairs != null) {
            for (Pair lemmaPair : lemmaPairs) {
                if (tagPairs.contains(lemmaPair)) {
                    result.add(lemmaPair);
                }
            }
        }
        return result;
    }

    public List<Pair> searchByTokenAndLemmAndTag(String token, String lemm, String tag, boolean caseSensitive) {
        List<Pair> result = new ArrayList<>();
        List<Pair> tokenPairs = caseSensitive ? tokenIndex.get(token) : tokenIndexLowerCase.get(token.toLowerCase());
        List<Pair> lemmaPairs = caseSensitive ? lemmaIndex.get(lemm) : lemmaIndexLowerCase.get(lemm.toLowerCase());
        List<Pair> tagPairs = caseSensitive ? posTagIndex.get(tag) : posTagIndexLowerCase.get(tag.toLowerCase());

        if (tokenPairs != null && lemmaPairs != null && tagPairs != null) {
            for (Pair tokenPair : tokenPairs) {
                if (lemmaPairs.contains(tokenPair) && tagPairs.contains(tokenPair)) {
                    result.add(tokenPair);
                }
            }
        }
        return result;
    }

    @Getter
    public static class Pair implements Serializable {
        private static final long serialVersionUID = 42L;
        private final int sentenceIndex;
        private final int tokenIndex;

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
