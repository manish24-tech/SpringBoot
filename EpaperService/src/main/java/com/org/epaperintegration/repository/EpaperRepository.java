package com.org.epaperintegration.repository;

import com.org.epaperintegration.model.Epaper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EpaperRepository extends JpaRepository<Epaper, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM Epaper e WHERE e.fileName = ?1")
    Boolean isFileNameExists(String fileName);

    Page<Epaper> findByNewsPaperName(String newsPaperName, Pageable pageable);
}
