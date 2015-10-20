package com.sc.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;


public class DateUtil {
	private static final String FORMAT_PATTERN = "YYYY-MM-DD";
	private static TimeZone timeZone = TimeZone.getTimeZone("GMT+8");

	public static void main(String[] args) {
		String beginDate = "2013-01-01";
		String endDate = "2013-10-27";
		
		long days = getDaysBetween(new DateTime(beginDate), new DateTime(endDate));
		System.out.println("days: " + days);
		long theDateSecs = getMilliseconds(endDate);
		DateTime d = getDaysAfter(theDateSecs, 1);
		System.out.println(d.toString());
	}
	
	public static DateTime getDateByMilliseconds(long milliseconds) {
		return DateTime.forInstant(milliseconds, timeZone);
	}
	
	public static long getMilliseconds(String dateStr) {
		DateTime date = new DateTime(dateStr);
		return getMilliseconds(date);
	}
	
	public static long getMilliseconds(DateTime date) {
		return date.getMilliseconds(timeZone);
	}
	
	public static String getStrByMilliseconds(long milliseconds) {
		DateTime d = getDateByMilliseconds(milliseconds);
		return d.format(FORMAT_PATTERN);
	}
	
	
	
	public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
		int size = list.size();
		int step = (int) Math.ceil(size / (splitSize * 1.0));
		//System.out.println("step: " + step + ", dateList size: " + size);
		//System.out.println("dateList: " + list);

		List<List<T>> dataList = new ArrayList<List<T>>(step);
		for(int j = 0; j < step; j++) {
			int fromIndex = j * splitSize;
			int toIndex = (j + 1) * splitSize;
			toIndex = toIndex < size ? toIndex : size;
			
			//System.out.println(fromIndex + ":" + toIndex);
			List<T> subList = list.subList(fromIndex, toIndex);
			dataList.add(subList);
		}
		return dataList;
	}
	
	
	public static DateTime getDaysBefore(long theDataSecs, int days) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(theDataSecs));
        calendar.add(Calendar.DATE, -days);
        Date date = calendar.getTime();
        return getDateByMilliseconds(date.getTime());
	}
	
	public static DateTime getDaysAfter(long theDataSecs, int days) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(theDataSecs));
        calendar.add(Calendar.DATE, days);
        Date date = calendar.getTime();
        return getDateByMilliseconds(date.getTime());
	}
	
	private static long getDaysBetween(DateTime startDate, DateTime endDate) {
		long diff = getMilliseconds(endDate) - getMilliseconds(startDate);
		return diff / (1000*3600*24) + 1;
	}

	private static boolean validate(String dateStr, List<String> holidays) {
		if(holidays.contains(dateStr))
			return true;
		return false;
	}
	
	private static List<String> getWeekends(int year){
		List<String> list = new ArrayList<String>();
		
		DateTime date = DateTime.forDateOnly(year, 12, 31);
		int days = date.getDayOfYear();
		
		for(int day = 1; day <= days; day++){
			int weekDay = date.getWeekDay();
			
			if(weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY){
				list.add(date.format(FORMAT_PATTERN));
			}
			date = date.minusDays(1);
		}
		return list;
	}

}
