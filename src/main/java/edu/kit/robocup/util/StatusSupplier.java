package edu.kit.robocup.util;


import edu.kit.robocup.interf.mdp.IState;

interface StatusSupplier<T> {
    void updateStatus(IState state);
    T getStatus();
}
