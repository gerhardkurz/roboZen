package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IPlayer;
import edu.kit.robocup.game.action.IAction;
import edu.kit.robocup.game.PlayerState;

import java.io.Serializable;
import java.util.Map;

public interface IPolicy extends Serializable {

    Map<IPlayer, IAction> getAction(IState state);

    //FileOutputStream fout = new FileOutputStream("G:\\address.ser");
    //ObjectOutputStream oos = new ObjectOutputStream(fout);
    //oos.writeObject(MyClassList);
}
