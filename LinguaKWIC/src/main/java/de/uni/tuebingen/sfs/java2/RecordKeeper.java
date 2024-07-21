package de.uni.tuebingen.sfs.java2;

import lombok.Getter;
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

    public RecordKeeper(List<TextSearch.Pair> results, List<List<String>> tokens,
                        List<List<String>> lemmas, List<List<String>> posTags) {
        this.results = results;
        this.tokens = tokens;
        this.lemmas = lemmas;
        this.posTags = posTags;
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
        return "RecordKeeper{" +
                "results=" + results +
                ", tokens=" + tokens +
                ", lemmas=" + lemmas +
                ", posTags=" + posTags +
                '}';
    }
}

