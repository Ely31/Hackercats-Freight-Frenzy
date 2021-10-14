package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.TeleopDrive;

@TeleOp(name="",group="")
public class teleopdrive_classtest extends LinearOpMode {
    // Pre-init
    TeleopDrive drive = new TeleopDrive();
    @Override
    public void runOpMode() {
        // Init
        drive.init(hardwareMap);

        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop
            drive.drive(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x,gamepad1.right_trigger);

            telemetry.addData("heading", Math.toDegrees(-drive.heading));
            telemetry.update();
        }
    }
}
