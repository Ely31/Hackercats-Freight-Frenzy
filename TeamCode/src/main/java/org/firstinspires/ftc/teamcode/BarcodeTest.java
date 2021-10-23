package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.EocvBarcodePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp(name="",group="")
public class BarcodeTest extends LinearOpMode {
    // Pre-init
    // Create webcam and pipline
    OpenCvWebcam webcam;
    EocvBarcodePipeline pttfPipline = new EocvBarcodePipeline();

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
        waitForStart();

        // Pre-run

        while (opModeIsActive()) {
            // TeleOp loop
            // Display telemetry every 200ms
            sleep(200);

            double posX = pttfPipline.getRectMidpointX();
            telemetry.addData("position_x",posX);

            telemetry.addData("barcodepos",pttfPipline.getBarcodePos());

            telemetry.update();

        }
    }
}
