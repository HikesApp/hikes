package com.chekalin.hikes.services;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.exceptions.HikeNotFoundException;
import com.chekalin.hikes.repositories.HikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

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

    public Hike loadById(String id) {
        return hikeRepository.findOne(id).orElseThrow(HikeNotFoundException::new);
    }
}
