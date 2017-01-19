package com.chekalin.hikes.web;

import com.chekalin.hikes.service.HikeService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HikesControllerTest {

    @InjectMocks
    private HikesController hikesController;

    @Mock
    private HikeService hikeService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void loadAllHikesShouldReturnAllHikesFromService() throws Exception {
        List<HikeDto> expectedHikes = newArrayList();

        given(hikeService.loadHikes()).willReturn(expectedHikes);

        List<HikeDto> result = hikesController.loadAllHikes();

        assertThat(result, is(sameInstance(expectedHikes)));
    }

    @Test
    public void createHikeShouldCreateNewHike() throws Exception {
        String savedHikeId = "hikeId";
        HikeDto newHike = HikeDto.builder().name("newHike").build();
        HikeDto savedHike = HikeDto.builder().id(savedHikeId).build();
        given(hikeService.createHike(newHike)).willReturn(savedHike);

        ResponseEntity<HikeDto> result = hikesController.createHike(newHike);

        assertThat(result.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(result.getHeaders().getLocation().toString(), containsString("/hikes"));
        assertThat(result.getHeaders().getLocation().toString(), containsString(savedHikeId));
        assertThat(result.getBody(), is(sameInstance(savedHike)));
    }

    @Test
    public void loadShouldReturnHikeFromService() throws Exception {
        UUID hikeId = UUID.randomUUID();
        HikeDto expectedLoadedDto = new HikeDto();
        given(hikeService.loadById(hikeId)).willReturn(expectedLoadedDto);

        HikeDto result = hikesController.load(hikeId);

        assertThat(result, is(sameInstance(expectedLoadedDto)));
    }

    @Test
    public void deleteHikeShouldDeleteHike() throws Exception {
        UUID id = UUID.randomUUID();

        hikesController.deleteHike(id);

        verify(hikeService).deleteHike(id);
    }
}