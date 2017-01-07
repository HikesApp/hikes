package com.chekalin.hikes;

import com.chekalin.hikes.dto.HikeDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyArray;
import static org.junit.Assert.assertThat;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HikesApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void loadsHikes() {
		HikeDto[] result = restTemplate.getForObject("/hikes", HikeDto[].class);

		assertThat(result, is(not(emptyArray())));
	}

	@Test
	public void createsAndGetsHike() {
		HikeDto hike = HikeDto.builder().name("testHike").build();
		ResponseEntity<HikeDto> result = restTemplate.postForEntity("/hikes", hike, HikeDto.class);

		assertThat(result.getStatusCode(), is(HttpStatus.CREATED));

		HikeDto savedHike = result.getBody();
		assertThat(savedHike.getName(), is(equalTo(hike.getName())));

		String location = result.getHeaders().getLocation().toString();
		HikeDto loadedHike = restTemplate.getForObject(location, HikeDto.class);
		assertThat(loadedHike.getName(), is(equalTo(hike.getName())));
	}

}
