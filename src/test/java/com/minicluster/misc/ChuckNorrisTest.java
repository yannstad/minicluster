package com.minicluster.misc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChuckNorrisTest {

    @Test
    public void testGetRandomQuote() throws Exception {
        String quote = new ChuckNorris().getRandomQuote();
        assertNotNull(quote);
        System.out.println(quote);
    }

    @Test
    public void testGetAsciiArtPicture() throws Exception {
        assertEquals(ChuckNorris.ASCII_CHUCK, new ChuckNorris().getAsciiArtPicture());
    }
}