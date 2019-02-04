package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Sam on 1/6/2019.
 */

@Autonomous

public class Simplified_Auto extends LinearOpMode {
    //Drivetrain motors
    DcMotor leftFront;
    DcMotor rightFront ;
    DcMotor leftRear;
    DcMotor rightRear;

    //Arm motors
    DcMotor twist;
    DcMotor extend;
    DcMotor climb;
    CRServo intake;
    Servo marker;

    int RANGE = 30;

    public void runOpMode(){
        leftFront = hardwareMap.get(DcMotor.class, "LF");
        rightFront = hardwareMap.get(DcMotor.class, "RF");
        leftRear = hardwareMap.get(DcMotor.class, "LB");
        rightRear = hardwareMap.get(DcMotor.class, "RB");

        run_to_position();

        twist = hardwareMap.get(DcMotor.class, "twist");
        extend = hardwareMap.get(DcMotor.class, "extend");
        climb = hardwareMap.get(DcMotor.class, "up");
        intake = hardwareMap.get(CRServo.class, "intake");
        marker = hardwareMap.get(Servo.class, "marker");

        climb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        climb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();
        //Actually move bot
        climb.setTargetPosition(10000); //TODO TWEAK THIS
        climb.setPower(1);
        sleep(5000);

        move_forward(-500);
        move_sideways(-500);
        move_forward(500);
        move_sideways(3500);
        stop();

        //rotate(200);
        //move_forward(500);
//        move_sideways(500);
//        rotate(-200);
//        rotate(-400);
//
//        move_forward(5000);
       // stop();


    }

    public void move_sideways(int ticks){
        reset_encoders();
        run_to_position();

        leftFront.setPower(-1);
        rightFront.setPower(-1);

        leftRear.setPower(1);
        rightRear.setPower(1);

        leftFront.setTargetPosition(-ticks);
        rightFront.setTargetPosition(-ticks);
        leftRear.setTargetPosition(ticks);
        rightRear.setTargetPosition(ticks);

        while(true){
            sleep(10);
            if (-ticks - RANGE <= leftFront.getCurrentPosition() && leftFront.getCurrentPosition() <= -ticks + RANGE) {
                if (-ticks - RANGE <= rightFront.getCurrentPosition() && rightFront.getCurrentPosition() <= -ticks + RANGE) {
                    if (ticks - RANGE <= leftRear.getCurrentPosition() && leftRear.getCurrentPosition() <= ticks + RANGE) {
                        if (ticks - RANGE <= rightRear.getCurrentPosition() && rightRear.getCurrentPosition() <= ticks + RANGE) {
                            break;
                        }
                    }
                }
            }
            idle();
        }

    }
    public void move_forward(int ticks){
        reset_encoders();
        run_to_position();

        leftFront.setPower(1);
        leftRear.setPower(1);

        rightFront.setPower(-1);
        rightRear.setPower(-1);

        leftFront.setTargetPosition(-ticks);
        rightFront.setTargetPosition(ticks);
        leftRear.setTargetPosition(-ticks);
        rightRear.setTargetPosition(ticks);

        while(!done_moving(ticks)){
            idle();
            telemetry.addData("Left front: ", leftFront.getCurrentPosition());
            telemetry.addData("Left rear: ", leftRear.getCurrentPosition());
            telemetry.addData("Right front: ", rightFront.getCurrentPosition());
            telemetry.addData("Right rear: ", rightRear.getCurrentPosition());
            telemetry.update();
        }

        telemetry.addData("Done: ", "true");
//        try {
//            Thread.sleep(4000);
//        }
//        catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }

    }

    public void rotate(int ticks){//1440 = ~90 degrees
        reset_encoders();
        run_to_position();

        leftFront.setPower(1);
        leftRear.setPower(1);

        rightFront.setPower(-1);
        rightRear.setPower(-1);

        leftFront.setTargetPosition(ticks);
        rightFront.setTargetPosition(ticks);
        leftRear.setTargetPosition(ticks);
        rightRear.setTargetPosition(ticks);

        while(true){
            sleep(10);
            if (ticks - RANGE <= leftFront.getCurrentPosition() && leftFront.getCurrentPosition() <= ticks + RANGE) {
                if (ticks - RANGE <= rightFront.getCurrentPosition() && rightFront.getCurrentPosition() <= ticks + RANGE) {
                    if (ticks - RANGE <= leftRear.getCurrentPosition() && leftRear.getCurrentPosition() <= ticks + RANGE) {
                        if (ticks - RANGE <= rightRear.getCurrentPosition() && rightRear.getCurrentPosition() <= ticks + RANGE) {
                            break;
                        }
                    }
                }
            }
        }
        idle();
    }

    public void run_to_position(){
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void reset_encoders(){
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public boolean done_moving(int ticks) {
        sleep(10);
        if (-ticks - RANGE <= leftFront.getCurrentPosition() && leftFront.getCurrentPosition() <= -ticks + RANGE) {
            if (ticks - RANGE <= rightFront.getCurrentPosition() && rightFront.getCurrentPosition() <= ticks + RANGE) {
                if (-ticks - RANGE <= leftRear.getCurrentPosition() && leftRear.getCurrentPosition() <= -ticks + RANGE) {
                    if (ticks - RANGE <= rightRear.getCurrentPosition() && rightRear.getCurrentPosition() <= ticks + RANGE) {
                        return true;
                    }
                }
            }
            idle();
        }
        return false;
    }
}
