package edu.kit.robocup.mdp;

import edu.kit.robocup.game.IAction;
import edu.kit.robocup.game.IPlayer;
import edu.kit.robocup.game.controller.IPlayerController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IPolicy extends Serializable {

    Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers);

    //FileOutputStream fout = new FileOutputStream("G:\\address.ser");
    //ObjectOutputStream oos = new ObjectOutputStream(fout);
    //oos.writeObject(MyClassList);
}
