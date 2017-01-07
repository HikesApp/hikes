package com.chekalin.hikes.services;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class IdGeneratorTest {

    private IdGenerator idGenerator = new IdGenerator();

    @Test
    public void generatesUniqueId() throws Exception {
        String id1 = idGenerator.generateId();
        String id2 = idGenerator.generateId();

        assertThat(id1, is(not(equalTo(id2))));
    }
}