package edu.kit.robocup.game;

import java.util.Vector;

import edu.kit.robocup.position.ObjectInfo;

public class Memory {

    public Vector<ObjectInfo> m_objects;

    // Split objects into specific lists
    private Vector<?> m_ball_list;
    private Vector<?> m_player_list;
    private Vector<?> m_flag_list;
    private Vector<?> m_goal_list;
    private Vector<?> m_line_list;

    // Constructor for 'see' information
    public Memory() {
        m_player_list = new Vector<Object>(22);
        m_ball_list = new Vector<Object>(1);
        m_goal_list = new Vector<Object>(10);
        m_line_list = new Vector<Object>(20);
        m_flag_list = new Vector<Object>(60);
        m_objects = new Vector<ObjectInfo>(113);
    }

    public Vector<?> getBallList() {
        return m_ball_list;
    }

    public Vector<?> getPlayerList() {
        return m_player_list;
    }

    public Vector<?> getGoalList() {
        return m_goal_list;
    }

    public Vector<?> getLineList() {
        return m_line_list;
    }

    public Vector<?> getFlagList() {
        return m_flag_list;
    }

    public void addInfo(ObjectInfo o) {
    	m_objects.add(o);
    }
}