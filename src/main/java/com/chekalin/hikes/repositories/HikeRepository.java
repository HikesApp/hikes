package com.chekalin.hikes.repositories;

import com.chekalin.hikes.domain.Hike;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Repository
public class HikeRepository {


    private Map<String, Hike> hikes = new HashMap<>();

    @PostConstruct
    public void mockData() {
        Hike mockHike = Hike.builder()
                .id(UUID.randomUUID().toString())
                .name("Seven Sisters")
                .startDate(LocalDate.of(2016, Month.FEBRUARY, 11))
                .distance(new BigDecimal(35))
                .build();

        hikes.put(mockHike.getId(), mockHike);
    }


    public Collection<Hike> findAll() {
        return hikes.values();
    }

    public Hike save(Hike hike) {
        hikes.put(hike.getId(), hike);
        return hike;
    }

    public Optional<Hike> findOne(String id) {
        return Optional.of(hikes.get(id));
    }
}
