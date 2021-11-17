package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.hardware.Deposit;
import org.firstinspires.ftc.teamcode.hardware.EocvBarcodePipeline;
import org.firstinspires.ftc.teamcode.hardware.FourBar;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.TruePress;

@Autonomous(name="",group="")
public class WareHouseSideAutoFull extends LinearOpMode {
    // Pre-init
    Camera webcam  = new Camera();
    EocvBarcodePipeline pipeline = new EocvBarcodePipeline();
    SampleMecanumDrive drive;
    FourBar fourBar = new FourBar();
    Deposit deposit = new Deposit();
    Intake intake = new Intake();

    int hubActiveLevel;

    final double originToWall = 141.0/2.0; // I guess the field is actually 141 inches wide
    final double wallDistance = originToWall - 6.5; // Center of bot is 6.5in from wall

    final Pose2d startPos = new Pose2d(11.4,-(originToWall-9), Math.toRadians(-90));
    Pose2d depositPos;
    Trajectory depositPreLoad;
    TrajectorySequence park;


    int side = 1; // Red alliance is 1, blue is -1

    @Override
    public void runOpMode() {
        // Init
        webcam.init(hardwareMap);
        webcam.webcam.setPipeline(pipeline);
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(startPos);
        fourBar.init(hardwareMap);
        deposit.init(hardwareMap);
        intake.init(hardwareMap);

        ElapsedTime depositTimer = new ElapsedTime();
        ElapsedTime pipelineThrottle = new ElapsedTime();

        FtcDashboard.getInstance().startCameraStream(webcam.webcam, 1); // Stream to dashboard at 1 fps

        while (!isStarted()&&!isStopRequested()){ // Init loop

            if (gamepad1.a) side = 1;
            if (gamepad1.b) side = -1;
             // Select alliance with gamepad and display it to telemetry
            telemetry.addData("red is 1",", blue is -1");
            telemetry.addData("alliance",side);
            telemetry.update();

           if (pipelineThrottle.milliseconds() > 2000) {// Throttle loop times to 2 seconds

               switch (pipeline.getBarcodePos()){
                   case 1:
                       if (side == -1) hubActiveLevel = 3;
                       else hubActiveLevel = 1;
                       break;
                   case 3:
                       if (side == -1) hubActiveLevel = 1;
                       else hubActiveLevel = 3;
                       break;
                   case 2:
                       hubActiveLevel = 2;
                       break;
               }

               switch (hubActiveLevel) {
                   case 1:
                       depositPos = new Pose2d(-7.0, -45*side, Math.toRadians(-70*side));
                       break;
                   case 2:
                       depositPos = new Pose2d(-7, -44*side, Math.toRadians(-70*side));
                       break;
                   case 3:
                       depositPos = new Pose2d(-7, -43*side, Math.toRadians(-70*side));
                       break;
                   case 0:
                       depositPos = new Pose2d(-7, -43.5, Math.toRadians(-70*side));
                       break;
               }

               depositPreLoad = drive.trajectoryBuilder(startPos)
                       .lineToSplineHeading(depositPos)
                       .build();

               park = drive.trajectorySequenceBuilder(depositPreLoad.end())
                       .lineToSplineHeading(new Pose2d(0, -wallDistance*side, Math.toRadians(0*side)))
                       .addTemporalMarker(0.5, () -> {
                           fourBar.retract();
                       })
                       .lineToSplineHeading(new Pose2d(36, -wallDistance*side, Math.toRadians(0*side))) // Go into warehouse
                       .strafeLeft(26)
                       .forward(18)
                       .build();

               telemetry.addData("going to level", hubActiveLevel);
               telemetry.update();
               pipelineThrottle.reset(); // Reset the throttle timer so the whole thing loops
           } // End of throttled section
        }// End of init loop

        waitForStart();
        // Pre-run
    
        if (opModeIsActive()) {
            // Autonomous instructions
            fourBar.runToLevel(hubActiveLevel); // Extend 4b before driving
            drive.followTrajectory(depositPreLoad); // Drive to spot where we'll deposit from
            depositTimer.reset();
            deposit.dump(depositTimer); // Dump
            sleep(1000); // Wait for that dump to finish
            deposit.dump(depositTimer);
            drive.followTrajectorySequence(park); // Park in warehouse
            intake.dropIntake();
        }
    }
}
