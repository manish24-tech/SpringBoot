package com.org.epaperintegration.controller;

import com.org.epaperintegration.dto.EpaperDTO;
import com.org.epaperintegration.exception.ErrorResponse;
import com.org.epaperintegration.service.EpaperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Epaper", description = "Manage epaper requests")
@RestController
@RequestMapping("/api/epaper")
@Validated
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EpaperController {

    private final EpaperService epaperService;

    @Operation(summary = "Upload a file to store epaper details")
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = String.class))
    })
    @ApiResponse(responseCode = "400", content = {
            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = APPLICATION_JSON_VALUE)
    })
    @ApiResponse(responseCode = "500", content = {
            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = APPLICATION_JSON_VALUE)
    })
    @PostMapping(value = "/upload-xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createNewsPaper(@RequestParam(required = true, name = "file") MultipartFile file) {
        epaperService.createEpaper(file);
        return new ResponseEntity<>("News paper is created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "search a filtered epapers")
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = EpaperDTO.class), mediaType = APPLICATION_JSON_VALUE)
    })
    @ApiResponse(responseCode = "404", content = {
            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = APPLICATION_JSON_VALUE)
    })
    @ApiResponse(responseCode = "500", content = {
            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = APPLICATION_JSON_VALUE)
    })
    @GetMapping("/search")
    public ResponseEntity<EpaperDTO> serachNewsPaper(
            @RequestParam(defaultValue = "") String newsPaperName,
            @ParameterObject Pageable pageable) {
        return new ResponseEntity<>( epaperService.retriveEpaper(newsPaperName, pageable), HttpStatus.OK);
    }
}
