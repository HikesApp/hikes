package com.chekalin.hikes.service;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.domain.HikeRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.IdGenerator;

import java.util.*;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void loadsAllHikes() throws Exception {
        List<Hike> expectedHikeList = new ArrayList<>();
        given(hikeRepository.findAll()).willReturn(expectedHikeList);

        Collection<Hike> hikes = hikeService.loadHikes();

        assertThat(hikes, is(sameInstance(expectedHikeList)));
    }

    @Test
    public void savesNewHike() throws Exception {
        UUID hikeId = UUID.randomUUID();
        given(idGenerator.generateId()).willReturn(hikeId);
        Hike expectedSavedHike = Hike.builder().build();
        given(hikeRepository.save(hikeArgumentCaptor.capture())).willReturn(expectedSavedHike);

        Hike result = hikeService.createHike(Hike.builder().build());

        Hike hikeBeforeSave = hikeArgumentCaptor.getValue();
        assertThat(hikeBeforeSave.getId(), is(equalTo(hikeId)));
        assertThat(result, is(sameInstance(expectedSavedHike)));
    }

    @Test
    public void loadsHike() throws Exception {
        UUID hikeId = UUID.randomUUID();
        Hike expectedHike = Hike.builder().build();
        given(hikeRepository.findById(hikeId)).willReturn(Optional.of(expectedHike));

        Hike result = hikeService.loadById(hikeId);

        assertThat(result, is(sameInstance(expectedHike)));
    }

    @Test
    public void throwsExceptionWhenHikeNotFoundOnFind() throws Exception {
        UUID hikeId = UUID.randomUUID();
        given(hikeRepository.findById(hikeId)).willReturn(Optional.empty());

        expectedException.expect(HikeNotFoundException.class);

        hikeService.loadById(hikeId);
    }

    @Test
    public void throwsExceptionWhenHikeNotFoundOnDelete() throws Exception {
        UUID hikeId = UUID.randomUUID();

        given(hikeRepository.findById(hikeId)).willReturn(Optional.empty());
        expectedException.expect(HikeNotFoundException.class);

        hikeService.deleteHike(hikeId);
    }

    @Test
    public void deletesHike() throws Exception {
        UUID hikeId = UUID.randomUUID();
        Hike hike = Hike.builder().build();
        given(hikeRepository.findById(hikeId)).willReturn(Optional.of(hike));

        hikeService.deleteHike(hikeId);

        verify(hikeRepository).delete(same(hike));
    }
}