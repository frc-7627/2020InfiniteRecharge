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

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;

import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

 /*
  * What to do:
  * make two (one in the code because they are connected mechanically) pnuemantic pistons go out on a button press
  * when the button is pressed again retract the piston and turn two motors to spin the winch
  */

public class Robot extends TimedRobot {

  Joystick joystick = new Joystick(0); 

  Compressor compress = new Compressor(0);
  DoubleSolenoid doubleSolenoid = new DoubleSolenoid(0, 1);

  Spark winchMotor1 = new Spark(2);
  Spark winchMotor2 = new Spark(3);

  Timer wait = new Timer();


  boolean up = false;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
  compress.setClosedLoopControl(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() 
  {
    
    if(joystick.getRawButtonPressed(2) && up == false)
    {
      doubleSolenoid.set(Value.kForward);
      up = true;
    }
    if(joystick.getRawButtonPressed(2) && up)
    {
      doubleSolenoid.set(Value.kReverse);
      winchMotor1.set(0.5);
      winchMotor2.set(0.5);
      
      //wait time in seconds ---------- will need to change time after testing ---------------------
      wait.delay(5);

      winchMotor1.set(0);
      winchMotor2.set(0);

      up = false;
    }
  }
