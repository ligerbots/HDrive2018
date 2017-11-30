package org.usfirst.frc.team2877.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2877.robot.OI;
import org.usfirst.frc.team2877.robot.Robot;
import org.usfirst.frc.team2877.robot.subsystems.DriveTrain;

/**
 *
 */
public class DriveCommand extends Command {

    boolean zeroed;
    boolean oldZeroed;
    double correctTurn;
    boolean canDriveNow;
    double startAngle;
    int ticker = 0;
    double topCorrectionSpeed = 1;
    double midCorrectionSpeed = 0.5;
    double botCorrectionSpeed = 0.2;
    double topCorrectionAngle = 10;
    double midCorrectionAngle = 5;
    OI oi;
    DriveTrain driveTrain;
    public DriveCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      startAngle = driveTrain.getAngle();
      oi = Robot.oi;
      driveTrain = Robot.driveTrain;
      zeroed = true;
      topCorrectionSpeed = SmartDashboard.getNumber("Top Correction Speed", 1);
      midCorrectionSpeed = SmartDashboard.getNumber("Middle Correction Speed", 0.5);
      botCorrectionSpeed = SmartDashboard.getNumber("Low Correction Speed", 0.2);
      topCorrectionAngle = SmartDashboard.getNumber("Top Correction Angle", 10);
      midCorrectionAngle = SmartDashboard.getNumber("Middle Correction Angle", 5);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    
      zeroed = oi.turnZeroed();
      /*if (!zeroed && oldZeroed) {
        ticker += 1;
        if (ticker >= 50) {
          canDriveNow = true;
          ticker = 0;
        }
      }*/
      if (zeroed && !oldZeroed) {
        startAngle = driveTrain.getAngle();
      }
      if (zeroed) {
        double angleError = startAngle - driveTrain.getAngle();
        double temp = Math.abs(angleError);
        if (temp >= topCorrectionAngle) {
            correctTurn = Math.signum(angleError) * topCorrectionSpeed;
        }
        else if (temp >= midCorrectionAngle && topCorrectionAngle < 10) {
            correctTurn = Math.signum(angleError) * midCorrectionSpeed;
        }
        else {
            correctTurn = Math.signum(angleError) * botCorrectionSpeed;
        }
      }
      else {
          correctTurn = Robot.oi.getTurn();
      }
      Robot.driveTrain.strafe(Robot.oi.getStrafe());
      Robot.driveTrain.arcadeDrive(Robot.oi.getThrottle(), correctTurn);
      oldZeroed = zeroed;      
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
