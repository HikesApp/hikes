package com.chekalin.hikes.services;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.repositories.HikeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class HikeServiceTest {

    @InjectMocks
    private HikeService hikeService;

    @Mock
    private HikeRepository hikeRepository;

    @Mock
    private IdGenerator idGenerator;

    @Captor
    private ArgumentCaptor<Hike> hikeArgumentCaptor;

    @Test
    public void loadsAllHikes() throws Exception {
        List<Hike> expectedHikeList = new ArrayList<>();
        given(hikeRepository.findAll()).willReturn(expectedHikeList);

        Collection<Hike> hikes = hikeService.loadHikes();

        assertThat(hikes, is(sameInstance(expectedHikeList)));
    }

    @Test
    public void savesNewHike() throws Exception {
        given(idGenerator.generateId()).willReturn("newHikeId");
        Hike expectedSavedHike = Hike.builder().build();
        given(hikeRepository.save(hikeArgumentCaptor.capture())).willReturn(expectedSavedHike);

        Hike result = hikeService.createHike(Hike.builder().build());

        Hike hikeBeforeSave = hikeArgumentCaptor.getValue();
        assertThat(hikeBeforeSave.getId(), is(equalTo("newHikeId")));
        assertThat(result, is(sameInstance(expectedSavedHike)));
    }

    @Test
    public void loadsHike() throws Exception {
        String hikeId = "hikeId";
        Hike expectedHike = Hike.builder().build();
        given(hikeRepository.findOne(hikeId)).willReturn(Optional.of(expectedHike));

        Hike result = hikeService.loadById(hikeId);

        assertThat(result, is(sameInstance(expectedHike)));
    }

    @Test(expected = HikeNotFoundException.class)
    public void throwsExceptionWhenHikeNotFound() throws Exception {
        String hikeId = "hikeId";
        given(hikeRepository.findOne(hikeId)).willReturn(Optional.empty());

        hikeService.loadById(hikeId);
    }
}