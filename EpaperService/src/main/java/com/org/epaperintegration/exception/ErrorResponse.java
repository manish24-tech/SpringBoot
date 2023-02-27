package com.org.epaperintegration.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Builder
@Setter
@Getter
public class ErrorResponse {

    private final String reason;
    private final String code;
    private final long timestamp;
    private final Collection<String> errors;

    public ErrorResponse(String reason, String code, long timestamp, Collection<String> errors) {
        this.reason = reason;
        this.code = code;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public ErrorResponse(String reason, String code, long timestamp, String error) {
        this.reason = reason;
        this.code = code;
        this.timestamp = timestamp;
        this.errors = List.of(error);
    }
}
