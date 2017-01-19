package com.chekalin.hikes.web;

import com.chekalin.hikes.web.HikeDto;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class HikeDtoValidationTest {

    private Validator validator;

    private HikeDto validHike;

    @Before
    public void setUp() throws Exception {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        validHike = HikeDto.builder()
                .name("testName")
                .startDate(LocalDate.now())
                .build();
    }

    @Test
    public void nameIsNull() throws Exception {
        validHike.setName(null);

        Set<ConstraintViolation<HikeDto>> result = validator.validate(validHike);

        assertThat(result, hasSize(1));
        assertThat(result.iterator().next().getPropertyPath().toString(), is("name"));
    }

    @Test
    public void startDateIsNull() throws Exception {
        validHike.setStartDate(null);

        Set<ConstraintViolation<HikeDto>> result = validator.validate(validHike);

        assertThat(result, hasSize(1));
        assertThat(result.iterator().next().getPropertyPath().toString(), is("startDate"));
    }
}