package edu.kit.robocup.constant;


import edu.kit.robocup.interf.game.IGameObject;
import edu.kit.robocup.game.SimpleGameObject;

public class Goal extends SimpleGameObject {
    private final IGameObject upperPost;
    private final IGameObject lowerPost;

    public Goal(double positionX, double width) {
        super(positionX, 0);
        upperPost = new SimpleGameObject(positionX, width / 2);
        lowerPost = new SimpleGameObject(positionX, -width / 2);
    }

    public IGameObject getUpperPost() {
        return upperPost;
    }

    public IGameObject getLowerPost() {
        return lowerPost;
    }
}
