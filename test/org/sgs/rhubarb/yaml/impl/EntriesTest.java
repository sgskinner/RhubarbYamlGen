package org.sgs.rhubarb.yaml.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
	
	@Test
	public void testToRecipientsEntry(){ 
		ToRecipientsEntry toRecipientsEntry = new ToRecipientsEntry("foo@bar.com", "bar@baz.net", "baz@foo.org");
		
		String expectedResult = "  to:" + NEWLINE +
				                "  - foo@bar.com" + NEWLINE +
				                "  - bar@baz.net" + NEWLINE +
				                "  - baz@foo.org" + NEWLINE;
		String actualResult = toRecipientsEntry.getFormattedEntry();
		
		assertTrue(actualResult.equals(expectedResult));
	}

	
	@Test
	public void testCcRecipientsEntry(){ 
		CcRecipientsEntry ccRecipientsEntry = new CcRecipientsEntry("foo@bar.com", "bar@baz.net", "baz@foo.org");
		
		String expectedResult = "  cc:" + NEWLINE +
				                "  - foo@bar.com" + NEWLINE +
				                "  - bar@baz.net" + NEWLINE +
				                "  - baz@foo.org" + NEWLINE;
		String actualResult = ccRecipientsEntry.getFormattedEntry();
		
		assertTrue(actualResult.equals(expectedResult));
	}
	
	
	@Test
	public void testAttachmentsDirEntry(){
		AttachmentsDirEntry attachmentsDirEntry = new AttachmentsDirEntry("/opt/kuali/rhubarb/config");
		
		String expectedResult = "  attachments_dir: /opt/kuali/rhubarb/config" + NEWLINE;
		String actualResult = attachmentsDirEntry.getFormattedEntry();
		
		assertTrue(actualResult.equals(expectedResult));
	}
	
	@Test
	public void testAttachmentsGlobsEntry(){ 
		AttachmentsGlobsEntry attachmentsGlobsEntry = new AttachmentsGlobsEntry("buildingImportErrorReport_*.txt",
																				"buildingImportSuccessReport_*.txt",
																				"roomImportErrorReport_*.txt",
																				"roomImportSuccessReport_*.txt");
		
		String expectedResult = "  attachments_globs:" + NEWLINE +
				                "  - buildingImportErrorReport_*.txt" + NEWLINE +
				                "  - buildingImportSuccessReport_*.txt" + NEWLINE +
				                "  - roomImportErrorReport_*.txt" + NEWLINE +
				                "  - roomImportSuccessReport_*.txt" + NEWLINE;
		String actualResult = attachmentsGlobsEntry.getFormattedEntry();
		
		assertTrue(actualResult.equals(expectedResult));
	}

}
