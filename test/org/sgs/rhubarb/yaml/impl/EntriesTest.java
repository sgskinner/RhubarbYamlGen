package org.sgs.rhubarb.yaml.impl;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntriesTest {
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
	@Test
	public void testStartEntry(){
		StartEntry startEntry = new StartEntry();
		String line = startEntry.getFormattedEntry();
		assertTrue(line.equals("---" + NEWLINE));
	}
	
	
	@Test
	public void testOutputEntry(){
		OutputEntry outputEntry = new OutputEntry();
		String line = outputEntry.getFormattedEntry();
		assertTrue(line.equals("output:" + NEWLINE));
	}
	
	@Test
	public void testJobNameEntry(){
		JobNameEntry jobNameEntry = new JobNameEntry("ARCHIBUS");
		String line = jobNameEntry.getFormattedEntry();
		assertTrue(line.equals("name: ARCHIBUS" + NEWLINE));
	}

}
