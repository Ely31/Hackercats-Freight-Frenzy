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
import org.firstinspires.ftc.teamcode.hardware.CarouselSpinner;
import org.firstinspires.ftc.teamcode.hardware.Deposit;
import org.firstinspires.ftc.teamcode.hardware.EocvBarcodePipeline;
import org.firstinspires.ftc.teamcode.hardware.FourBar;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.AutoToTele;

@Autonomous(name="",group="")
public class CarouselSideAutoFull extends LinearOpMode {
    // Pre-init
    Camera webcam  = new Camera();
    EocvBarcodePipeline pipeline = new EocvBarcodePipeline();
    SampleMecanumDrive drive;
    FourBar fourBar = new FourBar();
    Deposit deposit = new Deposit();
    Intake intake = new Intake();
    CarouselSpinner carouselMech = new CarouselSpinner();

    int hubActiveLevel = 0;

    final double originToWall = 141.0/2.0; // I guess the field is actually 141 inches wide
    final double wallDistance = originToWall - 6.5; // Center of bot is 6.5in from wall
    final double carouselXCoordinate = -55;
    final double carouselYCoordinate = -58;

    Pose2d startPos = new Pose2d(11.4,-(originToWall-9), Math.toRadians(-90));
    Pose2d depositPos;
    Trajectory depositPreLoad;
    TrajectorySequence carouselAndPark;


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
        carouselMech.init(hardwareMap);

        ElapsedTime depositTimer = new ElapsedTime();
        ElapsedTime pipelineThrottle = new ElapsedTime();

        FtcDashboard.getInstance().startCameraStream(webcam.webcam, 1); // Stream to dashboard at 1 fps

        while (!isStarted()&&!isStopRequested()){ // Init loop

            if (gamepad1.a) side = 1;
            if (gamepad1.b) side = -1;
             // Select alliance with gamepad and display it to telemetry
            telemetry.addData("red is 1",", blue is -1");
            telemetry.addData("alliance",side);
            telemetry.addData("going to level", hubActiveLevel);
            telemetry.update();

           if (pipelineThrottle.milliseconds() > 2000) {// Throttle loop times to 2 seconds
               // Update startpos to match side
               startPos = new Pose2d(-35,(-(originToWall-9))*side, Math.toRadians(-90*side));
               drive.setPoseEstimate(startPos);

               switch (pipeline.getBarcodePos()){
                   case 1:
                       hubActiveLevel = 1;
                       break;
                   case 3:
                       hubActiveLevel = 3;
                       break;
                   case 2:
                       hubActiveLevel = 2;
                       break;
               }

               switch (hubActiveLevel) {
                   case 1:
                       depositPos = new Pose2d(-16.60, -43*side, Math.toRadians(250*side));
                       break;
                   case 2:
                       depositPos = new Pose2d(-16.6, -44*side, Math.toRadians(250*side));
                       break;
                   case 3:
                       depositPos = new Pose2d(-16.6, -43*side, Math.toRadians(250*side));
                       break;
                   case 0:
                       depositPos = new Pose2d(-16.6, -43.5, Math.toRadians(250*side));
                       break;
               }

               depositPreLoad = drive.trajectoryBuilder(startPos)
                       .lineToSplineHeading(depositPos)
                       .build();

               carouselAndPark = drive.trajectorySequenceBuilder(depositPreLoad.end())
                       // .lineToSplineHeading(new Pose2d(0, -wallDistance*side, Math.toRadians(0*side)))
                       .addTemporalMarker(0.5, () -> {
                           fourBar.retract();
                       })
                       .lineToSplineHeading(new Pose2d(carouselXCoordinate, carouselYCoordinate*side, Math.toRadians(0*side))) // Go to carousel
                       .addTemporalMarker(() -> {
                           carouselMech.deliver(side); // Spin carousel
                               })
                       .waitSeconds(4)
                       .lineTo(new Vector2d(carouselXCoordinate+7,carouselYCoordinate*side)) // Back off carousel
                       .lineToSplineHeading(new Pose2d(-62,-33*side,Math.toRadians(0*side))) // Park
                       .build();

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
            drive.followTrajectorySequence(carouselAndPark); // Park in warehouse
            intake.dropIntake();

            AutoToTele.endOfAutoPose = drive.getPoseEstimate();
            AutoToTele.endOfAutoHeading = drive.getExternalHeading();
            // Save this information to a class so we can use it in tele to calibate feild centric

            telemetry.addData("endheading",Math.toDegrees( AutoToTele.endOfAutoHeading));
            telemetry.update();
            sleep(5*1000);
        }
    }
}
