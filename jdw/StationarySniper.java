package jdw;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * A robot that sits still and shoots at others
 */
public class StationarySniper extends AdvancedRobot
{

	public final double INITIAL_FIND_SPEED = 60;
	public final double AIMING_SPEED = 1;
	
	private enum State {
		ALIGNING,
		FINDING,
		AIMING,
		FIRING,
	};
	private State state = State.FINDING;
	/**
	 * run: MyFirstRobot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		//setAdjustGunForRobotTurn(true);
		//setAdjustRadarForGunTurn(true);

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {	
			//double energy;
			//energy = getEnergy();
			//out.println("My energy right now is: " + energy);
			double curGunHeading = getGunHeading();
			double curRadarHeading = getRadarHeading();
			double misalignment = (curGunHeading - curRadarHeading + 180.0) % 360.0 - 180.0;

			switch(state) {
				case ALIGNING:


					break;
				case FINDING:
					// Spin the radar fast, since we can move that faster than the gun
					setTurnRadarLeft(INITIAL_FIND_SPEED);
					break;
				case AIMING:
					setTurnRadarLeft(AIMING_SPEED);
					break;
				case FIRING:
					setTurnGunRight(misalignment); 
					fire(1);
					break;
			}
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		fire(3);
		switch(state) {
			case ALIGNING:
				
				break;
			case FINDING:
				out.println("Transitioning to state AIMING");
				out.println("Saw robot at " + e.getDistance() + " @ " + e.getBearing());
				state = State.AIMING;
				break;
			case AIMING:
				out.println("Transitioning to state FIRING");
				state = State.FIRING;
				break;
			case FIRING:
				//fire(10);
				break;
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
}
