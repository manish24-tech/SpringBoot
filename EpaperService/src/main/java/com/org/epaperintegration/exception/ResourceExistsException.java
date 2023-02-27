package com.org.epaperintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResourceExistsException extends RuntimeException {

    private final String error;
    private final String errorDescription;

}
