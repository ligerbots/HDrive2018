package org.ligerbots.commands;

import org.ligerbots.OI;
import org.ligerbots.Robot;
import org.ligerbots.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveCommand extends Command {

    boolean navxOn = false;
    boolean zeroed;
    boolean oldZeroed;
    boolean oldOldZeroed;
    double correctTurn;
    boolean canDriveNow;
    double startAngle;
    int ticker = 0;
    double topCorrectionSpeed = 1;
    double midCorrectionSpeed = 0.5;
    double botCorrectionSpeed = 0.2;
    double topCorrectionAngle = 10;
    double midCorrectionAngle = 5;
    double p;
    double i;
    double d;
    OI oi;
    DriveTrain driveTrain;
    public DriveCommand() {
      driveTrain = Robot.driveTrain;
      oi = Robot.oi;
      // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      startAngle = driveTrain.getAngle();
      zeroed = true;
      oldZeroed = false;
      topCorrectionSpeed = SmartDashboard.getNumber("Top Correction Speed", 1);
      midCorrectionSpeed = SmartDashboard.getNumber("Middle Correction Speed", 0.5);
      botCorrectionSpeed = SmartDashboard.getNumber("Low Correction Speed", 0.2);
      topCorrectionAngle = SmartDashboard.getNumber("Top Correction Angle", 10);
      midCorrectionAngle = SmartDashboard.getNumber("Middle Correction Angle", 5);
      p = SmartDashboard.getNumber("P", 0.045);
      i = SmartDashboard.getNumber("I", 0.004);
      d = SmartDashboard.getNumber("D", 0.06);
      driveTrain.setPID(p, i, d);
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
       // startAngle = driveTrain.getAngle();
          startAngle = driveTrain.getYaw();
      }
      if (zeroed && Math.abs(oi.getThrottle()) >= 0.01 && Math.abs(oi.getStrafe()) >= 0.1) {
       /* double angleError = startAngle - driveTrain.getAngle();
        double temp = Math.abs(angleError);
        if (temp >= topCorrectionAngle) {
            correctTurn = Math.signum(angleError) * topCorrectionSpeed;
        }
        else if (temp >= midCorrectionAngle && topCorrectionAngle < 10) {
            correctTurn = Math.signum(angleError) * midCorrectionSpeed;
        }
        else {
            correctTurn = Math.signum(angleError) * botCorrectionSpeed;
        }*/
     //   driveTrain.enableTurningControl(startAngle, 0.3);
          driveTrain.enableTurningControl(startAngle - driveTrain.getYaw(), 0.3);

          correctTurn = driveTrain.getTurnOutput();
      }
      else {
          driveTrain.disablePID();
          correctTurn = Robot.oi.getTurn();
      }
      Robot.driveTrain.allDrive(Robot.oi.getThrottle(), driveTrain.getNavXOn() ? correctTurn : Robot.oi.getTurn(), Robot.oi.getStrafe());
      oldOldZeroed = oldZeroed;
      oldZeroed = zeroed;  
      
      SmartDashboard.putNumber("Get Yaw Angle", driveTrain.getYaw());
      SmartDashboard.putNumber("Get Angle Angle", driveTrain.getAngle());
      SmartDashboard.putBoolean("PID on", driveTrain.getNavXOn());
      SmartDashboard.putBoolean("Is PID on?", driveTrain.isPidOn());
      SmartDashboard.putBoolean("Zeroed?", zeroed);
      SmartDashboard.putNumber("Start Angle", startAngle);
      
      driveTrain.checkTalonVoltage();
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
