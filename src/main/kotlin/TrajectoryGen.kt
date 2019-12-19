import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.visualizer.AutoTrajectoryGenerator
import com.visualizer.TrajectoryUtils
import com.visualizer.TrajectoryUtils.flipIfBlue


object TrajectoryGen {
    private var startPose = Pose2d(-33.0, -63.0, 90.0.toRadians)

    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val trajectories = AutoTrajectoryGenerator(TrajectoryUtils.Alliance.BLUE, startPose)

        startPose = flipIfBlue(trajectories.alliance, startPose)

        for (traj in trajectories.getTrajectories1Stone(AutoTrajectoryGenerator.SkystonePattern.LEFT)) {
            list.add(traj)
        }

        return list
    }

    fun drawOffbounds() {
        GraphicsUtil.fillRect(Vector2d(0.0, -63.0), 18.0, 18.0) // robot against the wall
    }
}

val Double.toRadians get() = (Math.toRadians(this))