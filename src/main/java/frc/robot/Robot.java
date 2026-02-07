// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Test comment from Isaac!

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//added gb
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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
  private static final String kDefaultAuto = "Default";
  private static final String kCenterForward = "Center Move Forward";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();


  //gb added
    private final XboxController controller0 = new XboxController(0);
    private final SparkMax leftMotor = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax rightMotor = new SparkMax(2, MotorType.kBrushless);

    private final DifferentialDrive robotDrive = new DifferentialDrive(leftMotor, rightMotor);
    private final SparkMaxConfig driveConfig = new SparkMaxConfig();

    private final Timer timer1 = new Timer();
    //used in teleop to control drive speed... slow it down so not so jumpy
    private double driveSpeed = 1;

   



    //private final DifferentialDrive robotDrive =
    //        new DifferentialDrive(leftMotor::set, rightMotor::set);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Center Move Forward", kCenterForward);
    SmartDashboard.putData("Auto choices", m_chooser);

    //gb add        
    SendableRegistry.addChild(robotDrive, leftMotor);
    SendableRegistry.addChild(robotDrive, rightMotor);
    // Configure Spark Max motor controllers 
    driveConfig.smartCurrentLimit(60);
    driveConfig.voltageCompensation(12.0);
    

    // Apply configuration to left motor
    leftMotor.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // Apply configuration to right motor and invert it
    driveConfig.inverted(true);
    rightMotor.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // Persist parameters to retain configuration in the event of a power cycle


    //start timer
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
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    //gb added reset timer1 to zero
    timer1.restart();
    //git demo comment

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCenterForward:
        // drive forward
        //stop after 2 seconds
        if(timer1.get() < 2) {
          robotDrive.tankDrive(0.5,0.5); //drive forward at half speed
        } 
        else if(timer1.get() < 4) { //spin for 2 seconds
          robotDrive.tankDrive(0.5,0); 
        } else {  //stop
          robotDrive.tankDrive(0.0,0.0); 
        }


        break;
      case kDefaultAuto:
      default:

if(timer1.get() < 2) {
          robotDrive.tankDrive(0.2,0.0); //drive forward at half speed
        } 
        else if(timer1.get() < 5) { //spin for 2 seconds
          robotDrive.tankDrive(0.2,0); 
        }
        else if(timer1.get() < 7) {
          robotDrive.tankDrive(0.5,0); 
        }
        else if(timer1.get() < 9) {
          robotDrive.tankDrive(0.5,0);

        } else {  //stop
          robotDrive.tankDrive(0.0,0.0); 
        }



        // Put default auto code here
        //GB added
        /* better code below *
        leftMotor.set(0.5); //half speed forward
        rightMotor.set(0.5);
        Timer.delay(2.0); //drive for 2 seconds
        leftMotor.set(0); //stop
        rightMotor.set(0);  
        */

        //more elegant solution
        //robotDrive.tankDrive(0.5,0.5);
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

         System.out.println("==========I am moving!==========");

if (controller0.getLeftBumperPressed()) {
  driveSpeed = 2; //divide by 2 to get 50% speed

}
if (controller0.getRightBumperPressed()) {
  driveSpeed = driveSpeed - 0.1;  //increase speed by 10%
  if (driveSpeed > 1) {
    driveSpeed = 1;
  }
  System.out.println("Drive Speed Set to: " + driveSpeed);
}   
         // Drive with tank drive.
       
        // read controller joystick value range -1.0 to +1.0 note flip the sign as joystick values are reversed
        // robotDrive.tankDrive(-controller0.getLeftY()/driveSpeed, -controller0.getRightY()/driveSpeed);

        //arcade drive 
        // That means that the Y axis drives forward the left stick
        // and backward, and the X turns left and right the right stick
        robotDrive.arcadeDrive(-controller0.getLeftY()/driveSpeed, -controller0.getRightX()/driveSpeed);



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
}
