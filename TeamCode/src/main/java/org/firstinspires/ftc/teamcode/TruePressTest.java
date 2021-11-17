package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.TruePress;

@TeleOp(name="",group="")
public class TruePressTest extends LinearOpMode {
    // Pre-init
    TruePress test = new TruePress();
    int testvar = 1;
    @Override
    public void runOpMode() {
        // Init
    
        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop
            if (test.trueInput(gamepad1.a)){
                if (testvar == 1) testvar = -1;
                if (testvar == -1) testvar = 1;
            }
            telemetry.addData("testvar", testvar);
            telemetry.update();
        }
    }
}
