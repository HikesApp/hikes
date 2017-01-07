package com.chekalin.hikes.mappers;

import com.chekalin.hikes.domain.Hike;
import com.chekalin.hikes.dto.HikeDto;
import org.springframework.stereotype.Component;

@Component
public class HikeMapper {

    public HikeDto toDto(Hike hike) {
        return HikeDto.builder()
                .name(hike.getName())
                .startDate(hike.getStartDate())
                .endDate(hike.getEndDate())
                .distance(hike.getDistance())
                .build();
    }

}
