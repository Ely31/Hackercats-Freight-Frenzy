package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name="AutoConfigSideTest",group="ParkAutos")
public class AutoConfigSideTest extends LinearOpMode {
    // Pre-init
    SampleMecanumDrive drive;
    TrajectorySequence test;

    int side = 1;
    int delay = 5;
    String sideName = "red";

    @Override
    public void runOpMode() {
        // Init
        drive = new SampleMecanumDrive(hardwareMap);

        while (!isStarted()&&!isStopRequested()){ // Init loop
            // Change delay with dpad left and right
            if (gamepad1.dpad_right) {
                delay += 1;
                sleep(200);
            }
            if (gamepad1.dpad_left) {
                delay -=1;
                sleep(200);
            }
            if (gamepad1.dpad_down) side = -1;
            if (gamepad1.dpad_up) side = 1;

            if (side == 1) sideName = "red";
            else sideName = "blue";

            telemetry.addData("side",sideName);
            telemetry.addData("delay",delay);

            if (gamepad1.a){
                telemetry.clear();

                drive.setPoseEstimate(new Pose2d(-36,-63*side,Math.toRadians(-90*side)));
                test = drive.trajectorySequenceBuilder(new Pose2d(-36,-63*side,Math.toRadians(-90*side)))
                        .back(0.5)
                        .splineToSplineHeading(new Pose2d(12,-67*side,Math.toRadians(180*side)), Math.toRadians(-30*side))
                        .back(26)
                        .build();

                telemetry.addLine("Trajectory built");
                telemetry.update();
            }
            telemetry.update();
        }

        waitForStart();
        // Pre-run
        if (opModeIsActive()) {
            // Autonomous

            sleep(delay* 1000L); // Wait the number of seconds configured in init
            drive.followTrajectorySequence(test); // Follow the trajectory
        }
    }
}
