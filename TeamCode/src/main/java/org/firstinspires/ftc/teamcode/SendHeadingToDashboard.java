package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="",group="")
public class SendHeadingToDashboard extends LinearOpMode {
    // Pre-init
    BNO055IMU imu;
    double heading;
    @Override
    public void runOpMode() {
        // Init
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // Autonomous instructions
            heading =  Math.toDegrees( imu.getAngularOrientation().firstAngle);
            telemetry.addData("heading",heading);
        }
    }
}
