import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.visualizer.AutoTrajectoryGenerator
import com.visualizer.TrajectoryUtils.*


object TrajectoryGen {
    val ALLIANCE = Alliance.RED

    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val trajectories = AutoTrajectoryGenerator(ALLIANCE, START_POSE_SIDE)

        for (traj in trajectories.getTrajectoriesSideArm2Stones(AutoTrajectoryGenerator.SkystonePattern.LEFT)) {
            list.add(traj)
        }

        /*list.add(TrajectoryBuilder(Pose2d(-50.0,0.0, 0.0.toRadians), DriveConstraints(85.0, 150.0, 0.0, 360.0.toRadians, 0.0.toRadians, 0.0.toRadians))
            .forward(100.0)
            .build())*/

        return list
    }

    fun drawOffbounds() {
        GraphicsUtil.fillRect(flipIfBlue(ALLIANCE, Vector2d(0.0, BRIDGE_OUTER_Y)), 18.0, 18.0) // robot against the wall
        // GraphicsUtil.fillRect(flipIfBlue(ALLIANCE, Vector2d(0.0,  BRIDGE_INNER_Y)), 18.0, 18.0) // robot inner
    }
}

val Double.toRadians get() = (Math.toRadians(this))