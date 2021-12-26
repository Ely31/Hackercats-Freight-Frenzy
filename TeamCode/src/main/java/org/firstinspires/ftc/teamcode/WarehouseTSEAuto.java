package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.hardware.CapMech;
import org.firstinspires.ftc.teamcode.hardware.Deposit;
import org.firstinspires.ftc.teamcode.hardware.FourBar;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.AutoToTele;
import org.firstinspires.ftc.teamcode.vision.SkystoneStyleThreshold;

@Autonomous
public class WarehouseTSEAuto extends LinearOpMode {
    // Pre-init
    Camera webcam  = new Camera();
    SkystoneStyleThreshold pipeline = new SkystoneStyleThreshold();
    SampleMecanumDrive drive;
    FourBar fourBar = new FourBar();
    Deposit deposit = new Deposit();
    Intake intake = new Intake();
    CapMech capMech = new CapMech();

    int hubActiveLevel = 0;

    int side; // Red alliance is 1, blue is -1

    final double originToWall = 141.0/2.0; // I guess the field is actually 141 inches wide
    final double wallDistance = originToWall - 6.5; // Center of bot is 6.5in from wall

    // Realative to warehouse
    private Pose2d farTsePosition;
    private Pose2d middleTsePosition;
    private Pose2d closeTsePosition;

    Pose2d startPos = new Pose2d(11.4,-(originToWall-9), Math.toRadians(-90));
    Pose2d depositPos;
    Pose2d tsePos;
    Trajectory depositPreLoad;
    TrajectorySequence pickUpTSE;
    TrajectorySequence park;

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
        capMech.init(hardwareMap);

        ElapsedTime depositTimer = new ElapsedTime();
        ElapsedTime pipelineThrottle = new ElapsedTime();

        FtcDashboard.getInstance().startCameraStream(webcam.webcam, 1); // Stream to dashboard at 1 fps

        AutoToTele.allianceSide = 1;

        tsePos = new Pose2d(12,-50*side,Math.toRadians(225*side));

        while (!isStarted()&&!isStopRequested()){ // Init loop

            // Select alliance with gamepad and display it to telemetry
            if (gamepad1.b) AutoToTele.allianceSide = 1;
            if (gamepad1.x) AutoToTele.allianceSide = -1;
            side = AutoToTele.allianceSide;
            switch (side) {
                case 1:
                    telemetry.addLine("red alliance");
                    break;
                case -1:
                    telemetry.addLine("blue alliance");
                    break;
            }
            telemetry.addData("going to level", hubActiveLevel);
            telemetry.update();

           if (pipelineThrottle.milliseconds() > 1000) {// Throttle loop times to 1 second
               // Update startpos to match side
               startPos = new Pose2d(11.4,(-(originToWall-9))*side, Math.toRadians(-90*side));
               drive.setPoseEstimate(startPos); // Set pose estimate to match which side of the field we're on

               farTsePosition = new Pose2d(3.2,-50*side,Math.toRadians(-90*side));
               middleTsePosition = new Pose2d(11.8,-50*side,Math.toRadians(-90*side));
               closeTsePosition = new Pose2d(8,-45*side,Math.toRadians(225*side));

               switch (pipeline.getAnalysis()){
                   case LEFT:
                       hubActiveLevel = 1;
                       break;
                   case MIDDLE:
                       hubActiveLevel = 2;
                       break;
                   case RIGHT:
                       hubActiveLevel = 3;
                       break;
               }

               switch (hubActiveLevel) {
                   case 1:
                       depositPos = new Pose2d(-7.0, -43*side, Math.toRadians(-70*side));
                       if (side == 1) tsePos = farTsePosition; // Switch close and far positions on blue alliance
                       else tsePos = closeTsePosition;
                       break;
                   case 2:
                       depositPos = new Pose2d(-7, -44*side, Math.toRadians(-70*side));
                       tsePos = middleTsePosition;
                       break;
                   case 3:
                       depositPos = new Pose2d(-7, -43*side, Math.toRadians(-70*side));
                       if (side == 1) tsePos = closeTsePosition;  // Switch close and far positions on blue alliance
                       else tsePos = farTsePosition;
                       break;
               }

               // Tse pick up trajectory
               pickUpTSE = drive.trajectorySequenceBuilder(startPos)
                       .addTemporalMarker(() ->{
                           capMech.openGripper();
                           capMech.levelBase();
                       })
                       .lineToSplineHeading(tsePos)
                       .waitSeconds(1)
                       .addTemporalMarker(() -> capMech.closeGripper())
                       .waitSeconds(1)
                       .addTemporalMarker(() -> capMech.retract())
                       .build();

               // Deposit trajectory
               depositPreLoad = drive.trajectoryBuilder(pickUpTSE.end())
                       .lineToSplineHeading(depositPos)
                       .build();

               // Park trajectory
               park = drive.trajectorySequenceBuilder(depositPreLoad.end())
                       .lineToSplineHeading(new Pose2d(0, -wallDistance*side, Math.toRadians(0*side)))
                       .addTemporalMarker(0.5, () -> fourBar.retract())
                       .lineToSplineHeading(new Pose2d(36, -wallDistance*side, Math.toRadians(0*side))) // Go into warehouse
                       .lineTo(new Vector2d(36,(-(originToWall-34))*side))
                       .forward(18)
                       .build();

               // Telemetry
               telemetry.addData("going to level", hubActiveLevel);
               telemetry.update();
               pipelineThrottle.reset(); // Reset the throttle timer so the whole thing loops
           } // End of throttled section
        }// End of init loop

        waitForStart();
        // Pre-run
    
        if (opModeIsActive()) {
            // Autonomous instructions
            drive.followTrajectorySequence(pickUpTSE);
            fourBar.runToLevel(hubActiveLevel); // Extend 4b before driving
            drive.followTrajectory(depositPreLoad); // Drive to spot where we'll deposit from
            depositTimer.reset();
            deposit.dump(depositTimer); // Dump
            sleep(1000); // Wait for that dump to finish
            deposit.dump(depositTimer);
            drive.followTrajectorySequence(park); // Park in warehouse
            intake.dropIntake();

            AutoToTele.endOfAutoPose = drive.getPoseEstimate();
            AutoToTele.endOfAutoHeading = drive.getExternalHeading();
            // Save this information to a class so we can use it in tele to calibate feild centric

            // For debug purposes
            telemetry.addData("endheading",Math.toDegrees( AutoToTele.endOfAutoHeading));
            telemetry.update();
            sleep(2*1000);
        }
    }
}
