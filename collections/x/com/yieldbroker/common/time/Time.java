package com.yieldbroker.common.time;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * The time class is used to represent a comparable time stamp that has 
 * a higher precision than that from {@link java.lang.System}. This allows
 * more accurate measurements of time.
 * 
 * @author Niall Gallagher
 */
public class Time implements Serializable, Comparable<Time>, Cloneable {
	
	private long timeInMillis;
	private long timeInNanos;
	
	public Time() {
		super();
	}
	
	public Time(Time time) {
		this(time.timeInMillis, time.timeInNanos);
	}
	
	public Time(long timeInMillis) {
		this(timeInMillis, 0);
	}	
	
	public Time(long time, TimeUnit timeUnit) {
		this(timeUnit.toMillis(time), 0);
	}	
	
	public Time(long timeInMillis, long timeInNanos) {
		this.timeInMillis = timeInMillis;
		this.timeInNanos = timeInNanos;
	}	
	
	public long getNanoTime() {
		return timeInNanos;
	}
	
	public void setNanoTime(long timeInNanos) {
		this.timeInNanos = timeInNanos;
	}
	
	public long getMillisTime() {
		return timeInMillis;
	}
	
	public void setTimeInMillis(long timeInMillis) {
		this.timeInMillis = timeInMillis;
	}
	
	public Time clone() {
		return new Time(this);
	}
	
	@Override
	public int hashCode() {
		return (int)(timeInMillis ^ timeInNanos);
	}
	
	@Override
	public boolean equals(Object other) { 
		if(other instanceof Time) {
			Time time = (Time)other;
			return sameTime(time);
		}
		return false;
	}		
	
	@Override
	public int compareTo(Time other) {
		return before(other) ? -1 : sameTime(other) ? 0 : 1; 
	}
	
	public boolean sameTime(Time other) {		
		if(timeInMillis != other.timeInMillis) {
			return false;
		}
		if(timeInNanos != other.timeInNanos) {
			return false;
		}
		return true;
	}
	
	public boolean after(Time other) {
		if(timeInMillis > other.timeInMillis) {
			return true;
		}
		if(timeInMillis == other.timeInMillis) {
			return timeInNanos > other.timeInNanos;			
		}
		return false;
	}
	
	public boolean before(Time other) {
		if(timeInMillis < other.timeInMillis) {
			return true;
		}
		if(timeInMillis == other.timeInMillis) {
			return timeInNanos < other.timeInNanos;			
		}
		return false;
	}
	
	public String toString() {
		return new Date(timeInMillis).toString();
	}
}
