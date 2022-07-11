package com.raftProject.raftImplementation;

import java.util.TimerTask;

import org.springframework.stereotype.Component;



@Component
public class Timeout extends TimerTask {
	
	public static int i = 0;

	@Override
	public void run() {
		System.out.println("Timer ran " + ++i);
		
	}

}
