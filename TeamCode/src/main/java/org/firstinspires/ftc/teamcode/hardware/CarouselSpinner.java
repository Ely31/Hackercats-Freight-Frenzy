package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CarouselSpinner {

    private DcMotor carousel;

    private final double ticksPerRotation = 103.8;

    public void init(HardwareMap hwmap){
        carousel = hwmap.get(DcMotor.class,"carousel");
    }

    public void spinRotations(double rotations,double speed){ // Set the target pos to a number of rotations from the current pos
        carousel.setTargetPosition((int) (carousel.getCurrentPosition()+(rotations*ticksPerRotation)));
        carousel.setPower(speed);
        carousel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void deliver(){ // To be used in auto to deliver the duck
        spinRotations(5,1);
    }

    public void setSpeed(double input, double speedMultiplier){
        carousel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        carousel.setPower(input*speedMultiplier);
    }
}
