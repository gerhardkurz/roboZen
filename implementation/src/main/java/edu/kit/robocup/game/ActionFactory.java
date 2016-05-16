package edu.kit.robocup.game;
/**
 * creates an action of actionsIndex
 * @author dani
 *
 */
public class ActionFactory {

	public ActionFactory() {
		
	};
	public IAction getAction(int actionIndex) {
		switch(actionIndex) {
		case 0: {
			return new Kick(0,0);
		}
		case 1: {
			return new Dash(0);
		}
		case 2: {
			return new Turn(0, 0);
		}
		case 3: {
			return new Move(0,0);
		}
		default: {
			return null;
		}
		}
	}
}
