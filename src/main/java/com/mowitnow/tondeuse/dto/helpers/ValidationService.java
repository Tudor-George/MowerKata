package com.mowitnow.tondeuse.dto.helpers;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationService {

	private static final Pattern CONTROL_PATTERN = Pattern.compile("^[DGA]+$");
	private static final Pattern ORIENTATION_PATTERN = Pattern.compile("^[NEWS]+$");
	private static final Pattern POSITION_PATTERN = Pattern.compile("^\\d+ \\d+ [NEWS]$");
	private int largeur, longeur; // These need to be set when validating the first line
    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);
	// Methods to get grid dimensions
	public int getLargeur() {
		return this.largeur;
	}

	public int getLongeur() {
		return this.longeur;
	}

	public boolean isValidControl(String control) {
		return control != null && CONTROL_PATTERN.matcher(control).matches();
	}

	public boolean isValidOrientation(String orientation) {
		return orientation != null && ORIENTATION_PATTERN.matcher(orientation).matches();
	}

	public boolean isNotNullLine(List<String> input, int lineIndex) {
		return input.size() > lineIndex && input.get(lineIndex) != null;
	}

	public boolean isInBoundaryPosition(int tondeuseCoordinateX, int tondeuseCoordinateY) {
		return (tondeuseCoordinateX >= 0 && tondeuseCoordinateX <= largeur)
				&& (tondeuseCoordinateY >= 0 && tondeuseCoordinateY <= longeur);
	}

	// Validates the first line of the file and initializes the grid dimensions
	public boolean validateAndInitializeGridSize(String gridSizeLine) {
		if (gridSizeLine == null || gridSizeLine.isEmpty())
			return false;
		String[] parts = gridSizeLine.split(" ");
		if (parts.length != 2)
			return false;
		try {
			largeur = Integer.parseInt(parts[0]);
			longeur = Integer.parseInt(parts[1]);
			return true;
		} catch (NumberFormatException e) {
			logger.error("Wrong number format for grid size");
			return false;
		}
	}

	// Validates the mower's position and orientation line
	public boolean isValidMowerPosition(String positionLine) {
		if (positionLine == null || !POSITION_PATTERN.matcher(positionLine).matches())
			return false;
		String[] parts = positionLine.split(" ");
		try {
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			return isInBoundaryPosition(x, y) && isValidOrientation(parts[2]);
		} catch (NumberFormatException e) {
			logger.error("Wrong number format for mower position");
			return false;
		}
	}

	// Integration into mowerInputValid method (you'll need to adjust this to fit
	// within your logic)
	public boolean mowerInputValid(List<String> input, int lineIndex) {
		if (!isNotNullLine(input, lineIndex))
			return false;
		if (lineIndex == 0)
			return validateAndInitializeGridSize(input.get(lineIndex));
		if (lineIndex % 2 != 0) { // Position and orientation line
			return isValidMowerPosition(input.get(lineIndex));
		} else { // Control line
			return isValidControl(input.get(lineIndex));
		}
	}
}
