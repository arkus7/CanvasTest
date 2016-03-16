package com.mooduplabs.canvas;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Ignore
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void collisionTest() throws Exception {
        Entity entity1 = new Entity(1, 1, 1);
        Entity entity2 = new Entity(1, 2, 2);
        assertTrue(entity1.isCollided(entity2));
        entity2.setX(5);
        assertFalse(entity1.isCollided(entity2));
        entity2.setX(2);
        entity2.setRadius(0.2);
        assertFalse(entity1.isCollided(entity2));
    }
}