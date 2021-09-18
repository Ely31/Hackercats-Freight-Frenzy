package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class rrTest extends LinearOpMode {
    SampleMecanumDrive drive;
    TrajectorySequence test;
    Pose2d startpos = new Pose2d(-50,-50,Math.toRadians(0));

    @Override
    public void runOpMode() {
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(startpos);

        test =  drive.trajectorySequenceBuilder(startpos)
                .strafeLeft(55)
                .splineToSplineHeading(new Pose2d(100-50,69-50,Math.toRadians(-90)),Math.toRadians(0))
                .forward(12)
                .build();

        waitForStart();
        if (opModeIsActive()) {
            drive.followTrajectorySequence(test);
        }
    }
}
