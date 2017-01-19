package com.chekalin.hikes.web;

import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.exceptions.HikeWithIdPassedToCreateException;
import com.chekalin.hikes.service.HikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HikesController.class)
public class HikesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HikeService hikeService;

    @Test
    public void loadAllHikesShouldReturnAllHikes() throws Exception {
        HikeDto hike = HikeDto.builder().id("testId").build();
        given(hikeService.loadHikes()).willReturn(newArrayList(hike));

        mockMvc.perform(MockMvcRequestBuilders.get("/hikes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id': 'testId'}]"));
    }

    @Test
    public void loadHikeWhenHikeExistsShouldReturnHike() throws Exception {
        UUID hikeId = UUID.randomUUID();

        HikeDto hike = HikeDto.builder().id(hikeId.toString()).name("testHike").build();
        given(hikeService.loadById(hikeId)).willReturn(hike);

        mockMvc.perform(MockMvcRequestBuilders.get("/hikes/" + hikeId))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id': '" + hikeId + "'}"));
    }

    @Test
    public void loadHikeWhenHikeDoesNotExistShouldReturn404() throws Exception {
        UUID hikeId = UUID.randomUUID();
        doThrow(HikeNotFoundException.class).when(hikeService).loadById(hikeId);

        mockMvc.perform(MockMvcRequestBuilders.get("/hikes/" + hikeId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createHikeShouldReturn201AndLocationHeader() throws Exception {
        String hikeId = UUID.randomUUID().toString();
        HikeDto expectedSavedHike = HikeDto.builder().id(hikeId).build();
        given(hikeService.createHike(any())).willReturn(expectedSavedHike);

        HikeDto newHike = HikeDto.builder().name("testHike").startDate(LocalDate.of(2017, Month.JANUARY, 18)).build();
        mockMvc.perform(post("/hikes").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newHike)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/hikes/" + hikeId))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedSavedHike)));
    }

    @Test
    public void createHikeWheHikeIsInvalidShouldReturnBadRequest() throws Exception {
        HikeDto invalidHike = HikeDto.builder().build();
        mockMvc.perform(post("/hikes").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidHike)))
                .andExpect(status().isBadRequest())
                .andDo(result -> verifyZeroInteractions(hikeService));
    }

    @Test
    public void createHikeWheHikeHasIdShouldReturnBadRequestWhenServiceThrowsHikeWithIdPassedToCreateException() throws Exception {
        HikeDto hike = HikeDto.builder().id("someId").name("hikeName").startDate(LocalDate.now()).build();
        given(hikeService.createHike(hike)).willThrow(new HikeWithIdPassedToCreateException());

        mockMvc.perform(post("/hikes").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hike)))
                .andExpect(status().isBadRequest())
                .andDo(result -> verify(hikeService).createHike(hike));
    }

    @Test
    public void deleteHikeShouldDeleteHike() throws Exception {
        UUID hikeId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/hikes/" + hikeId))
                .andExpect(status().isOk())
                .andDo(result -> verify(hikeService).deleteHike(hikeId));
    }

    @Test
    public void deleteHikeWhenHikeDoesNotExistShouldReturn404() throws Exception {
        UUID hikeId = UUID.randomUUID();
        doThrow(HikeNotFoundException.class).when(hikeService).deleteHike(hikeId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/hikes/" + hikeId))
                .andExpect(status().isNotFound());
    }
}