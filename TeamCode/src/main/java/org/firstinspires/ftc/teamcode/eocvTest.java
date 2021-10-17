package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp(name="",group="")
public class eocvTest extends LinearOpMode {
    // Pre-init
    OpenCvWebcam webcam;

    EocvPinkttf pttfPipline = new EocvPinkttf();

    @Override
    public void runOpMode() {
        // Init
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                webcam.setPipeline(pttfPipline);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop
        }
    }
}
