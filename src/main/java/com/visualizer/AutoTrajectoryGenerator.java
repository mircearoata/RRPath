package com.visualizer;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryConstraints;

import java.util.ArrayList;
import java.util.List;

import static com.visualizer.TrajectoryUtils.*;
import static com.visualizer.TrajectoryUtils.ROBOT_W;

public class AutoTrajectoryGenerator {
    private static DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
            35, 40, 0.0,
            Math.toRadians(180.0), Math.toRadians(180.0), 0.0
    );
    private static DriveConstraints SLOW_CONSTRAINTS = new DriveConstraints(
            20, 20, 0.0,
            Math.toRadians(180.0), Math.toRadians(180.0), 0.0
    );
    private static DriveConstraints FAST_CONSTRAINTS = new DriveConstraints(
            45, 45, 0.0,
            Math.toRadians(180.0), Math.toRadians(180.0), 0.0
    );

    private TrajectoryUtils.Alliance alliance;
    private Pose2d startPose;

    public AutoTrajectoryGenerator(TrajectoryUtils.Alliance alliance, Pose2d start) {
        this.alliance = alliance;
        startPose = start;
    }

    public enum SkystonePattern {
        LEFT,
        MIDDLE,
        RIGHT
    }

    public TrajectoryUtils.Alliance getAlliance() {
        return alliance;
    }

    public List<Trajectory> getTrajectoriesSideArm2Stones(SkystonePattern skystonePattern) {
        List<Trajectory> trajectories = new ArrayList<>();
        Pose2d firstStone;
        if(skystonePattern == SkystonePattern.LEFT) {
            firstStone = new Pose2d(getStonePosition(0).minus(new Vector2d(0, STONE_W / 2)).plus(new Vector2d(ROBOT_L / 2, 0)), Math.toRadians(180));
        } else if(skystonePattern == SkystonePattern.MIDDLE) {
            firstStone = new Pose2d(getStonePosition(1).minus(new Vector2d(0, STONE_W / 2)).plus(new Vector2d(ROBOT_L / 2, 0)), Math.toRadians(180));
        } else {
            firstStone = new Pose2d(getStonePosition(2).minus(new Vector2d(0, STONE_W / 2)).plus(new Vector2d(ROBOT_L / 2, 0)), Math.toRadians(180));
        }
        trajectories.add(makeTrajectoryBuilder(trajectories, Speed.NORMAL)
                .splineTo(flipIfBlue(firstStone))
                .build());

        trajectories.add(makeTrajectoryBuilder(trajectories, Speed.NORMAL, true)
                .splineTo(flipIfBlue(new Pose2d(0, -40)))
                .splineTo(flipIfBlue(new Pose2d(48, -34)))
                .build());

        Pose2d secondStone;
        if(skystonePattern == SkystonePattern.LEFT) {
            secondStone = new Pose2d(getStonePosition(3).minus(new Vector2d(0, STONE_W / 2)).plus(new Vector2d(ROBOT_L / 2, 0)), Math.toRadians(180));
        } else if(skystonePattern == SkystonePattern.MIDDLE) {
            secondStone = new Pose2d(getStonePosition(4).minus(new Vector2d(0, STONE_W / 2)).plus(new Vector2d(ROBOT_L / 2, 0)), Math.toRadians(180));
        } else {
            secondStone = new Pose2d(getStonePosition(5).minus(new Vector2d(0, STONE_W / 2)).plus(new Vector2d(ROBOT_L / 2, 0)), Math.toRadians(180));
        }
        trajectories.add(makeTrajectoryBuilder(trajectories, Speed.NORMAL)
                .splineTo(flipIfBlue(new Pose2d(0, -40, Math.toRadians(180))))
                .splineTo(flipIfBlue(secondStone))
                .build());

        trajectories.add(makeTrajectoryBuilder(trajectories, Speed.NORMAL, true)
                .splineTo(flipIfBlue(new Pose2d(0, -40)))
                .splineTo(flipIfBlue(new Pose2d(48, -34)))
                .build());

        trajectories.add(makeTrajectoryBuilder(trajectories, Speed.NORMAL)
                .splineToSplineHeading(new Pose2d(44, -42, Math.toRadians(-90)), Math.toRadians(-90))
                .splineToSplineHeading(new Pose2d(44, -34, Math.toRadians(-90)), Math.toRadians(-90))
                .build());

        trajectories.add(makeTrajectoryBuilder(trajectories, Speed.NORMAL)
                .splineToSplineHeading(new Pose2d(38, -50, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Pose2d(48, -50, Math.toRadians(180)))
                .build());

        trajectories.add(makeTrajectoryBuilder(trajectories, Speed.NORMAL)
                .splineTo(flipIfBlue(new Pose2d(0, -40, Math.toRadians(180))))
                .build());

        return trajectories;
    }

    private TrajectoryBuilder makeTrajectoryBuilder(List<Trajectory> currentTrajectories, Speed speed, boolean reversed) {
        Pose2d endPose = currentTrajectories.size() == 0 ? startPose : currentTrajectories.get(currentTrajectories.size() - 1).end();
        if(reversed)
            endPose = new Pose2d(endPose.getX(), endPose.getY(), endPose.getHeading() - Math.toRadians(180));
        return new TrajectoryBuilder(endPose, reversed, constraintsFromSpeed(speed));
    }

    private TrajectoryBuilder makeTrajectoryBuilder(List<Trajectory> currentTrajectories, Speed speed) {
        return makeTrajectoryBuilder(currentTrajectories, speed, false);
    }

    private Pose2d flipIfBlue(Pose2d pose2d) {
        return TrajectoryUtils.flipIfBlue(alliance, pose2d);
    }

    private Vector2d flipIfBlue(Vector2d vector2d) {
        return TrajectoryUtils.flipIfBlue(alliance, vector2d);
    }

    private Pose2d flipIfBluePositive(Pose2d pose2d) {
        return TrajectoryUtils.flipIfBluePositive(alliance, pose2d);
    }

    private TrajectoryConstraints constraintsFromSpeed(Speed speed) {
        switch (speed) {
            case SLOW:
                return SLOW_CONSTRAINTS;
            case NORMAL:
                return BASE_CONSTRAINTS;
            case FAST:
                return FAST_CONSTRAINTS;
        }
        return BASE_CONSTRAINTS;
    }

    private enum Speed {
        SLOW,
        NORMAL,
        FAST
    }
}
