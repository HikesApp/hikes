package com.chekalin.hikes.service;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.web.HikeDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HikeMapper {

    public HikeDto toDto(Hike hike) {
        return HikeDto.builder()
                .id(hike.getId().toString())
                .name(hike.getName())
                .startDate(hike.getStartDate())
                .endDate(hike.getEndDate())
                .distance(hike.getDistance())
                .build();
    }

    public Hike toDomain(HikeDto hikeDto) {
        return Hike.builder()
                .id(hikeDto.getId() != null ? UUID.fromString(hikeDto.getId()) : null)
                .name(hikeDto.getName())
                .startDate(hikeDto.getStartDate())
                .endDate(hikeDto.getEndDate())
                .distance(hikeDto.getDistance())
                .build();
    }
}
