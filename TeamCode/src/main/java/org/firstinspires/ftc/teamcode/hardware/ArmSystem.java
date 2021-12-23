package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArmSystem {

    private DcMotor fourBar;
    private DcMotor turret;

    private final double FOURBAR_TICKS_PER_REV = 1425.1*2; // *2 because of the external gear ratio
    private final double FOURBAR_TICKS_PER_DEGREE = FOURBAR_TICKS_PER_REV /360.0;

    private final double TURRET_TICKS_PER_REV = 384.5*5; // *5 because of gear redection
    private final double TURRET_TICKS_PER_DEGREE = TURRET_TICKS_PER_REV/360.0;

    // Define the safe range of the 4b
    private double fourBarSafeRangeMin = -0.5; // This will be increased if the turret is rotated
    private final double FOURBAR_SAFERANGE_MAX = 110;
    private final double FOURBAR_SAFE_TO_SPIN_TURRET_ANGLE = 20;

    // Define the safe range for the turret
    private final double TURRET_SAFERANGE_MIN = -90;
    private final double TURRET_SAFERANGE_MAX = 90;
    private final double TURRET_SAFE_TO_RETRACT_FOURBAR_RANGE = 2;

    private final double FOURBAR_MAX_SPEED = 0.5; // The speed the 4b will always run at while doing any movement
    private final double TURRET_MAX_SPEED = 0.3; // Max speed of the turret

    // Positions of the 4b, in degrees, of the 3 levels we want it to run to
    public double level1 = 25;
    public double level2 = 53;
    public double level3 = 90;

    public int activeLevel; // The level the 4b will run to when told to raise, and the level whose offset is edited
    public int turretActiveAngle;

    public void init(HardwareMap hwmap){ //
        fourBar = hwmap.get(DcMotor.class,"fourbar");
        turret = hwmap.get(DcMotor.class,"turret");
        activeLevel = 3;
        turretActiveAngle = 0;
        zeroFourbar();
        zeroTurret();
    }

    public double getFourbarAngle(){
        return (fourBar.getCurrentPosition()/FOURBAR_TICKS_PER_DEGREE);
    }

    public double getTurretAngle(){
        return (turret.getCurrentPosition()/TURRET_TICKS_PER_DEGREE);
    }

    public void updateFourBarMin(){
        if (Math.abs(getTurretAngle()) < TURRET_SAFE_TO_RETRACT_FOURBAR_RANGE){
            fourBarSafeRangeMin = 0;
        }
        else {
            fourBarSafeRangeMin = FOURBAR_SAFE_TO_SPIN_TURRET_ANGLE;
        }
    }

    public boolean TurretSafeToMove(){
        if (getFourbarAngle() > FOURBAR_SAFE_TO_SPIN_TURRET_ANGLE) return true;
        else return false;
    }

    public void fourbarRunToAngle(double angle){// Converts angle input to ticks and runs the motor there after checking if it's safe
        updateFourBarMin();
        if ((angle > fourBarSafeRangeMin && angle < FOURBAR_SAFERANGE_MAX)) {
            fourBar.setTargetPosition((int) (angle* FOURBAR_TICKS_PER_DEGREE));
        }
            // Checks if the angle is in a safe range before running there
            // We don't want to make a mistake in the code and break the 4b because of it (or driver error)
        else if (angle < fourBarSafeRangeMin) fourBar.setTargetPosition((int)(fourBarSafeRangeMin * FOURBAR_TICKS_PER_DEGREE));
            // If the input angle is below the safe range, run the 4b to the minimum of the range
        else if (angle > FOURBAR_SAFERANGE_MAX) fourBar.setTargetPosition((int)(FOURBAR_SAFERANGE_MAX * FOURBAR_TICKS_PER_DEGREE));
        // If the input angle is above the safe range, run the 4b to the to maximum of the range
        fourBar.setPower(FOURBAR_MAX_SPEED);
        fourBar.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void turretRunToAngle(double degrees){
        // Check if the fourbar is high enough and that the input angle is within the safe limit before moving
        if (TurretSafeToMove() && (degrees > TURRET_SAFERANGE_MIN && degrees < TURRET_SAFERANGE_MAX)) {
            turret.setTargetPosition((int) (degrees * TURRET_TICKS_PER_DEGREE));
            turret.setPower(TURRET_MAX_SPEED);
            turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void retractFourbar(){
        fourbarRunToAngle(0);
    }

    public void runToLevel1(){
        fourbarRunToAngle(level1);
    }

    public void runToLevel2(){
        fourbarRunToAngle(level2);
    }

    public void runToLevel3(){
        fourbarRunToAngle(level3);
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

    // edit the positions of each level so drivers can account for shipping hub wobble
    public void editFourbarLevelOffset(double change){
        switch (activeLevel){
            case 1: level1 += change;
                break;

            case 2: level2 += change;
                break;

            case 3: level3 +=change;
                break;
        }
    }

    // Control both 4b and turret with one method
    public void setArmPosition(double fourbarAngle, double turretAngle){
        fourbarRunToAngle(fourbarAngle);
        turretRunToAngle(turretAngle);
    }

    public void zeroFourbar(){ // Reset the zero point (the angle where it's fully retracted)
        fourBar.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void zeroTurret(){
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
