package edu.kit.robocup.constant;


import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
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

    @Override
    public double getDistance(IGameObject other) {
        if (Math.abs(other.getPositionY()) <= upperPost.getPositionY()) {
            return Math.abs(other.getPositionX() - upperPost.getPositionX());
        } else {
            if (other.getPositionY() > 0) {
                return upperPost.getDistance(other);
            } else {
                return lowerPost.getDistance(other);
            }
        }
    }
}
