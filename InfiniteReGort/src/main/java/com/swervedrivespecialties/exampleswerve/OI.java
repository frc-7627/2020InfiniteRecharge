package com.swervedrivespecialties.exampleswerve;

import com.swervedrivespecialties.exampleswerve.subsystems.DrivetrainSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class OI {
    /*
       Add your joysticks and buttons here
     */
    private Joystick joystick = new Joystick(0);
    private Joystick flightStick = new Joystick(1);

    public OI() {
        // Back button zeroes the drivetrain
        new JoystickButton(joystick, 7).whenPressed(
                new InstantCommand(() -> DrivetrainSubsystem.getInstance().resetGyroscope())
        );
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public Joystick getFlightStick() {
        return flightStick;
    }
}
