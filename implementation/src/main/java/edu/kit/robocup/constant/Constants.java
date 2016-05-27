package edu.kit.robocup.constant;



public class Constants {
    public static final double KICKABLE_MARGIN = 0.7;
    public static final double PITCH_LENGTH = 105;
    public static final double PITCH_WIDTH = 68;
    public static final double GOAL_WIDTH = 14.02;
    public static final double PITCH_MARGIN = 5.0;
    public static final double CENTER_CIRCLE_R  =9.15;
    public static final double PENALTY_AREA_LENGTH =16.5;
    public static final double PENALTY_AREA_WIDTH =40.32;
    public static final double GOAL_AREA_LENGTH = 5.5;
    public static final double GOAL_AREA_WIDTH = 18.32;
    public static final double GOAL_DEPTH = 2.44;
    public static final double PENALTY_SPOT_DIST =11.0;
    public static final double CORNER_ARC_R = 1.0;
    public static final Goal GOAL_EAST = new Goal(PITCH_LENGTH / 2.0, GOAL_WIDTH);
    public static final Goal GOAL_WEST = new Goal(-PITCH_LENGTH / 2.0, GOAL_WIDTH);
    public static final double maxmoment = 180;
    public static final double maxneckang = 90;
    public static final double maxneckmoment = 180;
    public static final double maxpower = 100;
    public static final double min_dash_angle = -180;
    public static final double min_dash_power = -100;
    public static final double minmoment = -180;
    public static final double minneckang = -90;
    public static final double minneckmoment = -180;
    public static final double minpower = -100;
}
