package com.visualizer;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.BaseTrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import java.util.ArrayList;
import java.util.List;

import static com.visualizer.TrajectoryUtils.flipIfBlue;

public class AutoTrajectoryGenerator {
    public static DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
            30, 30, 0.0,
            Math.toRadians(180.0), Math.toRadians(180.0), 0.0
    );
    public static DriveConstraints SLOW_CONSTRAINTS = new DriveConstraints(
            25, 30, 0.0,
            Math.toRadians(180.0), Math.toRadians(180.0), 0.0
    );

    private TrajectoryUtils.Alliance alliance;
    private Pose2d start;

    public AutoTrajectoryGenerator(TrajectoryUtils.Alliance alliance, Pose2d start) {
        this.alliance = alliance;
        this.start = flipIfBlue(alliance, start);
        SkystoneTrajectoryBuilder.reset(start);
    }

    public enum SkystonePattern {
        LEFT,
        MIDDLE,
        RIGHT
    }

    public TrajectoryUtils.Alliance getAlliance() {
        return alliance;
    }

    private List<Trajectory> trajectories = new ArrayList<>();

    public List<Trajectory> getTrajectories1Stone(SkystonePattern skystonePattern) {
        int firstSkystone = 3;
        if (skystonePattern == SkystonePattern.LEFT) {
            firstSkystone = 3;
        } else if (skystonePattern == SkystonePattern.MIDDLE) {
            firstSkystone = 4;
        } else if (skystonePattern == SkystonePattern.RIGHT) {
            firstSkystone = 2;
        }

        trajectories.clear();


        trajectories.add(makeTrajectoryBuilder(Speed.SLOW)
                .getStone(firstSkystone)
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(true)
                .actualSplineTo(new Pose2d(-10.0, -39.0, Math.toRadians(180.0)))
                .actualStrafeTo(new Vector2d(24.0, -37.0))
                .actualSplineTo(new Pose2d(41.5, -37.0, Math.toRadians(270.0)))
                .actualStrafeTo(new Vector2d(41.5, -29.0))
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(false)
                .actualSplineTo(new Pose2d(24.0, -48.0, Math.toRadians(180.0)))
                .actualStrafeTo(new Vector2d(45.0, -48.0))
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(false)
                .park()
                .build());
        return trajectories;
    }

    public List<Trajectory> getTrajectories2Stones(SkystonePattern skystonePattern) {
        int firstSkystone = 3;
        int secondSkystone = 3;
        if (skystonePattern == SkystonePattern.LEFT) {
            firstSkystone = 3;
            secondSkystone = 0;
        } else if (skystonePattern == SkystonePattern.MIDDLE) {
            firstSkystone = 4;
            secondSkystone = 1;
        } else if (skystonePattern == SkystonePattern.RIGHT) {
            firstSkystone = 5;
            secondSkystone = 2;
        }

        trajectories.clear();


        trajectories.add(makeTrajectoryBuilder(Speed.SLOW)
                .getStone(firstSkystone)
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(true)
                .actualSplineTo(new Pose2d(-10.0, -39.0, Math.toRadians(180.0)))
                .actualStrafeTo(new Vector2d(24.0, -37.0))
                .actualSplineTo(new Pose2d(41.5, -37.0, Math.toRadians(270.0)))
                .actualStrafeTo(new Vector2d(41.5, -29.0))
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(false)
                .actualSplineTo(new Pose2d(24.0, -48.0, Math.toRadians(180.0)))
                .actualStrafeTo(new Vector2d(45.0, -48.0))
                .build());

        trajectories.add(makeTrajectoryBuilder(Speed.SLOW)
                .actualStrafeTo(new Vector2d(10.0,  -37.0))
                .actualStrafeTo(new Vector2d(-10.0,  -37.0))
                .getStone(secondSkystone)
                .build());
        trajectories.add(makeTrajectoryBuilder()
                .actualSetReversed(true)
                .actualSplineTo(new Pose2d(-10.0, -37.0, Math.toRadians(180.0)))
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
