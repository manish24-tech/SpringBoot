package com.org.epaperintegration.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class XmlValidatorUnitTest {
    private static final String VALID_XML_PATH = "validEpaper.xml";
    private static final String INVALID_XML_PATH = "invalidEpaper.xml";
    private static final String XSD_PATH = "epaper.xsd";

    @DisplayName("valid xml - return false in case of requested wrong xml that encounter during validation")
    @Test
    public void validXML_validation_success() throws IOException, SAXException {
        assertTrue(new XmlValidator(XSD_PATH, VALID_XML_PATH).isValid());
    }

    @DisplayName("Invalid xml - return false in case of requested wrong xml that encounter during validation")
    @Test
    public void invalidXML_validation_failed() throws IOException, SAXException {
        assertFalse(new XmlValidator(XSD_PATH, INVALID_XML_PATH).isValid());
    }

    @DisplayName("valid xml - return size of an exception that encounter during validation")
    @Test
    public void validXML_exceptions_size() throws IOException, SAXException {
        assertEquals(0, new XmlValidator(XSD_PATH, VALID_XML_PATH).listParsingExceptions().size());
    }

    @DisplayName("Invalid xml - return size of an exception that encounter during validation")
    @Test
    public void invalidXML_exceptions_size() throws IOException, SAXException {
        assertEquals(5, new XmlValidator(XSD_PATH, INVALID_XML_PATH).listParsingExceptions().size());
    }
}
