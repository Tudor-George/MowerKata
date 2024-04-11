package com.mowitnow.tondeuse;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.ExecutionContext;

import com.mowitnow.tondeuse.dto.helpers.MowerDataInputWarper;
import com.mowitnow.tondeuse.dto.helpers.ValidationService;
import com.mowitnow.tondeuse.mapper.FileToDataInput;



class FileToDataInputTest {

    @Mock
    private ValidationService validationServiceMock;
    
    @Mock
    private ExecutionContext executionContextMock;
    
    private FileToDataInput fileToDataInput;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        fileToDataInput = new FileToDataInput();
    }

    @Test
    void process_ShouldReturnNullForEmptyInput() throws Exception {
        MowerDataInputWarper input = new MowerDataInputWarper();
        assertNull(FileToDataInput.process(input));
    }



    @Test
    void process_ShouldLogErrorForInvalidGridSize() throws Exception {
        String inputLine = "A B";
        MowerDataInputWarper inputWarper = new MowerDataInputWarper(inputLine);
        when(validationServiceMock.isValidTwoNumbersFormat(anyString())).thenReturn(false);

        assertNull(FileToDataInput.process(inputWarper));
        
    }




}


