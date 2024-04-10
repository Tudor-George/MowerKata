package com.mowitnow.tondeuse.dto.business;

public class Mower {

    private OrientedPosition orientedPosition;

    public Mower(OrientedPosition orientedPosition) {
        this.orientedPosition = orientedPosition;
    }

    public void swing(char c) {
        if (c == 'D') {
            this.orientedPosition.setOrientation(Orientation.values()[(this.orientedPosition.getOrientation().ordinal() + 1) % 4]);
        } else {
            this.orientedPosition.setOrientation(Orientation.values()[
                    this.orientedPosition.getOrientation() != Orientation.N ? (this.orientedPosition.getOrientation().ordinal() - 1) % 4 : 3
                    ]);
        }
    }

    public OrientedPosition moveForward(Position upperRightCorner) {

        switch (this.orientedPosition.getOrientation()) {
            case N:
                if(this.orientedPosition.getPosition().getY() < upperRightCorner.getY()) {
                    this.orientedPosition.getPosition().setY(this.orientedPosition.getPosition().getY() + 1);
                }
                break;
            case E:
                if(this.orientedPosition.getPosition().getX() < upperRightCorner.getX()) {
                    this.orientedPosition.getPosition().setX(this.orientedPosition.getPosition().getX() + 1);
                }
                break;
            case W:
                if(this.orientedPosition.getPosition().getX() > 0) {
                    this.orientedPosition.getPosition().setX(this.orientedPosition.getPosition().getX() - 1);
                }
                break;
            case S:
                if(this.orientedPosition.getPosition().getY() > 0) {
                    this.orientedPosition.getPosition().setY(this.orientedPosition.getPosition().getY() - 1);
                }
                break;
        }
        return this.orientedPosition;
    }

    public OrientedPosition move(char c, Position upperRightCorner) {

        if (c == 'A') {
            this.moveForward(upperRightCorner);
        } else {
            this.swing(c);
        }
        return  this.orientedPosition;
    }

    public void executeInstructions(Position upperRightCorner,String instructions) {
        instructions.chars().forEach(c -> {
            this.move((char)c, upperRightCorner);
        });
    }


    public OrientedPosition getOrientedPosition() {
        return orientedPosition;
    }

    public void setOrientedPosition(OrientedPosition orientedPosition) {
        this.orientedPosition = orientedPosition;
    }
}
