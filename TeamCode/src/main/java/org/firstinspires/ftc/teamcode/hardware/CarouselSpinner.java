package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CarouselSpinner {

    private DcMotor carousel;

    private final double ticksPerRotation = 103.8;
    private final double maxSpeed = 0.2;

    public void init(HardwareMap hwmap){
        carousel = hwmap.get(DcMotor.class,"carousel");
    }

    public void spinRotations(double rotations,double speed){ // Set the target pos to a number of rotations from the current pos
        carousel.setTargetPosition((int) (carousel.getCurrentPosition()+(rotations*ticksPerRotation)));
        carousel.setPower(speed);
        carousel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void deliver(int side){ // To be used in auto to deliver the duck
        spinRotations(5*side,0.15);
    }

    public void setSpeed(float input){
        carousel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        carousel.setPower(input*maxSpeed);
    }
}
