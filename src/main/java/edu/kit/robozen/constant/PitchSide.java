package edu.kit.robozen.constant;


public enum PitchSide {
    EAST, WEST, DUMMY;

    @Override
    public String toString() {
        return name();
    }

    public PitchSide flipSide() {
        if (this == EAST)
            return WEST;
        else if (this == WEST)
            return EAST;
        else
            return DUMMY;
    }
}
