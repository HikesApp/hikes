package com.chekalin.hikes.repositories;

import com.chekalin.hikes.domain.Hike;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Repository
public class HikeRepository {

    public List<Hike> findAll() {
        return newArrayList(Hike.builder()
                .name("Seven Sisters")
                .startDate(LocalDate.of(2016, Month.FEBRUARY, 11))
                .distance(new BigDecimal(35))
                .build());
    }

}
