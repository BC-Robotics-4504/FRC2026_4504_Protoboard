// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Test comment from Isaac!

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

//import com.revrobotics.spark.*;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;


/*
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  // Initialize autonomous choices
  private static final String kDefaultAuto = "Default";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Initialize controller
  private final XboxController controller0 = new XboxController(0);
  // Initialize motors
  private final SparkMax leftMotor = new SparkMax(1, MotorType.kBrushless);
  private final SparkMax rightMotor = new SparkMax(2, MotorType.kBrushless);
  // Initialize motor config
  private final SparkMaxConfig intakeConfig = new SparkMaxConfig();

  private final Timer timer1 = new Timer();

  // Intake Parameters, in a CommandRobot structure would probably be put in Constants.java
  private double intakeSpeed = 0.75;
  private double ejectSpeed = -0.25; // This number should be negative
  private IntakeStatus intakeStatus = IntakeStatus.STOP;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Configure Spark Max motor controllers.
    intakeConfig.smartCurrentLimit(60);
    intakeConfig.voltageCompensation(12.0);

    // Apply configuration to left motor
    leftMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // Disable the right motor, we don't need it right now
    rightMotor.disable();

    // Start timer
    timer1.start();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();

    System.out.println("Auto selected: " + m_autoSelected);
    timer1.restart();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kDefaultAuto:
      default:
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    switch (intakeStatus) {
      case INTAKE:
        leftMotor.set(intakeSpeed);
        break;
      case EJECT:
        leftMotor.set(ejectSpeed);
        break;
      case STOP:
        leftMotor.set(0.0);
        break;
    }
    updateIntakeStatus();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}

  /** Updates the intake status based on driver input. */
  private void updateIntakeStatus() {
    if (controller0.getAButton() && controller0.getRightBumper()) {
      // Keep the status the same.
      return;
    }

    else if (controller0.getAButton()) {
      intakeStatus = IntakeStatus.INTAKE;
    }

    else if (controller0.getRightBumper()) {
      intakeStatus = IntakeStatus.EJECT;
    }

    else {
      intakeStatus = IntakeStatus.STOP;
    }
  }

  private enum IntakeStatus {
    INTAKE,
    EJECT,
    STOP,
  }
}
