package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class Open_House_Tele extends LinearOpMode {

    private DcMotor mtr_left;
    private DcMotor mtr_right;
    private DcMotor extend;
    private Servo srv_left;
    private Servo srv_right;

    @Override
    public void runOpMode() {
        mtr_left = hardwareMap.get(DcMotor.class, "mtr_left");
        mtr_right = hardwareMap.get(DcMotor.class, "mtr_right");
        extend = hardwareMap.get(DcMotor.class, "extend");
        srv_left = hardwareMap.get(Servo.class, "srv_left");
        srv_right = hardwareMap.get(Servo.class, "srv_right");

        mtr_left.setDirection(DcMotorSimple.Direction.FORWARD);
        mtr_right.setDirection(DcMotorSimple.Direction.REVERSE);

        while(true){
            if(gamepad1.a){
                //Close Servos
                //TODO: FIX THIS SECTION IT PROBABLY DOESN'T WORK
                srv_left.setPosition(0);
                srv_right.setPosition(1);
            }
            else if(gamepad1.b){
                //Open servos
                //TODO: FIX THIS SECTION IT PROBABLY DOESN'T WORK
                srv_left.setPosition(1);
                srv_right.setPosition(0);
            }
            else if(gamepad1.dpad_up){
                extend.setPower(1);

            }
            else if(gamepad1.dpad_down){
                extend.setPower(-1);
            }
            mtr_right.setPower(gamepad1.right_stick_y);
            mtr_left.setPower(gamepad1.left_stick_x);
        }
    }
}
