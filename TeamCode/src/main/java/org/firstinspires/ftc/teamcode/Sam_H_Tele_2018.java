package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.Range;

@TeleOp
public class Sam_H_Tele_2018 extends LinearOpMode {
    //Drivetrain motors
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;

    //Arm motors
    DcMotorEx twist;
    DcMotor extend;
    DcMotor twist2;
    CRServo intake;
    Servo bucket;

    //Hook motor
    DcMotor climb;

    boolean run_intake_continuously = false;
    float twist_position = 0;
    boolean twisted = false;
    boolean xy_held = false;

    PIDFCoefficients my_PIDFs = new PIDFCoefficients(0,0,0,0);

    @Override
    public void runOpMode() {
        //Drivetrain motors
        leftFront = hardwareMap.get(DcMotor.class, "LF");
        rightFront = hardwareMap.get(DcMotor.class, "RF");
        leftRear = hardwareMap.get(DcMotor.class, "LB");
        rightRear = hardwareMap.get(DcMotor.class, "RB");

        //Arm motors
        twist = (DcMotorEx)hardwareMap.get(DcMotor.class, "twist");
        extend = hardwareMap.get(DcMotor.class, "extend");
        twist2 = hardwareMap.get(DcMotor.class, "climb");
        intake = hardwareMap.get(CRServo.class, "intake");
        bucket = hardwareMap.get(Servo.class, "bucket");

        //Hook Motor
        climb = hardwareMap.get(DcMotor.class, "up");
        climb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        climb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int climb_position = 0;

        twist.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        twist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        twist.setPower(0);

        twist2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        twist2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        twist2.setPower(0);

        //PIDCoefficients current_PID = twist.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION);

        //PIDCoefficients current_PID = twist.
        PIDFCoefficients current_PID = twist.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
        current_PID = twist.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Current PID: ", current_PID);
        telemetry.update();



        waitForStart();

        while(opModeIsActive()){
//            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
//            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
//            double rightX = gamepad1.right_stick_x;
//            final double v1 = r * Math.cos(robotAngle) + rightX;
//            final double v2 = r * Math.sin(robotAngle) - rightX;
//            final double v3 = r * Math.sin(robotAngle) + rightX;
//            final double v4 = r * Math.cos(robotAngle) - rightX;
//
//            leftFront.setPower(v1);
//            rightFront.setPower(v2);
//            leftRear.setPower(v3);
//            rightRear.setPower(v4);

            float left_y = gamepad1.left_stick_y;
            float left_x = gamepad1.left_stick_x;
            float right_x = gamepad1.right_stick_x;

            float front_left = -left_y - left_x + right_x;
            float front_right = left_y - left_x + right_x;
            float back_right = left_y + left_x + right_x;
            float back_left = -left_y + left_x + right_x;

            front_left = Range.clip(front_left, -1, 1);
            front_right = Range.clip(front_right, -1, 1);
            back_right = Range.clip(back_right, -1, 1);
            back_left = Range.clip(back_left, -1, 1);

            //leftFront.setPower(-front_left);
            //leftFront.setPower(back_right);
            leftFront.setPower(-back_left);
            //rightFront.setPower(-front_right);
            //rightFront.setPower(back_left);
            rightFront.setPower(-back_right);
            //rightRear.setPower(-back_right);
            //rightRear.setPower(front_left);
            rightRear.setPower(-front_right);
            //leftRear.setPower(-back_left);
            //leftRear.setPower(front_right);
            leftRear.setPower(-front_left);

            telemetry.addData("twist position: ", twist.getCurrentPosition());
            telemetry.addData("Current PID: ", current_PID);
            telemetry.addData("left y: ", left_y);
            telemetry.addData("left x: ", left_x);
            telemetry.addData("right x: ", right_x);
            telemetry.addData("LF Power: ", -front_left);
            //telemetry.addData("")
            telemetry.update();


            //telemetry.update();

            //Add slack to intake arm when fully extended
            twisted = false;
            run_intake_continuously = false;
            xy_held = false;

            if(gamepad1.dpad_up){
                extend.setPower(1);
            }

            //Extend intake arm intake arm
            else if(gamepad1.dpad_down){
                extend.setPower(-1);
            }

            //Extend hook away from latch
            else if(gamepad1.left_bumper){
                climb_position -= 20;
                climb.setTargetPosition(climb_position);
                climb.setPower(-1);
            }

            else if(gamepad1.right_bumper){
                climb_position += 20;
                climb.setTargetPosition(climb_position);
                climb.setPower(1);
            }

            //Twist arm forwards
            else if(gamepad1.dpad_left){
                //twist.setPower(0.7);
                twist_position += 5;

                twist.setTargetPosition(Math.round(twist_position));
                twist.setPower(-0.7);

                twist2.setTargetPosition(Math.round(-twist_position));
                twist2.setPower(0.7);

                twisted = true;
               // bucket.setPosition(bucket.getPosition() + 0.01);

            }

            //Twist arm backwards
            else if(gamepad1.dpad_right){
                //twist.setPower(-0.1);
                twist_position -= 5;
                twist.setTargetPosition(Math.round(twist_position));
                twist.setPower(0.7);

                twist2.setTargetPosition(-Math.round(twist_position));
                twist2.setPower(-0.7);

                twisted = false;
                //bucket.setPosition(bucket.getPosition() - 0.01);
            }

            //Run intake continuously forwards
            else if(gamepad1.a){
                //intake.setDirection(DcMotorSimple.Direction.FORWARD);
                intake.setPower(0.75);
                run_intake_continuously = true;
            }

            //Run intake continuously backwards
            else if(gamepad1.b){
                //intake.setDirection(DcMotorSimple.Direction.REVERSE);
                intake.setPower(-0.75);
                run_intake_continuously = true;
            }

            //Run intake forwards while held
            else if(gamepad1.x){
                intake.setPower(0.75);
                xy_held = true;
            }

            //Run intake backwards while held
            else if(gamepad1.y){
                intake.setPower(-0.75);
                xy_held = true;
            }

            else if(gamepad1.left_trigger != 0){
                bucket.setPosition(bucket.getPosition() - gamepad1.left_trigger * 0.002);
            }

            else if(gamepad1.right_trigger != 0){
                bucket.setPosition(bucket.getPosition() + gamepad1.right_trigger * 0.002);
            }

            else if(gamepad2.right_trigger != 0){
                leftRear.setPower(gamepad2.right_trigger);
                leftFront.setPower(gamepad2.right_trigger);
                rightRear.setPower(gamepad2.right_trigger);
                rightFront.setPower(gamepad2.right_trigger);
            }
            
            else if(gamepad2.left_trigger != 0){
                leftRear.setPower(-gamepad2.left_trigger);
                leftFront.setPower(-gamepad2.left_trigger);
                rightRear.setPower(-gamepad2.left_trigger);
                rightFront.setPower(-gamepad2.left_trigger);
            }

            else{
                all_Zeroes();
            }



        }
    }

    public void all_Zeroes(){
        //twist.setPower(0);
//        if(twist.getCurrentPosition() > twist_position){
//            twist.setTargetPosition(Math.round(twist_position));
//            twist.setPower(-0.05);
//
//        }
//        else if(twist.getCurrentPosition() < twist_position){
//            twist.setTargetPosition(Math.round(twist_position));
//            twist.setPower(0.05);
//        }
//        else{
//
//        }
        //Twist motor PID Controller
        //int error = (int) twist_position - twist.getCurrentPosition();
        //int correction_speed = error
        //twist.setPower(1);
        extend.setPower(0);
        //intake.setPower(0);
        //climb.setPower(0);

        if(run_intake_continuously || xy_held){

        }
        else {
            intake.setPower(0);
        }


    }
    public void hold_twist(){
        //TODO
    }
}
