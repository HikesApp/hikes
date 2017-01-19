package com.chekalin.hikes.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@JsonTest
public class HikeDtoJsonIT {

    @Autowired
    private JacksonTester<HikeDto> json;

    @Test
    public void serializesToJson() throws Exception {
        HikeDto hike = HikeDto.builder()
                .id(UUID.randomUUID().toString())
                .name("testHike")
                .startDate(LocalDate.of(2017, Month.JANUARY, 18))
                .endDate(LocalDate.of(2017, Month.JANUARY, 19))
                .distance(new BigDecimal(65))
                .build();

        JsonContent<HikeDto> result = json.write(hike);

        result.assertThat().extractingJsonPathStringValue("@.name").isEqualTo("testHike");
        result.assertThat().extractingJsonPathStringValue("@.startDate").isEqualTo("2017-01-18");
        result.assertThat().extractingJsonPathStringValue("@.endDate").isEqualTo("2017-01-19");
        result.assertThat().extractingJsonPathNumberValue("@.distance").isEqualTo(65);
    }

    @Test
    public void deserializesJson() throws Exception {
        String id = UUID.randomUUID().toString();
        String content = "{" +
                "\"id\": \"" + id + "\"," +
                "\"name\": \"testHike\"," +
                "\"startDate\": \"2017-01-18\"," +
                "\"endDate\": \"2017-01-19\"," +
                "\"distance\": \"65\"" +
                "}";

        HikeDto result = json.parseObject(content);

        assertThat(result.getId(), is(equalTo(id)));
        assertThat(result.getName(), is(equalTo("testHike")));
        assertThat(result.getStartDate(), is(equalTo(LocalDate.of(2017, Month.JANUARY, 18))));
        assertThat(result.getEndDate(), is(equalTo(LocalDate.of(2017, Month.JANUARY, 19))));
        assertThat(result.getDistance(), is(closeTo(new BigDecimal(65), BigDecimal.ZERO)));
    }
}
