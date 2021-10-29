package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FourBar {

    private DcMotor fourBar;

    double ticksPerRev = 1425.1*2; // *2 because of the external gear ratio
    double ticksPerDegree = ticksPerRev/360;

    // Define the safe range
    private double safeRangeMin = -1;
    private double safeRangeMax = 110;

    // Positions of the 4b, in degrees, of the 4 places we would want it to run to
    public double level1 = 5;
    public double level2 = 30;
    public double level3 = 90;

   public void init(HardwareMap hwmap){ //
        fourBar = hwmap.get(DcMotor.class,"fourbar");
        fourBar.setTargetPosition(0);
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

    public void zero(){ // Reset the zero point (the angle where it's fully retracted)
       fourBar.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       fourBar.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
