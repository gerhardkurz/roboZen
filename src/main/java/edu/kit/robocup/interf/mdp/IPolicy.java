package edu.kit.robocup.interf.mdp;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.interf.game.IAction;
import edu.kit.robocup.game.controller.IPlayerController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IPolicy extends Serializable {

    Map<IPlayerController, IAction> apply(IState state, List<? extends IPlayerController> playerControllers, PitchSide pitchSide);

    //FileOutputStream fout = new FileOutputStream("G:\\address.ser");
    //ObjectOutputStream oos = new ObjectOutputStream(fout);
    //oos.writeObject(MyClassList);
}
