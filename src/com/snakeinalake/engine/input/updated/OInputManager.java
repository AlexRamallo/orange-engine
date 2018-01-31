package com.snakeinalake.engine.input.updated;

import java.util.Vector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.snakeinalake.engine.OrangeDevice;
import com.snakeinalake.engine.core.boundbox2df;
import com.snakeinalake.engine.core.vector3df;
import com.snakeinalake.engine.ortho.ONode2D;
import com.snakeinalake.engine.ortho.ONode2DSprite;

public class OInputManager{
	private Vector<OVirtualPeripheral> peripherals;
//	private HashMap<Integer, Integer> pointerReserve;	// <OVirtualPeripheral, PointerID>
	private int[] pointersOnScreen;
	private OrangeDevice device;
	private SensorManager nmgr;
	public OInputAccelerometer Accel;
	private Sensor accelSense;
	private boolean supportAccel;
	private boolean activated;
	
	public OInputManager(OrangeDevice Device){
		device = Device;
		peripherals = new Vector<OVirtualPeripheral>();
//		pointerReserve = new HashMap<Integer, Integer>();
		nmgr = (SensorManager) device.getContext().getSystemService(Context.SENSOR_SERVICE);
		Accel = new OInputAccelerometer();
		if(nmgr.getSensorList(Sensor.TYPE_ACCELEROMETER).size()<=0){
			activated = false;
			accelSense = null;
		}else{
			accelSense = nmgr.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			activated = true;
			if(accelSense == null)
				supportAccel = false;
			else
				supportAccel = true;
		}
		pointersOnScreen = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
	}
	
	public void setActivated(boolean set){
		activated = set;
	}
	
	public int addButton(vector3df position, ONode2D sp1, ONode2D sp2){
		OVirtualButton btn = new OVirtualButton(this,peripherals.size(),position,sp1,sp2);
		peripherals.add(btn);
		return btn.ID;
	}
	public int addButton(float px, float py, ONode2D sp1, ONode2D sp2){
		OVirtualButton btn = new OVirtualButton(this,peripherals.size(),px,py,sp1,sp2);
		peripherals.add(btn);
		return btn.ID;
	}
	public int addSlider(float px, float py, ONode2D sp1, boolean Vertical){
		OVirtualSlider btn = new OVirtualSlider(this,peripherals.size(),px,py,sp1,Vertical);
		peripherals.add(btn);
		return btn.ID;
	}
	public int addToggleButton(float px, float py, ONode2D sp1, ONode2D sp2){
		OVirtualToggleButton btn = new OVirtualToggleButton(this,peripherals.size(),px,py,sp1,sp2);
		peripherals.add(btn);
		return btn.ID;
	}
	public int addLooper(float px, float py, Vector<ONode2D> options){
		OVirtualLoopButton btn = new OVirtualLoopButton(this,peripherals.size(),px, py,options);
		peripherals.add(btn);
		return btn.ID;
	}
	public int addRadio(float px, float py, ONode2D checked, ONode2D unchecked, ONode2D label, ONode2D label2){
		OVirtualRadio btn = new OVirtualRadio(this,peripherals.size(),px, py,checked,unchecked,label,label2);
		peripherals.add(btn);
		return btn.ID;
	}	
	public int addGestureListener(boundbox2df bounds){
		OVirtualSlideGesture slide = new OVirtualSlideGesture(this, peripherals.size(),bounds);
		peripherals.add(slide);
		return slide.ID;
	}	
	public int addJoystick(float px, float py, ONode2DSprite sp1, ONode2DSprite sp2){
		OVirtualJoystick joy = new OVirtualJoystick(this,peripherals.size(),px,py,sp1,sp2);
		peripherals.add(joy);
		return joy.ID;
	}
	public int addDPad(float px, float py, ONode2DSprite sp1){
		OVirtualDPad joy = new OVirtualDPad(this,peripherals.size(),px,py,sp1);
		peripherals.add(joy);
		return joy.ID;
	}
	public int addCustomPeripheral(OVirtualPeripheral add){
		add.ID = peripherals.size();
		peripherals.add(add);
		return add.ID;
	}
	/**
	 * Get a virtual peripheral thats been created by its ID number. Use addXX() to create one
	 * @param id ID number returned by addXX()
	 * @return Instance of OVirtualPeripheral that may be type-casted
	 * */
	public OVirtualPeripheral getPeripheralById(int id){
		return peripherals.get(id);
	}
	
	private class touchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(!activated)return false;
			if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP || (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP){
				unreserve(event.getPointerId(event.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT));
			}
			boolean ret = false;
			try{
				for(int i=0; i<peripherals.size(); i++){
					OVirtualPeripheral periph = peripherals.get(i);
					if(periph.isAcceptingPointers()){
						if(periph.event(event))
							ret = true;
					}
					periph.update(event);
				}
			}catch(IllegalArgumentException e){
				//Log.e("OInputManager", "IllegalArgumentException caught; rescuing touch...");
			}catch(ArrayIndexOutOfBoundsException e){
				//Idk, fuck it.
			}
			return ret;
		}
	}
	
	public void attachToSurface(View v){
		v.setOnTouchListener(new touchListener());
	}
	
	public boolean reserve(int peripheralId, int pointerId){
		if(pointersOnScreen[pointerId]!=-1){
			return false;			
		}else{
			pointersOnScreen[pointerId] = peripheralId;
			return true;
		}
	}
	public void unreserve(int pointerId)throws ArrayIndexOutOfBoundsException{
		if(pointersOnScreen[pointerId]!=-1){
			peripherals.get(pointersOnScreen[pointerId]).Active = false;
			peripherals.get(pointersOnScreen[pointerId]).PointerID = -1;
			pointersOnScreen[pointerId] = -1;
			return;
		}
	}
	/**
	 * Check if the device supports Accelerometer input
	 * */
	public boolean supportsAccel(){
		return supportAccel;
	}
	/**
	 * Enable or disable the accelerometer. Should be disabled when not in use because
	 * it can kill the battery!
	 * */
	public void toggleAccel(boolean set){
		if(set)
			nmgr.registerListener(Accel, accelSense, SensorManager.SENSOR_DELAY_GAME);
		else
			nmgr.unregisterListener(Accel, accelSense);
	}
	/**
	 * Remove all virtual peripherals and settings. Use at the end of a game if this is a singleton
	 * */
	public void clear(){
		peripherals.clear();
		for(int i=0; i<pointersOnScreen.length; i++)
			pointersOnScreen[i]=-1;
		toggleAccel(false);
	}
	/**
	 * Removes a peripheral by removing it from the list so it can be collected by the GC
	 * */
	public void removePeripheralById(int ID){
		peripherals.remove(ID);
	}	
	
	public class OInputAccelerometer implements SensorEventListener{
		public int accelAccuracy;
		public float accelVals[],accelCalib[];
		
		public OInputAccelerometer(){
			accelVals = new float[3];
			accelCalib = new float[3];
			accelAccuracy = 0;
		}
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			if(sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				accelAccuracy = accuracy;
		}
		public void calibrate(){
			accelCalib[0] = accelVals[0];
			accelCalib[1] = accelVals[1];
			accelCalib[2] = accelVals[2];
		}
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				accelVals[0] = event.values[0] - accelCalib[0];
				accelVals[1] = event.values[1] - accelCalib[1];
				accelVals[2] = event.values[2] - accelCalib[2];
			}
		}		
	}
}
