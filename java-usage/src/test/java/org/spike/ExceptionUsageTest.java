package org.spike;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ExceptionUsageTest {

    static class ExceptionA extends Exception {

        public ExceptionA(Exception exception) {
            super(exception);
        }
    }

    static class ExceptionB extends Exception {

    }

    static class ExceptionC extends Exception {

    }

    @Test
    public void testException() {
        try {
            call();
            fail("An excetion should be thrown");
        } catch (Exception e) {
            assertEquals("In finally", e.getMessage());
        }
    }

    @Test
    public void testExceptionReturnInFinnaly() throws Exception {
        // No exception because a return in finally
        String result = callFinallyReturn();
        assertEquals("In finally", result);
    }

    private void call() throws Exception {
        try {
            throw new Exception("In try");
        } catch (Exception exception) {
            throw new Exception("In catch", exception);
        } finally {
            throw new Exception("In finally");
        }
    }


    private void callNoException() {
        try {
            throw new RuntimeException();
        } catch (Exception exception) {
            throw exception;
        }
    }

    private String callFinallyReturn() throws Exception {
        try {
            throw new Exception("In try");
        } catch (Exception exception) {
            throw new Exception("In catch", exception);
        } finally {
            return "In finally";
        }
    }


}
