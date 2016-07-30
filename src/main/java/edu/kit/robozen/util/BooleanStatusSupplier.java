package edu.kit.robozen.util;


import edu.kit.robozen.interf.mdp.IState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class BooleanStatusSupplier<T> implements StatusSupplier<T> {
    private final Class<T> booleanClass;
    private T status;

    public BooleanStatusSupplier(Class<T> booleanClass) {
        if (!booleanClass.equals(Boolean.class)) {
            throw new UnsupportedOperationException();
        }
        this.booleanClass = booleanClass;
    }

    protected abstract boolean getStatus(IState state);

    @Override
    public void updateStatus(IState state) {
        try {
            Constructor<T> booleanConstructor = booleanClass.getDeclaredConstructor(boolean.class);
            status = booleanConstructor.newInstance(getStatus(state));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getStatus() {
        return status;
    }
}
