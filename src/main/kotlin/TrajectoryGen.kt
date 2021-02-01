import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint


object TrajectoryGen {
    private val VELOCITY_CONSTRAINT = MinVelocityConstraint(listOf(AngularVelocityConstraint(30.0), MecanumVelocityConstraint(30.0, 16.0)))
    private val ACCELERATION_CONSTRAINT = ProfileAccelerationConstraint(30.0)

    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()

        list.add(TrajectoryBuilder(Pose2d(0.0,0.0,0.0), 0.0, VELOCITY_CONSTRAINT, ACCELERATION_CONSTRAINT)
            .splineToConstantHeading(Vector2d(0.0, 10.0), 0.0)
            .splineTo(Vector2d(10.0, 10.0), 0.0)
            .build())

        return list
    }

    fun drawOffbounds() {

    }
}

val Double.toRadians get() = (Math.toRadians(this))