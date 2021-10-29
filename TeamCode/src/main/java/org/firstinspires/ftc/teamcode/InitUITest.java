package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="",group="")
public class InitUITest extends LinearOpMode {
    // Pre-init

    TruePress dPadUp = new TruePress();
    TruePress dPadDown = new TruePress();

    int val1 = 0;
    int val2 = 1;
    boolean val3 = false;

    int numSelected = 1;

    char val1Selected = '.';
    char val2Selected = '.';
    char val3Selected = '.';

    @Override
    public void runOpMode() {
        // Init
        while (!isStarted()&&!isStopRequested()){
            if (dPadUp.trueInput(gamepad1.dpad_up)) numSelected +=1;
            if (dPadDown.trueInput(gamepad1.dpad_down)) numSelected -=1;
            if (numSelected == 4) numSelected = 1; // Loop it back to 1
            if (numSelected == 0) numSelected = 1;


        }
        waitForStart();
    
        // Pre-run
    
        if (opModeIsActive()) {
            // Autonomous instructions
        }
    }
}
