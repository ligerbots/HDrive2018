package org.usfirst.frc.team2877.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
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
    boolean navxOn = false;
    CANTalon leftSlave;
    CANTalon rightMaster;
    CANTalon rightSlave;
    CANTalon centerMaster;
    CANTalon centerSlave;
    RobotDrive robotDrive;
    boolean fieldCentric = false;
    PIDController turningController;
    double turnOutput = 0;
    double limitedStrafe = 0;
    
    AHRS navx;
    
    public DriveTrain() {
      
      SmartDashboard.putNumber("Top Correction Speed", 1);
      SmartDashboard.putNumber("Middle Correction Speed", 0.5);
      SmartDashboard.putNumber("Low Correction Speed", 0.2);
      SmartDashboard.putNumber("Top Correction Angle", 10);
      SmartDashboard.putNumber("Middle Correction Angle", 5);
      
      SmartDashboard.putNumber("P", 0.045);
      SmartDashboard.putNumber("I", 0.004);
      SmartDashboard.putNumber("D", 0.06);
      
      SmartDashboard.putNumber("turnP", 0.1);
      SmartDashboard.putNumber("turnI", 0);
      SmartDashboard.putNumber("turnD", 0.05);
      
      SmartDashboard.putNumber("Strafe Ramp Rate", 0.08);
      
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
      
      navx = new AHRS(SPI.Port.kMXP, (byte) 200);
      
      turningController = new PIDController(0.045, 0.004, 0.06, navx,
          output -> this.turnOutput = output);
    }
    
    public void allDrive(double throttle, double rotate, double strafe) {
      if (fieldCentric) {
        double scale = Math.max(Math.sin(getYaw()), Math.cos(getYaw()));
        robotDrive.arcadeDrive(throttle * Math.cos(getYaw()) / scale, rotate);
        centerMaster.set(strafe * Math.sin(getYaw()) / scale);
      }
      else {
        double rampRate = SmartDashboard.getNumber("Strafe Ramp Rate", 0.08);
        double change = throttle - limitedStrafe;
        if (throttle > 0 && change > 0) {
          if (change > rampRate) { //volts per tick
            change = rampRate;
          }
          limitedStrafe += change;
        } else if (throttle < 0 && change < 0) {
          if (change < -rampRate) {
            change = -rampRate;
          }
          limitedStrafe += change;
        } else {
          limitedStrafe = throttle;
        }
        robotDrive.arcadeDrive(throttle, rotate);
        centerMaster.set(limitedStrafe);
      }
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
    
    public boolean getNavXOn() {
      return navxOn;
    }
    
    public void toggleNavXControl() {
      if (navxOn) {
        navxOn = false;
      }
      else {
        navxOn = true;
      }
    }
    
    public void checkTalonVoltage() {
      Arrays.asList(leftMaster, rightMaster, leftSlave, rightSlave, centerMaster, centerSlave).forEach((CANTalon talon) -> SmartDashboard.putNumber(((Integer)talon.getDeviceID()).toString(), talon.getOutputVoltage() * talon.getOutputCurrent()));
    }
    
    double temporaryFixDegrees(double input) {
        if (input > 180) {
          return input - 360;
        }
        else if (input < -180){
          return input + 360;
        }
        else {
          return input;
        }
    }
    
    public void enableTurningControl(double angle, double tolerance) {
      double startAngle = this.getYaw();
      double temp = startAngle + angle;
     // RobotMap.TURN_P = turningController.getP();
     // RobotMap.TURN_D = turningController.getD();
     // RobotMap.TURN_I = turningController.getI();
      temp = temporaryFixDegrees(temp);
      turningController.setSetpoint(temp);
      turningController.enable();
      turningController.setInputRange(-180.0, 180.0);
      turningController.setOutputRange(-1.0,1.0);
      turningController.setAbsoluteTolerance(tolerance);
      turningController.setToleranceBuffer(1);
      turningController.setContinuous(true);
      turningController.setSetpoint(temp);
    }

    
    public void toggleFieldCentric() {
        if (fieldCentric) {
          fieldCentric = false;
        }
        else {
          fieldCentric = true;
        }
    }
    
    public void setPID (double p, double i, double d) {
      turningController.setPID(p, i, d);
    }
    
    public void disablePID () {
      turningController.disable();
    }
    
    public boolean isPidOn() {
      return turningController.isEnabled();
    }
    
    public double getTurnOutput() {
      return turnOutput;
    }
}

