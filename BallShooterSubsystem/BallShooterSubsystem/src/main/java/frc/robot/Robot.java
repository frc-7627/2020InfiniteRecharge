/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //these are temporary variables for the purpose of writing code
  Joystick stick = new Joystick(0);
  Joystick flightStick = new Joystick(1);

  //Shooter Angle Motor
  Spark shooterAngle = new Spark(0);
  //Spinning Shooter Disks Motor
  Spark shooterDisks = new Spark(1);
  //TODO - edit ports for spark motors

  //Shooter Angle Encoder
  Encoder angleEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
  //TODO - edit (0, 1,..) with real DIO ports if needed


  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    angleEncoder.setDistancePerPulse((4./256.) * 360);
    angleEncoder.setSamplesToAverage(5);
  }


  @Override
  public void robotPeriodic() {

    if (angleEncoder.get() > 42) {
      shooterAngle.set(.20);
    }

    

  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopPeriodic() {
    if (flightStick.getRawButton(1)){
      shooterDisks.set(.75);
    }else {
      shooterDisks.set(0);
    }
  }

  

  @Override
  public void testPeriodic() {
  }
}
