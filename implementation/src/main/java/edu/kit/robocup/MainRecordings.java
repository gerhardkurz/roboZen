package edu.kit.robocup;

import edu.kit.robocup.constant.PitchSide;
import edu.kit.robocup.game.PlayMode;
import edu.kit.robocup.game.SimpleGameObject;
import edu.kit.robocup.game.controller.Team;
import edu.kit.robocup.game.controller.Trainer;
import edu.kit.robocup.game.state.Ball;
import edu.kit.robocup.game.state.PlayerState;
import edu.kit.robocup.interf.mdp.IPolicy;
import edu.kit.robocup.mdp.policy.AllActionCombinationsPolicy;
import edu.kit.robocup.mdp.policy.ChaseAndKickPolicy;
import edu.kit.robocup.mdp.policy.PerPlayModePolicy;
import edu.kit.robocup.mdp.policy.heurisic.KickOffPolicy;
import edu.kit.robocup.recorder.GameRecorder;
import edu.kit.robocup.util.Util;
import org.apache.log4j.Logger;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainRecordings {

    static Logger logger = Logger.getLogger(MainRecordings.class.getName());



    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(1000);
        PerPlayModePolicy policyRecordRandom = new PerPlayModePolicy(new GameRecorder("recordings/random", new AllActionCombinationsPolicy()));
        policyRecordRandom.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        PerPlayModePolicy policyChaseAndKick = new PerPlayModePolicy(new ChaseAndKickPolicy());
        policyChaseAndKick.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        PerPlayModePolicy policyRecordChaseAndKick1 = new PerPlayModePolicy(new GameRecorder("recordings/chaseAndKick", new ChaseAndKickPolicy()));
        policyRecordChaseAndKick1.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        PerPlayModePolicy policyRecordChaseAndKick2 = new PerPlayModePolicy(new GameRecorder("recordings/chaseAndKick1", new ChaseAndKickPolicy()));
        policyRecordChaseAndKick2.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);
        PerPlayModePolicy policyRecordChaseAndKick3 = new PerPlayModePolicy(new GameRecorder("recordings/chaseAndKick2", new ChaseAndKickPolicy()));
        policyRecordChaseAndKick3.replacePolicyForPlayMode(new KickOffPolicy(), PlayMode.KICK_OFF_EAST, PlayMode.KICK_OFF_WEST, PlayMode.GOAL_SIDE_EAST, PlayMode.GOAL_SIDE_WEST);


        IPolicy[] policiesTeamWest = {policyChaseAndKick, policyChaseAndKick, policyChaseAndKick, policyChaseAndKick};
        IPolicy[] policiesTeamEast = {policyRecordRandom, policyRecordChaseAndKick1, policyRecordChaseAndKick2, policyRecordChaseAndKick3};

        List<List<SimpleGameObject>> posTeamWest = new ArrayList<>();
        List<SimpleGameObject> gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(-30, 60));
        gameObjects.add(new SimpleGameObject(-30, -30));
        posTeamWest.add(gameObjects);
        gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(-5, 5));
        gameObjects.add(new SimpleGameObject(-5, -5));
        posTeamWest.add(gameObjects);
        gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(-5, 5));
        gameObjects.add(new SimpleGameObject(-5, -5));
        posTeamWest.add(gameObjects);
        gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(-50, 50));
        gameObjects.add(new SimpleGameObject(-50, -50));
        posTeamWest.add(gameObjects);

        List<List<SimpleGameObject>> posTeamEast = new ArrayList<>();
        gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(10, 10));
        gameObjects.add(new SimpleGameObject(10, 10));
        posTeamEast.add(gameObjects);
        gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(5, 5));
        gameObjects.add(new SimpleGameObject(5, -5));
        posTeamEast.add(gameObjects);
        gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(50, 50));
        gameObjects.add(new SimpleGameObject(50, -50));
        posTeamEast.add(gameObjects);
        gameObjects = new ArrayList<>();
        gameObjects.add(new SimpleGameObject(5, 5));
        gameObjects.add(new SimpleGameObject(5, -5));
        posTeamEast.add(gameObjects);

        List<Ball> ball = new ArrayList<>();
        ball.add(new Ball(10, 10));
        ball.add(new Ball(0,0));
        ball.add(new Ball(0,0));
        ball.add(new Ball(0,0));

        for (int i = 0; i < policiesTeamEast.length; i++) {
            Util.TeamDescription teamDescriptionWest = new Util.TeamDescription(policiesTeamWest[i], 2, posTeamWest.get(i));
            Util.TeamDescription teamDescriptionEast = new Util.TeamDescription(policiesTeamEast[i], 2, posTeamEast.get(i));
            Util.executeGame(teamDescriptionWest, teamDescriptionEast, ball.get(i));
        }
        Util.killTask("rcssmonitor.exe");
        Util.killTask("rcssserver.exe");
    }
}
