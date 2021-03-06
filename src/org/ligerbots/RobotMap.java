package org.ligerbots;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;
    // Compile 
	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;

    public static final int CT_LEFT_1 = 5;
    public static final int CT_LEFT_2 = 4;
    public static final int CT_RIGHT_1 = 3; 
    public static final int CT_RIGHT_2 = 6;
    public static final int CT_CENTER_1 = 33;	// fix these numbers when we put it back on
    public static final int CT_CENTER_2 = 66;
    
    public static final int LED_PWM_CHANNEL = 9;
}
