package com.chekalin.hikes;

import com.chekalin.hikes.domain.HikeRepository;
import com.chekalin.hikes.web.HikeDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.junit.Assert.assertThat;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class HikesApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HikeRepository hikeRepository;

    @Before
    public void setUp() throws Exception {
        hikeRepository.deleteAll();
    }

    @Test
    public void loadsHikes() {
        HikeDto testHike1 = restTemplate.postForObject("/hikes", createTestHike("testHike1"), HikeDto.class);
        HikeDto testHike2 = restTemplate.postForObject("/hikes", createTestHike("testHike2"), HikeDto.class);

        HikeDto[] result = restTemplate.getForObject("/hikes", HikeDto[].class);

        assertThat(result, is(arrayWithSize(2)));
        assertThat(result, is(arrayContaining(testHike1, testHike2)));
    }

    @Test
    public void createsAndGetsHike() {
        HikeDto hike = createTestHike("testHike");
        ResponseEntity<HikeDto> result = restTemplate.postForEntity("/hikes", hike, HikeDto.class);

        assertThat(result.getStatusCode(), is(HttpStatus.CREATED));

        HikeDto savedHike = result.getBody();
        assertThat(savedHike.getName(), is(equalTo(hike.getName())));

        HikeDto loadedHike = restTemplate.getForObject("/hikes/" + savedHike.getId(), HikeDto.class);
        assertThat(loadedHike, is(notNullValue()));
    }

    @Test
    public void deletesHike() throws Exception {
        HikeDto testHike = restTemplate.postForObject("/hikes", createTestHike("testHike"), HikeDto.class);

        restTemplate.delete("/hikes/{id}", testHike.getId());

        ResponseEntity<HikeDto> response = restTemplate.getForEntity("/hikes/{id}", HikeDto.class, testHike.getId());

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    private HikeDto createTestHike(String name) {
        return HikeDto.builder()
                .name(name)
                .startDate(LocalDate.of(2017, Month.JANUARY, 7))
                .endDate(LocalDate.of(2017, Month.JANUARY, 8))
                .distance(new BigDecimal(45.50))
                .build();
    }

}
