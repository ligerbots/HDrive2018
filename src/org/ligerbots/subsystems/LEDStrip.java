package org.ligerbots.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.ligerbots.RobotMap;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Spark;


/**
 *
 */
public class LEDStrip extends Subsystem {
	
	int lastRow = 0;
	double voltage = -0.99;		// start at row 1

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Spark revBlinkin = new Spark(RobotMap.LED_PWM_CHANNEL);
    public void doNothing () {
    	return;
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setLights(int row) {
      // "row" is the row in the rev Robotics table of voltage to LED pattern
      voltage = -1.01+(0.02*row);
      if (row != lastRow) {
          System.out.println("Changed revblinkin speed " + row + "Voltage: " + voltage);
          lastRow = row;
      }
      revBlinkin.set(voltage);
      SmartDashboard.putNumber("LED Strip voltage", voltage);
    }
    
    public void refreshLights() {
        revBlinkin.set(voltage);
    }
    
    public void lightsOff() {
      revBlinkin.setSpeed(0.0);
    }
}

