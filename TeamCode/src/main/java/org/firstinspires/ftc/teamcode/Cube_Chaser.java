package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class Cube_Chaser extends OpMode {
   private DcMotor mtr_left;
   private DcMotor mtr_right;
    private GoldAlignDetector gold;

    @Override
    public void init(){
        mtr_left = hardwareMap.get(DcMotor.class, "mtr_left");
        mtr_right = hardwareMap.get(DcMotor.class, "mtr_right");

       mtr_left.setDirection(DcMotorSimple.Direction.FORWARD);
        mtr_right.setDirection(DcMotorSimple.Direction.REVERSE);

        gold = new GoldAlignDetector();
        gold.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        gold.useDefaults();

        gold.alignSize = 200; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        gold.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        //gold.downscale = 0.4; // How much to downscale the input frames

        gold.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //gold.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        gold.maxAreaScorer.weight = 0.005;

        gold.ratioScorer.weight = 5;
        gold.ratioScorer.perfectRatio = 1.0;

        gold.enable();
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if(gold.getXPosition() < 110){
            mtr_right.setPower(0.75);
            mtr_left.setPower(0.65);
        }
       else if(gold.getXPosition() > 317){
           mtr_right.setPower(0.65);
         mtr_left.setPower(0.75);
        }
        else{
            mtr_right.setPower(0.7);
           mtr_left.setPower(0.7);
        }

    }
}
