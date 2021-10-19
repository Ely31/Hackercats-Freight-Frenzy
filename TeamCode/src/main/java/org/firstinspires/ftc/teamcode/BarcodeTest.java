package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp(name="",group="")
public class BarcodeTest extends LinearOpMode {
    // Pre-init
    OpenCvWebcam webcam;

    EocvPinkttf pttfPipline = new EocvPinkttf();

    @Override
    public void runOpMode() {
        // Init

        int barcodeNum;

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

            sleep(200);

            double posX = pttfPipline.getRectMidpointX();
            telemetry.addData("position_x",posX);

            if (posX>0 && 100 > posX) barcodeNum = 1;
            else if (posX > 100 && 200 >posX) barcodeNum = 2;
            else if (posX >200 && 320 >posX) barcodeNum = 3;
            else barcodeNum = 0;

            telemetry.addData("barcodenum",barcodeNum);

            telemetry.update();

        }
    }
}
