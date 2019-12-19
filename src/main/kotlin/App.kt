import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

import javafx.stage.Stage
import javafx.util.Duration

class App : Application() {
    val robotRect = Rectangle(100.0, 100.0, 10.0, 10.0)
    val startRect = Rectangle(100.0, 100.0, 10.0, 10.0)
    val endRect = Rectangle(100.0, 100.0, 10.0, 10.0)

    var startTime = Double.NaN
    val trajectories = TrajectoryGen.createTrajectory()

    lateinit var fieldImage: Image
    lateinit var stage: Stage

    val trajectoryDurations = trajectories.map { it.duration() }
    val duration = trajectoryDurations.sum()
    val numberOfTrajectories = trajectories.size

    companion object {
        var WIDTH = 0.0
        var HEIGHT = 0.0
    }

    private var scrubbingSpeed = 1.0

    override fun start(stage: Stage?) {
        this.stage = stage!!
        fieldImage = Image("/field.png")

        val root = Group()

        WIDTH = fieldImage.width
        HEIGHT = fieldImage.height
        GraphicsUtil.pixelsPerInch = WIDTH / GraphicsUtil.FIELD_WIDTH
        GraphicsUtil.halfFieldPixels = WIDTH / 2.0

        val canvas = Canvas(WIDTH, HEIGHT)
        val gc = canvas.graphicsContext2D
        val t1 = Timeline(KeyFrame(Duration.millis(10.0), EventHandler<ActionEvent> { run(gc) }))
        t1.cycleCount = Timeline.INDEFINITE

        stage.scene = Scene(
            StackPane(
                root
            )
        )

        stage.scene.onKeyPressed = EventHandler<KeyEvent> {
            when (it.code) {
                KeyCode.SPACE -> paused = !paused
                KeyCode.LEFT -> runtime -= scrubbingSpeed
                KeyCode.RIGHT -> runtime += scrubbingSpeed
                KeyCode.ADD -> scrubbingSpeed *= 2
                KeyCode.SUBTRACT-> scrubbingSpeed /= 2
                else -> {}
            }
        }

        root.children.addAll(canvas, startRect, endRect, robotRect)

        stage.title = "PathVisualizer"
        stage.isResizable = false

        println("duration $duration")

        stage.show()
        t1.play()
    }

    var runtime = 0.0
    var prevTime = Clock.seconds

    var paused = false

    fun trajectoriesTime(trajectoryIdx: Int): Double {
        var x = 0.0
        for (i in 0 until trajectoryIdx)
            x += trajectoryDurations[i]
        return x
    }

    fun run(gc: GraphicsContext) {
        val deltaTime = Clock.seconds - prevTime
        prevTime = Clock.seconds
        if (!paused) {
            runtime += deltaTime
        }
        if (startTime.isNaN())
            startTime = Clock.seconds

        GraphicsUtil.gc = gc
        gc.drawImage(fieldImage, 0.0, 0.0)

        gc.lineWidth = GraphicsUtil.LINE_THICKNESS

        gc.globalAlpha = 0.5
        GraphicsUtil.setColor(Color.RED)
        TrajectoryGen.drawOffbounds()
        gc.globalAlpha = 1.0

        while (runtime >= duration)
            runtime -= duration
        while (runtime < 0)
            runtime += duration

        var activeTrajectoryIndex = 0
        var prevDurations = 0.0
        while (activeTrajectoryIndex < numberOfTrajectories && prevDurations + trajectoryDurations[activeTrajectoryIndex] < runtime) {
            prevDurations += trajectoryDurations[activeTrajectoryIndex]
            activeTrajectoryIndex++
        }

        val trajectory = trajectories[activeTrajectoryIndex]

        val profileTime = runtime - prevDurations

        val start = trajectories.first().start()
        val end = trajectories.last().end()
        val current = trajectory[profileTime]

        trajectories.forEach { GraphicsUtil.drawSampledPath(it.path) }

        GraphicsUtil.updateRobotRect(startRect, start, GraphicsUtil.END_BOX_COLOR, 0.5)
        GraphicsUtil.updateRobotRect(endRect, end, GraphicsUtil.END_BOX_COLOR, 0.5)

        GraphicsUtil.updateRobotRect(robotRect, current, GraphicsUtil.ROBOT_COLOR, 0.75)
        GraphicsUtil.drawRobotVector(current)
        stage.title = "Profile duration : ${"%.2f".format(duration)} - time in profile ${"%.2f".format(profileTime)} - scrubbing speed ${"%.4f".format(scrubbingSpeed)}"
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}