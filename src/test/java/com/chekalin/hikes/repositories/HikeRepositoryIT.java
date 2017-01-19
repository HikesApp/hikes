package com.chekalin.hikes.repositories;

import com.chekalin.hikes.domain.Hike;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HikeRepositoryIT {

    @Autowired
    private HikeRepository hikeRepository;

    @Test
    public void findByIdWhenHikeExistsShouldReturnHike() throws Exception {
        UUID hikeId = UUID.randomUUID();
        Hike hike = Hike.builder().id(hikeId).name("testHike").startDate(LocalDate.now()).build();
        hikeRepository.save(hike);

        Optional<Hike> result = hikeRepository.findById(hikeId);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(equalTo(hike)));
    }

    @Test
    public void findByIdWhenHikeDoesNotExistShouldReturnHikeAbsentOptional() throws Exception {
        Optional<Hike> result = hikeRepository.findById(UUID.randomUUID());

        assertThat(result.isPresent(), is(false));
    }
}