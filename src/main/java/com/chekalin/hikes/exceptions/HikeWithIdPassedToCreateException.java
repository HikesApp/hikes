package com.chekalin.hikes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "ID should be empty for new hikes")
public class HikeWithIdPassedToCreateException extends RuntimeException {
}
