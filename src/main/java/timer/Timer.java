/*******************************************************************************
 |	Copyright 2006	Jason Whatson. All rights reserved.                    |
 *******************************************************************************/

/*
 * Author: Jason Whatson
 * File: Timer.java
 * Purpose: Be able to time things with a instance of this timer.
 * multiple timer instance can be created to time mulitiple things.
 * a timer can be paused, stoped and formatted (limited)
 * Created on: 6 December 2006, 10:58
 */

package timer;

import java.util.Calendar;
import java.util.Date;

class Timer {
    private long now;
    private long timePassed;
    private static Calendar cn = Calendar.getInstance();
    private boolean isPaused = false;

    Timer() {
        now = System.currentTimeMillis();
    }

    long getTimePassed(){
        if (!isPaused){
            timePassed = System.currentTimeMillis() - now;
        }
        return timePassed;
    }

    static String formatAsDate(long date){
        cn.setTime(new Date(date));

        return cn.get(Calendar.HOUR_OF_DAY) + ":" + cn.get(Calendar.MINUTE) +
            ":" + cn.get(Calendar.SECOND)
            + ":" + cn.get(Calendar.MILLISECOND);
    }
}
