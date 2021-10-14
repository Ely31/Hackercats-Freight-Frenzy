package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Intake;

@TeleOp(name="",group="")
public class intakeClassTest extends LinearOpMode {
    // Pre-init
    Intake intake = new Intake();
    long toggle = 0;
    @Override
    public void runOpMode() {
        // Init
        intake.init(hardwareMap);

        boolean prevA = false;

        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop
            if (!prevA && gamepad1.a) {
                if (toggle == 0) toggle = 1;
                if (toggle == 1) toggle = 0;
            }
            prevA = gamepad1.a;
            intake.toggleintake(toggle);
        }
    }
}
