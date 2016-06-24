package edu.kit.robocup.tree;


import edu.kit.robocup.interf.mdp.IState;

public interface IReward {
    int reward(IState currentState);
}
