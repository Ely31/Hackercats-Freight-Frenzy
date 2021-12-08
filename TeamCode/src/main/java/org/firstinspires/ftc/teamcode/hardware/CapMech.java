package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class CapMech {
    private Servo gripper;
    private Servo neck;
    private Servo basePivot;

    public static double GRIPPER_CLOSED_POSITION = 0;
    public static double GRIPPER_OPEN_POSITION = 1;

    public static double NECK_RETRACTED_POSITION = 0;
    public static double NECK_OPEN_POSITION = 1;

    public static double BASE_PIVOT_RETRACTED_POSITION = 0;
    public static double BASE_PIVOT_LEVEL_POSITION = 1;

    public void init(HardwareMap hwmap){
        gripper = hwmap.get(Servo.class, "capGripper");
        neck = hwmap.get(Servo.class, "capNeck");
        basePivot = hwmap.get(Servo.class, "capBasePivot");
        closeGripper();
        retractNeck();
        retractBase();
    }

    public void closeGripper(){
        gripper.setPosition(GRIPPER_CLOSED_POSITION);
    }
    public void openGripper(){
        gripper.setPosition(GRIPPER_OPEN_POSITION);
    }

    public void retractBase(){
        basePivot.setPosition(BASE_PIVOT_RETRACTED_POSITION);
    }
    public void levelBase(){
        basePivot.setPosition(BASE_PIVOT_LEVEL_POSITION);
    }

    public void retractNeck(){
        neck.setPosition(NECK_RETRACTED_POSITION);
    }
    public void openNeck(){
        neck.setPosition(NECK_OPEN_POSITION);
    }


    public void retract(){
        retractBase();
        retractNeck();
    }

    public void extend(){
        levelBase();
        openNeck();
    }
}