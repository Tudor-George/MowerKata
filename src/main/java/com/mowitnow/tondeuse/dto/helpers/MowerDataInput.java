package com.mowitnow.tondeuse.dto.helpers;

import com.mowitnow.tondeuse.dto.business.Orientation;
import com.mowitnow.tondeuse.dto.business.Position;

public class MowerDataInput {

    private Position position;
    private Orientation orientation;
    private String instructions;

    public MowerDataInput(Position position, Orientation orientation, String instructions) {
        this.position = position;
        this.orientation = orientation;
        this.instructions = instructions;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
