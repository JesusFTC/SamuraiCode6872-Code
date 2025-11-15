package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ConfigureDistance {
    private DistanceSensor DistanceSens;
    DcMotor Motor1;

    public void init(HardwareMap hwMap)
    {
        DistanceSens = hwMap.get(DistanceSensor.class, "DistanceSensor");

    }
    public double getDistance()
    {
        return DistanceSens.getDistance(DistanceUnit.CM);
    }


}