package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class AutoFreightPathTest extends LinearOpMode {
    SampleMecanumDrive drive;
    TrajectorySequence test;
    Pose2d startpos = new Pose2d(11.4, -61,Math.toRadians(-90));

    @Override
    public void runOpMode() {
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(startpos);

        test =  drive.trajectorySequenceBuilder(startpos)
                .lineToSplineHeading(new Pose2d(-7.0, -45,Math.toRadians(-70)))
                .lineToSplineHeading(new Pose2d(0,-61,Math.toRadians(0)))
                .build();

        waitForStart();
        if (opModeIsActive()) {
            drive.followTrajectorySequence(test);
        }
    }
}
