package com.mowitnow.tondeuse.mapper;

import com.mowitnow.tondeuse.dto.business.Orientation;
import com.mowitnow.tondeuse.dto.helpers.GridMowerDataInput;
import com.mowitnow.tondeuse.dto.helpers.MowerDataInput;
import com.mowitnow.tondeuse.dto.helpers.ValidationService;
import com.mowitnow.tondeuse.dto.business.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FileToDataInput {
	private static final Logger logger = LoggerFactory.getLogger(FileToDataInput.class);
	private static ValidationService validationService = new ValidationService();

	public static GridMowerDataInput transformFileInput(List<String> input) {
		if (input == null || input.isEmpty()) {
			logger.error("Input list is null or empty.");
			return null;
		}

		// Validates and initializes the grid size.
		if (!validationService.validateAndInitializeGridSize(input.get(0))) {
			logger.error("Failed to validate and initialize grid size.");
			return null; // Invalid grid size, cannot proceed.
		}
		logger.info("Grid size validated and initialized. Proceeding with mower data inputs.");
		// Extract grid dimensions from the validated and initialized values in the
		// ValidationService.
		int largeur = validationService.getLargeur();
		int longeur = validationService.getLongeur();

		GridMowerDataInput gridMowerDataInput = new GridMowerDataInput();
		gridMowerDataInput.setUpperRightCorner(new Position(largeur, longeur));

		List<MowerDataInput> mowerDataInputs = new ArrayList<>();

		// Start from 1 to skip the grid size line, increment by 2 for each mower
		// position and command set.
		for (int i = 1; i < input.size() - 1; i += 2) {
			// Check if both the mower's position and the commands are valid.
			if (validationService.isValidMowerPosition(input.get(i))
					&& validationService.isValidControl(input.get(i + 1))) {
				logger.info("Processing mower data input at lines {} and {}", i, i + 1);
				String[] positionParts = input.get(i).split(" ");
				int tondeuseCoordinateX = Integer.parseInt(positionParts[0]);
				int tondeuseCoordinateY = Integer.parseInt(positionParts[1]);
				Orientation orientation = Orientation.valueOf(positionParts[2]);

				// Assuming commands are on the next line (i + 1).
				String commands = input.get(i + 1);

				MowerDataInput mowerDataInputTemp = new MowerDataInput(
						new Position(tondeuseCoordinateX, tondeuseCoordinateY), orientation, commands);
				mowerDataInputs.add(mowerDataInputTemp);
			} else {
				logger.warn("Invalid mower position or commands at lines {} and {}", i, i + 1);
			}
		}
		if (mowerDataInputs.isEmpty()) {
			logger.warn("No valid mower data inputs found.");
		} else {
			logger.info("Successfully processed {} mower data inputs.", mowerDataInputs.size());
		}
		gridMowerDataInput.setMowerDataInputs(mowerDataInputs);
		return gridMowerDataInput;
	}
}
