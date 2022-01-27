package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous
public class rrIntakeTest extends LinearOpMode {
    // Pre-init
    Intake intake = new Intake();
    SampleMecanumDrive drive;

    Trajectory forward;
    TrajectorySequence back;

    @Override
    public void runOpMode() {
        // Init
        intake.init(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d(0,0,Math.toRadians(90)));

        forward =drive.trajectoryBuilder(new Pose2d(0,0,Math.toRadians(90)))
                .forward(50,
                        SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL,DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        back = drive.trajectorySequenceBuilder(new Pose2d(0,5,Math.toRadians(90)))
                .waitSeconds(0.5)
                .lineTo(new Vector2d(0,0))
                .build();
    
        waitForStart();
        if (opModeIsActive()) {
            // Autonomous instructions
            drive.followTrajectoryAsync(forward);
            intake.on();
            while(!intake.freightStatus()){
                drive.update();
            }
            drive.setMotorPowers(0,0,0,0);

            back = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                    .waitSeconds(0.5)
                    .lineTo(new Vector2d(0,0))
                    .build();

            drive.followTrajectorySequence(back);

        }
    }
}
