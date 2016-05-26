package edu.kit.robocup.interf.mdp;


import edu.kit.robocup.mdp.transition.Transitions;

public interface ISolver {
    IPolicy solve();
}
