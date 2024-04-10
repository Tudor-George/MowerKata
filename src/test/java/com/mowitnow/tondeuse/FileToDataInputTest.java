package com.mowitnow.tondeuse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mowitnow.tondeuse.dto.helpers.GridMowerDataInput;
import com.mowitnow.tondeuse.dto.helpers.ValidationService;
import com.mowitnow.tondeuse.mapper.FileToDataInput;



class FileToDataInputTest {
	
	
	
    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    @Test
    void testTransformFileInputValid() {
        List<String> input = Arrays.asList("5 5", "1 2 N", "GAGAGAGAA");
        GridMowerDataInput result = FileToDataInput.transformFileInput(input);
        assertNotNull(result, "Result should not be null for valid input");
        assertEquals(5, result.getUpperRightCorner().getX(), "Grid width should be 5");
        assertEquals(5, result.getUpperRightCorner().getY(), "Grid height should be 5");
        assertFalse(result.getMowerDataInputs().isEmpty(), "There should be at least one mower data input");
    }

    @Test
    void testTransformFileInputInvalidGridSize() {
        List<String> input = Arrays.asList("5", "1 2 N", "GAGAGAGAA");
        GridMowerDataInput result = FileToDataInput.transformFileInput(input);
        assertNull(result, "Result should be null for invalid grid size input");
    }

    @Test
    void testTransformFileInputInvalidMowerPosition() {
        List<String> input = Arrays.asList("5 5", "1 2 Z", "GAGAGAGAA"); // Invalid orientation 'Z'
        GridMowerDataInput result = FileToDataInput.transformFileInput(input);
        assertNotNull(result, "Result should not be null even for invalid mower position");
        assertTrue(result.getMowerDataInputs().isEmpty(), "There should be no mower data inputs for invalid mower position");
    }

    // Add more tests to cover other scenarios and invalid inputs
}


