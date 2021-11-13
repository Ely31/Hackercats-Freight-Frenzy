package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FourBar {

    private DcMotor fourBar;

    private final double ticksPerRev = 1425.1*2; // *2 because of the external gear ratio
    private final double ticksPerDegree = ticksPerRev/360.0;
    private final double speed = 0.5; // The speed the 4b will always run at while doing any movement

    // Define the safe range
    private final double safeRangeMin = -0.5;
    private final double safeRangeMax = 110;

    // Positions of the 4b, in degrees, of the 3 levels we want it to run to
    public double level1 = 25;
    public double level2 = 53;
    public double level3 = 90;

    public int activeLevel; // The level the 4b will run to when told to raise, and the level whose offset is edited

   public void init(HardwareMap hwmap){ //
        fourBar = hwmap.get(DcMotor.class,"fourbar");
        activeLevel = 3;
        zero();
    }

    public void runToAngle(double angle){ // Converts angle input to ticks and runs the motor there after checking if it's safe
       if (angle > safeRangeMin && angle < safeRangeMax) fourBar.setTargetPosition((int) (angle*ticksPerDegree));
       // Checks if the angle is in a safe range before running there
        // We don't want to make a mistake in the code and break the 4b because of it (or driver error)
        else if (angle < safeRangeMin) fourBar.setTargetPosition((int)(safeRangeMin*ticksPerDegree));
        // If the input angle is below the safe range, run the 4b to the minimum of the range
        else if (angle > safeRangeMax) fourBar.setTargetPosition((int)(safeRangeMax*ticksPerDegree));
        // If the input angle is above the safe range, run the 4b to the to maximum of the range
        fourBar.setPower(speed);
        fourBar.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void retract(){
       runToAngle(0);
    }

    public void runToLevel1(){
       runToAngle(level1);
    }

    public void runToLevel2(){
       runToAngle(level2);
    }

    public void runToLevel3(){
       runToAngle(level3);
    }

    public void runToLevel(int level){ // Run to a level specified by an integer
       switch (level){
           case 1:
               runToLevel1();
               break;
           case 2:
               runToLevel2();
               break;
           case 3:
               runToLevel3();
               break;
       }
    }

    public void zero(){ // Reset the zero point (the angle where it's fully retracted)
       fourBar.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    // edit the positions of each level so drivers can account for shipping hub wobble
    public void editLevelOffset(double change){
       switch (activeLevel){
           case 1: level1 += change;
           break;

           case 2: level2 += change;
           break;

           case 3: level3 +=change;
           break;
       }
    }
}
