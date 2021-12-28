package org.firstinspires.ftc.teamcode.util;

public class Utility {
    // Takes an input and if it is outside the range, make it inside the range
    public double clipValue(double min, double max, double input){
        double output;
        if (input > min && input < max) output = input;
        else {
            if (input < min) output = min;
            else output = max;
        }
        return output;
    }
}
