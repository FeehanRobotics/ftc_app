package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Sam on 1/17/2019.
 */

@TeleOp
public class team_two_test_tele extends LinearOpMode {

    //DcMotors
    private DcMotor front_left;
    private DcMotor front_right;
    private DcMotor back_left;
    private DcMotor back_right;

    //Servos
    private Servo test_servo;


    @Override
    public void runOpMode() throws InterruptedException {

        front_left = hardwareMap.get(DcMotor.class, "name");

    }


}
