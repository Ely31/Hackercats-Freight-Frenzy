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


    @Override
    public void runOpMode() {
        // Init
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(-36,-63,Math.toRadians(-90)));
        int delay = 5;

        test = drive.trajectorySequenceBuilder(new Pose2d(-36,-63,Math.toRadians(-90)))
                .back(0.5)
                .splineToSplineHeading(new Pose2d(12,-67,Math.toRadians(180)), Math.toRadians(-30))
                .back(26)
                .build();
        while (!isStarted()&&!isStopRequested()){
            if (gamepad1.dpad_right) {
                delay += 1;
                sleep(200);
            }
            if (gamepad1.dpad_left) {
                delay -=1;
                sleep(200);
            }
            telemetry.addData("delay",delay);
            telemetry.update();
        }

        waitForStart();
        // Pre-run
        if (opModeIsActive()) {
            // Autonomous

            sleep(delay* 1000L); // Wait the number of second configured in init
            drive.followTrajectorySequence(test); // Follow the trajectory
        }
    }
}
