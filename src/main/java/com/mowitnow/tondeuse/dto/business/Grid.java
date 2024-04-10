package com.mowitnow.tondeuse.dto.business;

import java.util.List;
import java.util.stream.Collectors;

public class Grid {
    Position upperRightCorner;
    List<Mower> mowerList;

    public Grid(Position upperRightCorner) {
        this.upperRightCorner = upperRightCorner;
    }

    @Override
    public String toString() {
        return this.mowerList != null ? this.mowerList.stream().map(m -> m.getOrientedPosition().toString()).collect(Collectors.joining(" ")) : "";
    }

    public Position getUpperRightCorner() {
        return upperRightCorner;
    }

    public void setUpperRightCorner(Position upperRightCorner) {
        this.upperRightCorner = upperRightCorner;
    }

    public List<Mower> getMowerList() {
        return mowerList;
    }

    public void setMowerList(List<Mower> mowerList) {
        this.mowerList = mowerList;
    }
}
