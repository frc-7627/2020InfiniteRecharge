package com.swervedrivespecialties.exampleswerve;

import com.swervedrivespecialties.exampleswerve.subsystems.DrivetrainSubsystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.frcteam2910.common.robot.Utilities;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
    private static OI oi;
    public static OI getOi() {
        return oi;
    }
    
    private static DrivetrainSubsystem drivetrain;

    private static final String kAuto1 = "Go Reverse";
    private static final String kAuto2 = "Shoot Ball(Must Be In Front of Target)";

    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

      /**
   * Change the I2C port below to match the connection of your color sensor
   */
    private final I2C.Port i2cPort = I2C.Port.kOnboard;

    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
  
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  
    Encoder colorEncoder;

    double diameter = 3/12;
    double dist = diameter * 3.14 / 1024;
    String gameData;

    Timer autoTimer = new Timer();
    Timer climbTimer = new Timer();
    Timer magTimer = new Timer();

    int ballCount;

    Joystick joystick = new Joystick(0);
    Joystick flightStick = new Joystick(1);

    
    Spark intakeMotor = new Spark(4);
    Spark magazineMotor = new Spark(5);
    Spark shooterDisks = new Spark(6);
    Spark shooterAngle = new Spark(7);
    Spark winchMotors = new Spark(8);
    Spark wheelSpinner = new Spark(9);

    Compressor compress = new Compressor(0);
    DoubleSolenoid doubleSolenoid = new DoubleSolenoid(0, 1);

    //Encoder magazineEncoder = new Encoder(0,1, false, Encoder.EncodingType.k4X);
    Encoder angleEncoder = new Encoder(2,3, false, Encoder.EncodingType.k4X);

    //final int MAX_MAGAZINE_ENCODER_COUNT = 1;

    @Override
    public void robotInit() {
        m_chooser.setDefaultOption("Go Reverse", kAuto1);
        m_chooser.addOption("Shoot Ball", kAuto2);
        SmartDashboard.putData("Auto choices", m_chooser);

        oi = new OI();
        drivetrain = DrivetrainSubsystem.getInstance();

        //magazineEncoder.reset();
        angleEncoder.setDistancePerPulse((4./256.) * 360);
        angleEncoder.setSamplesToAverage(5);

        compress.setClosedLoopControl(true);

        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget); 

        CameraServer.getInstance().startAutomaticCapture(0);
        CameraServer.getInstance().startAutomaticCapture(1);
    
        //Define an encoder on Channels 0 and 1, without reversing direction, and 4x encoding type
        colorEncoder = new Encoder(4, 5, false, Encoder.EncodingType.k4X);
        //set distence per pulse to the predetermined distance
        colorEncoder.setDistancePerPulse(dist); 
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        if (angleEncoder.get() > 42) {
            shooterAngle.set(.2);
        }
        SmartDashboard.putNumber("Ball Count", ballCount);
    }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    autoTimer.reset();
    autoTimer.start();
    ballCount = 5;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kAuto2:
        //"Shoot Ball" Code
        if (autoTimer.get() < 1) {
          swerveDrive(.5, 0, 0); //Go straight for 1 second
        } else if (autoTimer.get() < 6) {
          swerveDrive(0, 0, 0);
          shootBall(1, .5); //shoot for five seconds
        } else if (autoTimer.get() < 8) {
          shootBall(0, 0);
          ballCount = 0;
          swerveDrive(-.5, 0, 0); //Go in Reverse for 2 seconds
        } else {
          swerveDrive(0,0,0); //stop swerve drive
        }
        break;

        
      case kAuto1:
      default:
        // "Go Reverse" Code / Default
        if (autoTimer.get() < 1) {
          swerveDrive(-.5, 0, 0); //Go reverse for 1 second
        } else {
          swerveDrive(0,0,0);
        }
        break;
    }
  }

  @Override
  public void teleopInit() {
    magTimer.reset();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
      double speed = .6; //Math.abs(flightStick.getRawAxis(3));
      //swerveDrive(flightStick.getRawAxis(1) * speed, flightStick.getRawAxis(0) * speed, flightStick.getRawAxis(2) * speed);
      swerveDrive(joystick.getRawAxis(1) * speed, joystick.getRawAxis(0) * speed, joystick.getRawAxis(4) * speed);

      //SmartDashboard.putNumber("Magazine Encoder Value", magazineEncoder.get());

      if (flightStick.getRawButtonPressed(5)) { 
        ballCount++;
      } 

      if (flightStick.getRawButtonPressed(3)) { //reset ball count
        ballCount = 0;
      } 

      if (flightStick.getRawButtonPressed(1)) { // trigger button on the flightstick controller
        if (shooterDisks.get() == 0) {
          shootBall(1, .5);
        } else {
          shootBall(0, 0);
        }
      } else if (magTimer.get() > .5) { //Made this an else if to avoid mag motor being called twice
        magazineMotor.set(0);
        magTimer.stop();
        magTimer.reset();
      } else if (flightStick.getRawButtonPressed(2) && magTimer.get() == 0) { 
        magazineMotor.set(.5);
        magTimer.start();
      } 

      if (joystick.getRawButtonPressed(5)) {
        if(intakeMotor.get() == 0) {
          intakeMotor.set(1);
        } else {
          intakeMotor.set(0);
        } 
      }

      if(joystick.getRawButtonPressed(2) && doubleSolenoid.get() == Value.kOff) {
        doubleSolenoid.set(Value.kForward);
      } else if(joystick.getRawButtonPressed(2) && doubleSolenoid.get() == Value.kForward) {
        doubleSolenoid.set(Value.kReverse);
        winchMotors.set(0.5);
        climbTimer.start();
      } else if (climbTimer.get() > 5) {
        winchMotors.set(0);
        climbTimer.stop();
        climbTimer.reset();
      }
  }

  @Override
  public void testInit() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public void swerveDrive(double forward, double strafe, double rotation) {
    forward = Utilities.deadband(forward, .1);
    
    strafe = Utilities.deadband(strafe, .1);
    if(forward == 0 && Math.abs(strafe) > 0)
      forward = 0.1;

    // Square the forward stick
    forward = Math.copySign(Math.pow(forward, 2.0), forward);

    // Square the strafe stick
    strafe = Math.copySign(Math.pow(strafe, 2.0), strafe);

    rotation = Utilities.deadband(rotation);
    // Square the rotation stick
    rotation = Math.copySign(Math.pow(rotation, 2.0), rotation);

    DrivetrainSubsystem.getInstance().drive(new Translation2d(forward, strafe), rotation, true);
  }

  public void shootBall(double shooterSpeed, double magazineSpeed) {
    shooterDisks.set(shooterSpeed);
    magazineMotor.set(magazineSpeed);
  }
}
