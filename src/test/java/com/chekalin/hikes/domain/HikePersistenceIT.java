package com.chekalin.hikes.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HikePersistenceIT {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void persistsHike() throws Exception {
        Hike hike = Hike.builder()
                .id(UUID.randomUUID())
                .name("testHike")
                .startDate(LocalDate.of(2017, Month.JANUARY, 18))
                .endDate(LocalDate.of(2017, Month.JANUARY, 19))
                .distance(new BigDecimal(65))
                .build();

        Hike persisted = entityManager.persistFlushFind(hike);

        assertThat(persisted.getId(), is(equalTo(hike.getId())));
        assertThat(persisted.getName(), is(equalTo(hike.getName())));
        assertThat(persisted.getStartDate(), is(equalTo(hike.getStartDate())));
        assertThat(persisted.getEndDate(), is(equalTo(hike.getEndDate())));
        assertThat(persisted.getDistance(), is(closeTo(hike.getDistance(), BigDecimal.ZERO)));
    }
}