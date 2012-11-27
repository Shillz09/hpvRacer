package edu.wvu.hpvracer;

public class uploader {
	
	protected static int counter = 0;
	
	public void doUpload() {
		counter++;
	}
	
	public static int getCounter() {
		return counter;
	}

}
