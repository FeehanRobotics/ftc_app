package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class Cube_Chaser extends LinearOpMode {
    private DcMotor mtr_left;
    private DcMotor mtr_right;
    private GoldAlignDetector gold;

    @Override
    public void runOpMode() {
        mtr_left = hardwareMap.get(DcMotor.class, "mtr_left");
        mtr_right = hardwareMap.get(DcMotor.class, "mtr_right");

        mtr_left.setDirection(DcMotorSimple.Direction.FORWARD);
        mtr_right.setDirection(DcMotorSimple.Direction.REVERSE);

        gold = new GoldAlignDetector();
        gold.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

    }
}
