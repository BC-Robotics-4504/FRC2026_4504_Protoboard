// Copyright (c) FIRST and other WPILIB contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

// REVLib 2026 exposes Spark controllers under com.revrobotics.spark
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Robot extends TimedRobot {
    // Initialize autonomous choices
    private static final String kDefaultAuto = "Default";
    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    // Initialize controller
    private final XboxController controller0 = new XboxController(0);

    // Use REVLib SparkMax (2026) API
    private final SparkMax intakeMotor = new SparkMax(1, MotorType.kBrushless);
    // private final SparkMax rightMotor = new SparkMax(2, MotorType.kBrushless);

    private final Timer timer1 = new Timer();

    @Override
    public void robotInit() {
        m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
        SmartDashboard.putData("Auto choices", m_chooser);

        // REVLib 2026's SparkMax API changed; avoid calling removed convenience
        // methods. Ensure motor is stopped at startup.
        intakeMotor.stopMotor();

        // Start timer if needed
        timer1.start();
    }

    @Override
    public void robotPeriodic() {
        // Optional diagnostics
    }

    @Override
    public void autonomousInit() {
        m_autoSelected = m_chooser.getSelected();
        System.out.println("Auto selected: " + m_autoSelected);
        timer1.restart();
    }

    @Override
    public void autonomousPeriodic() {
        switch (m_autoSelected) {
            case kDefaultAuto:
            default:
                // Autonomous code here
                break;
        }
    }

    @Override
    public void teleopInit() {
        // Optional: Reset or initialize things at teleop start
    }

    @Override
    public void teleopPeriodic() {
        // Control the intake motor based on buttons
        boolean isAButtonHeld = controller0.getAButton(); // Run forward at 75%
        boolean isRightBumperHeld = controller0.getRightBumper(); // Run reverse at 25%

        if (isAButtonHeld && !isRightBumperHeld) {
            // Run intake in forward direction at 75%
            intakeMotor.set(0.75);
        } else if (isRightBumperHeld && !isAButtonHeld) {
            // Run intake in reverse at 25%
            intakeMotor.set(-0.25);
        } else {
            // Neither button pressed, stop the motor
            intakeMotor.set(0);
        }
    }

    @Override
    public void disabledInit() {
        intakeMotor.set(0);
    }

    @Override
    public void disabledPeriodic() {
        // Optional
    }

    @Override
    public void testInit() {
        // Optional
    }

    @Override
    public void testPeriodic() {
        // Optional
    }

    @Override
    public void simulationInit() {
        // Optional
    }

    @Override
    public void simulationPeriodic() {
        // Optional
    }
}
