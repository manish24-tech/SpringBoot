package com.org.epaperintegration.component;

import java.io.IOException;

import com.org.epaperintegration.exception.ResourceExistsException;
import com.org.epaperintegration.exception.ResourceInvalidException;
import com.org.epaperintegration.exception.ResourceNotFoundException;
import com.org.epaperintegration.repository.EpaperRepository;
import com.org.epaperintegration.validation.XmlValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@Component
@Slf4j
public class FileManager {

    @Autowired
    private EpaperRepository epaperRepository;

    public void validateFile(MultipartFile file) {
        if (null == file || file.isEmpty()) {
            throw new ResourceNotFoundException("File_Not_Found", "File is not yet uploaded");
        }

        Boolean isFileExists = epaperRepository.isFileNameExists(getFileName(file));
        if (Boolean.TRUE.equals(isFileExists)) {
            throw new ResourceExistsException("File_Already_Exists", "file is present with name : ".concat(getFileName(file)));
        }
    }

    public void validateXMLAgainstXSD(MultipartFile file, String xsdFileLocation) {
        XmlValidator xmlValidator = new XmlValidator(xsdFileLocation);
        try {
            xmlValidator.isValid(file.getInputStream());
        } catch (SAXException | IOException e) {
            throw new ResourceInvalidException("Invalid_XML_File", ExceptionUtils.getMessage(e));
        }
    }

    public Document getDocument(MultipartFile file) {
        try {
            // Parse the XML file into a DOM Document object
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file.getInputStream());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("Error while parsing XML file into DOM document object due to exception : {}", ExceptionUtils.getStackTrace(e));
            throw new ResourceInvalidException("FAILED_TO_PARSE_XML", "Unable to parse xml and create DOM document object");
        }
    }

    public String getFileName(MultipartFile file) {
        return StringUtils.cleanPath(file.getOriginalFilename());
    }
}
