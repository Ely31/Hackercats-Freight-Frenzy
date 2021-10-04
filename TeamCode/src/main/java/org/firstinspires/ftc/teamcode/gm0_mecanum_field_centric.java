package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class gm0_mecanum_field_centric extends OpMode {
    DcMotor fl;
    DcMotor bl;
    DcMotor fr;
    DcMotor br;
    BNO055IMU imu;
    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("lf");
        bl = hardwareMap.dcMotor.get("lb");
        fr = hardwareMap.dcMotor.get("rf");
        br = hardwareMap.dcMotor.get("rb");
        // Reverse the right side motors
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        telemetry.addData("program", "initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x * 1.1; // counteract slower strafing
        double y = -gamepad1.left_stick_y; // reverse stick y axis
        double rx = gamepad1.right_stick_x;
        double heading = -imu.getAngularOrientation().firstAngle;

        x = (x*Math.cos(heading))-(y*Math.sin(heading));
        y = (x*Math.sin(heading))+(y*Math.cos(heading));

        double flPower = y + x + rx;
        double blPower = y - x + rx;
        double frPower = y - x - rx;
        double brPower = y + x - rx;

        fl.setPower(flPower);
        bl.setPower(blPower);
        fr.setPower(frPower);
        br.setPower(brPower);

        telemetry.addData("heading", Math.toDegrees(heading));
    }
}
