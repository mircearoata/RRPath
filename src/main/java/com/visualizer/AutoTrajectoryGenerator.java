package com.visualizer;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import java.util.ArrayList;
import java.util.List;

public class AutoTrajectoryGenerator {
    private static DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
            30, 30, 0.0,
            Math.toRadians(180.0), Math.toRadians(180.0), 0.0
    );
    private static DriveConstraints SLOW_CONSTRAINTS = new DriveConstraints(
            25, 30, 0.0,
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

    public List<Trajectory> getTrajectoriesMoveFoundation() {
        SkystoneTrajectoryBuilder.reset(startPose);
        List<Trajectory> trajectories = new ArrayList<>();

        trajectories.add(makeTrajectoryBuilder()
                .toFoundation()
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .moveFoundation()
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .park()
                .build());

        return trajectories;
    }

    public List<Trajectory> getTrajectories1Stone(SkystonePattern skystonePattern) {
        int firstSkystone;
        if (skystonePattern == SkystonePattern.LEFT) {
            firstSkystone = 3;
        } else if (skystonePattern == SkystonePattern.MIDDLE) {
            firstSkystone = 4;
        } else {
            firstSkystone = 2;
        }

        SkystoneTrajectoryBuilder.reset(startPose);
        List<Trajectory> trajectories = new ArrayList<>();

        trajectories.add(makeTrajectoryBuilder(Speed.SLOW)
                .getStone(firstSkystone)
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(true)
                .passBridge()
                .toFoundation()
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .moveFoundation()
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(false)
                .park()
                .build());

        return trajectories;
    }

    public List<Trajectory> getTrajectories2Stones(SkystonePattern skystonePattern) {
        int firstSkystone;
        int secondSkystone;
        if (skystonePattern == SkystonePattern.LEFT) {
            firstSkystone = 3;
            secondSkystone = 0;
        } else if (skystonePattern == SkystonePattern.MIDDLE) {
            firstSkystone = 4;
            secondSkystone = 1;
        } else  {
            firstSkystone = 5;
            secondSkystone = 2;
        }

        SkystoneTrajectoryBuilder.reset(startPose);
        List<Trajectory> trajectories = new ArrayList<>();

        trajectories.add(makeTrajectoryBuilder(Speed.SLOW)
                .getStone(firstSkystone)
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(true)
                .passBridge()
                .toFoundation()
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .moveFoundation()
                .build());

        trajectories.add(makeTrajectoryBuilder(Speed.SLOW)
                .passBridge()
                .getStone(secondSkystone)
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(true)
                .passBridge()
                .actualStrafeTo(new Vector2d(45.0, -39.0))
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(false)
                .park()
                .build());

        return trajectories;
    }

    private enum Speed {
        SLOW,
        NORMAL
    }

    private SkystoneTrajectoryBuilder makeTrajectoryBuilder() {
        return makeTrajectoryBuilder(Speed.NORMAL);
    }

    private SkystoneTrajectoryBuilder makeTrajectoryBuilder(Speed speed) {
        switch (speed) {
            case SLOW:
                return new SkystoneTrajectoryBuilder(SLOW_CONSTRAINTS, alliance);
            case NORMAL:
            default:
                return new SkystoneTrajectoryBuilder(BASE_CONSTRAINTS, alliance);
        }
    }
}
