package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "RobotManual")
public class ManualRobot extends OpMode {
    //Recordatorio: Evitar o eliminar lineas de codigo u objetos que no se usan
    //En el codigo completo, esto puede generar delay a la hora de ejecutar el codigo.
    //---------------------------C-h-a-s-i-s--------------------------
    DcMotorEx m_fl;
    DcMotorEx m_fr;
    DcMotorEx m_bl;
    DcMotorEx m_br;
    DcMotorEx m_intake;
    DcMotorEx m_shooter1;
    DcMotorEx m_shooter2;
    public Servo Servo90;
    IMUBench bench = new IMUBench();

    double power = 0;


    @Override
    public void init() {

        //---------------------------C-h-a-s-i-s---I-n-i-t-i-a-l-i-z-e-----------------------

        m_fl = hardwareMap.get(DcMotorEx.class, "FLMotor");
        m_fr = hardwareMap.get(DcMotorEx.class, "FRMotor");
        m_bl = hardwareMap.get(DcMotorEx.class, "BLMotor");
        m_br = hardwareMap.get(DcMotorEx.class, "BRMotor");
        m_intake = hardwareMap.get(DcMotorEx.class, "IntakeMotor");
        m_shooter1 = hardwareMap.get(DcMotorEx.class, "ShooterMotorA");
        m_shooter2 = hardwareMap.get(DcMotorEx.class, "ShooterMotorB");
        Servo90 = hardwareMap.get(Servo.class, "Servo");

        bench.init(hardwareMap);


        m_shooter2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        m_shooter2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        m_intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m_intake.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        //--------------C-o-n-f-i-g-u-r-a-c-i-ó-n d-e P-I-D------------------
        //m_shooter.setVelocityPIDFCoefficients(10,0,0,12);

        m_fr.setDirection(DcMotorSimple.Direction.REVERSE);
        m_bl.setDirection(DcMotorSimple.Direction.FORWARD);
        m_br.setDirection(DcMotorSimple.Direction.REVERSE);
        m_fl.setDirection(DcMotorSimple.Direction.FORWARD);

    }


    @Override
    public void loop() {
        telemetry.addData("Current Orientation:", bench.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Shooter Velocity:", m_shooter2.getVelocity());
        telemetry.addData("Intake Velocity:", m_intake.getVelocity());
        telemetry.addData("Posicion:", Servo90.getPosition());
        telemetry.update();

        double y = gamepad1.left_stick_y;
        double x = -gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;
        //Ejemplo : 200 = 22RPM
        double DesearedVelocity = 1900; //1850


        //----------------------C-h-a-s-i-s-----------------------

        // Se convierte de grados a radianes por qué al usar Math.sin o Math.cos, ambos de estos
        //Utilizan radianes. . .
        double botHeading = Math.toRadians(bench.getHeading(AngleUnit.DEGREES));

        // Se rota el vector (Direccion y magnitud) del joystick según la orientyación del robot
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Se calcula la potencia de cada motor, usando las variables rotX y rotY
        double FLpower = rotY + rotX - rx;
        double BLpower = rotY - rotX - rx;
        double FRpower = rotY - rotX + rx;
        double BRpower = rotY + rotX + rx;

        /*Se utiliza un max y un abs, el max para hacer que los datos no sobresalgan de 1
            Y el abs, para darlo como un numero positivo siempre.
         */

        double MaxPower = Math.max(1.0, Math.max(Math.abs(FLpower),
                Math.max(Math.abs(FRpower), Math.max(Math.abs(BLpower), Math.abs(BRpower)))));

        m_fl.setPower(FLpower / MaxPower);
        m_fr.setPower(FRpower / MaxPower);
        m_bl.setPower(BLpower / MaxPower);
        m_br.setPower(BRpower / MaxPower);


        //----------------------M-e-c-h-a-n-i-s-m-s-----------------------

        if (gamepad1.left_trigger >= 0.1) {
            m_intake.setPower(-0.8);
        }
        else {
            m_intake.setPower(0);
        }


        if (gamepad1.right_trigger >= 0.1) {
            m_shooter1.setPower(-gamepad1.right_trigger);
            m_shooter2.setPower(gamepad1.right_trigger);
        } else {
            m_shooter1.setPower(0);
            m_shooter2.setPower(0);
        }

        if (gamepad1.left_bumper) {
            m_intake.setPower(0);
        }

        //Reseteo de frente
        if (gamepad1.x) {
            bench.resetImu();
        }

        //Servo
        if (m_intake.getVelocity() < 0 && m_shooter2.getVelocity() < 1) {
            Servo90.setPosition(0.15);
        }
        if (m_shooter2.getVelocity() >= 950 && m_intake.getVelocity() != 0) {
            Servo90.setPosition(0.3);
        } else {
            Servo90.setPosition(0.15);
        }

        //Poder del motor
        if (gamepad1.b) {
            power = 0.40;
        }
        if (gamepad1.a) {
            power = 0.5;
        }
    }
}