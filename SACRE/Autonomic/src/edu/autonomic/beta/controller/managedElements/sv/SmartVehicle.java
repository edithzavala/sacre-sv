package edu.autonomic.beta.controller.managedElements.sv;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import edu.autonomic.beta.controller.dB.DBSVController;
import edu.autonomic.beta.controller.documents.IDocument;
import edu.autonomic.beta.controller.documentsImp.ctxReqOpeCtx.ContextReqOpeContext;
import edu.autonomic.beta.controller.documentsImp.svContext.SVContext;
import edu.autonomic.beta.controller.functions.Spliter;
import edu.autonomic.beta.controller.mapek.IComponent;
import edu.autonomic.beta.controller.smartVehicleBehavior.ASmartVehicle;
import edu.autonomic.beta.controller.smartVehicleComponents.AlarmBeep;
import edu.autonomic.beta.controller.smartVehicleComponents.AlarmLED;
import edu.autonomic.beta.controller.smartVehicleComponents.DriverSeat;
import edu.autonomic.beta.controller.smartVehicleComponents.SensorSVECG;
import edu.autonomic.beta.controller.smartVehicleComponents.SensorSVLaneCamera;
import edu.autonomic.beta.controller.smartVehicleComponents.SensorSVSteeringWheel;
import edu.autonomic.beta.controller.smartVehicleComponents.SensorSVSunVisorCamera;
import edu.autonomic.beta.controller.smartVehicleComponents.SteeringWheel;
import edu.autonomic.beta.controller.smartVehicleComponents.Wheel;
import edu.autonomic.beta.controller.touchpointsImp.sensors.SensorsSV;
import edu.autonomic.beta.model.DAO.Behaviors;
import edu.autonomic.beta.model.DAO.Contexts;
import edu.autonomic.beta.model.DAO.ContextualRequirements;
import edu.autonomic.beta.model.DAO.Sensors;
import edu.autonomic.beta.model.DAO.Variables;

/** 
* @author Edith Zavala
*/

public class SmartVehicle extends ASmartVehicle implements IComponent {

	private Wheel[] wheels;
	private SteeringWheel swheel;

	private DriverSeat dSeat;
	private AlarmLED alarmLED;
	private AlarmBeep alarmBeep;
	private boolean laneKeepingSupport;
	private boolean laneKeepingSupportEnable;

	private SensorSVLaneCamera laneRightCamera;
	private SensorSVLaneCamera laneLeftCamera;
	private SensorSVSunVisorCamera sunVisorCamera;
	private SensorSVSteeringWheel pressureSWheel;
	private SensorSVECG dECG;

	private ArrayList<IComponent> observers;
	private State state;
	private String name;
	private DBSVController dbc;
	private SVContext svCtx;

	private long interReadingsTime;
	private ArrayList<Contexts> ctx;
	private ArrayList<Behaviors> beh;
	private ArrayList<ContextualRequirements> cr;
	private ArrayList<Sensors> srs;
	private ArrayList<Variables> var;
	private HashMap<String, Object> ope;
	private HashMap<String, String> srsVarsId;
	private HashMap<String, Object> srsVarsValues;

	private SmartVehicleClient svc;

	public SmartVehicle(State state, String name, String fileName) {
		this.state = state;
		this.name = name;
		this.observers = new ArrayList<IComponent>();
		this.dbc = new DBSVController();
		this.svCtx = new SVContext();
		this.ctx = new ArrayList<Contexts>();
		this.beh = new ArrayList<Behaviors>();
		this.cr = new ArrayList<ContextualRequirements>();
		this.srs = new ArrayList<Sensors>();
		this.var = new ArrayList<Variables>();
		this.ope = new HashMap<String, Object>();
		this.srsVarsValues = new HashMap<String, Object>();
		this.srsVarsId = new HashMap<String, String>();

		this.speed = 0;
		this.accelerationRate = 0;
		this.numberOfWheels = 4;
		this.wheels = new Wheel[this.numberOfWheels];
		for (int i = 0; i < this.wheels.length; i++) {
			this.wheels[i] = new Wheel();
		}
		this.swheel = new SteeringWheel();

		this.dSeat = new DriverSeat();
		this.alarmLED = new AlarmLED();
		this.alarmBeep = new AlarmBeep();
		this.laneKeepingSupport = false;
		laneKeepingSupportEnable = true;

		this.laneRightCamera = new SensorSVLaneCamera();
		this.laneLeftCamera = new SensorSVLaneCamera();
		this.sunVisorCamera = new SensorSVSunVisorCamera();
		this.pressureSWheel = new SensorSVSteeringWheel();
		this.dECG = new SensorSVECG();

		this.svc = new SmartVehicleClient(this);
		initSVConfig(fileName);
	}

	private void initSVConfig(String fileName) {
		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream(fileName);

		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.interReadingsTime = Long.parseLong(prop
				.getProperty("interReadingsTime"));
		String[] vars = Spliter.split(prop.getProperty("vars"), ",");
		for (int i = 0; i < vars.length; i++) {
			this.srsVarsId.put(vars[i], prop.getProperty(vars[i]));
		}
	}

	@Override
	public void notifyState() {
		ArrayList<IComponent> copyObservers;
		SVContext copysvCtx;
		synchronized (this) {
			copyObservers = new ArrayList<IComponent>(this.observers);
			copysvCtx = this.svCtx;

		}
		Iterator<IComponent> i = copyObservers.iterator();
		while (i.hasNext()) {
			IComponent aux = i.next();
			String type = (aux.getClass().toString());
			/* TODO: Next line need to be revised and simplified */
			if (!copysvCtx.isEmpty()
					&& type.equals("class edu.autonomic.beta.controller.touchpointsImp.sensors.SensorsSV")) {
				aux.notified(this.name, this.state, copysvCtx);
			} else if (copysvCtx.isEmpty()) {
				aux.notified(this.name, this.state, null);
			}
		}
		return;
	}

	
	public void calculateVars() {
		ArrayList<IComponent> copyObservers;
		SVContext copysvCtx;
		synchronized (this) {
			copyObservers = new ArrayList<IComponent>(this.observers);
			copysvCtx = this.svCtx;

		}
		Iterator<IComponent> i = copyObservers.iterator();
		while (i.hasNext()) {
			IComponent aux = i.next();
			String type = (aux.getClass().toString());
			/* TODO: Next line need to be revised and simplified */
			if (!copysvCtx.isEmpty()
					&& type.equals("class edu.autonomic.beta.controller.touchpointsImp.sensors.SensorsSV")) {
				((SensorsSV)aux).calculateVariables(copysvCtx);
			}
		}
		return;
	}

	public void notifiedResponse(HashMap<String, Object> srsVarsValues) {
		this.srsVarsValues = srsVarsValues;
		if(!this.srsVarsValues.isEmpty()){
			SmartVehicleUpdate.updateState(this);
		}
	}
	
	private void getContext() {
		svCtx.getSensorCtx().setSensors(readSensors());
		svCtx.getBehaviorCtx().setBehaviors(readBehaviors());
		calculateVars();
		svCtx.getSensorCtx().setSensors(readSensors());
		svCtx.getBehaviorCtx().setBehaviors(readBehaviors());
		notifyState();
	}

	private HashMap<String, Object> readBehaviors() {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		for (int i = 0; i < beh.size(); i++) {
			boolean behavior = false;
			switch (beh.get(i).getBehaviorDescription()) {
				case "Activate Seat Vibration":
					behavior = this.dSeat.isVibrating();
					break;
				case "Activate Sound/Light Alert":
					behavior = (this.alarmLED.getState() && this.alarmBeep
							.getState());
					break;
				case "Support Lane Keeping":
					behavior = this.laneKeepingSupport;
					break;
			}
			hm.put(beh.get(i).getBehaviorId(), new Boolean(behavior));
		}
		return hm;
	}

	private HashMap<String, Object> readSensors() {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		for (int i = 0; i < var.size(); i++) {
			Variables v = var.get(i);
			Object value = null;
			for (int j = 0; j < srs.size(); j++) {
				if (srs.get(j).getSensorId().equals(v.getSensorId())) {
					Sensors s = srs.get(j);
					switch (s.getSensorType()) {
					case "Steering Wheel":
						value = this.pressureSWheel.getValue(v.getVarName());
						break;
					case "Sun Visor Camera":
						value = this.sunVisorCamera.getValue(v.getVarName());
						break;
					case "ECG":
						value = this.dECG.getValue(v.getVarName());
						break;
					}
					hm.put(v.getVarName(), value);
				}
			}
		}
		return hm;
	}

	@Override
	public void setAdaptationActions(String param) {
		System.out.println(this.name + ": is adapting according to " + param);
	}

	@Override
	public void attachObserver(IComponent mc) {
		observers.add(mc);

	}

	@Override
	public void deattachObserver(IComponent mc) {
		int idx = observers.indexOf(mc);
		observers.remove(idx);

	}

	@Override
	public void notified(String name, State state, IDocument doc) {
		if (doc != null) {
			this.ope = ((ContextReqOpeContext) doc).getOperationalizations()
					.getOperationalizations();
		}
	}

	public void startDB() throws IOException {
		initDB("/ISmartVehicle.txt");
	}

	public void start() throws IOException {
		Timer timer = new Timer();
		timer.schedule(new Cycle(this), new Date(), interReadingsTime);
	}

	private void initDB(String file) throws IOException {
		dbc.initDB(file, ctx, beh, cr, srs, var);
	}

	@Override
	public void accelerate(Double accelerationRate) {
		this.speed = this.speed + accelerationRate.doubleValue();
		this.accelerationRate = accelerationRate.doubleValue();
	}

	@Override
	public void brake(Double decelerationRate) {
		this.speed = this.speed - decelerationRate.doubleValue();
		this.accelerationRate = decelerationRate.doubleValue() * (-1);
	}

	public void setAccelerationRate(Object val) {
		this.accelerationRate = Double.parseDouble(val.toString());
	}

	public double getAccelerationRate() {
		return this.accelerationRate;
	}

	public double getSpeed() {
		return this.speed;
	}

	public long getInterReadingsTime() {
		return this.interReadingsTime;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Wheel[] getWheels() {
		return wheels;
	}

	public void setWheels(Wheel[] wheels) {
		this.wheels = wheels;
	}

	public SteeringWheel getSwheel() {
		return swheel;
	}

	public void setSwheel(SteeringWheel swheel) {
		this.swheel = swheel;
	}

	public DriverSeat getdSeat() {
		return dSeat;
	}

	public void setdSeat(DriverSeat dSeat) {
		this.dSeat = dSeat;
	}

	public AlarmLED getAlarmLED() {
		return alarmLED;
	}

	public void setAlarmLED(AlarmLED alarmLED) {
		this.alarmLED = alarmLED;
	}

	public AlarmBeep getAlarmBeep() {
		return alarmBeep;
	}

	public void setAlarmBeep(AlarmBeep alarmBeep) {
		this.alarmBeep = alarmBeep;
	}

	public boolean getLaneKeepingSupport() {
		return laneKeepingSupport;
	}

	public void setLaneKeepingSupport(Object laneKeepingSupport) {
		this.laneKeepingSupport = Boolean.parseBoolean(laneKeepingSupport
				.toString());
	}
	
	public void setEnableSLK(Object enable){
		this.laneKeepingSupportEnable = Boolean.parseBoolean(enable.toString());
	}
	
	public boolean isEnableSLK(){
		return this.laneKeepingSupportEnable;
	}

	public SensorSVLaneCamera getLaneRightCamera() {
		return laneRightCamera;
	}

	public void setLaneRightCamera(SensorSVLaneCamera laneRightCamera) {
		this.laneRightCamera = laneRightCamera;
	}

	public SensorSVLaneCamera getLaneLeftCamera() {
		return laneLeftCamera;
	}

	public void setLaneLeftCamera(SensorSVLaneCamera laneLeftCamera) {
		this.laneLeftCamera = laneLeftCamera;
	}

	public SensorSVSunVisorCamera getSunVisorCamera() {
		return sunVisorCamera;
	}

	public void setSunVisorCamera(SensorSVSunVisorCamera sunVisorCamera) {
		this.sunVisorCamera = sunVisorCamera;
	}

	public SensorSVSteeringWheel getPressureSWheel() {
		return pressureSWheel;
	}

	public void setPressureSWheel(SensorSVSteeringWheel pressureSWheel) {
		this.pressureSWheel = pressureSWheel;
	}

	public SensorSVECG getdECG() {
		return dECG;
	}

	public void setdECG(SensorSVECG dECG) {
		this.dECG = dECG;
	}

	public HashMap<String, Object> getOpe() {
		return this.ope;
	}

	public HashMap<String, String> getSrsVarsId() {
		return this.srsVarsId;
	}

	public HashMap<String, Object> getSrsVarsValues() {
		return this.srsVarsValues;
	}

	public ArrayList<Behaviors> getBeh() {
		return this.beh;
	}

	public void setSpeed(Object newSpeed) {
		this.speed = Double.parseDouble(newSpeed.toString());
	}

	class Cycle extends TimerTask {
		SmartVehicle sv;
		int i = 1;
		boolean hasData;

		public Cycle(SmartVehicle sv) {
			this.sv = sv;
		}

		@Override
		public void run() {
			try {
				hasData = sv.svc.getSVData();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (hasData) {
				sv.getContext();
				try {
					sv.svc.setSVData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				svCtx.getSensorCtx().getSensors().clear();
				svCtx.getBehaviorCtx().getBehaviors().clear();
				notifyState();
			}
			i++;
		}
	}

}
