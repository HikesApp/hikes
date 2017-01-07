package com.chekalin.hikes;

import com.chekalin.hikes.dto.HikeDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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

}
