package edu.kit.robocup.game.intf.input;

import edu.kit.robocup.game.intf.client.Player;
import edu.kit.robocup.game.intf.parser.IPlayerInput;
import org.apache.log4j.Logger;



public class PlayerInput implements IPlayerInput {
    private final static Logger logger = Logger.getLogger(PlayerInput.class.getName());
    private final Player player;


    public PlayerInput(Player player) {
        this.player = player;
    }

}
