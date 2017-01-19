package com.chekalin.hikes.web;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.exceptions.HikeWithIdPassedToCreateException;
import com.chekalin.hikes.service.HikeMapper;
import com.chekalin.hikes.service.HikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
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
    public HikeDto load(@PathVariable UUID id) {
        return hikeMapper.toDto(hikeService.loadById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HikeDto> createHike(@RequestBody @Valid HikeDto hikeDto) {
        if (hikeDto.getId() != null) {
            throw new HikeWithIdPassedToCreateException();
        }

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

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteHike(@PathVariable UUID id) {
        hikeService.deleteHike(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Hike")
    private void handleHikeNotFound(HikeNotFoundException ex) {
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "ID should be empty for new hikes")
    private void handleHikeWithIdPassedToCreate(HikeWithIdPassedToCreateException ex) {
    }

}
