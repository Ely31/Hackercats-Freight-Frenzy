package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.ArmSystem;
import org.firstinspires.ftc.teamcode.util.TruePress;

@TeleOp
public class TurretTestin extends LinearOpMode {
    // Pre-init
    ArmSystem armSystem = new ArmSystem();

    enum FourBarState {
        EXTENDED,
        RETRACTED
    }

    FourBarState fourBarState = FourBarState.RETRACTED;
    TruePress fourbarToggleInput = new TruePress();
    @Override
    public void runOpMode() {
        // Init
    armSystem.init(hardwareMap);
        waitForStart();
    
        // Pre-run
    
        while (opModeIsActive()) {
            // TeleOp loop

            switch (fourBarState) { // Gamepad2 A toggles the extended/retracted state of the 4b
                case RETRACTED:
                    armSystem.setArmPosition(0,0); // Retract arm
                    armSystem.turretTargetAngle = 0; // Reset turret angle so that when the arm is first extended the turret doesn't move
                    if (fourbarToggleInput.trueInput(gamepad2.a)) {
                        fourBarState = TurretTestin.FourBarState.EXTENDED;
                    }
                    break;
                case EXTENDED:
                    // Run the 4b and turret to their desired positions
                    armSystem.setArmPosition(armSystem.levelToAngle(armSystem.activeLevel), armSystem.turretTargetAngle);
                    if (gamepad2.left_bumper) armSystem.turretTargetAngle -= 1; // Change turret angle with bumpers
                    if (gamepad2.right_bumper) armSystem.turretTargetAngle += 1;// Change turret angle with bumpers
                    if (fourbarToggleInput.trueInput(gamepad2.a)) {
                        fourBarState = TurretTestin.FourBarState.RETRACTED;
                    }
                    break;
            }
            telemetry.addData("state", fourBarState);
            telemetry.addData("turretpos", armSystem.TurretAngle());
            telemetry.addData("4b pos", armSystem.FourbarAngle());
            telemetry.addData("turret target", armSystem.turretTargetAngle);
            telemetry.update();
        }
    }
}
