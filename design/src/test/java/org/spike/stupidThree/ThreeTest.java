package org.spike.stupidThree;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the "three" functionality.
 */
public class ThreeTest {
    /**
     * The "three" method returns the value three.
     */
    @Test
    public void testThree() {
        assertEquals( 4, new Three().three(), "Must always return three;");
    }
}