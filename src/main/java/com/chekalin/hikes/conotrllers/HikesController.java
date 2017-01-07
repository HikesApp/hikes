package com.chekalin.hikes.conotrllers;

import com.chekalin.hikes.dto.HikeDto;
import com.chekalin.hikes.mappers.HikeMapper;
import com.chekalin.hikes.repositories.HikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hikes")
public class HikesController {

    private final HikeRepository hikeRepository;

    private final HikeMapper hikeMapper;

    @Autowired
    public HikesController(HikeRepository hikeRepository, HikeMapper hikeMapper) {
        this.hikeRepository = hikeRepository;
        this.hikeMapper = hikeMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<HikeDto> getHikes() {
        return hikeRepository.findAll().stream()
                .map(hikeMapper::toDto)
                .collect(Collectors.toList());
    }

}
