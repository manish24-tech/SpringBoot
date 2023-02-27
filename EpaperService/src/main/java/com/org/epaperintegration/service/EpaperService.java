package com.org.epaperintegration.service;

import com.org.epaperintegration.dto.EpaperDTO;
import com.org.epaperintegration.model.Epaper;
import com.org.epaperintegration.repository.EpaperRepository;
import com.org.epaperintegration.component.FileManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;

import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EpaperService {

    private final EpaperRepository epaperRepository;
    private final FileManager fileManager;

    public void createEpaper(MultipartFile multipartFile) {
        fileManager.validateFile(multipartFile);
        fileManager.validateXMLAgainstXSD(multipartFile, Paths.get("xml", "epaper.xsd").toString());
        // Process the XML document
        Document document = fileManager.getDocument(multipartFile);
        //  Get the value of the newspaperName element
        Element newspaperNameElement = (Element) document.getElementsByTagName("newspaperName").item(0);
        String newspaperNameValue = newspaperNameElement.getTextContent();
        log.info("News paper name: {}",newspaperNameValue);
        // Get the attributes of screenInfo element
        Element screenInfoElements = (Element) document.getElementsByTagName("screenInfo").item(0);
        String width = screenInfoElements.getAttribute("width");
        String height = screenInfoElements.getAttribute("height");
        String dpi = screenInfoElements.getAttribute("dpi");
        log.info("Width: {} | height: {} | dpi: {}",width, height, dpi); // Output: 1280
        // Store epaper details to db
        Epaper epaper = new Epaper();
        epaper.setFileName(fileManager.getFileName(multipartFile));
        epaper.setNewsPaperName(newspaperNameValue);
        epaper.setHeight(Integer.valueOf(height));
        epaper.setWidth(Integer.valueOf(width));
        epaper.setDpi(Integer.valueOf(dpi));
        epaper.setUploadTime(LocalDateTime.now());
        epaperRepository.save(epaper);
    }

    public EpaperDTO retriveEpaper(String newsPaperName, Pageable pageable) {
        Page<Epaper> epaper;
        if (StringUtils.isBlank(newsPaperName)) {
            epaper =  epaperRepository.findAll(pageable);
        } else {
            epaper =  epaperRepository.findByNewsPaperName(newsPaperName, pageable);
        }

        return new EpaperDTO(epaper.getContent(), epaper.getTotalElements(), epaper.getTotalPages(), epaper.getNumber());
    }
}