package com.chekalin.hikes.conotrllers;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.dto.HikeDto;
import com.chekalin.hikes.mappers.HikeMapper;
import com.chekalin.hikes.services.HikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hikes")
@AllArgsConstructor
public class HikesController {

    private final HikeMapper hikeMapper;

    private final HikeService hikeService;

    @RequestMapping(method = RequestMethod.GET)
    public List<HikeDto> loadAllHikes() {
        return hikeService.loadHikes().stream()
                .map(hikeMapper::toDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HikeDto load(@PathVariable String id) {
        return hikeMapper.toDto(hikeService.loadById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HikeDto> createHike(@RequestBody HikeDto hikeDto) {
        Hike hike = hikeMapper.toDomain(hikeDto);
        Hike savedHike = hikeService.createHike(hike);

        URI location = UriComponentsBuilder
                .fromPath("/hikes/{id}")
                .buildAndExpand(savedHike.getId())
                .toUri();

        HikeDto result = hikeMapper.toDto(savedHike);

        return ResponseEntity
                .created(location)
                .body(result);
    }

}
