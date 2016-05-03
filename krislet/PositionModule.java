package edu.kit.robocup.position;

import java.awt.Point;
import java.util.Vector;

import com.sun.javafx.css.CalculatedValue;

import edu.kit.robocup.krislet.info.FlagInfo;
import edu.kit.robocup.krislet.info.ObjectInfo;
import edu.kit.robocup.krislet.info.VisualInfo;
import javafx.geometry.Point2D;

public class PositionModule {
	//predifined values in ServerParam.cpp
	final double PITCH_LENGTH = 105.0;
	final double PITCH_WIDTH = 68.0;
	final double PITCH_MARGIN = 5.0;
	
	//x-axis is horizontal and positive to the right, y-axis is vertical and positive to the top
	final Point2D flagc = new Point2D(0, 0);
	final Point2D goall = new Point2D(0, -PITCH_LENGTH/2.0);
	final Point2D goalr = new Point2D(0,PITCH_LENGTH/2.0);
	final Point2D flagcb = new Point2D(-PITCH_WIDTH/2.0,0);
	final Point2D flagct = new Point2D(PITCH_WIDTH/2.0,0);
	final Point2D flaglb = new Point2D(-PITCH_WIDTH/2.0,-PITCH_LENGTH/2.0);
	final Point2D flagrb = new Point2D(PITCH_WIDTH/2.0,-PITCH_LENGTH/2.0);
	final Point2D flaglt = new Point2D(-PITCH_WIDTH/2.0,PITCH_LENGTH/2.0);
	final Point2D flagrt = new Point2D(PITCH_WIDTH/2.0,PITCH_LENGTH/2.0);

	/**
	 * calculates position in field
	 * @param v VisualInfo of actual scene
	 * @return point of the position in field, returns null, if number of flags is less than 3
	 */
	public Point2D getPosition(VisualInfo v) {
		int c = 0;
		int numberOfPoints = 0;
		Vector<ObjectInfo> points = new Vector<ObjectInfo>(3);
		while (c <  v.m_objects.size() && (((ObjectInfo) v.m_objects.elementAt(c)).m_type == "flag") && numberOfPoints < 3) {
			ObjectInfo f = (ObjectInfo) v.m_objects.elementAt(c);
			if (f.m_type.split(" ")[0].matches("flag (c|l|r) (b|t)")) {
				numberOfPoints++;
				points.add((FlagInfo) f);
			}
			c++;
		}

//		ObjectInfo pointsflagcb = null;
//		ObjectInfo pointsflagct = null;
//		ObjectInfo pointsflaglb = null;
//		ObjectInfo pointsflaglt = null;
//		ObjectInfo pointsflagrb = null;
//		ObjectInfo pointsflagrt = null;
		// Array declaring which flags were seen: cb, ct, lb, lt, rb, rt
		Vector<int> found = new Vector<int>();
		ObjectInfo[] foundobj = new ObjectInfo[6];
		for (int i = 0; i < 6; i++) {
			foundobj[i] = null;
		}
		Point2D[] flags = new Point2D[6];
		flags[0] = flagcb;
		flags[1] = flagct;
		flags[2] = flaglb;
		flags[3] = flaglt;
		flags[4] = flagrb;
		flags[5] = flagrt;
		
		if(points.size() > 3) {
			for (int i = 0; i < points.size(); i++) {
				switch (points.get(i).m_type.split(" ")[1]) {
				case "c": {
					if ((points.get(i).m_type.split(" ")[2]) == "b") {
						foundobj[0] = (FlagInfo) points.get(i);
						found.add(0);
					} else {
						foundobj[1] = (FlagInfo) points.get(i);
						found.add(1);
					}
					break;
				}
				case "l": {
					if ((points.get(i).m_type.split(" ")[2]) == "b") {
						foundobj[2] = (FlagInfo) points.get(i);
						found.add(2);
					} else {
						foundobj[3] = (FlagInfo) points.get(i);
						found.add(3);
					}
					break;
				}
				case "r": {
					if ((points.get(i).m_type.split(" ")[2]) == "b") {
						foundobj[4] = (FlagInfo) points.get(i);
						found.add(4);
					} else {
						foundobj[5] = (FlagInfo) points.get(i);
						found.add(5);
					}
					break;
				}
				default:
					try {
						throw new Exception("regex not working");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			
			if (found.size() < 3) {
				try {
					throw new Exception("regex not working");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			} else {
				return calculateIntersection(foundobj[found.get(0)].m_distance, foundobj[found.get(1)].m_distance, foundobj[found.get(2)].m_distance, flags[found.get(0)], flags[found.get(1)], flags[found.get(2)]);
			}
			
			return null;
		} else {
			return null;
		}
	}
	
	/**
	 * calculates intersection of three circles
	 * @param dist1 radius of first circle
	 * @param dist2 radius of second circle
	 * @param dist3 radius of third circle
	 * @param p1 origin of first circle
	 * @param p2 origin of second circle
	 * @param p3 origin of third circle
	 * @return intersection point
	 */
	public static Point2D calculateIntersection(double dist1, double dist2, double dist3, Point2D p1, Point2D p2, Point2D p3) {
		double d = p1.distance(p2);
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		double a = (dist1 * dist1 - dist2 * dist2 + d * d) / (2.0 * d);
		double h = Math.sqrt(dist1 * dist1 - a * a);
		// first intersection of first and second circle
		double solx1 = p1.getX() + (a/d) * dx - (h/d) * dy;
		double soly1 = p1.getY() + (a/d) * dy + (h/d) * dx;
		Point2D sol1 = new Point2D(solx1, soly1);
		//second intersection of first and second circle
		double solx2 = p1.getX() + (a/d) * dx + (h/d) * dy;
		double soly2 = p1.getY() + (a/d) * dy - (h/d) * dx;
		Point2D sol2 = new Point2D(solx2, soly2);
		//check which point is on the third circle
		if (Math.abs(sol1.distance(p3) - dist3) < 0.000000001) {
			return sol1;
		} else {
			return sol2;
		}

	}
	
	public static void main(String[] args) {
		Point2D a1 = new Point2D(0, 0);
		Point2D a2 = new Point2D(0, 10);
		Point2D a3 = new Point2D(10,10);
		Point2D correct = new Point2D(5, 5);
		Point2D sol = calculateIntersection(a1.distance(correct),a2.distance(correct),a3.distance(correct), a1, a2, a3);
		System.out.println(sol.getX() + " " + sol.getY());
	}

}