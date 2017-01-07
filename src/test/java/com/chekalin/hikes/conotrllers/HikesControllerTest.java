package com.chekalin.hikes.conotrllers;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.dto.HikeDto;
import com.chekalin.hikes.mappers.HikeMapper;
import com.chekalin.hikes.repositories.HikeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class HikesControllerTest {

    @InjectMocks
    private HikesController hikesController;

    @Mock
    private HikeRepository hikeRepository;

    @Mock
    private HikeMapper hikeMapper;

    @Test
    public void loadsAllHikesFromRepositoryAndMapsThem() throws Exception {
        Hike testHike1 = Hike.builder()
                .name("testHike1")
                .build();
        Hike testHike2 = Hike.builder()
                .name("testHike2")
                .build();

        given(hikeRepository.findAll()).willReturn(newArrayList(testHike1, testHike2));
        HikeDto expectedHike1 = HikeDto.builder().name("mapped1").build();
        given(hikeMapper.toDto(testHike1)).willReturn(expectedHike1);
        HikeDto expectedHike2 = HikeDto.builder().name("mapped2").build();
        given(hikeMapper.toDto(testHike2)).willReturn(expectedHike2);

        List<HikeDto> result = hikesController.getHikes();

        assertThat(result, hasSize(2));
        assertThat(result, hasItems(expectedHike1, expectedHike2));
    }
}