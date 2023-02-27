package com.org.epaperintegration.validation;

import com.org.epaperintegration.exception.ResourceInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static javax.xml.validation.SchemaFactory.newInstance;

@Slf4j
public class XmlValidator {

    private String xsdPath;
    private String xmlPath;

    public XmlValidator(String xsdPath) {
        this.xsdPath = xsdPath;
    }

    public XmlValidator(String xsdPath, String xmlPath) {
        this.xsdPath = xsdPath;
        this.xmlPath = xmlPath;
    }

    public boolean isValid() throws IOException, SAXException {
        Validator validator = initValidator(xsdPath);
        try {
            validator.validate(new StreamSource(getFile(xmlPath)));
            return true;
        } catch (SAXException e) {
            return false;
        }
    }

    public void isValid(InputStream xmlInputStream) throws IOException, SAXException {
        initValidator(xsdPath).validate(new StreamSource(xmlInputStream));
    }


    public List<SAXParseException> listParsingExceptions() throws IOException, SAXException {
        XmlErrorHandler xsdErrorHandler = new XmlErrorHandler();
        Validator validator = initValidator(xsdPath);
        validator.setErrorHandler(xsdErrorHandler);
        try {
            validator.validate(new StreamSource(getFile(xmlPath)));
        } catch (SAXParseException e) {
            log.error("error while validating xml file : {}", ExceptionUtils.getStackTrace(e));
        }
        xsdErrorHandler.getExceptions().forEach(e -> log.info(e.getMessage()));
        return xsdErrorHandler.getExceptions();
    }

    private Validator initValidator(String xsdPath) throws SAXException, IOException {
        Source schemaFile = new StreamSource(getFile(xsdPath));
        // Create a schema object from the XSD document
        Schema schema = newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
        // Create a validator object from the schema
        return schema.newValidator();
    }

    private File getFile(String location) throws IOException {
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + location);
    }
}
