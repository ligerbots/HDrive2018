package org.ligerbots.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.Robot;

public class LEDChangeColor extends Command {

  public LEDChangeColor(int startingRow) {
	  startingRow = selectedRow;
  }
	
  // Row from the LED Pattern table. From 1 to 100 
  static final int firstRow = 1;
  static final int lastRow = 100;
  int startingRow;
  int selectedRow;

  
  protected void initialize() {
	  selectedRow = startingRow;
  }
  
  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
	if (++selectedRow > 100) selectedRow = 1;
    Robot.ledStrip.setLights(selectedRow);    //indicate the column value as the input
  }
  
  // Called once after isFinished returns true
  protected void end() {
    //Robot.ledstrip.lightsOff();
  }

  @Override
  protected boolean isFinished() {
    // TODO Auto-generated method stub
    return true;
    
  }
  
  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {
    end();
  }

}
