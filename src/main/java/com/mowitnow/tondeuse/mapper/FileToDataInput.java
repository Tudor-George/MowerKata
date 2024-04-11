package com.mowitnow.tondeuse.mapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;

import com.mowitnow.tondeuse.dto.business.Mower;
import com.mowitnow.tondeuse.dto.business.Orientation;
import com.mowitnow.tondeuse.dto.business.OrientedPosition;
import com.mowitnow.tondeuse.dto.business.Position;
import com.mowitnow.tondeuse.dto.helpers.MowerDataInput;
import com.mowitnow.tondeuse.dto.helpers.MowerDataInputWarper;
import com.mowitnow.tondeuse.dto.helpers.ValidationService;

public class FileToDataInput {

	private static final Logger logger = LoggerFactory.getLogger(FileToDataInput.class);
	private static ValidationService validationService = new ValidationService();

	private static ExecutionContext executionContext = new ExecutionContext();

	public static MowerDataInputWarper process(MowerDataInputWarper items) throws Exception {
		String input = items.getFileLine();

		if (input == null || input.isEmpty()) {
			logger.error("Input list is null or empty.");
			return null;
		}

		if (validationService.isValidTwoNumbersFormat(input)) {
			initializeGrid(input);
		}

		// Check if the mower's position is valid
		if (validationService.isValidMowerPosition(input)) {
			List<MowerDataInput> mowerDataInputs = new ArrayList<>();
			logger.info("Processing mower data input  {}", input);
			String[] positionParts = input.split(" ");
			executionContext.putInt("tondeuseCoordinateX", Integer.parseInt(positionParts[0]));
			executionContext.putInt("tondeuseCoordinateY", Integer.parseInt(positionParts[1]));
			executionContext.putString("orientation", positionParts[2]);
		}
		if (validationService.isValidControl(input)) {
			Orientation orientation = Orientation.valueOf(executionContext.getString("orientation"));
			OrientedPosition orientatedPosition = new OrientedPosition(
					new Position(executionContext.getInt("tondeuseCoordinateX"),
							executionContext.getInt("tondeuseCoordinateY")),
					orientation);
			Mower currentMower = new Mower(orientatedPosition);
			String commands = input;

			currentMower.executeInstructions(new Position(executionContext.getInt("x"), executionContext.getInt("y")),
					input);
			items.setFileLine(currentMower.getOrientedPosition().toString());
			return items;
		}
		return null;
	}

	private static void initializeGrid(String input) {
		String[] parts = input.split(" ");
		try {
			// save x and y in context
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			executionContext.putInt("x", x);
			executionContext.putInt("y", y);
			validationService.setLargeur(x);
			validationService.setLongeur(y);
			logger.info("Grid size validated and initialized. Proceeding with mower data inputs.");

		} catch (NumberFormatException e) {
			logger.error("Wrong number format for grid size");
		}
	}
}
