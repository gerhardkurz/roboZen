package edu.kit.robozen.util;


import edu.kit.robozen.interf.mdp.IState;

interface StatusSupplier<T> {
    void updateStatus(IState state);
    T getStatus();
}
