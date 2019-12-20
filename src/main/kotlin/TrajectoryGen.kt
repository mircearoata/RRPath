import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.visualizer.AutoTrajectoryGenerator
import com.visualizer.TrajectoryUtils.Alliance
import com.visualizer.TrajectoryUtils.START_POSE_FOUNDATION


object TrajectoryGen {

    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()
        val trajectories = AutoTrajectoryGenerator(Alliance.BLUE, START_POSE_FOUNDATION)

        for (traj in trajectories.getTrajectoriesMoveFoundation(/*AutoTrajectoryGenerator.SkystonePattern.LEFT*/)) {
            list.add(traj)
        }

        return list
    }

    fun drawOffbounds() {
        GraphicsUtil.fillRect(Vector2d(0.0, -63.0), 18.0, 18.0) // robot against the wall
    }
}

val Double.toRadians get() = (Math.toRadians(this))