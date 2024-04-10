package com.mowitnow.tondeuse.dto.helpers;

import com.mowitnow.tondeuse.dto.business.Position;

import java.util.List;

public class GridMowerDataInput {

    private Position upperRightCorner;

    private List<MowerDataInput> mowerDataInputs;

    public Position getUpperRightCorner() {
        return upperRightCorner;
    }

    public void setUpperRightCorner(Position upperRightCorner) {
        this.upperRightCorner = upperRightCorner;
    }

    public List<MowerDataInput> getMowerDataInputs() {
        return mowerDataInputs;
    }

    public void setMowerDataInputs(List<MowerDataInput> mowerDataInputs) {
        this.mowerDataInputs = mowerDataInputs;
    }
}
