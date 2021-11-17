package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.Intake;
@Config
@TeleOp(name="",group="")
public class dropservoTest extends LinearOpMode {
    // Pre-init
    Intake intake = new Intake();
    @Override
    public void runOpMode() {
        // Init
    intake.init(hardwareMap);
        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop
            if (gamepad1.a) intake.dropIntake();
            else intake.resetDropper();
        }
    }
}
