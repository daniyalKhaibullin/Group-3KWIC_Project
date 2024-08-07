package de.uni.tuebingen.sfs.java2;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class to save the result of search for history.
 */
@Getter
public class RecordKeeper {

    private final List<TextSearch.Pair> results;
    private final List<List<String>> tokens;
    private final List<List<String>> lemmas;
    private final List<List<String>> posTags;
    private final List<String> singleResult;
    private final String target;
    private final String searchString;
    private final String type;


    public RecordKeeper(List<TextSearch.Pair> results, List<List<String>> tokens,
                        List<List<String>> lemmas, List<List<String>> posTags,String target, String searchString, String type) {
        this.results = results;
        this.tokens = tokens != null ? tokens : new ArrayList<>();
        this.lemmas = lemmas != null ? lemmas : new ArrayList<>();
        this.posTags = posTags != null ? posTags : new ArrayList<>();
        this.searchString = searchString;
        this.type = type;
        this.target=target;
        this.singleResult = new ArrayList<>();

        for (TextSearch.Pair result : results) {
            assert tokens != null;
            List<String> tokenList = tokens.get(result.getSentenceIndex());
            assert lemmas != null;
            List<String> lemmaList = lemmas.get(result.getSentenceIndex());
            assert posTags != null;
            List<String> posTagList = posTags.get(result.getSentenceIndex());

            // Handle possible null lists
            String tokenString = tokenList != null ? String.join(" ", tokenList) : "";
            String lemmaString = lemmaList != null ? String.join(" ", lemmaList) : "";
            String posTagString = posTagList != null ? String.join(" ", posTagList) : "";

            String singleRes = "Sentence: " + tokenString + "\n" +
                    "lemma: " + lemmaString + "\n" +
                    "posTag: " + posTagString + "\n";

            this.singleResult.add(singleRes);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordKeeper that = (RecordKeeper) o;
        return Objects.equals(results, that.results) &&
                Objects.equals(tokens, that.tokens) &&
                Objects.equals(lemmas, that.lemmas) &&
                Objects.equals(posTags, that.posTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(results, tokens, lemmas, posTags);
    }

    @Override
    public String toString() {
        return singleResult.toString();
    }
}

