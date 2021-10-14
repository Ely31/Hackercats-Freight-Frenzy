package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TeleopDrive {
    private DcMotor lf;
    private DcMotor lb;
    private DcMotor rf;
    private DcMotor rb;

    private BNO055IMU imu;
    public double heading;

    double rotX;
    double rotY;

    public void init(HardwareMap hwmap){
        lf = hwmap.get(DcMotor.class,"lf");
        lb = hwmap.get(DcMotor.class,"lb");
        rf = hwmap.get(DcMotor.class,"rf");
        rb = hwmap.get(DcMotor.class,"rb");

        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        //brake when you stop gamepad input
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //initialize imu
        imu = hwmap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
    }

    public void drive(double x,double y,double turn,double multiplier){

        multiplier = (-0.875*multiplier)+1;

        heading = -imu.getAngularOrientation().firstAngle;

        rotX = x * Math.cos(heading) - -y * Math.sin(heading);
        rotY = x * Math.sin(heading) + -y * Math.cos(heading);

        double lfPower = rotY + rotX + turn;
        double lbPower = rotY - rotX + turn;
        double rfPower = rotY - rotX - turn;
        double rbPower = rotY + rotX - turn;

        lf.setPower(lfPower*multiplier);
        lb.setPower(lbPower*multiplier);
        rf.setPower(rfPower*multiplier);
        rb.setPower(rbPower*multiplier);
    }

}
