package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Sam on 12/23/2018.
 */

@TeleOp
public class Intake_Test extends LinearOpMode {
    CRServo intake;
    Servo bucket;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        intake = hardwareMap.get(CRServo.class, "intake");
        bucket = hardwareMap.get(Servo.class, "bucket");

        while(opModeIsActive()) {

            if (gamepad1.a) {
                intake.setPower(0.5);
            } else if (gamepad1.b) {
                intake.setPower(-0.5);
            } else if(gamepad1.x){
                bucket.setPosition(bucket.getPosition() + 0.01);
            }
            else if (gamepad1.y){
                bucket.setPosition(bucket.getPosition() - 0.01);
            }
        }
    }
}
