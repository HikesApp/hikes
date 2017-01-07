package com.chekalin.hikes.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Hike {

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal distance;

}
