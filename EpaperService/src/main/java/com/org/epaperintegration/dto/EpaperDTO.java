package com.org.epaperintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.org.epaperintegration.model.Epaper;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EpaperDTO {

    @JsonProperty("content")
    private List<Epaper> content = new ArrayList<>();

    @JsonProperty("total_elements")
    private Long totalElements;

    @JsonProperty("total_page")
    private Integer totalPage;

    @JsonProperty("current_page")
    private Integer currentPage;
}
