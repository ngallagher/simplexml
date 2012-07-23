package com.yieldbroker.common.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Date and time class used to represent an immutable date and time value. This
 * contains various convenience methods to simplify date and time manupulation.
 * Each date and time also comes with a time zone to ensure times can be
 * represented across various regions.
 * 
 * @author Niall Gallagher
 */
public class DateTime implements Comparable<DateTime> {

	private final TimeZone timeZone;
	private final long dateTime;

	public DateTime() {
		this(System.currentTimeMillis());
	}

	public DateTime(long dateTime) {
		this(dateTime, TimeZone.getDefault());
	}

	public DateTime(long dateTime, String timeZone) {
		this(dateTime, TimeZone.getTimeZone(timeZone));
	}

	public DateTime(long dateTime, TimeZone timeZone) {
		this.timeZone = timeZone;
		this.dateTime = dateTime;
	}

	public long getTime() {
		return dateTime;
	}

	public Date getDate() {
		return new Date(dateTime);
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public Calendar getCalendar() {
		Calendar calendar = GregorianCalendar.getInstance(timeZone);
		calendar.setTimeInMillis(dateTime);
		return calendar;
	}

	public int getYear() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar calendar = getCalendar();
		return 1 + calendar.get(Calendar.MONTH);
	}

	public int getDay() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public int getHour() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.MINUTE);
	}

	public int getSecond() {
		Calendar calendar = getCalendar();
		return calendar.get(Calendar.SECOND);
	}

	public DateTime minusSeconds(int seconds) {
		return addSeconds(-seconds);
	}

	public DateTime minusMinutes(int minutes) {
		return addMinutes(-minutes);
	}

	public DateTime minusHours(int hours) {
		return addHours(-hours);
	}

	public DateTime minusDays(int days) {
		return addDays(-days);
	}

	public DateTime minusMonths(int months) {
		return addDays(-months);
	}

	public DateTime minusYears(int years) {
		return addDays(-years);
	}

	public DateTime addSeconds(int seconds) {
		return add(seconds, Calendar.SECOND);
	}

	public DateTime addMinutes(int minutes) {
		return add(minutes, Calendar.MINUTE);
	}

	public DateTime addHours(int hours) {
		return add(hours, Calendar.HOUR);
	}

	public DateTime addDays(int days) {
		return add(days, Calendar.DAY_OF_YEAR);
	}

	public DateTime addMonths(int months) {
		return add(months, Calendar.MONTH);
	}

	public DateTime addYears(int years) {
		return add(years, Calendar.YEAR);
	}

	private DateTime add(int count, int unit) {
		Calendar calendar = getCalendar();
		calendar.add(unit, count);
		long time = calendar.getTimeInMillis();
		return new DateTime(time, timeZone);
	}

	public String formatDate(String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return formatDate(format);
	}

	public String formatDate(DateFormat format) {
		return format.format(getDate());
	}

	public Duration timeDifference(DateTime time) {
		return timeDifference(time.dateTime);
	}

	public Duration timeDifference(long time) {
		long diff = Math.abs(dateTime - time);
		long millis = diff % 1000;
		long seconds = (diff / 1000) % 60;
		long minutes = (diff / 60000) % 60;
		long hours = (diff / 3600000) % 24;
		long days = (diff / 86400000);
		return new Duration(diff, days, hours, minutes, seconds, millis);
	}

	public boolean before(DateTime time) {
		return dateTime < time.dateTime;
	}

	public boolean after(DateTime time) {
		return dateTime > time.dateTime;
	}

	public boolean equals(Object value) {
		if (value instanceof DateTime) {
			DateTime time = (DateTime) value;
			return equals(time);
		}
		return false;
	}

	public boolean equals(DateTime time) {
		if (dateTime == time.dateTime) {
			return timeZone.equals(time.timeZone);
		}
		return false;
	}

	public int hashCode() {
		return (int) dateTime;
	}

	public int compareTo(DateTime time) {
		if (dateTime < time.dateTime) {
			return -1;
		}
		if (dateTime == time.dateTime) {
			return 0;
		}
		return 1;
	}

	public static DateTime now() {
		long currentTime = System.currentTimeMillis();
		return new DateTime(currentTime);
	}

	public static DateTime now(TimeZone timeZone) {
		long currentTime = System.currentTimeMillis();
		return new DateTime(currentTime, timeZone);
	}

	public static class Duration implements Comparable<Duration> {

		private long diff;
		private long days;
		private long hours;
		private long minutes;
		private long seconds;
		private long millis;

		public Duration(long diff, long days, long hours, long minutes, long seconds, long millis) {
			this.diff = diff;
			this.days = days;
			this.hours = hours;
			this.minutes = minutes;
			this.seconds = seconds;
			this.millis = millis;
		}
		
		public long getDifference() {
			return diff;
		}

		public long getDays() {
			return days;
		}

		public long getHours() {
			return hours;
		}

		public long getMinutes() {
			return minutes;
		}

		public long getSeconds() {
			return seconds;
		}

		public long getMillis() {
			return millis;
		}		

		public int compareTo(Duration duration) {
			if (diff < duration.diff) {
				return -1;
			}
			if (diff == duration.diff) {
				return 0;
			}
			return 1;
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (days > 0) {
				builder.append(days);
				builder.append(" days ");
			}
			if (days > 0 || hours > 0) {
				builder.append(hours);
				builder.append(" hours ");
			}
			if (days > 0 || hours > 0 || minutes > 0) {
				builder.append(minutes);
				builder.append(" minutes ");
			}
			builder.append(seconds);
			builder.append(" seconds");
			return builder.toString();
		}
	}
}
