package com.chekalin.hikes.conotrllers;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.dto.HikeDto;
import com.chekalin.hikes.mappers.HikeMapper;
import com.chekalin.hikes.services.HikeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class HikesControllerTest {

    @InjectMocks
    private HikesController hikesController;

    @Mock
    private HikeMapper hikeMapper;

    @Mock
    private HikeService hikeService;

    @Test
    public void loadsAllHikesFromServiceAndMapsThem() throws Exception {
        Hike testHike1 = Hike.builder()
                .name("testHike1")
                .build();
        Hike testHike2 = Hike.builder()
                .name("testHike2")
                .build();

        given(hikeService.loadHikes()).willReturn(newArrayList(testHike1, testHike2));
        HikeDto expectedHike1 = HikeDto.builder().name("mapped1").build();
        given(hikeMapper.toDto(testHike1)).willReturn(expectedHike1);
        HikeDto expectedHike2 = HikeDto.builder().name("mapped2").build();
        given(hikeMapper.toDto(testHike2)).willReturn(expectedHike2);

        List<HikeDto> result = hikesController.loadAllHikes();

        assertThat(result, hasSize(2));
        assertThat(result, hasItems(expectedHike1, expectedHike2));
    }

    @Test
    public void createsNewHike() throws Exception {
        HikeDto newHikeDto = HikeDto.builder().name("newHike").build();
        Hike hike = Hike.builder().build();
        given(hikeMapper.toDomain(newHikeDto)).willReturn(hike);
        Hike savedHike = Hike.builder().id(UUID.randomUUID()).build();
        given(hikeService.createHike(hike)).willReturn(savedHike);
        HikeDto expectedMappedHike = HikeDto.builder().build();
        given(hikeMapper.toDto(savedHike)).willReturn(expectedMappedHike);

        ResponseEntity<HikeDto> result = hikesController.createHike(newHikeDto);

        assertThat(result.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(result.getHeaders().getLocation().toString(), containsString("/hikes"));
        assertThat(result.getHeaders().getLocation().toString(), containsString(savedHike.getId().toString()));
        assertThat(result.getBody(), is(sameInstance(expectedMappedHike)));
    }

    @Test
    public void loadsSingleHike() throws Exception {
        UUID hikeId = UUID.randomUUID();
        Hike loadedHike = Hike.builder().build();
        given(hikeService.loadById(hikeId)).willReturn(loadedHike);
        HikeDto expectedLoadedDto = new HikeDto();
        given(hikeMapper.toDto(loadedHike)).willReturn(expectedLoadedDto);

        HikeDto result = hikesController.load(hikeId);

        assertThat(result, is(sameInstance(expectedLoadedDto)));
    }
}