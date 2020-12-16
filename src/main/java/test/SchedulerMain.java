package test;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class SchedulerMain {
	
	public static void main(String args[]) throws InterruptedException {
		
		Date date = new Date();
		date = new SchedulerMain().getScheduledDateObject(date);
		Timer time = new Timer(); 
		ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
		System.out.println("Scheduled date : "+date);
		time.schedule(st, date);
	}
	
	public Date getScheduledDateObject(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 15);
	    calendar.set(Calendar.MINUTE, 5);
	    calendar.set(Calendar.SECOND, 6);
	    return calendar.getTime();
	}
}
