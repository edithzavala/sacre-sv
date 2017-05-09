package edu.smartvehicle.view.path;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import edu.smartVehicle.view.SVViewWindow;

/** 
* @author Edith Zavala
*/

public class SVViewPath extends JPanel {

	public static final int CANVAS_WIDTH = 300;
	public static final int CANVAS_HEIGHT = 300;
	public static final String TITLE = "SACRE";

	private String vehFileName = "\\sportVeh.png";
	private String treeFileName = "\\circlesTree.png";
	private Image img;
	private Rectangle2D veh;
	private Shape transformedVeh;
	private Image[] trees;
	private int imgWidth, imgHeight;
	private HashMap<String, Path2D> paths;

	private double speed;
	private double rotationAngle;
	private double vehAngle;

	private AffineTransform vehicleTransform;
	private AffineTransform pathTransform;
	private AffineTransform[] treesTransform;
	private Graphics2D g2d;

	private double latDisLeft;
	private double latDisRight;
	private double fronDisLeft;
	private double fronDisRight;
	private double devAngle;
	private HashMap<String, Object> vehInfo;

	private String pathKind;
	private SVViewWindow svvw;

	public SVViewPath(String path, SVViewWindow svvw) {
		URL urlVehicle = getClass().getClassLoader().getResource(vehFileName);
		this.pathKind = path;
		this.vehInfo = new HashMap<String, Object>();

		URL treeImg = getClass().getClassLoader().getResource(treeFileName);
		trees = new Image[100];
		this.treesTransform = new AffineTransform[trees.length];

		for (int i = 0; i < trees.length; i++) {
			try {
				trees[i] = ImageIO.read(treeImg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (urlVehicle == null) {
			System.out.println("Couldn't find file: " + vehFileName);
		} else {
			try {
				img = ImageIO.read(urlVehicle);
				imgWidth = img.getWidth(this);
				imgHeight = img.getHeight(this);
				veh = new Rectangle2D.Double(0, 0, imgWidth - 1, imgHeight / 5);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		if (path.equals("curve")) {
			this.paths = SVViewPathCurve.getPath(CANVAS_WIDTH, CANVAS_HEIGHT,
					imgWidth, imgHeight);
			
			for (int i = 0; i < treesTransform.length; i++) {
				this.treesTransform[i] = new AffineTransform();
				switch (i % 4) {
				case 0:
					this.treesTransform[i].translate(20, 100 - (50 * i));
					break;
				case 1:
					this.treesTransform[i].translate(310, 150 - (40 * i));
					break;
				case 2:
					this.treesTransform[i].translate(10, 100 - (50 * i));
					break;
				case 3:
					this.treesTransform[i].translate(315, 100 - (50 * i));
					break;
				}
			}
		} else {
			this.paths = SVViewPathStraight.getPath(CANVAS_WIDTH,
					CANVAS_HEIGHT, imgWidth, imgHeight);

			for (int i = 0; i < treesTransform.length; i++) {
				this.treesTransform[i] = new AffineTransform();
				switch (i % 4) {
				case 0:
					this.treesTransform[i].translate(50, 100 - (50 * i));
					break;
				case 1:
					this.treesTransform[i].translate(250, 150 - (40 * i));
					break;
				case 2:
					this.treesTransform[i].translate(35, 100 - (50 * i));
					break;
				case 3:
					this.treesTransform[i].translate(275, 100 - (50 * i));
					break;
				}
			}
		}
		this.speed = 0;
		this.rotationAngle = 0;
		this.vehAngle = 0;
		this.devAngle = 0;
		latDisRight = 0.50;
		latDisLeft = 0.50;

		this.vehicleTransform = new AffineTransform();
		this.pathTransform = new AffineTransform();

		this.vehicleTransform.translate(CANVAS_WIDTH / 2 - imgWidth / 2,
				(CANVAS_HEIGHT - 1) / 2 - imgHeight / 2);

		this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		Timer timer = new Timer();
		timer.schedule(new Cycle(this), new Date(), 1000);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		setBackground(Color.LIGHT_GRAY);
		g2d = (Graphics2D) g;
		AffineTransform saveTransform = g2d.getTransform();

		updatePath(this.paths);
		updateVehicle();
		calculateDistances();

		g2d.setTransform(saveTransform);
	}

	private void calculateDistances() {
		boolean interLeft = false;
		boolean outLeft = false;
		boolean interRight = false;
		boolean outRight = false;
		
		if (pathKind.equals("curve")) {
			PathIterator piLeftInv = paths.get("leftInv").getPathIterator(
					pathTransform);
			PathIterator piLeft = paths.get("left").getPathIterator(
					pathTransform);
			
			PathIterator piRightInv = paths.get("rightInv").getPathIterator(
					pathTransform);
			PathIterator piRight = paths.get("right").getPathIterator(
					pathTransform);
			

			double[] startLeft = new double[2];
			double[] startLeftInv = new double[2];
			
			double[] startRight = new double[2];
			double[] startRightInv = new double[2];

			double[] endLeft = new double[2];
			double[] endLeftInv = new double[2];
			
			double[] endRight = new double[2];
			double[] endRightInv = new double[2];
			
			while (!piLeft.isDone() && !piRight.isDone()) {
				piLeft.currentSegment(startLeft);
				piLeftInv.currentSegment(startLeftInv);
				piLeft.next();
				piLeftInv.next();
				piLeft.currentSegment(endLeft);
				piLeftInv.currentSegment(endLeftInv);
				
				piRight.currentSegment(startRight);
				piRightInv.currentSegment(startRightInv);
				piRight.next();
				piRightInv.next();
				piRight.currentSegment(endRight);
				piRightInv.currentSegment(endRightInv);

				Line2D segLeft = new Line2D.Double(startLeft[0], startLeft[1],
						endLeft[0], endLeft[1]);
				Line2D segLeftInv = new Line2D.Double(startLeftInv[0],
						startLeftInv[1], endLeftInv[0], endLeftInv[1]);
				
				Line2D segRight = new Line2D.Double(startRight[0], startRight[1],
						endRight[0], endRight[1]);
				Line2D segLefRight = new Line2D.Double(startRightInv[0],
						startRightInv[1], endRightInv[0], endRightInv[1]);

				if (segLeftInv.intersects(transformedVeh.getBounds2D())) {
					interLeft = true;
				}

				if (segLeft.intersects(transformedVeh.getBounds2D())) {
					outLeft = true;
					break;
				}
				
				if (segLefRight.intersects(transformedVeh.getBounds2D())) {
					interRight = true;
				}

				if (segRight.intersects(transformedVeh.getBounds2D())) {
					outRight = true;
					break;
				}
			}

		} else {
			interLeft = pathTransform.createTransformedShape(
					paths.get("leftInv")).intersects(
					transformedVeh.getBounds2D());
			outLeft = pathTransform.createTransformedShape(paths.get("left"))
					.intersects(transformedVeh.getBounds2D());
			
			interRight = pathTransform.createTransformedShape(
					paths.get("rightInv")).intersects(transformedVeh.getBounds2D());

			outRight = pathTransform.createTransformedShape(
					paths.get("right")).intersects(transformedVeh.getBounds2D());
		}

		boolean interCenter = pathTransform.createTransformedShape(
				paths.get("center")).intersects(transformedVeh.getBounds2D());

		if (interLeft) {
			if (outLeft) {
				latDisLeft = 0;
				latDisRight = 1;
				fronDisLeft = 0;
				fronDisRight = 5;
				devAngle = -10;
			} else {
				latDisLeft = 0.25;
				latDisRight = 0.75;
				fronDisLeft = 5;
				fronDisRight = 10;
				devAngle = -5;
			}
		} else if (interRight) {
			if (outRight) {
				latDisRight = 0;
				latDisLeft = 1;
				fronDisLeft = 5;
				fronDisRight = 0;
				devAngle = 10;
			} else {
				latDisRight = 0.25;
				latDisLeft = 0.75;
				fronDisLeft = 10;
				fronDisRight = 5;
				devAngle = 5;
			}
		} else if (interCenter) {
			latDisRight = 0.5;
			latDisLeft = 0.5;
			fronDisLeft = 15;
			fronDisRight = 15;
			devAngle = 0;
		} else {
			latDisRight = -1;
			latDisLeft = -1;
			fronDisLeft = -1;
			fronDisRight = -1;
			devAngle = -1;
		}

		vehInfo.put("lateralDisRight", new Double(latDisRight));
		vehInfo.put("lateralDisLeft", new Double(latDisLeft));
		vehInfo.put("frontalDisLeft", new Double(fronDisLeft));
		vehInfo.put("frontalDisRight", new Double(fronDisRight));
		vehInfo.put("devAngle", new Double(devAngle));
	}

	public HashMap<String, Object> getVehInfo() {
		return vehInfo;
	}

	private void updatePath(HashMap<String, Path2D> paths) {

		double modVehAngle = this.vehAngle % 360;
		double moveX;
		double moveY;

		moveX = (-1) * this.speed * Math.sin(Math.toRadians(modVehAngle));

		moveY = this.speed * Math.cos(Math.toRadians(modVehAngle));

		String legend = "Rot Angle: " + this.rotationAngle + " Veh Angle: "
				+ this.vehAngle;
		this.pathTransform.translate(moveX, moveY);

		g2d.setColor(Color.YELLOW);
		g2d.draw(paths.get("right").createTransformedShape(this.pathTransform));
		g2d.draw(paths.get("left").createTransformedShape(this.pathTransform));

		if (trees != null) {
			for (int i = 0; i < trees.length; i++) {
				this.treesTransform[i].translate(moveX, moveY);
				g2d.drawImage(trees[i], treesTransform[i], this);
			}
		}
	}

	private void updateVehicle() {
		this.vehAngle = this.vehAngle + this.rotationAngle;
		vehicleTransform.rotate(Math.toRadians(this.rotationAngle),
				imgWidth / 2, imgHeight / 2);
		transformedVeh = vehicleTransform.createTransformedShape(veh);
		g2d.drawImage(img, this.vehicleTransform, this);
	}

	public void setSpeed(double speed) {
		this.speed = ((speed * 1000 / 3600) * 16) / 17;
	}

	public void setRotationAngle(double rotationAngle) {
		if (this.speed > 0) {
			this.rotationAngle = rotationAngle;
		} else {
			this.rotationAngle = 0;
		}
	}

	class Cycle extends TimerTask {
		private SVViewPath pathwin;

		public Cycle(SVViewPath pathwin) {
			this.pathwin = pathwin;
		}

		@Override
		public void run() {
			this.pathwin.repaint();
		}
	}
}
