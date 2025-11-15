package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp (name = "DistanceSensor")
public class ExecuteDistSens extends OpMode {
    ConfigureDistance Configure = new ConfigureDistance();
    DcMotor Motor1;

    @Override
    public void init() {
        Configure.init(hardwareMap);
        Motor1 = hardwareMap.get(DcMotor.class, "Motor");
    }

    @Override
    public void loop() {
        telemetry.addData("Distance", Configure.getDistance());
        if (Configure.getDistance() >= 5 &&  Configure.getDistance() <= 15)
        {
            Motor1.setPower(1);
        }
        else
        {
            Motor1.setPower(0);
        }
    }
}
