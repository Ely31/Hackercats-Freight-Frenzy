package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.Deposit;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.TeleopDrive;

@TeleOp(name="",group="")
public class testFullTeleop extends LinearOpMode {
    // Pre-init

    TeleopDrive drive = new TeleopDrive();
    Intake intake = new Intake();
    Deposit deposit = new Deposit();

    ElapsedTime dumptime = new ElapsedTime();

    boolean prevA;
    boolean intakeOn;

    @Override
    public void runOpMode() {
        // Init
        drive.init(hardwareMap);
        intake.init(hardwareMap);
        deposit.init(hardwareMap);

        intakeOn = false;
        prevA = false;

        dumptime.reset();

        boolean intakeIsReverseing = false;

        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop

            //mecdrive
            drive.drive(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,gamepad1.right_trigger);

            //intake control (this code is a nightmare)
            if (gamepad1.b) {
                intake.reverse();
                intakeIsReverseing = true;
            } else if (gamepad1.a && !prevA){
                intakeOn = !intakeOn;
                if (intakeOn) intake.on();
                else intake.off();
            }

            if (intakeIsReverseing && !gamepad1.b) {
                if (intakeOn) intake.on();
                else intake.off();
                intakeIsReverseing = false;
            }
            prevA = gamepad1.a;


            // deposit
            if (gamepad1.left_bumper){
                dumptime.reset();
            }
            deposit.dump(dumptime);
        }
    }
}
