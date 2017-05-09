package edu.smartvehicle.view.path;

import java.awt.geom.Path2D;
import java.util.HashMap;

/** 
* @author Edith Zavala
*/

public class SVViewPathCurve {
	private static HashMap<String,Path2D> path;
	
	public static HashMap<String,Path2D> getPath(int canvasWidth, int canvasHeight, int vehWidth, int vehHeight){
		path = new HashMap<String, Path2D>();
		
		path.put("left", new Path2D.Double());
		path.put("center", new Path2D.Double());
		path.put("right", new Path2D.Double());
		
		path.put("leftInv", new Path2D.Double());
		path.put("rightInv", new Path2D.Double());
		
		path.get("left").moveTo((canvasWidth/2)-(vehWidth/2)-10, canvasHeight-1);
		path.get("center").moveTo((canvasWidth/2), canvasHeight-1);
		path.get("right").moveTo((canvasWidth/2)+(vehWidth/2)+10, canvasHeight-1);
		
		path.get("leftInv").moveTo((canvasWidth/2)-(vehWidth/2)-4, canvasHeight-1);
		path.get("rightInv").moveTo((canvasWidth/2)+(vehWidth/2)+4, canvasHeight-1);
		
		double currentXLeft;
		double currentXCenter;
		double currentXRight;
		double currentXLeftInv;
		double currentXRightInv;
		
		double currentYLeft;
		double currentYCenter;
		double currentYRight;
		double currentYRightInv;
		double currentYLeftInv;
		
		double xLeft;
		double xCenter;
		double xRight;
		double xLeftInv;
		double xRightInv;
		
		double yLeft;
		double yCenter;
		double yRight;
		double yLeftInv;
		double yRightInv;
		
		double angle = 0;
		
		for(int i=0; i<12000; i++){
			currentXLeft = path.get("left").getCurrentPoint().getX();
			currentXCenter = path.get("center").getCurrentPoint().getX();
			currentXRight = path.get("right").getCurrentPoint().getX();
			currentXLeftInv = path.get("leftInv").getCurrentPoint().getX();
			currentXRightInv = path.get("rightInv").getCurrentPoint().getX();
			
			currentYLeft = path.get("left").getCurrentPoint().getY();
			currentYCenter = path.get("center").getCurrentPoint().getY();
			currentYRight = path.get("right").getCurrentPoint().getY();
			currentYLeftInv = path.get("leftInv").getCurrentPoint().getY();
			currentYRightInv = path.get("rightInv").getCurrentPoint().getY();
			
			double indicator = i%480;

			if((indicator>=80 && indicator<=120) || (indicator>=360 && indicator<=400)){
				angle = angle + 0.5;
			}else if((indicator>=160 && indicator<=200) || (indicator>=280 && indicator<=320)){
				angle = angle - 0.5;
			}
						
			xLeft = currentXLeft + (2.5)*(Math.sin(Math.toRadians(angle)));
			xCenter = currentXCenter + (2.5)*(Math.sin(Math.toRadians(angle)));
			xRight = currentXRight + (2.5)*(Math.sin(Math.toRadians(angle)));
			xLeftInv = currentXLeftInv + (2.5)*(Math.sin(Math.toRadians(angle)));
			xRightInv = currentXRightInv + (2.5)*(Math.sin(Math.toRadians(angle)));
			
			yLeft = currentYLeft - (2.5)*(Math.cos(Math.toRadians(angle)));
			yCenter = currentYCenter - (2.5)*(Math.cos(Math.toRadians(angle)));
			yRight = currentYRight - (2.5)*(Math.cos(Math.toRadians(angle)));
			yLeftInv = currentYLeftInv - (2.5)*(Math.cos(Math.toRadians(angle)));
			yRightInv = currentYRightInv - (2.5)*(Math.cos(Math.toRadians(angle)));
			
			path.get("left").lineTo(xLeft, yLeft);
			path.get("center").lineTo(xCenter, yCenter);
			path.get("right").lineTo(xRight, yRight);
			path.get("leftInv").lineTo(xLeftInv, yLeftInv);
			path.get("rightInv").lineTo(xRightInv, yRightInv);
		}
		return SVViewPathCurve.path;
	}
}
