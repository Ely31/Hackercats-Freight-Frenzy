package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name="",group="ParkAutos")
public class redWarehouseSidePark extends LinearOpMode {
    // Pre-init
    SampleMecanumDrive drive;
    TrajectorySequence test;
    @Override
    public void runOpMode() {
        // Init
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(36,-63,Math.toRadians(-90)));

        test = drive.trajectorySequenceBuilder(new Pose2d(36,-63,Math.toRadians(-90)))
                .back(2.5)
                .turn(Math.toRadians(90))
                .strafeRight(5.5)
                .forward(26)
                .strafeLeft(28)
                .forward(23)
                .build();

        waitForStart();
    
        // Pre-run
    
        if (opModeIsActive()) {
            // Autonomous instructions
            drive.followTrajectorySequence(test);
        }
    }
}
