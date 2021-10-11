package com.g3ida.withflyingcolours;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationTests extends TestCase {

    @Test
    public void testAdd() throws Exception {
        Assert.assertEquals(10, Utils.add(3, 7));
    }
}