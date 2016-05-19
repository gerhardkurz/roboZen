package edu.kit.robocup.constant;



public class Constants {
    public static final double KICKABLE_MARGIN = 0.7;
    public static final double PITCH_WIDTH = 105;
    public static final double GOAL_WIDTH = 14;
    public static final Goal GOAL_EAST = new Goal(PITCH_WIDTH / 2.0, GOAL_WIDTH);
    public static final Goal GOAL_WEST = new Goal(-PITCH_WIDTH / 2.0, GOAL_WIDTH);
}
