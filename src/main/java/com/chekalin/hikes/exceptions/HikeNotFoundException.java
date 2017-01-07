package com.chekalin.hikes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Hike")
public class HikeNotFoundException extends RuntimeException {
}
