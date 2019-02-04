package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Sam on 1/6/2019.
 */

public class Sam_H_Auto_2 extends LinearOpMode {
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

    private final int RANGE = 30;
    //private SamplingOrderDetector detector;
    private GoldAlignDetector detector;
    private int location = 0;
    //boolean run_intake_continuously = false;

    @Override
    public void runOpMode() throws InterruptedException {
        //-----------INIT---------------------------------------------------------------------------
        //Drivetrain motors
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

        detector = new GoldAlignDetector(); // Create detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        detector.useDefaults(); // Set detector to use default settings

        // Optional tuning
        detector.alignSize = 700; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005; //

        detector.ratioScorer.weight = 5; //
        detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

        detector.enable(); // Start the detector!

        climb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        climb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

//----------------------END INIT--------------------------------------------------------------------
//----------------------DRIVER CODE-----------------------------------------------------------------
        waitForStart();
        //DESCEND FROM LANDER
        climb.setTargetPosition(10000); //TODO TWEAK THIS
        climb.setPower(1);
        sleep(5000);
        move_sideways(-300);
        rotate(400);
        //move_forward(500);
        move_sideways(700);
        rotate(-400);
        rotate(-400);

        //Debugging stop point
        //detector.disable();
        //stop();

        move_forward(1000);
        rotate(-1650);

        //MINERAL DETECTION
        move_forward(-1300);
        boolean scored = false;

        sleep(2500);

        if(detector.getAligned()){
            move_sideways(-2000);
            move_sideways(2000);
            scored = true;

        }
        move_forward(1440);
        rotate(-150);

        sleep(2500);
        if(detector.getAligned() && !scored){
            move_sideways(-2000);
            move_sideways(2000);
        }
        move_forward(1440);
        rotate(-150);

        sleep(2500);

        if(detector.getAligned() && !scored){
            move_sideways(-2000);
            move_sideways(2000);
        }

        detector.disable();

        //DROP TEAM MARKER
        move_forward(2200);
        rotate(4250);
        move_forward(-500);
        move_sideways(4000);
        marker.setPosition(1);
        sleep(750);
        marker.setPosition(0);

        //MOVE TO PARK IN CRATER
        //TODO: UNCOMMENT AND TUNE THIS SECTION FOR COMP
        move_sideways(10000);

        move_forward(1440);
        rotate(1000);
        move_sideways(3000);
        marker.setPosition(-1);
        sleep(2000);
        marker.setPosition(0);
        stop();

    }

    // public void forward(int distance){
    //reset_encoders();

    //}
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

