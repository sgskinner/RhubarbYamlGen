package org.sgs.rhubarb.yaml.impl;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntriesTest {
	
	private static final String NEWLINE_PROP_KEY = "line.separator";
	private static final String NEWLINE = System.getProperty(NEWLINE_PROP_KEY);
	
	
	@Test
	public void testStartEntry(){
		StartEntry startEntry = new StartEntry();
		String line = startEntry.getFormattedEntry();
		assertTrue(line.equals("---" + NEWLINE));
	}
	
	
	@Test
	public void testJobNameEntry(){
		JobNameEntry jobNameEntry = new JobNameEntry("ARCHIBUS");
		String line = jobNameEntry.getFormattedEntry();
		assertTrue(line.equals("name: ARCHIBUS" + NEWLINE));
	}
	
	
	@Test
	public void testOutputNameEntry(){
		OutputNameEntry outputNameEntry = new OutputNameEntry("report");
		String line = outputNameEntry.getFormattedEntry();
		assertTrue(line.equals("  name: report" + NEWLINE));
	}
	
	@Test
	public void testSubjectEntry(){
		String env = "STG";
		String jobName = "UAF-ARCHB-DLV-LOADRPT";
		String description = "Archibus Processing Information, Capital Assets Team Review";
		SubjectEntry subjectEntry = new SubjectEntry(env, jobName, description);
		
		String expectedResult = "  subject: STG - UAF-ARCHB-DLV-LOADRPT - Archibus Processing Information, Capital Assets Team Review" + NEWLINE;
		String actualResult = subjectEntry.getFormattedEntry();
		
		assertTrue(actualResult.equals(expectedResult));
	}
	
	
	@Test
	public void testMessageEntry(){
		MessageEntry messageEntry = new MessageEntry("             This is a an oddly padded first line,       \n",
				                                     "And a second line with \"Window's\" line terminators\r\n",
				                                     "Finally, a regular last line." );
		
		// Formats should add a label, strip old newlines, indent, and add current system newlines
		String expectedResult = "  message: >" + NEWLINE +
				                "    This is a an oddly padded first line," + NEWLINE +
				                "    And a second line with \"Window's\" line terminators" + NEWLINE +
				                "    Finally, a regular last line." + NEWLINE;
		String actualResult = messageEntry.getFormattedEntry();
		
		assertTrue(actualResult.equals(expectedResult));
	}
}
