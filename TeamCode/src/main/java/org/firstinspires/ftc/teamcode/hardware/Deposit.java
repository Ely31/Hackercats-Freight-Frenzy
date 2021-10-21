package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Deposit {

    private Servo pusher;
    private Servo door;

    double pusherhome; // The retracted position of the pusher
    double doorhome; // The closed position of the door

    //delay between opening the door and pushing freight out
    public static long pusherlag = 50;

    //time to wait before we close the door and reset
    // the pusher after the freight has fallen out
    public static long falltime = 300;

    public void init(HardwareMap hwmap) {
        pusher = hwmap.get(Servo.class, "pusher");
        door = hwmap.get(Servo.class, "door");

        pusherhome = 0.05;
        doorhome = 0.1;

        pusher.setPosition(pusherhome);
        door.setPosition(doorhome);
    }

    public void dump(ElapsedTime time){
        if (time.milliseconds() < pusherlag) door.setPosition(0.6); // Open the door if the timer is less than pusherlag
        if (time.milliseconds() > pusherlag && time.milliseconds() < pusherlag+falltime) pusher.setPosition(0.45); // Push freight out once pusherlag is over
        if (time.milliseconds() > pusherlag+falltime) { // Reset deposit after falltime is over
            pusher.setPosition(pusherhome);
            door.setPosition(doorhome);
        }

    }

    public void reset(){
        pusher.setPosition(pusherhome);
        door.setPosition(doorhome);
    }
}
