package com.mowitnow.tondeuse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mowitnow.tondeuse.dto.helpers.ValidationService;

class ValidationServiceTest {
    
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
        validationService.setLargeur(5); // Example grid width
        validationService.setLongeur(5); // Example grid length
    }

    @Test
    void testIsValidControlValid() {
        assertTrue(validationService.isValidControl("DGA"));
    }

    @Test
    void testIsValidControlInvalid() {
        assertFalse(validationService.isValidControl("XYZ"));
    }

    @Test
    void testIsValidOrientationValid() {
        assertTrue(validationService.isValidOrientation("N"));
    }

    @Test
    void testIsValidOrientationInvalid() {
        assertFalse(validationService.isValidOrientation("A"));
    }

    @Test
    void testIsNotNullLineValid() {
        assertTrue(validationService.isNotNullLine(Arrays.asList("1", "2"), 0));
    }

    @Test
    void testIsNotNullLineInvalid() {
        assertFalse(validationService.isNotNullLine(Arrays.asList("1", null), 1));
    }

    @Test
    void testIsInBoundaryPositionValid() {
        assertTrue(validationService.isInBoundaryPosition(5, 5));
    }

    @Test
    void testIsInBoundaryPositionInvalid() {
        assertFalse(validationService.isInBoundaryPosition(6, 6));
    }

    @Test
    void testIsValidTwoNumbersFormatValid() {
        assertTrue(validationService.isValidTwoNumbersFormat("12 34"));
    }

    @Test
    void testIsValidTwoNumbersFormatInvalid() {
        assertFalse(validationService.isValidTwoNumbersFormat("12, 34"));
    }

    @Test
    void testIsValidMowerPositionValid() {
        assertTrue(validationService.isValidMowerPosition("1 2 N"));
    }

    @Test
    void testIsValidMowerPositionInvalid() {
        assertFalse(validationService.isValidMowerPosition("12 N"));
    }

    @Test
    void testMowerInputValidControlLine() {
        assertTrue(validationService.mowerInputValid(Arrays.asList("DGA", "1 2 N"), 0));
    }

    @Test
    void testMowerInputValidPositionLine() {
        assertTrue(validationService.mowerInputValid(Arrays.asList("DGA", "1 2 N"), 1));
    }

    @Test
    void testMowerInputInvalid() {
        assertFalse(validationService.mowerInputValid(Arrays.asList("XYZ", "12 N"), 0));
    }
}
