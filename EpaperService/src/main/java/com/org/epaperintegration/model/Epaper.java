package com.org.epaperintegration.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Epapers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Epaper {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "newspaper_name")
    private String newsPaperName;

    private Integer width;

    private Integer height;

    private Integer dpi;

    private String fileName;
    private LocalDateTime uploadTime;

}