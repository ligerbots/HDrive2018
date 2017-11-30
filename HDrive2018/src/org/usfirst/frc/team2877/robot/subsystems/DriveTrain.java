package org.usfirst.frc.team2877.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import org.usfirst.frc.team2877.robot.RobotMap;

/**
 *
 */
public class DriveTrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    CANTalon leftMaster;
    CANTalon leftSlave;
    CANTalon rightMaster;
    CANTalon rightSlave;
    CANTalon centerMaster;
    CANTalon centerSlave;
    RobotDrive robotDrive;
    
    AHRS navx;
    
    public DriveTrain() {
      
      SmartDashboard.putNumber("Top Correction Speed", 1);
      SmartDashboard.putNumber("Middle Correction Speed", 0.5);
      SmartDashboard.putNumber("Low Correction Speed", 0.2);
      SmartDashboard.putNumber("Top Correction Angle", 10);
      SmartDashboard.putNumber("Middle Correction Angle", 5);
      
      leftMaster = new CANTalon(RobotMap.CT_LEFT_1);
      leftSlave = new CANTalon(RobotMap.CT_LEFT_2);
      rightMaster = new CANTalon(RobotMap.CT_RIGHT_1);
      rightSlave = new CANTalon(RobotMap.CT_RIGHT_2);
      centerMaster = new CANTalon(RobotMap.CT_CENTER_1);
      centerSlave = new CANTalon(RobotMap.CT_CENTER_2);
      
      leftMaster.changeControlMode(TalonControlMode.PercentVbus);
      rightMaster.changeControlMode(TalonControlMode.PercentVbus);
      centerMaster.changeControlMode(TalonControlMode.PercentVbus);
      
      leftSlave.changeControlMode(TalonControlMode.Follower);
      rightSlave.changeControlMode(TalonControlMode.Follower);
      centerSlave.changeControlMode(TalonControlMode.Follower);
      
      leftSlave.set(RobotMap.CT_LEFT_1);
      rightSlave.set(RobotMap.CT_RIGHT_1);
      centerSlave.set(RobotMap.CT_CENTER_1);
      
      Arrays.asList(leftMaster, rightMaster, leftSlave, rightSlave, centerMaster, centerSlave).forEach((CANTalon talon) -> talon.enableBrakeMode(true));
      
      robotDrive = new RobotDrive(leftMaster, rightMaster);
      
    }
    
    public void arcadeDrive(double throttle, double rotate) {
      robotDrive.arcadeDrive(throttle, rotate);
    }
    
    public void strafe(double x) {
      centerMaster.set(x);
    }
    
    public double getYaw() {
      return navx.getYaw();
    }
    
    public double getRate() {
      return navx.getRate();
    }
    
    public double getAngle() {
      return navx.getAngle();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

