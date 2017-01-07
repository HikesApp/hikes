package com.chekalin.hikes.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HIKES")
public class Hike {

    @Id
    private UUID id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal distance;

}
