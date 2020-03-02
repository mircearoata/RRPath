import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints
import com.visualizer.AutoTrajectoryGenerator
import com.visualizer.TrajectoryUtils.*


object TrajectoryGen {
    val ALLIANCE = Alliance.RED

    private val BASE_CONSTRAINTS = DriveConstraints(
        35.0, 40.0, 0.0,
        Math.toRadians(180.0), Math.toRadians(180.0), 0.0
    )

    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val trajectories = AutoTrajectoryGenerator(ALLIANCE, START_POSE_SIDE)

        for (traj in trajectories.getTrajectoriesSideArm2Stones(AutoTrajectoryGenerator.SkystonePattern.LEFT)) {
            list.add(traj)
        }

//        list.add(TrajectoryBuilder(Pose2d(0.0,0.0,0.0), 0.0, BASE_CONSTRAINTS)
//            .splineToConstantHeading(Pose2d(0.0, 10.0, 0.0))
//            .splineTo(Pose2d(10.0, 10.0, 0.0))
//            .build())

        return list
    }

    fun drawOffbounds() {
        GraphicsUtil.fillRect(flipIfBlue(ALLIANCE, Vector2d(0.0, BRIDGE_OUTER_Y)), 18.0, 18.0) // robot against the wall
        // GraphicsUtil.fillRect(flipIfBlue(ALLIANCE, Vector2d(0.0,  BRIDGE_INNER_Y)), 18.0, 18.0) // robot inner
    }
}

val Double.toRadians get() = (Math.toRadians(this))