package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.CarouselSpinner;
import org.firstinspires.ftc.teamcode.hardware.Deposit;
import org.firstinspires.ftc.teamcode.hardware.FourBar;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.TeleopDrive;
import org.firstinspires.ftc.teamcode.util.TruePress;

@TeleOp
public class fullTeleop extends LinearOpMode {
    // Pre-init

    TeleopDrive drive = new TeleopDrive();
    Intake intake = new Intake();
    Deposit deposit = new Deposit();
    FourBar fourBar = new FourBar();
    CarouselSpinner carouselSpinner = new CarouselSpinner();

    TruePress truegp2a = new TruePress();

    ElapsedTime dumptime = new ElapsedTime();

    enum FourBarState {
        EXTENDED,
        RETRACTED
    }

    FourBarState fourBarState = FourBarState.RETRACTED;

    @Override
    public void runOpMode() {
        // Init
        drive.init(hardwareMap);
        intake.init(hardwareMap);
        deposit.init(hardwareMap);
        fourBar.init(hardwareMap);
        carouselSpinner.init(hardwareMap);

        dumptime.reset();
        sleep(300); // Hack to fix deposit firing if you init too quickly

        telemetry.addLine("Initialized");
        telemetry.update();

        waitForStart();
        // Pre-run
        while (opModeIsActive()) { // TeleOp loop
            // Mecdrive control
            drive.drive(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,gamepad1.right_trigger);

            // Intake control
            if (gamepad1.b) intake.reverse();
            else intake.toggle(gamepad1.a);

            // Deposit control
            if (gamepad1.left_bumper) dumptime.reset();
            deposit.dump(dumptime);

            // Change active level of 4b
            if (gamepad2.x) fourBar.activeLevel = 1;
            else if (gamepad2.y) fourBar.activeLevel = 2;
            else if (gamepad2.b) fourBar.activeLevel = 3;

            // Tune the height of the active 4b level to account for shipping hub inbalance
            if (gamepad2.dpad_up) fourBar.editLevelOffset(0.1);
            if (gamepad2.dpad_down) fourBar.editLevelOffset(-0.1);

            // Four bar control
            switch (fourBarState) { // Gamepad2 A toggles the extended/retracted state of the 4b
                case RETRACTED:
                    fourBar.retract();
                    if (truegp2a.trueInput(gamepad2.a)) {
                        fourBar.runToLevel(fourBar.activeLevel);
                    fourBarState = FourBarState.EXTENDED;
                    }
                    break;
                case EXTENDED:
                    fourBar.runToLevel(fourBar.activeLevel);
                    if (truegp2a.trueInput(gamepad2.a)) {
                        fourBarState = FourBarState.RETRACTED;
                    }
                    break;
            }

            // Carousel mech control
            carouselSpinner.setSpeed(gamepad2.left_trigger- gamepad2.right_trigger,0.3);
            // Subracting the right trigger input from the left is an easy way to make the left trigger turn it one way
            // and the right trigger turn it another
        }
    }
}
