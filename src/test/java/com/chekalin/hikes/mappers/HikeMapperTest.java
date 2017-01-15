package com.chekalin.hikes.mappers;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.dto.HikeDto;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class HikeMapperTest {

    private HikeMapper hikeMapper = new HikeMapper();

    @Test
    public void mapsToDto() throws Exception {
        Hike hike = Hike.builder()
                .id(UUID.randomUUID())
                .name("testHike")
                .startDate(LocalDate.of(2016, Month.APRIL, 18))
                .endDate(LocalDate.of(2016, Month.APRIL, 21))
                .distance(new BigDecimal(150))
                .build();

        HikeDto hikeDto = hikeMapper.toDto(hike);

        assertThat(hikeDto.getId(), is(equalTo(hike.getId().toString())));
        assertThat(hikeDto.getName(), is(equalTo(hike.getName())));
        assertThat(hikeDto.getStartDate(), is(equalTo(hike.getStartDate())));
        assertThat(hikeDto.getEndDate(), is(equalTo(hike.getEndDate())));
        assertThat(hikeDto.getDistance(), is(closeTo(hike.getDistance(), BigDecimal.ZERO)));
    }

    @Test
    public void mapsToDomain() throws Exception {
        UUID hikeId = UUID.randomUUID();
        HikeDto hikeDto = HikeDto.builder()
                .id(hikeId.toString())
                .name("testHike")
                .startDate(LocalDate.of(2016, Month.APRIL, 18))
                .endDate(LocalDate.of(2016, Month.APRIL, 20))
                .distance(new BigDecimal(120))
                .build();

        Hike hike = hikeMapper.toDomain(hikeDto);

        assertThat(hike.getId(), is(equalTo(hikeId)));
        assertThat(hike.getName(), is(equalTo(hikeDto.getName())));
        assertThat(hike.getStartDate(), is(equalTo(hikeDto.getStartDate())));
        assertThat(hike.getEndDate(), is(equalTo(hikeDto.getEndDate())));
        assertThat(hike.getDistance(), is(equalTo(hikeDto.getDistance())));
    }

    @Test
    public void mapsToDomainWithoutId() throws Exception {
        HikeDto hikeDto = HikeDto.builder().build();

        Hike hike = hikeMapper.toDomain(hikeDto);

        assertThat(hike.getId(), is(nullValue()));
    }
}