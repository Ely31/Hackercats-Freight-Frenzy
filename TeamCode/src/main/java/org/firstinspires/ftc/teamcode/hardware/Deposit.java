package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Deposit {

    private Servo pusher;
    private Servo door;

    //time to wait before we close the door and reset
    // the pusher after the freight has fallen out
    private ElapsedTime falltime;

    //delay between opening the door and pushing freight out
    private ElapsedTime pusherlag;

    public void init(HardwareMap hwmap) {
        pusher = hwmap.get(Servo.class, "pusher");
        door = hwmap.get(Servo.class, "door");

        double pusherhome = 0.05;
        double doorhome = 0.1;

        pusher.setPosition(pusherhome);
        door.setPosition(doorhome);

        falltime.reset();
        pusherlag.reset();
    }

    public void dump(){
        door.setPosition(0.6);
        pusher.setPosition(0.45);
    }
}
