package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IAction;
import edu.kit.robocup.game.Player;

import java.io.Serializable;
import java.util.Map;

public interface IPolicy extends Serializable {

    //FileOutputStream fout = new FileOutputStream("G:\\address.ser");
    //ObjectOutputStream oos = new ObjectOutputStream(fout);
    //oos.writeObject(MyClassList);

    Map<Player, IAction> getAction(IState state);
}
