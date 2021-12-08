package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CarouselSpinner {

    private DcMotor carousel;

    private final double TICKS_PER_ROTATION = 103.8;
    private final double MAX_SPEED = 0.17;

    public void init(HardwareMap hwmap){
        carousel = hwmap.get(DcMotor.class,"carousel");
        carousel.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void spinRotations(double rotations,double speed){ // Set the target pos to a number of rotations from the current pos
        carousel.setTargetPosition((int) (carousel.getCurrentPosition()+(rotations* TICKS_PER_ROTATION)));
        carousel.setPower(speed);
        carousel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void deliver(int side){ // To be used in auto to deliver the duck
        spinRotations(9*side,0.12);
    }

    public void setSpeed(float input){
        carousel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        carousel.setPower(input* MAX_SPEED);
    }
}
