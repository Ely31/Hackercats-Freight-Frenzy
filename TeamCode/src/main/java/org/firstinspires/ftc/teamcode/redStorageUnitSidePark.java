package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name="",group="ParkAutos")
public class redStorageUnitSidePark extends LinearOpMode {
    // Pre-init
    SampleMecanumDrive drive;
    TrajectorySequence test;
    @Override
    public void runOpMode() {
        // Init
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(-36,-63,Math.toRadians(-90)));
        double delay = 5;

        test = drive.trajectorySequenceBuilder(new Pose2d(-36,-63,Math.toRadians(-90)))
                .back(0.5)
                .splineToSplineHeading(new Pose2d(12,-67,Math.toRadians(180)), Math.toRadians(-30))
                .back(26)
                .build();

        waitForStart();
        // Pre-run

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


        if (opModeIsActive()) {
            // Autonomous instructions

            sleep((long) (delay*1000));
            drive.followTrajectorySequence(test);
        }
    }
}
