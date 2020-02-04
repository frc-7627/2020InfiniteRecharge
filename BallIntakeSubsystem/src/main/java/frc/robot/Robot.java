/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;

/*
-Have intake spin forward/backward and stop w/ a button
-If sensor detects ball, add 1 to the counter
-Keep spinning a certain amount of time to get the ball into the magazine */


public class Robot extends TimedRobot {
  Joystick primaryJoystick = new Joystick(0); //logitech Joystick
  Joystick secondaryJoystick = new Joystick(1); //logitech flightstick

  Spark intakeMotor = new Spark(4);
  Spark magazineMotor = new Spark(5);

  Encoder magazineEncoder = new Encoder(0,1, false, Encoder.EncodingType.k4X);
  DigitalInput ballDetector = new DigitalInput(2);

  int ballCount;
  final int BALL_LIMIT = 5;
  final int MAX_MAGAZINE_ENCODER_COUNT = 1;

  @Override
  public void robotInit() {
    ballCount = 5;
    magazineEncoder.reset();
  }

  @Override
  public void robotPeriodic() {
    if (ballCount < BALL_LIMIT) {
      intakeMotor.set(1);
    } else {
      intakeMotor.set(0);
    }
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {
    SmartDashboard.putNumber("Ball Count", ballCount);
    SmartDashboard.putNumber("Magazine Encoder Value", magazineEncoder.get());
    SmartDashboard.putBoolean("Ball Detected", ballDetector.get());


    if (ballDetector.get()) {
      magazineMotor.set(1);
    }
    if (magazineEncoder.get() > MAX_MAGAZINE_ENCODER_COUNT) {
      magazineMotor.set(0);
      magazineEncoder.reset();
      ballCount++;
    }
    if (primaryJoystick.getRawButtonPressed(3)) { // X button on the logitech controller
      shootBall();
    }
  }

  @Override
  public void testPeriodic() {
  }

  public void shootBall() {
    //pseudo code
    ballCount = 0;
  }
}
