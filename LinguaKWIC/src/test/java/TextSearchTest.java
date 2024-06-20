/**
 * junit test for TextSearch
 */

import de.uni.tuebingen.sfs.java2.TextSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextSearchTest {

    private TextSearch textSearch;

    @BeforeEach
    void setUp() {
        List<List<String>> tokens = Arrays.asList(
                Arrays.asList("the", "quick", "brown", "fox"),
                Arrays.asList("jumps", "over", "the", "lazy", "dog")
        );
        List<List<String>> posTags = Arrays.asList(
                Arrays.asList("DT", "JJ", "NN", "NN"),
                Arrays.asList("VBZ", "IN", "DT", "JJ", "NN")
        );
        List<List<String>> lemmas = Arrays.asList(
                Arrays.asList("the", "quick", "brown", "fox"),
                Arrays.asList("jump", "over", "the", "lazy", "dog")
        );
        textSearch = new TextSearch(tokens, posTags, lemmas);
    }

    @Test
    void testSearchByTokenAndLemm() {
        List<TextSearch.Pair> result = textSearch.searchByTokenAndLemm("the", "the");
        List<TextSearch.Pair> expected = Arrays.asList(
                new TextSearch.Pair(0, 0),
                new TextSearch.Pair(1, 2)
        );
        assertEquals(expected, result);
    }

    @Test
    void testSearchByTokenAndTag() {
        List<TextSearch.Pair> result = textSearch.searchByTokenAndTag("the", "DT");
        List<TextSearch.Pair> expected = Arrays.asList(
                new TextSearch.Pair(0, 0),
                new TextSearch.Pair(1, 2)
        );
        assertEquals(expected, result);
    }

    @Test
    void testSearchByTagAndLemm() {
        List<TextSearch.Pair> result = textSearch.searchByTagAndLemm("JJ", "lazy");
        List<TextSearch.Pair> expected = Arrays.asList(
                new TextSearch.Pair(1, 3)
        );
        assertEquals(expected, result);
    }

    @Test
    void testSearchByTokenAndLemmAndTag() {
        List<TextSearch.Pair> result = textSearch.searchByTokenAndLemmAndTag("the", "the", "DT");
        List<TextSearch.Pair> expected = Arrays.asList(
                new TextSearch.Pair(0, 0),
                new TextSearch.Pair(1, 2)
        );
        assertEquals(expected, result);
    }
}

