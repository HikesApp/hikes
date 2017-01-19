package com.chekalin.hikes.service;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.domain.HikeRepository;
import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.exceptions.HikeWithIdPassedToCreateException;
import com.chekalin.hikes.web.HikeDto;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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
    private HikeMapper hikeMapper;

    @Mock
    private IdGenerator idGenerator;

    @Captor
    private ArgumentCaptor<Hike> hikeArgumentCaptor;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void loadHikesShouldLoadAllHikesFromRepository() throws Exception {
        Hike hikeEntity = new Hike();
        List<Hike> expectedHikeList = newArrayList(hikeEntity);
        given(hikeRepository.findAll()).willReturn(expectedHikeList);
        HikeDto expectedDto = new HikeDto();
        given(hikeMapper.toDto(hikeEntity)).willReturn(expectedDto);

        Collection<HikeDto> hikes = hikeService.loadHikes();

        assertThat(hikes, hasSize(1));
        assertThat(hikes, hasItem(expectedDto));
    }

    @Test
    public void createHikeShouldMapToEntityAndSaveInRepository() throws Exception {
        HikeDto newHikeDto = HikeDto.builder().build();

        UUID hikeId = UUID.randomUUID();
        given(idGenerator.generateId()).willReturn(hikeId);

        Hike mappedEntity = Hike.builder().build();
        given(hikeMapper.toDomain(newHikeDto)).willReturn(mappedEntity);

        Hike expectedSavedHike = Hike.builder().build();
        given(hikeRepository.save(mappedEntity)).willReturn(expectedSavedHike);

        HikeDto expectedSavedHikeDto = new HikeDto();
        given(hikeMapper.toDto(expectedSavedHike)).willReturn(expectedSavedHikeDto);

        HikeDto result = hikeService.createHike(newHikeDto);

        verify(hikeRepository).save(hikeArgumentCaptor.capture());
        Hike hikeBeforeSave = hikeArgumentCaptor.getValue();
        assertThat(hikeBeforeSave.getId(), is(equalTo(hikeId)));
        assertThat(result, is(sameInstance(expectedSavedHikeDto)));
    }

    @Test
    public void createHikeWhenHikeHasIdShouldThrowException() throws Exception {
        expectedException.expect(HikeWithIdPassedToCreateException.class);

        HikeDto hikeWithId = HikeDto.builder().id("someId").build();
        hikeService.createHike(hikeWithId);
    }

    @Test
    public void loadByIdShouldLoadHikeFromRepository() throws Exception {
        UUID hikeId = UUID.randomUUID();
        Hike hike = Hike.builder().build();
        given(hikeRepository.findById(hikeId)).willReturn(Optional.of(hike));

        HikeDto expectedDto = HikeDto.builder().build();
        given(hikeMapper.toDto(hike)).willReturn(expectedDto);

        HikeDto result = hikeService.loadById(hikeId);

        assertThat(result, is(sameInstance(expectedDto)));
    }

    @Test
    public void loadByIdWhenHikeDoesNotExistShouldThrowException() throws Exception {
        UUID hikeId = UUID.randomUUID();
        given(hikeRepository.findById(hikeId)).willReturn(Optional.empty());

        expectedException.expect(HikeNotFoundException.class);

        hikeService.loadById(hikeId);
    }

    @Test
    public void deleteHikeWhenHikeDoesNotExistShouldThrowException() throws Exception {
        UUID hikeId = UUID.randomUUID();

        given(hikeRepository.findById(hikeId)).willReturn(Optional.empty());
        expectedException.expect(HikeNotFoundException.class);

        hikeService.deleteHike(hikeId);
    }

    @Test
    public void deleteHikeShouldDeleteHikeFromRepository() throws Exception {
        UUID hikeId = UUID.randomUUID();
        Hike hike = Hike.builder().build();
        given(hikeRepository.findById(hikeId)).willReturn(Optional.of(hike));

        hikeService.deleteHike(hikeId);

        verify(hikeRepository).delete(same(hike));
    }
}