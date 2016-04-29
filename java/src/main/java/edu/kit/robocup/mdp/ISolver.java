package edu.kit.robocup.mdp;


public interface ISolver {
    IPolicy solve(IProblem problem);
}
