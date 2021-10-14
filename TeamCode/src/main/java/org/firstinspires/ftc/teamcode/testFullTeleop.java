package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Deposit;
import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.TeleopDrive;

@TeleOp(name="",group="")
public class testFullTeleop extends LinearOpMode {
    // Pre-init

    TeleopDrive drive = new TeleopDrive();
    Intake intake = new Intake();
    Deposit deposit = new Deposit();


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

        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop

            //mecdrive
            drive.drive(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,gamepad1.right_trigger);

            //intake control
            if (gamepad1.b) intake.reverse();

            else if (gamepad1.a && !prevA){
                intakeOn = !intakeOn;
                if (intakeOn) intake.on();
                else intake.off();
            }
            prevA = gamepad1.a;

            //deposit control
            if (gamepad1.left_bumper){
                deposit.dump();
                sleep(200);
            }
        }
    }
}
