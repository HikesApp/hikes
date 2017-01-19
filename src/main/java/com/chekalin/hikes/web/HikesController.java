package com.chekalin.hikes.web;

import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.exceptions.HikeWithIdPassedToCreateException;
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

@RestController
@RequestMapping("/hikes")
@AllArgsConstructor
public class HikesController {

    private final HikeService hikeService;

    @RequestMapping(method = RequestMethod.GET)
    public List<HikeDto> loadAllHikes() {
        return hikeService.loadHikes();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public HikeDto load(@PathVariable UUID id) {
        return hikeService.loadById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<HikeDto> createHike(@RequestBody @Valid HikeDto hikeDto) {
        HikeDto savedHike = hikeService.createHike(hikeDto);

        URI location = UriComponentsBuilder
                .fromPath("/hikes/{id}")
                .buildAndExpand(savedHike.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(savedHike);
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
