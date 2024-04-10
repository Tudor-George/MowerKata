package com.mowitnow.tondeuse.dto.business;

public class OrientedPosition {


    private Position position;
    private Orientation orientation;

    public OrientedPosition(Position position, Orientation orientation) {
        this.orientation = orientation;
        this.position = position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return position.toString() + " " + orientation.toString();
    }
}
