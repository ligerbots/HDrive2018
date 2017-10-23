package org.usfirst.frc.team2877.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2877.robot.OI;
import org.usfirst.frc.team2877.robot.Robot;
import org.usfirst.frc.team2877.robot.subsystems.DriveTrain;

/**
 *
 */
public class DriveCommand extends Command {

  DriveTrain driveTrain;
  OI oi;
    public DriveCommand() {
      driveTrain = Robot.driveTrain;
      oi = Robot.oi;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      driveTrain.strafe(oi.getStrafe());
      driveTrain.tankDrive(oi.getThrottle(), oi.getTurn());
      
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
