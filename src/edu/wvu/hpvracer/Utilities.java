package edu.wvu.hpvracer;

public class Utilities {
	
	public static long unixtime() {
		return System.currentTimeMillis() / 1000L;	
	}
	
	public static String uploadKey() {
		return "UPLOADING:"+unixtime();
	}
}
