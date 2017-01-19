package com.chekalin.hikes.service;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.domain.HikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.Collection;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HikeService {

    private final HikeRepository hikeRepository;
    private final IdGenerator idGenerator;

    public Collection<Hike> loadHikes() {
        return hikeRepository.findAll();
    }

    public Hike createHike(Hike hike) {
        hike.setId(idGenerator.generateId());
        return hikeRepository.save(hike);
    }

    public Hike loadById(UUID id) {
        return hikeRepository.findById(id).orElseThrow(HikeNotFoundException::new);
    }

    public void deleteHike(UUID id) {
        Hike hike = loadById(id);
        hikeRepository.delete(hike);
    }
}
