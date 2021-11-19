package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class Intake {

    private DcMotor intake;
    private Servo intakeRelease;

    double releaseHoldPosition = 0.1;
    double releaseDropPosition = 0.3;

    private boolean lastInput;
    private boolean intakeToggledStatus;

    public void init(HardwareMap hwmap) {
        intake = hwmap.get(DcMotor.class, "intake");
        intakeRelease = hwmap.get(Servo.class,"intakeRelease");
        intakeRelease.setPosition(releaseHoldPosition);
        lastInput = false;
        intakeToggledStatus = false;
    }

    public void on(){
        intake.setPower(1);
    }
    public void off(){
        intake.setPower(0);
    }
    public void reverse(){
        intake.setPower(-1);
    }

    public void toggle(boolean input){
        if (input && !lastInput){
            intakeToggledStatus = !intakeToggledStatus;
        }
        if (intakeToggledStatus) on();
        else off();

        lastInput = input;
    }

    public void dropIntake(){
        intakeRelease.setPosition(releaseDropPosition);
    }

    public void resetDropper(){
        intakeRelease.setPosition(releaseHoldPosition);
    }
}
