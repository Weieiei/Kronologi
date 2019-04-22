package appointmentscheduler.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringMatcherTest {

    @Test
    public void validString(){
        assertTrue(StringMatcher.verifyNaming("test.txt"));
    }

    @Test
    public void validUnderscoreString(){
        assertTrue(StringMatcher.verifyNaming("test_blob.txt"));
    }

    @Test
    public void validNumString(){
        assertTrue(StringMatcher.verifyNaming("test32.txt"));
    }

    @Test
    public void invalidDotString(){
        assertFalse(StringMatcher.verifyNaming("test-blob.txt.png"));
    }

    @Test
    public void invalidHyphenString(){
        assertFalse(StringMatcher.verifyNaming("test-blob.txt"));
    }

    @Test
    public void invalidString(){
        assertFalse(StringMatcher.verifyNaming("test.23txt"));
    }

}