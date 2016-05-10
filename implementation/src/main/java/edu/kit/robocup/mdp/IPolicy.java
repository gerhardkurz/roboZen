package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IPlayerController;

import java.io.Serializable;
import java.util.List;

public interface IPolicy extends Serializable {

    void apply(IState state, List<? extends IPlayerController> playerControllers);

    //FileOutputStream fout = new FileOutputStream("G:\\address.ser");
    //ObjectOutputStream oos = new ObjectOutputStream(fout);
    //oos.writeObject(MyClassList);
}
