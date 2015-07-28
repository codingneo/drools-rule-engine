package com.ps.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	//TODO: initialize it from config file
	private static final int VELOCITY_DURATION_MINUTES = 3;
	
	private static final String TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	public static Date getCutoffDate(Date newTranDate) {
	    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

	    long curTimeInMs = newTranDate.getTime();
	    Date afterAddingMins = new Date(curTimeInMs - (VELOCITY_DURATION_MINUTES * ONE_MINUTE_IN_MILLIS));
	    return afterAddingMins;
	}
	
	public static String formatDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
		return sdf.format(date);
	}
	
	public static Date parseDate(String dateStr){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
