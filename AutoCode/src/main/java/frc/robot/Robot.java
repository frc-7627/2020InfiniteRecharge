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

import edu.wpi.first.wpilibj.Timer;



public class Robot extends TimedRobot {
  private static final String kAuto1 = "Auto Position 1";
  private static final String kAuto2 = "Auto Position 2";
  private static final String kAuto3 = "Auto Position 3";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  Timer autoTimer = new Timer();


  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Auto Position 1", kAuto1);
    m_chooser.addOption("Auto Position 2", kAuto2);
    m_chooser.addOption("Auto Position 3", kAuto3);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    autoTimer.reset();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    autoTimer.start();

    switch (m_autoSelected) {
      case kAuto3:
        //Position 3 Code
        if (autoTimer.get() < 2) {
          swerveDrive(0, 1, 0); //Go straight for 2 seconds
        } else if (autoTimer.get() < 3) {
          swerveDrive(-1, 0, 0); //Strafe Left for 1 second
        } else if (autoTimer.get() < 3.5) {
          shootBall();
        } 
        break;


      case kAuto2:
        //Position 2 Code
        if (autoTimer.get() < 2) {
          swerveDrive(0, 1, 0); //Go straight for 2 seconds
        } else if (autoTimer.get() < 2.5) {
          swerveDrive(-1, 0, 0); //Strafe Left for half a second
        } else if (autoTimer.get() < 3) {
          shootBall();
        } 
        break;

        
      case kAuto1:
      default:
        // Position 1 Code / Default
        if (autoTimer.get() < 1) {
          swerveDrive(0, 1, 0); //Go straight for 1 second
        } else if (autoTimer.get() < 1.5) {
          shootBall();
        }
        break;
    }
  }
  @Override
  public void teleopInit() {
    autoTimer.stop();
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testPeriodic() {
  }

  public void swerveDrive(int x, int y, int z) {
  }

  public void shootBall() {
  }
}
