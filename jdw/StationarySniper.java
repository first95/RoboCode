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
		setAdjustRadarForGunTurn(false);

		// Robot main loop
		while(true) {	
			double curGunHeading = getGunHeading();
			double curRadarHeading = getRadarHeading();
			double gunMisalignment = shortWayAngleDeltaDegrees(curGunHeading, lastSeenEnemyHeading);
			double radarMisalignment =  shortWayAngleDeltaDegrees(curRadarHeading, lastSeenEnemyHeading);

			switch(state) {
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
			// Apply the commanded set...() movements
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
	 * Record the last seen location of the robot and start aiming at it
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// e.getHeading() tells you which direction the other robot is facing
		// e.getBearing() tells you the angle between your robot direction and the direction you'd be have to be facing to face directly at the other robot
		
		// Record the compass heading to the robot
		lastSeenEnemyHeading = getHeading() + e.getBearing();
		//out.println("Saw robot at " + e.getDistance() + " @ " + lastSeenEnemyHeading + " with gun at " + getGunHeading() + ", radar at " + getRadarHeading() + " and robot at " + getHeading());
		switch(state) {
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
	 * When hit, move a little bit.
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		out.println("Transitioning to state FINDING");
		state = State.FINDING;
		back(50);
	}
	
	/**
	 * If a bullet misses, the other robot probably moved.  
	 * Find it again.
	 */
	public void onBulletMissed(BulletMissedEvent event) {
		out.println("Transitioning to state FINDING");
		state = State.FINDING;
	}
}
