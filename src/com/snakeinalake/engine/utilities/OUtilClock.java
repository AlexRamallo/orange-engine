package com.snakeinalake.engine.utilities;

import java.util.Vector;

import android.util.Log;

import com.snakeinalake.engine.OrangeDevice;

public class OUtilClock {
	private boolean useDelay;
	private float delay;
	public enum IFC_UNIT{
		IFC_MILI,
		IFC_SECONDS,
		IFC_MINUTES,
		IFC_HOURS
	}
	/**
	 * Set the delay to use. Zero means no delay
	 * */
	public void setDelay(float set){
		useDelay = set!=0;
		delay = set;
	}
	public class beeperpack{
		public String name;
		public IFC_UNIT unit;
		public float on,off;
		public boolean state;
		public float counter;
	}
	private class timerpack{
		public String name;
		public float time;
		public boolean paused,enabled;
		public IFC_UNIT type;
		public void resume(){
			paused = false;
		}
		public void pause(){
			paused = true;
		}
		public boolean isPaused(){
			return paused;
		}
		@SuppressWarnings("unused")
		public void setTime(float set){
			time = set;
		}
		public void addTime(float set){
			time += set;
		}
	}
	private class fluctpack{
		public String name;
		public float value,maxVal,lowVal;
		public float upSpeed,downSpeed;
		public boolean state;
		
		public IFC_UNIT type;
		@SuppressWarnings("unused")
		public void setValue(float set){
			value = set;
		}
		public void addValue(float set){
			value += set;
		}
		@SuppressWarnings("unused")
		public void subValue(float set){
			value -= set;
		}
	}
	
	//public Vector<timerpack> TimerList;
	private timerpack[] TimerList;
	public Vector<beeperpack> BeeperList;
	public Vector<fluctpack> FluctList;
	private int tplC,bplC,fplC;
	
	@SuppressWarnings("unused")
	private OrangeDevice device;
	@SuppressWarnings("unused")
	private double time,elapsed,counting;
	private boolean FirstTimeCounting;
	
	public OUtilClock(OrangeDevice Device){
		device = Device;
		time = 0;
		elapsed = 0;
		counting = 0;
		FirstTimeCounting = true;
		//TimerList = new Vector<timerpack>();
		tplC = 0;
		TimerList = new timerpack[tplC];
		FluctList = new Vector<fluctpack>();
		BeeperList = new Vector<beeperpack>();
	}
	
	public synchronized void restart(){
		FirstTimeCounting = true;
		time = 0;
		elapsed = 0;
		counting = 0;
	}
	
	public synchronized void reset(){
		time = System.currentTimeMillis();
	}
	private void updateTimers(){
		//UPDATE TIMERS 
		if(tplC>0){
			updateTimers:
			for(int i=0; i<tplC; i++){
				timerpack pk = TimerList[i];
				if(pk == null)continue updateTimers;
		    	if(!pk.isPaused())
		    	switch(pk.type){
			    	case IFC_MILI:
			    		pk.addTime((float) -elapsed);
			    		break;
			    	case IFC_SECONDS:
			    		pk.addTime((float) (-elapsed*0.001));
			    		break;
			    	case IFC_MINUTES:
			    		pk.addTime((float) ((-elapsed*0.001)/60.0));
			    		break;
			    	case IFC_HOURS:
			    		pk.addTime((float) (((-elapsed*0.001)/60.0)/60.0));
			    		break;
		    	}
			}
		}
	}
	private void updateFluctuators(){
		 // UPDATE FLUCTUATORS 
		if(FluctList.size()>0)
		for(int i=0; i<FluctList.size(); i++){
			fluctpack pk = FluctList.get(i);
		    	switch(pk.type){
			    	case IFC_MILI:
			    		pk.addValue((float) (((pk.state)?1:-1)*elapsed*((pk.state)?pk.upSpeed:pk.downSpeed)));
			    		break;
			    	case IFC_SECONDS:
			    		pk.addValue((float) (((pk.state)?1:-1)*elapsed*((pk.state)?pk.upSpeed:pk.downSpeed)*0.001));
			    		break;
			    	case IFC_MINUTES:
			    		pk.addValue((float) ((((pk.state)?1:-1)*elapsed*((pk.state)?pk.upSpeed:pk.downSpeed)*0.001)/60.0));
			    		break;
			    	case IFC_HOURS:
			    		pk.addValue((float) (((((pk.state)?1:-1)*elapsed*((pk.state)?pk.upSpeed:pk.downSpeed)*0.001)/60.0)/60.0));
			    		break;
		    	}
		    	if(pk.value>=pk.maxVal){
		    		pk.state = false;
		    	}
		    	if(pk.value<=pk.lowVal){
		    		pk.state = true;
		    	}
		}
	}
	public void updateBeepers(){
		//UPDATE BEEPERS
		if(BeeperList.size()>0)
		for(int i=0; i<BeeperList.size(); i++){
			beeperpack beeper = BeeperList.get(i);
	    	switch(beeper.unit){
		    	case IFC_MILI:
		    		beeper.counter += elapsed;
		    		break;
		    	case IFC_SECONDS:
		    		beeper.counter += elapsed*0.001;
		    		break;
		    	case IFC_MINUTES:
		    		beeper.counter += (elapsed*0.001)/60;
		    		break;
		    	case IFC_HOURS:
		    		beeper.counter += ((elapsed*0.001)/60)/60;
		    		break;
	    	}
	    	if(beeper.counter>=beeper.on && beeper.state == true){
	    		beeper.state = false;
	    		beeper.counter = 0;
	    	}else{
	    		if(beeper.counter>=beeper.off && beeper.state == false){
		    		beeper.state = true;
		    		beeper.counter = 0;
		    	}	
	    	}
	    }					
	}
	/**
	 * Make the clock 'count' by itself
	 * This function will automatically try to calculate elapsed time as accurately as it can.</br>
	 * Requires <b>delay</b> to be set with <i>setDelay</i> if you are trying to cap the framerate. Not setting <b>delay</b> will cause the clock to include the amount slept (via a <i>sleep()</i> function) in it's calculations, causing it's estimates to get messed up.</br>
	 * Only recommended when you aren't managing time yourself.
	 * */
	public synchronized void count(){
		if(FirstTimeCounting){FirstTimeCounting = false;reset();}
		elapsed = (System.currentTimeMillis() - time);
		try{
	    	updateTimers();
	    	updateFluctuators();
	    	updateBeepers();
	    	reset();
		}catch(ArrayIndexOutOfBoundsException e){
			Log.e("OUtilClock", "caught ArrayIndexOutOfBounds exception");
		}
	}
	/**
	 * Make the clock 'count'<br/>
	 * @param delta
	 *			Calculated delta or elapsed time
	 * */
	public synchronized void count(double delta){
		elapsed = delta;
		try{
	    	updateTimers();
	    	updateFluctuators();
	    	updateBeepers();
		}catch(ArrayIndexOutOfBoundsException e){
			Log.e("OUtilClock", "caught ArrayIndexOutOfBounds exception");
		}
	}
	
	public double getElapsed(){
		return elapsed/((useDelay)?delay:1);
	}
	
	public synchronized void removeAllBeepers(){	
		BeeperList.clear();
	}
	/**
	 * Set all values in the list of timers to null so they can be removed by the GC
	 * */
	public synchronized void removeAllTimers(){
		for(int i=0; i<tplC; i++){
			TimerList[i]=null;
		}
	}
	
	public void removeAllFluctuators(){
		FluctList.clear();
	}
	
	public void removeEverything(){
		removeAllBeepers();
		removeAllTimers();
		removeAllFluctuators();
	}
	/*BEEPERS*/
	
	public void setConsistentBeeper(String name, IFC_UNIT unit, float on, float off){
		for(int i=0; i<BeeperList.size(); i++){
			beeperpack bp = BeeperList.get(i);
			if(bp.name.equals(name)){
				bp.name = name;
				bp.on = on;
				bp.off = off;
				bp.unit = unit;
				bp.state = false;
				bp.counter = 0;
				return;
			}
		}
		beeperpack beeper = new beeperpack();
		beeper.name = name;
		beeper.on = on;
		beeper.off = off;
		beeper.unit = unit;
		beeper.state = false;
		beeper.counter = 0;
		BeeperList.add(beeper);
	}
	
	public synchronized boolean isSetBeeper(String name){
		for(int i=0; i<BeeperList.size(); i++){
			beeperpack bp = BeeperList.get(i);
			if(bp.name.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean getBeeper(String name){
		for(int i=0; i<BeeperList.size(); i++){
			beeperpack bp = BeeperList.get(i);
			if(bp.name.equals(name)){
				return bp.state;
			}
		}
		return false;
	}
	
	public synchronized void removeBeeper(String name){
		for(int i = 0; i < BeeperList.size(); i++){
			if(BeeperList.get(i).name.equals(name)){
				BeeperList.remove(i); 
			}
		}
	}	
	/*TIMERS*/
	/**
	 * Adds a timer to the array. Returns true if the array had to be grown
	 * */
	private boolean addTimer(timerpack add){
		for(int i=0; i<tplC; i++){
			if(TimerList[i]==null){
				TimerList[i]=add;
				return false;
			}
		}
		tplC++;
		timerpack[] buff = (timerpack[])TimerList.clone();
		TimerList = new timerpack[tplC];
		for(int i=0; i<tplC-1; i++){
			TimerList[i]=buff[i];
		}
		TimerList[tplC-1] = add;
		return true;
	}
	/**
	 * Sets an existing timer
	 * */
	public void setTimer(String name, float Time, IFC_UNIT unit){
		int firstspot = -1;
		for(int i=0; i<tplC; i++){
			timerpack t = TimerList[i];
			if(t==null && firstspot == -1)firstspot = i;
			if(t!=null && t.name.equals(name)){
				t.time = Time;
				t.enabled = true;
				t.type = unit;
				return;
			}
		}
		timerpack t = new timerpack();
		t.time = Time;
		t.type = unit;
		t.enabled = true;
		t.name = name;
		//TimerList.add(t);
		if(firstspot!=-1)
			TimerList[firstspot] = t;
		else
			addTimer(t);
	}
	/**
	 * Sets an existing timer without IFC_UNIT
	 * @param unit 0 = MILI, 1 = SECONDS, 2 = MINUTES, 3 = HOURS
	 * */
	public void setTimer(String name, float Time, int unit){
		IFC_UNIT type;
		switch(unit){
			default:
				type = IFC_UNIT.IFC_MILI;
				break;
			case 1:
				type = IFC_UNIT.IFC_SECONDS;
				break;
			case 2:
				type = IFC_UNIT.IFC_MINUTES;
				break;
			case 3:
				type = IFC_UNIT.IFC_HOURS;
				break;
		}
		this.setTimer(name,Time,type);
	}
	
	/**
	 * Creates a new timer and adds it to the list
	 * @param name String identifier
	 * */
	public void createTimer(String name, float Time, IFC_UNIT unit){
		timerpack t = new timerpack();
		t.time = Time;
		t.type = unit;
		t.name = name;
		//TimerList.add(t);
		addTimer(t);
	}
	/**
	 * Creates a new timer and adds it to the list
	 * @param ID integer identifier to use rather than a string
	 * */
	public void createTimer(int ID, float Time, IFC_UNIT unit){
		timerpack t = new timerpack();
		t.time = Time;
		t.type = unit;
		//TimerList.add(t);
		addTimer(t);
	}
	
	/**
	 * Returns the value of an existing timer
	 * */
	public synchronized float getTimer(String name){
		for(int i=0; i<tplC; i++){
			timerpack tp = TimerList[i];
			if(tp!=null && tp.name.equals(name) && tp.enabled)
				return tp.time;
		}
		return 0;
	}
	/**
	 * Checks whether or not a timer exists
	 * */
	public synchronized boolean isSetTimer(String name){
		for(int i=0; i<tplC; i++){
			timerpack tp = TimerList[i];
			if(tp!=null && tp.name.equals(name) && tp.enabled)
				return true;
		}
		return false;
	}
	/**
	 * Remove a timer from the list
	 * <blockquote>
	 * <strong>NOTE:</strong>
	 * 	This sets the 'enabled' flag of the timer in the list to false. This has the advantage of preventing 
	 * the GC from being invoked unnecessarily. It could cause issues with memory in the future, however, and
	 * so there should be an option for making this function actually set the reference to NULL so that the GC
	 * can handle it.
	 * </blockquote>
	 * @param name The name of the timer to remove
	 * @see setTimer
	 * @see isSetTimer
	 * @see isPausedTimer
	 * @see pauseTimer
	 * @see resumeTimer
	 * */
	public synchronized void removeTimer(String name){
		for(int i=0; i<tplC; i++){
			timerpack tp = TimerList[i];
			if(tp!=null && tp.name.equals(name)){
				//TimerList[i]=null;
				TimerList[i].enabled = false;
				return;
			}
		}
	}
	
	public synchronized boolean isPausedTimer(String name){
		for(int i=0; i<tplC; i++){
			timerpack tp = TimerList[i];
			if(tp.name.equals(name)){
				return tp.isPaused();
			}
		}
		return false;
	}
	
	public void pauseTimer(String name){
		for(int i=0; i<tplC; i++){
			timerpack tp = TimerList[i];
			if(tp.name.equals(name)){
				tp.pause();
			}
		}
	}
	
	public void resumeTimer(String name){
		for(int i=0; i<tplC; i++){
			timerpack tp = TimerList[i];
			if(tp.name.equals(name)){
				tp.resume();
			}
		}
	}
	/*FLUCTUATORS*/
	
	public void setFluctuator(String name, IFC_UNIT unit, float maxValue, float minValue, float upSpeed, float downSpeed){
		for(int i=0; i<FluctList.size(); i++){
			fluctpack f = FluctList.get(i);
			if(f.name.equals(name)){
				f.downSpeed = downSpeed;
				f.upSpeed = upSpeed;
				f.lowVal = minValue;
				f.maxVal = maxValue;
				f.type = unit;
				return;
			}
		}
		fluctpack f = new fluctpack();
		f.name = name;
		f.downSpeed = downSpeed;
		f.upSpeed = upSpeed;
		f.lowVal = minValue;
		f.maxVal = maxValue;
		f.type = unit;
		FluctList.add(f);
	}
	
	public synchronized float getFluctuator(String name){
		for(int i=0; i<FluctList.size(); i++){
			fluctpack fp = FluctList.get(i);
			if(fp.name.equals(name)){
				return fp.value;
			}
		}
		return 0;
	}
	
	public synchronized boolean isSetFluctuator(String name){
		for(int i=0; i<FluctList.size(); i++){
			fluctpack fp = FluctList.get(i);
			if(fp.name.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public synchronized void removeFluctuator(String name){
		for(int i=0; i<FluctList.size(); i++){
			fluctpack fp = FluctList.get(i);
			if(fp.name.equals(name)){
				FluctList.remove(i);
			}
		}
	}
		
}
