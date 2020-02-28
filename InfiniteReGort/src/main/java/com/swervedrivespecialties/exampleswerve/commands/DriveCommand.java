package com.swervedrivespecialties.exampleswerve.commands;

//import com.swervedrivespecialties.exampleswerve.Robot;
import com.swervedrivespecialties.exampleswerve.subsystems.DrivetrainSubsystem;
import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.geometry.Translation2d;
//import org.frcteam2910.common.robot.Utilities;

public class DriveCommand extends Command {

    public DriveCommand() {
        requires(DrivetrainSubsystem.getInstance());
    }

    @Override
    protected void execute() {
        /* double slowmo = .5;
        double forward = Robot.getOi().getFlightStick().getRawAxis(1) * slowmo;
        forward = Utilities.deadband(forward, .1);
        // Square the forward stick
        forward = Math.copySign(Math.pow(forward, 2.0), forward);

        double strafe = Robot.getOi().getFlightStick().getRawAxis(0) * slowmo;
        strafe = Utilities.deadband(strafe, .1);
        // Square the strafe stick
        strafe = Math.copySign(Math.pow(strafe, 2.0), strafe);

        double rotation = -Robot.getOi().getFlightStick().getRawAxis(2) * slowmo; //Set this to 2 for Flightstick, 4 for joystick
        rotation = Utilities.deadband(rotation, .1);
        // Square the rotation stick
        rotation = Math.copySign(Math.pow(rotation, 2.0), rotation);

        DrivetrainSubsystem.getInstance().drive(new Translation2d(forward, strafe), rotation, true); */
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
