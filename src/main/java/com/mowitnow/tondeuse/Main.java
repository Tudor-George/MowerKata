package com.mowitnow.tondeuse;

import com.mowitnow.tondeuse.dto.business.*;
import com.mowitnow.tondeuse.dto.helpers.GridMowerDataInput;
import com.mowitnow.tondeuse.io.FileLoader;
import com.mowitnow.tondeuse.mapper.FileToDataInput;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                logger.error("File path is mandatory !");
                System.exit(1);
            } else {
                GridMowerDataInput gridMowerDataInput = FileToDataInput.transformFileInput(FileLoader.readFileToRowList(args[0]));
                
                Grid grid = new Grid(gridMowerDataInput.getUpperRightCorner());
                grid.setMowerList(new ArrayList<>());

                gridMowerDataInput.getMowerDataInputs().stream().forEach(d -> {
                    Mower mower = new Mower(new OrientedPosition(d.getPosition(), d.getOrientation()));
                    grid.getMowerList().add(mower);
                    mower.executeInstructions(grid.getUpperRightCorner(), d.getInstructions());
                });
                logger.info("Final mower positions: {}", grid.toString());
                System.exit(0);
            }

        } catch (IOException e) {
            logger.error("An exception occurred: ", e);
        }
    }
}
