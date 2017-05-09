package edu.smartvehicle.view.path;

import java.awt.geom.Path2D;
import java.util.HashMap;

/** 
* @author Edith Zavala
*/

public class SVViewPathStraight {
	private static HashMap<String,Path2D> path;
	
	public static HashMap<String,Path2D> getPath(int canvasWidth, int canvasHeight, int vehWidth, int vehHeight){
		path = new HashMap<String, Path2D>();
		
		path.put("left", new Path2D.Double());
		path.put("center", new Path2D.Double());
		path.put("right", new Path2D.Double());
		
		path.put("leftInv", new Path2D.Double());
		path.put("rightInv", new Path2D.Double());
		
		path.get("left").moveTo((canvasWidth/2)-(vehWidth/2)-10, canvasHeight-1);
		path.get("leftInv").moveTo((canvasWidth/2)-(vehWidth/2)-4, canvasHeight-1);
		
		path.get("center").moveTo((canvasWidth/2), canvasHeight-1);
		
		path.get("right").moveTo((canvasWidth/2)+(vehWidth/2)+10, canvasHeight-1);
		path.get("rightInv").moveTo((canvasWidth/2)+(vehWidth/2)+4, canvasHeight-1);
		
		double xLeft = path.get("left").getCurrentPoint().getX();
		double xLeftInv = path.get("leftInv").getCurrentPoint().getX();
		
		double xCenter = path.get("center").getCurrentPoint().getX();
		
		double xRight = path.get("right").getCurrentPoint().getX();
		double xRightInv = path.get("rightInv").getCurrentPoint().getX();
		
		double currentYLeft;
		double currentYLeftInv;
		
		double currentYCenter;
		
		double currentYRight;
		double currentYRightInv;
		
		for(int i=0; i<2000; i++){
			currentYLeft = path.get("left").getCurrentPoint().getY();
			currentYLeftInv = path.get("leftInv").getCurrentPoint().getY();
			
			currentYCenter = path.get("center").getCurrentPoint().getY();
			
			currentYRight = path.get("right").getCurrentPoint().getY();
			currentYRightInv = path.get("rightInv").getCurrentPoint().getY();
			
			path.get("left").lineTo(xLeft, currentYLeft-5);
			path.get("leftInv").lineTo(xLeftInv, currentYLeftInv-5);
			
			path.get("center").lineTo(xCenter, currentYCenter-5);
			
			path.get("right").lineTo(xRight, currentYRight-5);
			path.get("rightInv").lineTo(xRightInv, currentYRightInv-5);
		}
		return SVViewPathStraight.path;
	}
}
