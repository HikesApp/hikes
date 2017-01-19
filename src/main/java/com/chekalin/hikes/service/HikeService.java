package com.chekalin.hikes.service;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.domain.HikeRepository;
import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.exceptions.HikeWithIdPassedToCreateException;
import com.chekalin.hikes.web.HikeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HikeService {

    private final HikeMapper hikeMapper;
    private final HikeRepository hikeRepository;
    private final IdGenerator idGenerator;

    public List<HikeDto> loadHikes() {
        return hikeRepository.findAll().stream()
                .map(hikeMapper::toDto)
                .collect(Collectors.toList());
    }

    public HikeDto createHike(HikeDto hikeDto) {
        if (hikeDto.getId() != null) {
            throw new HikeWithIdPassedToCreateException();
        }
        Hike hike = hikeMapper.toDomain(hikeDto);
        hike.setId(idGenerator.generateId());
        Hike savedHike = hikeRepository.save(hike);
        return hikeMapper.toDto(savedHike);
    }

    public HikeDto loadById(UUID id) {
        Hike hike = findHikeOrThrowException(id);
        return hikeMapper.toDto(hike);
    }

    public void deleteHike(UUID id) {
        Hike hike = findHikeOrThrowException(id);
        hikeRepository.delete(hike);
    }

    private Hike findHikeOrThrowException(UUID id) {
        return hikeRepository.findById(id).orElseThrow(HikeNotFoundException::new);
    }
}
