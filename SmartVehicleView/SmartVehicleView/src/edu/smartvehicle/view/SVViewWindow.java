package edu.smartvehicle.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;

import org.json.JSONObject;

import sun.audio.AudioStream;
import sun.audio.AudioPlayer;
import edu.smartVehicle.view.path.SVViewPath;
import edu.smartVehicle.view.reader.SmartVehicleViewReader;

import java.awt.SystemColor;

/** 
* @author Edith Zavala
*/

public class SVViewWindow extends Observable {

	private JSONObject readings;
	private JSONObject readingsFromSV;
	private JSONObject currentDisplay;

	private JFrame frmSmartVehicle;
	private JPanel panelSetting;
	private JPanel panelPathDriver;
	private JSeparator separator;
	private JSeparator separator_1;
	private JToggleButton tglbtnAwake;
	private JToggleButton tglbtnTired;
	private JToggleButton tglbtnSleeping;
	private JToggleButton tglbtnDangerouslyTired;
	private JButton contPathDriver;
	private JButton contSetting;
	private JLabel lblNowPleaseSelect;
	private final ButtonGroup btnGSeeting = new ButtonGroup();
	private final ButtonGroup btnGPath = new ButtonGroup();
	private final ButtonGroup btnGDriver = new ButtonGroup();
	private JPanel panelDashborad;
	private JLabel lblSpeed;
	private JLabel lblAcce;
	private JLabel lblDec;
	private JLabel lblSWRot;
	private JLabel lblWheelsRot;
	private JLabel lblDevAngle;
	private JLabel lblFrontalDisRight;
	private JLabel lblLateralDisRight;
	private JLabel lblEyesState;
	private JLabel lblFacePos;
	private JLabel lblHbpm;
	private JTextField txtSpeed;
	private JTextField txtWheelsRot;
	private JTextField txtDevAngle;
	private JTextField txtFrontalDisRight;
	private JTextField txtLateralDisRight;
	private JTextField txtEyesState;
	private JTextField txtFacePos;
	private JTextField txtHbpm;
	private JTextField txtFrontalDisLeft;
	private JTextField txtLateralDisLeft;
	private JTextField txtAcce;
	private JTextField txtDec;
	private JTextField txtSWRot;
	private JLabel streetLineLeft;
	private JLabel streetLineRight;
	private JToggleButton tglBSupportLaneK;
	private JToggleButton tglBSeatVibration;
	private JToggleButton tglBSoundLinghtAlarm;
	private JSeparator separator_2;
	private JSlider sldSWRot;
	private JSlider sldAcce;
	private JSlider sldDec;
	private JToggleButton tglbtnStraight;
	private JToggleButton tglbtnCurve;
	private JToggleButton tglbtnManual;
	private JToggleButton tglbtnAutomatic;
	private JCheckBox chBLeftHand;
	private JCheckBox chBRightHand;
	private JLabel lblPleaseSelectA;
	private JToggleButton tglBOnOff;
	private JLabel lblLeftHand;
	private JLabel lblDistances;
	private JLabel label_17;
	private JLabel rightWheel;
	private JLabel barWheels;
	private JLabel leftWheel;
	private JLabel swheel;
	private JLabel lblFrontalDisLeft;
	private JLabel lblLateralDisLeft;
	private JLabel lblRightHand;
	private JLabel leftHand;
	private JLabel rightHand;
	private JCheckBox chBSLKEnabled;
	private JCheckBox chBSeatVEnabled;
	private JCheckBox chBSoundLightEnabled;

	private SmartVehicleViewReader svvr;
	private SVViewPath pathwin;
	private JFrame frame;

	private HashMap<String, Object> vehPosition;
	private AudioStream soundAlarm;
	private AudioStream vibrationAlarm;
	private boolean turnOffSlk;
	private JLabel lblMenu;
	private JLabel vehMenu;
	private JToggleButton tglbtnSensorFault;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.out.println("SVViewWindow: initializing");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SVViewWindow window = new SVViewWindow();
					SmartVehicleViewClient svv = new SmartVehicleViewClient();
					System.out.println("SVViewWindow: add observer");
					window.readings = new JSONObject();
					window.readingsFromSV = new JSONObject();
					window.currentDisplay = new JSONObject();
					window.svvr = null;
					window.addObserver(svv);
					window.frmSmartVehicle.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SVViewWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			InputStream in = new FileInputStream("...\\res\\soundAlarm.wav");
			InputStream in2 = new FileInputStream("...\\res\\vibrationAlarm.wav");
			soundAlarm = new AudioStream(in);
			vibrationAlarm = new AudioStream(in2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		KeyAdapter keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
					sldSWRot.setValue(sldSWRot.getValue() - 5);
				} else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
					sldSWRot.setValue(sldSWRot.getValue() + 5);
				} else if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					sldAcce.setValue(sldAcce.getValue() + 1);
				} else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					sldDec.setValue(sldDec.getValue() + 1);
				} else if (arg0.getKeyCode() == KeyEvent.VK_O) {
					if (tglBOnOff.isSelected()) {
						tglBOnOff.setSelected(false);
					} else {
						tglBOnOff.setSelected(true);
					}
				} else if (arg0.getKeyCode() == KeyEvent.VK_M) {
					if (lblMenu.isEnabled()) {
						panelPathDriver.setVisible(false);
						panelDashborad.setVisible(false);
						panelSetting.setVisible(true);
					}
				}
				updateFromUser();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sldAcce.setValue(0);
				} else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sldDec.setValue(0);
				}
			}
		};

		vehPosition = new HashMap<String, Object>();
		vehPosition.put("lateralDisRight", new Double(0.5));
		vehPosition.put("lateralDisLeft", new Double(0.5));
		vehPosition.put("frontalDisLeft", new Double(15));
		vehPosition.put("frontalDisRight", new Double(15));
		vehPosition.put("devAngle", new Double(0));

		frmSmartVehicle = new JFrame();
		frmSmartVehicle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSmartVehicle.setTitle("SACRE");
		frmSmartVehicle.setBounds(100, 100, 645, 525);
		frmSmartVehicle
				.setIconImage(Toolkit
						.getDefaultToolkit()
						.getImage("...\\res\\swheel.png"));
		frmSmartVehicle.getContentPane().setFont(
				new Font("Arial Narrow", Font.PLAIN, 13));
		frmSmartVehicle.getContentPane().setLayout(null);

		panelPathDriver = new JPanel();
		panelPathDriver.setBackground(new Color(0, 51, 102));
		panelPathDriver.setBounds(0, 0, 637, 498);
		frmSmartVehicle.getContentPane().add(panelPathDriver);
		panelPathDriver.setVisible(false);

		JLabel lblPath = new JLabel("Path");
		lblPath.setForeground(Color.LIGHT_GRAY);
		lblPath.setBounds(129, 78, 43, 26);
		lblPath.setHorizontalAlignment(SwingConstants.CENTER);
		lblPath.setFont(new Font("Dialog", Font.PLAIN, 20));

		JLabel lblDriver = new JLabel("Driver");
		lblDriver.setForeground(Color.LIGHT_GRAY);
		lblDriver.setBounds(432, 75, 81, 32);
		lblDriver.setHorizontalAlignment(SwingConstants.CENTER);
		lblDriver.setFont(new Font("Dialog", Font.PLAIN, 20));

		tglbtnStraight = new JToggleButton("Straight");
		tglbtnStraight.setSelected(true);
		btnGPath.add(tglbtnStraight);
		tglbtnStraight.setBounds(94, 197, 126, 35);
		tglbtnStraight.setFont(new Font("Dialog", Font.PLAIN, 16));

		tglbtnCurve = new JToggleButton("Curved");
		btnGPath.add(tglbtnCurve);
		tglbtnCurve.setBounds(94, 316, 126, 35);
		tglbtnCurve.setFont(new Font("Dialog", Font.PLAIN, 16));

		separator = new JSeparator();
		separator.setBounds(74, 113, 165, 2);

		separator_1 = new JSeparator();
		separator_1.setBounds(358, 113, 227, 2);

		tglbtnAwake = new JToggleButton("Awake");
		tglbtnAwake.setSelected(true);
		btnGDriver.add(tglbtnAwake);
		tglbtnAwake.setBounds(386, 149, 175, 37);
		tglbtnAwake.setFont(new Font("Dialog", Font.PLAIN, 16));

		tglbtnTired = new JToggleButton("Tired");
		btnGDriver.add(tglbtnTired);
		tglbtnTired.setBounds(386, 218, 175, 36);
		tglbtnTired.setFont(new Font("Dialog", Font.PLAIN, 16));

		tglbtnSleeping = new JToggleButton("Asleep");
		btnGDriver.add(tglbtnSleeping);
		tglbtnSleeping.setBounds(386, 351, 175, 37);
		tglbtnSleeping.setFont(new Font("Dialog", Font.PLAIN, 16));

		tglbtnDangerouslyTired = new JToggleButton("Dangerously Tired");
		btnGDriver.add(tglbtnDangerouslyTired);
		tglbtnDangerouslyTired.setBounds(386, 286, 175, 37);
		tglbtnDangerouslyTired.setFont(new Font("Dialog", Font.PLAIN, 16));

		tglbtnSensorFault = new JToggleButton("Sensor Fault");
		btnGDriver.add(tglbtnSensorFault);
		tglbtnSensorFault.setFont(new Font("Dialog", Font.PLAIN, 16));
		tglbtnSensorFault.setBounds(386, 414, 175, 37);

		tglbtnStraight.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglbtnCurve.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglbtnTired.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglbtnSleeping.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglbtnAwake.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglbtnDangerouslyTired.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});
		
		tglbtnSensorFault.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		contPathDriver = new JButton("Continue");
		contPathDriver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelPathDriver.setVisible(false);
				panelDashborad.setVisible(true);
			}
		});
		contPathDriver.setBounds(542, 462, 85, 25);
		contPathDriver.setFont(new Font("Dialog", Font.PLAIN, 12));

		lblNowPleaseSelect = new JLabel(
				"Which path and in which driver's state do you want to drive?");
		lblNowPleaseSelect.setForeground(Color.LIGHT_GRAY);
		lblNowPleaseSelect.setBounds(23, 30, 604, 37);
		lblNowPleaseSelect.setFont(new Font("Dialog", Font.BOLD, 20));
		panelPathDriver.setLayout(null);
		panelPathDriver.add(tglbtnCurve);
		panelPathDriver.add(tglbtnStraight);
		panelPathDriver.add(lblPath);
		panelPathDriver.add(separator);
		panelPathDriver.add(separator_1);
		panelPathDriver.add(tglbtnDangerouslyTired);
		panelPathDriver.add(tglbtnSensorFault);
		panelPathDriver.add(tglbtnTired);
		panelPathDriver.add(tglbtnSleeping);
		panelPathDriver.add(tglbtnAwake);
		panelPathDriver.add(lblDriver);
		panelPathDriver.add(contPathDriver);
		panelPathDriver.add(lblNowPleaseSelect);

		panelDashborad = new JPanel();
		panelDashborad.setBounds(0, 0, 637, 498);
		frmSmartVehicle.getContentPane().add(panelDashborad);
		panelDashborad.setLayout(null);
		panelDashborad.setVisible(false);
		panelDashborad.addKeyListener(keyListener);

		lblSpeed = new JLabel("Speed");
		lblSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeed.setFont(new Font("Dialog", Font.BOLD, 12));
		lblSpeed.setBounds(269, 115, 99, 14);
		panelDashborad.add(lblSpeed);

		lblAcce = new JLabel("Acceleration Rate");
		lblAcce.setHorizontalAlignment(SwingConstants.CENTER);
		lblAcce.setFont(new Font("Dialog", Font.BOLD, 12));
		lblAcce.setBounds(150, 163, 125, 16);
		panelDashborad.add(lblAcce);

		lblDec = new JLabel("Deceleration Rate");
		lblDec.setHorizontalAlignment(SwingConstants.CENTER);
		lblDec.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDec.setBounds(364, 164, 114, 14);
		panelDashborad.add(lblDec);

		lblSWRot = new JLabel("SWheel Rotation Angle");
		lblSWRot.setHorizontalAlignment(SwingConstants.CENTER);
		lblSWRot.setFont(new Font("Dialog", Font.BOLD, 12));
		lblSWRot.setBounds(244, 210, 148, 20);
		panelDashborad.add(lblSWRot);

		lblWheelsRot = new JLabel("Wheels Rotation Angle");
		lblWheelsRot.setMaximumSize(new Dimension(130, 14));
		lblWheelsRot.setHorizontalAlignment(SwingConstants.CENTER);
		lblWheelsRot.setFont(new Font("Dialog", Font.BOLD, 12));
		lblWheelsRot.setBounds(244, 11, 148, 26);
		panelDashborad.add(lblWheelsRot);

		lblDevAngle = new JLabel("Deviation Angle");
		lblDevAngle.setHorizontalAlignment(SwingConstants.CENTER);
		lblDevAngle.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDevAngle.setBounds(528, 210, 99, 17);
		panelDashborad.add(lblDevAngle);

		lblFrontalDisRight = new JLabel("Frontal Distance Right");
		lblFrontalDisRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblFrontalDisRight.setFont(new Font("Dialog", Font.BOLD, 12));
		lblFrontalDisRight.setBounds(0, 239, 147, 14);
		panelDashborad.add(lblFrontalDisRight);

		lblLateralDisRight = new JLabel("Lateral Distance Right");
		lblLateralDisRight.setHorizontalAlignment(SwingConstants.CENTER);
		lblLateralDisRight.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLateralDisRight.setBounds(0, 286, 147, 14);
		panelDashborad.add(lblLateralDisRight);

		lblEyesState = new JLabel("Eyes State");
		lblEyesState.setHorizontalAlignment(SwingConstants.CENTER);
		lblEyesState.setFont(new Font("Dialog", Font.BOLD, 12));
		lblEyesState.setBounds(528, 254, 99, 20);
		panelDashborad.add(lblEyesState);

		lblFacePos = new JLabel("Face Position");
		lblFacePos.setHorizontalAlignment(SwingConstants.CENTER);
		lblFacePos.setFont(new Font("Dialog", Font.BOLD, 12));
		lblFacePos.setBounds(528, 355, 99, 22);
		panelDashborad.add(lblFacePos);

		lblHbpm = new JLabel("Hbpm");
		lblHbpm.setHorizontalAlignment(SwingConstants.CENTER);
		lblHbpm.setFont(new Font("Dialog", Font.BOLD, 12));
		lblHbpm.setBounds(528, 303, 99, 22);
		panelDashborad.add(lblHbpm);

		lblLeftHand = new JLabel("Left Hand");
		lblLeftHand.setHorizontalAlignment(SwingConstants.CENTER);
		lblLeftHand.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLeftHand.setBounds(157, 324, 99, 22);
		panelDashborad.add(lblLeftHand);

		lblDistances = new JLabel("Distances");
		lblDistances.setHorizontalAlignment(SwingConstants.CENTER);
		lblDistances.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDistances.setBounds(0, 212, 147, 16);
		panelDashborad.add(lblDistances);

		txtSpeed = new JTextField();
		txtSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		txtSpeed.setColumns(10);
		txtSpeed.setBounds(269, 132, 99, 20);
		txtSpeed.setText("0.0 km/h");
		txtSpeed.setEditable(false);
		txtSpeed.addKeyListener(keyListener);
		panelDashborad.add(txtSpeed);

		txtWheelsRot = new JTextField();
		txtWheelsRot.setHorizontalAlignment(SwingConstants.CENTER);
		txtWheelsRot.setColumns(10);
		txtWheelsRot.setBounds(269, 37, 99, 20);
		txtWheelsRot.setEditable(false);
		txtWheelsRot.addKeyListener(keyListener);
		panelDashborad.add(txtWheelsRot);

		txtDevAngle = new JTextField();
		txtDevAngle.setHorizontalAlignment(SwingConstants.CENTER);
		txtDevAngle.setColumns(10);
		txtDevAngle.setBounds(528, 231, 99, 20);
		txtDevAngle.setEditable(false);
		txtDevAngle.addKeyListener(keyListener);
		panelDashborad.add(txtDevAngle);

		txtFrontalDisRight = new JTextField();
		txtFrontalDisRight.setHorizontalAlignment(SwingConstants.CENTER);
		txtFrontalDisRight.setColumns(10);
		txtFrontalDisRight.setBounds(30, 259, 91, 20);
		txtFrontalDisRight.setEditable(false);
		txtFrontalDisRight.addKeyListener(keyListener);
		panelDashborad.add(txtFrontalDisRight);

		txtLateralDisRight = new JTextField();
		txtLateralDisRight.setHorizontalAlignment(SwingConstants.CENTER);
		txtLateralDisRight.setColumns(10);
		txtLateralDisRight.setBounds(30, 305, 91, 20);
		txtLateralDisRight.setEditable(false);
		txtLateralDisRight.addKeyListener(keyListener);
		panelDashborad.add(txtLateralDisRight);

		txtEyesState = new JTextField();
		txtEyesState.setHorizontalAlignment(SwingConstants.CENTER);
		txtEyesState.setColumns(10);
		txtEyesState.setBounds(528, 278, 99, 20);
		txtEyesState.setEditable(false);
		txtEyesState.addKeyListener(keyListener);
		panelDashborad.add(txtEyesState);

		txtFacePos = new JTextField();
		txtFacePos.setHorizontalAlignment(SwingConstants.CENTER);
		txtFacePos.setColumns(10);
		txtFacePos.setBounds(528, 378, 99, 20);
		txtFacePos.setEditable(false);
		txtFacePos.addKeyListener(keyListener);
		panelDashborad.add(txtFacePos);

		txtHbpm = new JTextField();
		txtHbpm.setHorizontalAlignment(SwingConstants.CENTER);
		txtHbpm.setColumns(10);
		txtHbpm.setBounds(528, 330, 99, 20);
		txtHbpm.setEditable(false);
		txtHbpm.addKeyListener(keyListener);
		panelDashborad.add(txtHbpm);

		label_17 = new JLabel("");
		label_17.setBounds(0, 209, 147, 14);
		panelDashborad.add(label_17);

		rightWheel = new JLabel("");
		rightWheel
				.setIcon(new ImageIcon(
						"...\\res\\wheel.png"));
		rightWheel.setBounds(418, 11, 114, 128);
		panelDashborad.add(rightWheel);

		barWheels = new JLabel("");
		barWheels
				.setIcon(new ImageIcon(
						"...\\res\\line.png"));
		barWheels.setBounds(183, 71, 265, 14);
		panelDashborad.add(barWheels);

		leftWheel = new JLabel("");
		leftWheel
				.setIcon(new ImageIcon(
						"...\\res\\wheel.png"));
		leftWheel.setBounds(120, 11, 114, 128);
		panelDashborad.add(leftWheel);

		swheel = new JLabel("");
		swheel.setIcon(new ImageIcon(
				"...\\res\\swheel.png"));
		swheel.setHorizontalAlignment(SwingConstants.CENTER);
		swheel.setBounds(244, 286, 148, 130);
		panelDashborad.add(swheel);

		sldSWRot = new JSlider();
		sldSWRot.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (sldSWRot.isValid()) {
					txtSWRot.setText(sldSWRot.getValue() + " º");
				}
			}
		});
		sldSWRot.setMinimum(-270);
		sldSWRot.setMaximum(270);
		sldSWRot.setBounds(254, 258, 128, 24);
		sldSWRot.addKeyListener(keyListener);
		panelDashborad.add(sldSWRot);

		sldAcce = new JSlider();
		sldAcce.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (sldAcce.isValid() && sldAcce.getValue() >= 0) {
					sldDec.setValue(0);
					txtAcce.setText(sldAcce.getValue() + " km/h^2");
					txtDec.setText(sldDec.getValue() + " km/h^2");
					txtSpeed.setText((new Double(
							txtSpeed.getText().split(" ")[0]) + sldAcce
							.getValue())
							+ " km/h");
				}
			}
		});
		sldAcce.setOrientation(SwingConstants.VERTICAL);
		sldAcce.setMaximum(10);
		sldAcce.setBounds(183, 207, 60, 89);
		sldAcce.addKeyListener(keyListener);
		panelDashborad.add(sldAcce);

		sldDec = new JSlider();
		sldDec.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (sldDec.isValid() && sldDec.getValue() >= 0) {
					sldAcce.setValue(0);
					txtAcce.setText(sldAcce.getValue() + " km/h^2");
					txtDec.setText(sldDec.getValue() + " km/h^2");
					txtSpeed.setText((new Double(
							txtSpeed.getText().split(" ")[0]) - sldDec
							.getValue())
							+ " km/h");
				}
			}
		});
		sldDec.setOrientation(SwingConstants.VERTICAL);
		sldDec.setMaximum(10);
		sldDec.setBounds(394, 209, 60, 89);
		sldDec.addKeyListener(keyListener);
		panelDashborad.add(sldDec);

		lblFrontalDisLeft = new JLabel("Frontal Distance Left");
		lblFrontalDisLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblFrontalDisLeft.setFont(new Font("Dialog", Font.BOLD, 12));
		lblFrontalDisLeft.setBounds(0, 338, 147, 14);
		panelDashborad.add(lblFrontalDisLeft);

		txtFrontalDisLeft = new JTextField();
		txtFrontalDisLeft.setHorizontalAlignment(SwingConstants.CENTER);
		txtFrontalDisLeft.setColumns(10);
		txtFrontalDisLeft.setBounds(30, 357, 91, 20);
		txtFrontalDisLeft.setEditable(false);
		txtFrontalDisLeft.addKeyListener(keyListener);
		panelDashborad.add(txtFrontalDisLeft);

		lblLateralDisLeft = new JLabel("Lateral Distance Left");
		lblLateralDisLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblLateralDisLeft.setFont(new Font("Dialog", Font.BOLD, 12));
		lblLateralDisLeft.setBounds(0, 378, 147, 16);
		panelDashborad.add(lblLateralDisLeft);

		txtLateralDisLeft = new JTextField();
		txtLateralDisLeft.setHorizontalAlignment(SwingConstants.CENTER);
		txtLateralDisLeft.setColumns(10);
		txtLateralDisLeft.setBounds(30, 395, 91, 20);
		txtLateralDisLeft.setEditable(false);
		txtLateralDisLeft.addKeyListener(keyListener);
		panelDashborad.add(txtLateralDisLeft);

		lblRightHand = new JLabel("Right Hand");
		lblRightHand.setHorizontalAlignment(SwingConstants.CENTER);
		lblRightHand.setFont(new Font("Dialog", Font.BOLD, 12));
		lblRightHand.setBounds(396, 324, 91, 22);
		panelDashborad.add(lblRightHand);

		chBLeftHand = new JCheckBox("");
		chBLeftHand.setBounds(200, 370, 21, 23);
		chBLeftHand.addKeyListener(keyListener);
		panelDashborad.add(chBLeftHand);

		chBRightHand = new JCheckBox("");
		chBRightHand.setBounds(418, 373, 21, 23);
		chBRightHand.addKeyListener(keyListener);
		panelDashborad.add(chBRightHand);

		txtAcce = new JTextField();
		txtAcce.setHorizontalAlignment(SwingConstants.CENTER);
		txtAcce.setColumns(10);
		txtAcce.setBounds(168, 181, 85, 20);
		txtAcce.setEditable(false);
		txtAcce.addKeyListener(keyListener);
		panelDashborad.add(txtAcce);

		txtDec = new JTextField();
		txtDec.setHorizontalAlignment(SwingConstants.CENTER);
		txtDec.setColumns(10);
		txtDec.setBounds(380, 181, 85, 20);
		txtDec.setEditable(false);
		txtDec.addKeyListener(keyListener);
		panelDashborad.add(txtDec);

		txtSWRot = new JTextField();
		txtSWRot.setHorizontalAlignment(SwingConstants.CENTER);
		txtSWRot.setColumns(10);
		txtSWRot.setBounds(269, 235, 99, 20);
		txtSWRot.setEditable(false);
		txtSWRot.addKeyListener(keyListener);
		panelDashborad.add(txtSWRot);

		leftHand = new JLabel("");
		leftHand.setIcon(new ImageIcon(
				"...\\res\\LeftHand.png"));
		leftHand.setBounds(167, 344, 68, 68);
		panelDashborad.add(leftHand);

		rightHand = new JLabel("");
		rightHand
				.setIcon(new ImageIcon(
						"...\\res\\RightHand.png"));
		rightHand.setBounds(406, 347, 68, 68);
		panelDashborad.add(rightHand);

		streetLineLeft = new JLabel("");
		streetLineLeft
				.setIcon(new ImageIcon(
						"...\\res\\lineStreet.png"));
		streetLineLeft.setBounds(76, 0, 9, 204);
		panelDashborad.add(streetLineLeft);

		streetLineRight = new JLabel("");
		streetLineRight
				.setIcon(new ImageIcon(
						"...\\res\\lineStreet.png"));
		streetLineRight.setBounds(555, 0, 9, 202);
		panelDashborad.add(streetLineRight);

		tglBSupportLaneK = new JToggleButton("Support Lane Keeping");
		tglBSupportLaneK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		tglBSupportLaneK.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (!tglBSupportLaneK.isSelected()) {
					tglBSupportLaneK.setSelected(false);
					sldAcce.setValue(0);
					sldDec.setValue(0);
					turnOffSlk = true;
				}
			}
		});
		tglBSupportLaneK.setBounds(43, 464, 168, 23);
		tglBSupportLaneK.addKeyListener(keyListener);
		panelDashborad.add(tglBSupportLaneK);

		tglBSeatVibration = new JToggleButton("Seat Vibration");
		tglBSeatVibration.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tglBSeatVibration.isSelected()) {
					tglBSeatVibration.setEnabled(true);
				} else {
					tglBSeatVibration.setEnabled(false);
				}
			}
		});
		tglBSeatVibration.setBounds(265, 464, 121, 23);
		tglBSeatVibration.addKeyListener(keyListener);
		panelDashborad.add(tglBSeatVibration);

		tglBSoundLinghtAlarm = new JToggleButton("Sound-Light Alarm");
		tglBSoundLinghtAlarm.setBounds(429, 464, 175, 23);
		tglBSoundLinghtAlarm.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tglBSoundLinghtAlarm.isSelected()) {
					tglBSoundLinghtAlarm.setEnabled(true);
				} else {
					tglBSoundLinghtAlarm.setEnabled(false);
				}
			}
		});
		tglBSoundLinghtAlarm.addKeyListener(keyListener);
		panelDashborad.add(tglBSoundLinghtAlarm);

		tglBSeatVibration.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglBSupportLaneK.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglBSoundLinghtAlarm.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		separator_2 = new JSeparator();
		separator_2.setBounds(0, 231, 148, 8);
		panelDashborad.add(separator_2);

		tglBOnOff = new JToggleButton();
		tglBOnOff.setEnabled(false);
		tglBOnOff.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (tglBOnOff.isSelected()) {
					onState();
				} else {
					if (svvr != null) {
						svvr.destroy();
						svvr = null;
					}
					if (frame != null) {
						frame.dispatchEvent(new WindowEvent(frame,
								WindowEvent.WINDOW_CLOSING));
						frame = null;
					}

					readings = new JSONObject();
					readingsFromSV = new JSONObject();
					currentDisplay = new JSONObject();
					setChanged();
					notifyObservers();
					offState();
				}
			}
		});
		tglBOnOff.setBounds(288, 409, 60, 20);
		tglBOnOff.addKeyListener(keyListener);
		panelDashborad.add(tglBOnOff);

		chBSLKEnabled = new JCheckBox("Enabled");
		chBSLKEnabled.addKeyListener(keyListener);
		chBSLKEnabled.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (!chBSLKEnabled.isSelected()) {
					tglBSupportLaneK.setSelected(false);
					tglBSupportLaneK.setEnabled(false);
				} else {
					tglBSupportLaneK.setEnabled(true);
				}
			}
		});
		chBSLKEnabled.setHorizontalAlignment(SwingConstants.CENTER);
		chBSLKEnabled.setBounds(76, 434, 97, 23);
		panelDashborad.add(chBSLKEnabled);

		chBSeatVEnabled = new JCheckBox("Enabled");
		chBSeatVEnabled.addKeyListener(keyListener);
		chBSeatVEnabled.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!chBSeatVEnabled.isSelected()) {
					tglBSeatVibration.setSelected(false);
					tglBSeatVibration.setEnabled(false);
				}
			}
		});
		chBSeatVEnabled.setHorizontalAlignment(SwingConstants.CENTER);
		chBSeatVEnabled.setBounds(279, 436, 97, 23);
		panelDashborad.add(chBSeatVEnabled);

		chBSoundLightEnabled = new JCheckBox("Enabled");
		chBSoundLightEnabled.addKeyListener(keyListener);
		chBSoundLightEnabled.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!chBSoundLightEnabled.isSelected()) {
					tglBSoundLinghtAlarm.setSelected(false);
					tglBSoundLinghtAlarm.setEnabled(false);
				}
			}
		});
		chBSoundLightEnabled.setHorizontalAlignment(SwingConstants.CENTER);
		chBSoundLightEnabled.setBounds(466, 436, 97, 23);
		panelDashborad.add(chBSoundLightEnabled);

		vehMenu = new JLabel("");
		vehMenu.setIcon(new ImageIcon(
				"...\\res\\startMenu.png"));
		vehMenu.setBounds(0, 11, 49, 26);
		panelDashborad.add(vehMenu);

		lblMenu = new JLabel("Menu");
		lblMenu.setFont(new Font("Dialog", Font.BOLD, 12));
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenu.setBounds(0, 38, 54, 17);
		panelDashborad.add(lblMenu);

		offState();

		/*********************** PanelDashboard *********************************************/
		panelSetting = new JPanel();
		panelSetting.setBackground(new Color(0, 51, 102));
		panelSetting.setBounds(0, 0, 637, 498);
		frmSmartVehicle.getContentPane().add(panelSetting);

		tglbtnManual = new JToggleButton("Manual");
		tglbtnManual.setSelected(true);
		btnGSeeting.add(tglbtnManual);
		tglbtnManual.setBounds(413, 239, 153, 41);
		tglbtnManual.setFont(new Font("Dialog", Font.PLAIN, 25));

		tglbtnAutomatic = new JToggleButton("Automatic");
		btnGSeeting.add(tglbtnAutomatic);
		tglbtnAutomatic.setBounds(413, 317, 153, 41);
		tglbtnAutomatic.setFont(new Font("Dialog", Font.PLAIN, 25));

		tglbtnAutomatic.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		tglbtnManual.setUI(new MetalToggleButtonUI() {
			@Override
			protected Color getSelectColor() {
				return Color.YELLOW;
			}
		});

		contSetting = new JButton("Continue");
		contSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelSetting.setVisible(false);
				panelPathDriver.setVisible(true);
			}
		});
		contSetting.setBounds(542, 462, 85, 25);
		contSetting.setFont(new Font("Dialog", Font.PLAIN, 12));

		JLabel lblWelcomToSacre = new JLabel("Welcome to SACRE!");
		lblWelcomToSacre.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomToSacre.setForeground(new Color(230, 230, 250));
		lblWelcomToSacre.setBounds(212, 11, 212, 26);
		lblWelcomToSacre.setFont(new Font("Dialog", Font.BOLD, 20));
		panelSetting.setLayout(null);
		panelSetting.add(contSetting);
		panelSetting.add(tglbtnManual);
		panelSetting.add(tglbtnAutomatic);
		panelSetting.add(lblWelcomToSacre);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(
				"...\\res\\blackVehicle.png"));
		label.setBounds(-48, 124, 423, 315);
		panelSetting.add(label);

		lblPleaseSelectA = new JLabel(
				"How do you want to drive the smart vehicle?");
		lblPleaseSelectA.setForeground(Color.LIGHT_GRAY);
		lblPleaseSelectA.setFont(new Font("Dialog", Font.BOLD, 20));
		lblPleaseSelectA.setBounds(39, 57, 577, 33);
		panelSetting.add(lblPleaseSelectA);

		frmSmartVehicle.setFocusable(true);
		frmSmartVehicle.requestFocusInWindow();
		frmSmartVehicle.addKeyListener(keyListener);
		frmSmartVehicle.setLocation(0, 0);
	}

	private void onState() {
		String path = "";
		String driver = "";
		String setting = "";
		tglBOnOff.setText("Off");
		tglBOnOff.setSelected(true);
		turnOffSlk = false;
		lblMenu.setEnabled(false);
		vehMenu.setEnabled(false);

		if (tglbtnAwake.isSelected()) {
			driver = "awake";
		} else if (tglbtnTired.isSelected()) {
			driver = "tired";
		} else if (tglbtnDangerouslyTired.isSelected()) {
			driver = "dTired";
		} else if(tglbtnSleeping.isSelected()){
			driver = "asleep";
		}else{
			driver = "awake-e";
		}

		svvr = new SmartVehicleViewReader(SVViewWindow.this, driver);
		pathwin = new SVViewPath(path, this);
		frame = new JFrame("SACRE");
		frame.setContentPane(pathwin);
		frame.pack();
		frame.setLocation(650, 100);
		frame.setVisible(true);

		separator_2.setEnabled(true);
		lblSpeed.setEnabled(true);
		lblAcce.setEnabled(true);
		lblDec.setEnabled(true);
		tglBSoundLinghtAlarm.setEnabled(false);
		tglBSeatVibration.setEnabled(false);
		tglBSupportLaneK.setEnabled(true);
		streetLineRight.setEnabled(true);
		streetLineLeft.setEnabled(true);
		rightHand.setEnabled(true);
		leftHand.setEnabled(true);
		txtSWRot.setEnabled(true);
		txtDec.setEnabled(true);
		txtAcce.setEnabled(true);
		lblRightHand.setEnabled(true);
		txtLateralDisLeft.setEnabled(true);
		lblLateralDisLeft.setEnabled(true);
		lblFrontalDisLeft.setEnabled(true);
		swheel.setEnabled(true);
		leftWheel.setEnabled(true);
		barWheels.setEnabled(true);
		label_17.setEnabled(true);
		rightWheel.setEnabled(true);
		txtHbpm.setEnabled(true);
		txtFacePos.setEnabled(true);
		txtEyesState.setEnabled(true);
		txtLateralDisRight.setEnabled(true);
		lblSWRot.setEnabled(true);
		lblWheelsRot.setEnabled(true);
		lblDevAngle.setEnabled(true);
		lblFrontalDisRight.setEnabled(true);
		lblLateralDisRight.setEnabled(true);
		lblEyesState.setEnabled(true);
		lblFacePos.setEnabled(true);
		lblHbpm.setEnabled(true);
		lblLeftHand.setEnabled(true);
		lblDistances.setEnabled(true);
		txtSpeed.setEnabled(true);
		txtWheelsRot.setEnabled(true);
		txtDevAngle.setEnabled(true);
		txtFrontalDisRight.setEnabled(true);
		txtFrontalDisLeft.setEnabled(true);
		chBSLKEnabled.setEnabled(true);
		chBSeatVEnabled.setEnabled(true);
		chBSoundLightEnabled.setEnabled(true);
		chBSLKEnabled.setSelected(true);
		chBSeatVEnabled.setSelected(true);
		chBSoundLightEnabled.setSelected(true);
	}

	private void offState() {
		tglBOnOff.setText("On");
		tglBOnOff.setSelected(false);
		turnOffSlk = false;
		lblMenu.setEnabled(true);
		vehMenu.setEnabled(true);

		sldSWRot.setValue(0);
		sldAcce.setValue(0);
		sldDec.setValue(0);

		separator_2.setEnabled(false);
		lblSpeed.setEnabled(false);
		lblAcce.setEnabled(false);
		lblDec.setEnabled(false);

		tglBSoundLinghtAlarm.setEnabled(false);
		tglBSoundLinghtAlarm.setSelected(false);
		tglBSeatVibration.setEnabled(false);
		tglBSeatVibration.setSelected(false);
		tglBSupportLaneK.setEnabled(false);
		tglBSupportLaneK.setSelected(false);

		chBSLKEnabled.setEnabled(false);
		chBSLKEnabled.setSelected(false);
		chBSeatVEnabled.setEnabled(false);
		chBSeatVEnabled.setSelected(false);
		chBSoundLightEnabled.setEnabled(false);
		chBSoundLightEnabled.setSelected(false);

		streetLineRight.setEnabled(false);
		streetLineLeft.setEnabled(false);
		rightHand.setEnabled(false);
		leftHand.setEnabled(false);

		txtSWRot.setEnabled(false);
		txtSWRot.setText("");
		txtDec.setEnabled(false);
		txtDec.setText("");
		txtAcce.setEnabled(false);
		txtAcce.setText("");

		chBRightHand.setEnabled(false);
		chBRightHand.setSelected(false);
		chBLeftHand.setEnabled(false);
		chBLeftHand.setSelected(false);

		lblRightHand.setEnabled(false);
		lblLateralDisLeft.setEnabled(false);
		lblFrontalDisLeft.setEnabled(false);

		sldDec.setEnabled(false);
		sldAcce.setEnabled(false);
		sldSWRot.setEnabled(false);

		swheel.setEnabled(false);
		leftWheel.setEnabled(false);
		barWheels.setEnabled(false);
		label_17.setEnabled(false);
		rightWheel.setEnabled(false);

		txtHbpm.setEnabled(false);
		txtHbpm.setText("");
		txtFacePos.setEnabled(false);
		txtFacePos.setText("");
		txtEyesState.setEnabled(false);
		txtEyesState.setText("");
		txtLateralDisRight.setEnabled(false);
		txtLateralDisRight.setText("");
		txtLateralDisLeft.setEnabled(false);
		txtLateralDisLeft.setText("");

		lblSWRot.setEnabled(false);
		lblWheelsRot.setEnabled(false);
		lblDevAngle.setEnabled(false);
		lblFrontalDisRight.setEnabled(false);
		lblLateralDisRight.setEnabled(false);
		lblEyesState.setEnabled(false);
		lblFacePos.setEnabled(false);
		lblHbpm.setEnabled(false);
		lblLeftHand.setEnabled(false);
		lblDistances.setEnabled(false);

		txtSpeed.setEnabled(false);
		txtSpeed.setText("");
		txtWheelsRot.setEnabled(false);
		txtWheelsRot.setText("");
		txtDevAngle.setEnabled(false);
		txtDevAngle.setText("");
		txtFrontalDisRight.setEnabled(false);
		txtFrontalDisRight.setText("");
		txtFrontalDisLeft.setEnabled(false);
		txtFrontalDisLeft.setText("");
	}

	synchronized private void updateDisplay(String entity) {
		if (this.readings.length() == 0) {
			return;
		}

		if (turnOffSlk && !this.txtSpeed.getText().isEmpty()) {
			turnOffSlk = false;
			this.currentDisplay.put("supportLaneKeeping",
					this.tglBSupportLaneK.isSelected());
			this.currentDisplay.put("accelerationRate", 0);
			setChanged();
			notifyObservers();
			return;
		}

		if (!(this.pathwin.getVehInfo()).isEmpty()) {
			this.vehPosition = this.pathwin.getVehInfo();
		}

		Double frontalDisRight = new Double(
				(this.vehPosition.get("frontalDisRight")).toString());
		Double frontalDisLeft = new Double(
				(this.vehPosition.get("frontalDisLeft")).toString());
		Double lateralDisRight = new Double(
				(this.vehPosition.get("lateralDisRight")).toString());
		Double lateralDisLeft = new Double(
				(this.vehPosition.get("lateralDisLeft")).toString());
		Double devAngle = new Double(
				(this.vehPosition.get("devAngle")).toString());
		Double acceleration;
		Double swRot;
		Double speed;

		/****************************** Extract current values from display ***********************/
		if (this.txtAcce.getText().isEmpty() || this.txtDec.getText().isEmpty()) {
			acceleration = 0.0;
			sldAcce.setValue(0);
			sldDec.setValue(0);
			txtAcce.setText("0.0 km/h^2");
			txtDec.setText("0.0 km/h^2");
		} else {
			acceleration = new Double(sldAcce.getValue() - sldDec.getValue());
		}

		if (this.txtSWRot.getText().isEmpty()) {
			swRot = 0.0;
			this.txtSWRot.setText("0 º");
			sldSWRot.setValue(0);
		} else {
			swRot = new Double(this.txtSWRot.getText().split(" ")[0]);
		}

		this.txtWheelsRot.setText((Math.floor((swRot / 5) * 100) / 100) + " º");

		if (this.txtSpeed.getText().isEmpty()) {
			speed = 0.0;
			this.txtSpeed.setText("0.0 km/hr");
		} else {
			speed = new Double(this.txtSpeed.getText().split(" ")[0]);
		}

		Boolean empty = this.readingsFromSV.isNull("accelerationRate");

		/******************** Seat Vibration Alarm *********************/

		if (this.chBSeatVEnabled.isSelected() && !entity.equals("user")) {
			if (!empty) {
				Boolean seatVSV = new Boolean(
						Boolean.parseBoolean(this.readingsFromSV.get(
								"seatVibration").toString()));

				this.tglBSeatVibration.setSelected(seatVSV);
			}
		}

		/******************** Sound-Light Alarm *********************/

		if (this.chBSoundLightEnabled.isSelected() && !entity.equals("user")) {
			if (!empty) {
				Boolean soundLightSV = new Boolean(
						Boolean.parseBoolean(this.readingsFromSV.get(
								"lightAlarm").toString())
								&& Boolean.parseBoolean(this.readingsFromSV
										.get("soundAlarm").toString()));
				if (soundLightSV) {
					this.tglBSeatVibration.setSelected(false);
				}
				this.tglBSoundLinghtAlarm.setSelected(soundLightSV);
			}
		}

		/******************** SLK *********************/

		if (this.chBSLKEnabled.isSelected() && !entity.equals("user")) {
			if (!empty && !this.tglBSupportLaneK.isSelected()) {
				boolean slkState = Boolean.parseBoolean(this.readingsFromSV
						.get("supportLaneKeeping").toString());
				if (slkState) {
					this.tglBSeatVibration.setSelected(false);
					this.tglBSoundLinghtAlarm.setSelected(false);
				}
				this.tglBSupportLaneK.setSelected(slkState);
			}
		}

		Boolean supportLaneK = this.tglBSupportLaneK.isSelected();

		/*********************** Decide from where take information ***************************/
		if (supportLaneK && !empty) {
			if (entity.equals("sv")) {
				acceleration = new Double(
						Double.parseDouble(this.readingsFromSV.get(
								"accelerationRate").toString()));
				swRot = new Double(Double.parseDouble(this.readingsFromSV.get(
						"swheelRotation").toString()));

				double speedTemp = speed + acceleration;
				setVehicleAttr(speedTemp, swRot);
				if (acceleration < 0) {
					this.sldAcce.setValue(0);
					this.txtAcce.setText("0.0 km/h^2");
					this.sldDec.setValue(acceleration.intValue() * (-1));
					this.txtDec.setText(acceleration + " km/h^2");
				} else {
					this.sldDec.setValue(0);
					this.txtDec.setText("0.0 km/h^2");
					this.sldAcce.setValue(acceleration.intValue());
					this.txtAcce.setText(acceleration + " km/h^2");
				}
				this.txtSpeed.setText(speed + acceleration + " km/h");
				this.sldSWRot.setValue(swRot.intValue());
				this.txtSWRot.setText(Math.floor(swRot * 100) / 100 + " º");
				this.txtWheelsRot.setText((Math.floor((swRot / 5) * 100) / 100)
						+ " º");
			} else {
				acceleration = 0.0;
				swRot = 0.0 + swRot.intValue();
				double speedTemp = speed + acceleration;
				setVehicleAttr(speedTemp, swRot);
				if (acceleration < 0) {
					this.sldAcce.setValue(0);
					this.txtAcce.setText("0.0 km/h^2");
					this.sldDec.setValue(acceleration.intValue() * (-1));
					this.txtDec.setText(acceleration + " km/h^2");
				} else {
					this.sldDec.setValue(0);
					this.txtDec.setText("0.0 km/h^2");
					this.sldAcce.setValue(acceleration.intValue());
					this.txtAcce.setText(acceleration + " km/h^2");
				}
				this.txtSpeed.setText(speed + acceleration + " km/h");
				this.sldSWRot.setValue(swRot.intValue());
				this.txtSWRot.setText(Math.floor(swRot * 100) / 100 + " º");
				this.txtWheelsRot.setText((Math.floor((swRot / 5) * 100) / 100)
						+ " º");
			}
		} else {
			if (entity.equals("file")) {
				if (tglbtnAutomatic.isSelected()) {
					if (devAngle != 0) {
						if (speed > 20 && acceleration == 0.0) {
							acceleration = -1.0;
						} else {
							acceleration = 0.0;
						}
						swRot = devAngle * (-1);
					} else {
						if (speed < 30 && acceleration == 0.0) {
							acceleration = 1.0;
						} else if (speed > 30 && acceleration == 0.0) {
							acceleration = -1.0;
						} else {
							acceleration = 0.0;
						}
						swRot = 0.0;
					}
				}
			} else if (entity.equals("user")) {
				if (sldAcce.getValue() == 0) {
					acceleration = (new Double(sldDec.getValue())) * (-1);
				} else {
					acceleration = new Double(sldAcce.getValue());
				}
				swRot = new Double(sldSWRot.getValue());
			}

			if ((tglbtnAutomatic.isSelected() && entity.equals("file"))
					|| entity.equals("user")) {
				double speedTemp = speed + acceleration;
				setVehicleAttr(speedTemp, swRot);

				if (acceleration < 0) {
					this.sldAcce.setValue(0);
					this.sldDec.setValue(acceleration.intValue() * (-1));
				} else {
					this.sldDec.setValue(0);
					this.sldAcce.setValue(acceleration.intValue());
				}
				this.sldSWRot.setValue(swRot.intValue());
			}
		}

		/************************************** Driver information ******************************************/

		if (Double.parseDouble(this.readings.get("eyesState").toString()) == -1.0) {
			this.txtEyesState.setText("Unknown");
		} else if (Double
				.parseDouble(this.readings.get("eyesState").toString()) < 0.40) {
			this.txtEyesState.setText("Closed");
		} else {
			this.txtEyesState.setText("Open");
		}

		if (Double.parseDouble(this.readings.get("facePosition").toString()) == -1.0) {
			this.txtFacePos.setText("Unknown");
		} else if (Double.parseDouble(this.readings.get("facePosition")
				.toString()) == 1) {
			this.txtFacePos.setText("Distracted");
		} else {
			this.txtFacePos.setText("Attentive");
		}

		if (Double.parseDouble(this.readings.get("heartBeatsPerMinute")
				.toString()) == -1.0) {
			this.txtHbpm.setText("Unknown");
		} else {
			this.txtHbpm.setText(this.readings.get("heartBeatsPerMinute")
					.toString());
		}

		this.chBLeftHand.setSelected(Boolean.parseBoolean(this.readings.get(
				"leftHand").toString()));
		this.chBRightHand.setSelected(Boolean.parseBoolean(this.readings.get(
				"rightHand").toString()));

		/**************************** Vehicle information **************************************/

		lateralDisRight = new Double(vehPosition.get("lateralDisRight")
				.toString());
		lateralDisLeft = new Double(vehPosition.get("lateralDisLeft")
				.toString());

		frontalDisLeft = new Double(vehPosition.get("frontalDisLeft")
				.toString());
		frontalDisRight = new Double(vehPosition.get("frontalDisRight")
				.toString());
		devAngle = new Double(vehPosition.get("devAngle").toString());

		if (frontalDisRight == -1) {
			this.txtFrontalDisRight.setText("Unknown");
		} else if (frontalDisRight > 10) {
			this.txtFrontalDisRight.setText("infinite");
		} else {
			this.txtFrontalDisRight
					.setText((Math.floor(frontalDisRight * 100) / 100) + " m");
		}

		if (frontalDisLeft == -1) {
			this.txtFrontalDisLeft.setText("Unknown");
		} else if (frontalDisLeft > 10) {
			this.txtFrontalDisLeft.setText("infinite");
		} else {
			this.txtFrontalDisLeft
					.setText((Math.floor(frontalDisLeft * 100) / 100) + " m");
		}

		if (devAngle == -1) {
			this.txtDevAngle.setText("Unknown");
		} else {
			this.txtDevAngle.setText((Math.floor(devAngle * 100) / 100) + " º");
		}

		if (lateralDisRight == -1 && lateralDisLeft == -1) {
			this.txtLateralDisRight.setText("Unknown");
			this.txtLateralDisLeft.setText("Unknown");
		} else {
			this.txtLateralDisLeft.setText((Math
					.floor((lateralDisLeft * 100) * 100) / 100) + " cm");
			this.txtLateralDisRight.setText((Math
					.floor((lateralDisRight * 100) * 100) / 100) + " cm");
		}

		/******************************** Build output JSONObject ******************************************/
		this.currentDisplay.put("speed", speed);
		this.currentDisplay.put("accelerationRate", acceleration);
		this.currentDisplay.put("swheelRotation", swRot);
		this.currentDisplay.put("wheelsRotation", swRot / 5);
		this.currentDisplay.put("deviationAngle", devAngle);
		this.currentDisplay.put("frontalDistanceRight", frontalDisRight);
		this.currentDisplay.put("lateralDistanceRight", lateralDisRight);
		this.currentDisplay.put("frontalDistanceLeft", frontalDisLeft);
		this.currentDisplay.put("lateralDistanceLeft", lateralDisLeft);

		this.currentDisplay.put("seatVibration",
				this.tglBSeatVibration.isSelected());
		this.currentDisplay.put("soundAlarm",
				this.tglBSoundLinghtAlarm.isSelected());
		this.currentDisplay.put("lightAlarm",
				this.tglBSoundLinghtAlarm.isSelected());
		this.currentDisplay.put("supportLaneKeeping", supportLaneK);

		this.currentDisplay.put("seatVibrationE",
				this.chBSeatVEnabled.isSelected());
		this.currentDisplay.put("soundAlarmE",
				this.chBSoundLightEnabled.isSelected());
		this.currentDisplay.put("lightAlarmE",
				this.chBSoundLightEnabled.isSelected());
		this.currentDisplay.put("supportLaneKeepingE",
				this.chBSLKEnabled.isSelected());

		this.currentDisplay.put("eyesState", this.readings.get("eyesState"));
		this.currentDisplay.put("facePosition",
				this.readings.get("facePosition"));
		this.currentDisplay.put("heartBeatsPerMinute",
				this.readings.get("heartBeatsPerMinute"));
		this.currentDisplay.put("leftHand", this.readings.get("leftHand"));
		this.currentDisplay.put("rightHand", this.readings.get("rightHand"));

		if (!entity.equals("sv")) {
			setChanged();
			notifyObservers();
		}
	}

	private void setVehicleAttr(double speed, double angle) {
		this.pathwin.setSpeed(speed);
		this.pathwin.setRotationAngle(angle / 5);
	}

	public void updateFromSV(JSONObject readingsFromSV) {
		this.readingsFromSV = readingsFromSV;
		if (tglBOnOff.isSelected()) {
			System.out.println("SVViewWindow: Updated by SV "
					+ this.readingsFromSV);
			updateDisplay("sv");
		}
	}

	public void updateFromFile(JSONObject srsVal) {
		this.readings = srsVal;
		System.out.println("SVViewWindow: Updated by File " + this.readings);
		updateDisplay("file");
	}

	private void updateFromUser() {
		if (tglBOnOff.isSelected()) {
			System.out.println("SVViewWindow: Updated by User ");
			updateDisplay("user");
		}
	}

	public JSONObject getReadings() {
		System.out.println("SmartVehicleViewWindow: providing readings");
		return this.currentDisplay;
	}
}
