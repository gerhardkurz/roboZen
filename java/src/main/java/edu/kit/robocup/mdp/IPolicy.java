package edu.kit.robocup.mdp;

import edu.kit.robocup.game.action.IAction;
import edu.kit.robocup.game.Player;

import java.io.Serializable;
import java.util.Map;

public interface IPolicy extends Serializable {

    Map<Player, IAction> getAction(IState state);

    //FileOutputStream fout = new FileOutputStream("G:\\address.ser");
    //ObjectOutputStream oos = new ObjectOutputStream(fout);
    //oos.writeObject(MyClassList);
}
