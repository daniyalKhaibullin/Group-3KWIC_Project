// this is a junit file if you can it is good to add more tests
import de.uni.tuebingen.sfs.java2.LinguaKWIC;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;


public class LinguaKWICTest {

    private static LinguaKWIC linguaKWIC;
    private static final String TEST_TEXT = "For example, a search query including all of the words in an example definition " +
            "(\"KWIC is an acronym for Key Word In Context, the most common format for concordance lines\") " +
            "and the Wikipedia slogan in English (\"the free encyclopedia\"), searched against a Wikipedia page, " +
            "might yield a KWIC index as follows. A KWIC index usually uses a wide layout to allow the display of " +
            "maximum 'in context' information (not shown in the following example).";



    @Test
    public void testGetText() {
        linguaKWIC = new LinguaKWIC(TEST_TEXT);
        assertEquals(TEST_TEXT, linguaKWIC.getText());
    }

    @Test
    public void testGetSentences() {
        linguaKWIC = new LinguaKWIC(TEST_TEXT);
        String[] expectedSentences = {
                "For example, a search query including all of the words in an example definition (\"KWIC is an acronym for Key Word In Context, the most common format for concordance lines\") and the Wikipedia slogan in English (\"the free encyclopedia\"), searched against a Wikipedia page, might yield a KWIC index as follows.",
                "A KWIC index usually uses a wide layout to allow the display of maximum 'in context' information (not shown in the following example)."
        };
        assertArrayEquals(expectedSentences, linguaKWIC.getSentences());
    }

    @Test
    public void testGetTokens() {
        linguaKWIC = new LinguaKWIC(TEST_TEXT);
        List<List<String>> tokens = linguaKWIC.getTokens();
        assertNotNull(tokens);
        assertEquals(2, tokens.size());
        assertEquals("For", tokens.get(0).get(0));
        assertEquals("example", tokens.get(0).get(1));
        // Add more assertions to verify the tokens as needed
    }

    @Test
    public void testGetPosTags() {
        linguaKWIC = new LinguaKWIC(TEST_TEXT);
        List<List<String>> posTags = linguaKWIC.getPosTags();
        assertNotNull(posTags);
        assertEquals(2, posTags.size());
        assertEquals("IN", posTags.get(0).get(0));
        assertEquals("NN", posTags.get(0).get(1));
        // Add more assertions to verify the POS tags as needed
    }

    @Test
    public void testGetLemmas() {
        linguaKWIC = new LinguaKWIC(TEST_TEXT);
        List<List<String>> lemmas = linguaKWIC.getLemmas();
        assertNotNull(lemmas);
        assertEquals(2, lemmas.size());
        assertEquals("for", lemmas.get(0).get(0));
        assertEquals("example", lemmas.get(0).get(1));
        // Add more assertions to verify the lemmas as needed
    }
}
