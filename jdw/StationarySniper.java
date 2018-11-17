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
	private double lastSeenEnemyHeading = 0;	

	/**
	 * run: MyFirstRobot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		//setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(false);

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
			double gunMisalignment = shortWayAngleDeltaDegrees(curGunHeading, lastSeenEnemyHeading);
			double radarMisalignment =  shortWayAngleDeltaDegrees(curRadarHeading, lastSeenEnemyHeading);

			switch(state) {
				case ALIGNING:


					break;
				case FINDING:
					// Spin the radar fast, since we can move that faster than the gun
					setTurnRadarLeft(INITIAL_FIND_SPEED);
					break;
				case AIMING:
					//out.println("curGunHeading: " + curGunHeading + " misalignment: " + gunMisalignment);
					setTurnGunRight(gunMisalignment); 
					setTurnRadarRight(radarMisalignment);
					break;
				case FIRING:
					//out.println("curGunHeading: " + curGunHeading + " misalignment: " + gunMisalignment);
					setTurnGunRight(gunMisalignment); 
					setTurnRadarRight(radarMisalignment);
					fire(100); // It won't fire with a full 100 but it'll spend all it's allowed to
					break;
			}
			execute();
		}
	}

	/**
	 * If we wanna turn the gun or radar from heading a to angle b,
	 * compute how many degrees to turn, taking the shortest route.
	 */
	private static double shortWayAngleDeltaDegrees(double a, double b) {
		double delta = b - a;
		if(delta > 180.0) {
			delta -= 360.0;
		} else if (delta < -180.0) {
			delta += 360.0;
		}
		return delta;
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// e.getHeading() tells you which direction the other robot is facing
		// e.getBearing() tells you the angle between your robot direction and the direction you'd be have to be facing to face directly at the other robot
		
		// Replace the next line with any behavior you would like
		lastSeenEnemyHeading = getHeading() + e.getBearing();
		//out.println("Saw robot at " + e.getDistance() + " @ " + lastSeenEnemyHeading + " with gun at " + getGunHeading() + ", radar at " + getRadarHeading() + " and robot at " + getHeading());
		switch(state) {
			case ALIGNING:
				
				break;
			case FINDING:
				out.println("Transitioning to state AIMING");
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
	
	public void onBulletMissed(BulletMissedEvent event) {
		out.println("Transitioning to state FINDING");
		state = State.FINDING;
	}
}
