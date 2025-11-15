package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class IMUBench {
    private IMU imu;

    public void init(HardwareMap hwMap){
        imu = hwMap.get(IMU.class, "IMU");

        RevHubOrientationOnRobot RevOrient = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );

        imu.initialize(new IMU.Parameters(RevOrient));
    }

    public double getHeading(AngleUnit angleUnit){
        return  imu.getRobotYawPitchRollAngles().getYaw(angleUnit);
    }

    public void resetImu(){
        imu.resetYaw();
    }

}
