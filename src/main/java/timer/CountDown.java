//TODO: Make a start method the timer, rather than starting it on constructor
//TODO: Javadoc comment
//TODO: Make an array of listners rather than just one
//TODO: A restart method
//TODO: Unregistar listner method

/*******************************************************************************
 |	Copyright 2006	Jason Whatson. All rights reserved.                    |
 *******************************************************************************/

/*
 * Author: Jason Whatson
 * File: Timer.java
 * Purpose: Count a timer down from a defined time to 0. Once a timer is 0 
 * notify all registered listeners. Allow a count down to be paused and continued.
 * Also the time left can be retrived.
 * Created on: 6 December 2006, 16:32
 * Version: 1.0;
 */
package timer;

import java.util.Calendar;

public class CountDown {
    private Calendar remainingTime;
    private Timer timer;
    private long timePassed = 0;
    private long totalTime = 0;
    private boolean isPaused;

    private Alarmable countDownListners;

    public CountDown(int minutes) {
        remainingTime = Calendar.getInstance();
        
        remainingTime.set(Calendar.HOUR_OF_DAY, remainingTime.get(Calendar.HOUR_OF_DAY)); //Hours until alarm
        remainingTime.set(Calendar.MINUTE, remainingTime.get(Calendar.MINUTE) + minutes); //Minutes until alarm
        remainingTime.set(Calendar.SECOND, remainingTime.get(Calendar.SECOND)); //Seconds until alarm
        remainingTime.set(Calendar.MILLISECOND, remainingTime.get(Calendar.MILLISECOND)); //mili seconds until alarm
    }

    public long getTotalTime(){
        if (!this.isPaused){
            this.totalTime = (remainingTime.getTime().getTime() + this.timePassed) - System.currentTimeMillis();
            if (this.totalTime <= 0){
                this.fireNoTimeLeftEvent();
            }
        }
        return this.totalTime;
    }

    public String formatAsDate(long date){
        return Timer.formatAsDate(date);
    }

    public void pause(boolean paused){
        if (paused){
            timer = new Timer();
        }else{
            this.timePassed += timer.getTimePassed();
        }
        this.isPaused = paused;
    }

    private void fireNoTimeLeftEvent(){
        this.countDownListners.timesUp(new TimerEvent(this));
    }

    public synchronized void addTimesUpListener(Alarmable a) {
        this.countDownListners = a;
    }
}