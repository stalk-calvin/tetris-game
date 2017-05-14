/*******************************************************************************
 |	Copyright 2006	Jason Whatson. All rights reserved.                    |
 *******************************************************************************/

/*
 * Author: Jason Whatson
 * File: Alarmable.java
 * Purpose: All class's that want to recive alarm events need to implement this
 * Created on: 6 December 2006, 10:58
 */
package timer;

public interface Alarmable {
    void timesUp(TimerEvent Event);
}