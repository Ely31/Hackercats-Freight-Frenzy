package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Intake;
import org.firstinspires.ftc.teamcode.hardware.TeleopDrive;

@Autonomous
public class intakeInchTest extends LinearOpMode {
    // Pre-init
    Intake intake = new Intake();
    TeleopDrive drive = new TeleopDrive();

    double speed = 0.1;
    @Override
    public void runOpMode() {
        // Init
        intake.init(hardwareMap);
        drive.init(hardwareMap);
    
        waitForStart();
    
        // Pre-run
    
        if (opModeIsActive()) {
            // Autonomous instructions
            intake.on();
            while(intake.freightStatus()){
            drive.setWheelPowers(speed,speed,speed,speed);
            }
            drive.setWheelPowers(0,0,0,0);
        }
    }
}
