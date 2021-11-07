package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="",group="")
public class FourBarPosTest extends LinearOpMode {
    // Pre-init
    DcMotor fourB;
    @Override
    public void runOpMode() {
        // Init
    fourB = hardwareMap.dcMotor.get("fourbar");
    fourB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    fourB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    fourB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // Autonomous instructions
            telemetry.addData("pos",fourB.getCurrentPosition());
            telemetry.update();

        }
    }
}
