package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.hardware.Camera;
import org.firstinspires.ftc.teamcode.vision.TseThresholdBinary;
@Disabled
@Autonomous
public class PortfolioThreshold extends LinearOpMode {
    // Pre-init
    Camera camera = new Camera();
    TseThresholdBinary pipeline = new TseThresholdBinary();
    @Override
    public void runOpMode() {
        // Init
    camera.init(hardwareMap);
    camera.webcam.setPipeline(pipeline);

        FtcDashboard.getInstance().startCameraStream(camera.webcam, 1); // Stream to dashboard at 1 fps
        waitForStart();
    
        // Pre-run
    
        if (opModeIsActive()) {
            // Autonomous instructions
        }
    }
}
