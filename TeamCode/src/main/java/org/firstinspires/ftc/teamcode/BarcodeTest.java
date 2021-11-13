package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.EocvBarcodePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name="",group="")
public class BarcodeTest extends LinearOpMode {
    // Pre-init
    // Create webcam and pipline
    OpenCvWebcam webcam;
    EocvBarcodePipeline pttfPipline = new EocvBarcodePipeline();

    int barcodepos;

    @Override
    public void runOpMode() {
        // Init
        // Set up webcam
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));
        webcam.setPipeline(pttfPipline);

        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        FtcDashboard.getInstance().startCameraStream(webcam,1); // Stream to dashboard at 1 fps

        while (!isStarted()&&!isStopRequested()) { // Init loop
            // Display telemetry every 200ms
            sleep(1000);

            double posX = pttfPipline.getRectMidpointX();
            barcodepos = pttfPipline.getBarcodePos();
            telemetry.addData("position_x",posX);

            telemetry.addData("barcodepos",barcodepos);

            telemetry.update();

        }

        waitForStart();
        // Pre-run

        if (opModeIsActive()) {
            // Run Auto
            switch (barcodepos){
                case 1:
                    telemetry.addData("far side",0);
                    break;
                case 2:
                    telemetry.addData("middle",0);
                    break;
                case 3:
                    telemetry.addData("warehouse side",0);
                    break;
            }
            sleep(5*1000);
        }
    }
}
