package org.sgs.rhubarb.yaml.impl;

import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.utils.FileUtils;

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
	
	
	@Test
	public void testOrdinal() {
		
		// Instantiate an ordered data structure 
		Set<YamlEntry> entrySet = new TreeSet<YamlEntry>();
		
		// Populate each entry into ordered data set, we'll go
		// alphabetical just to see each entry's ordinal affecting
		// its place in an ordered data structure -- Note: the
		// alphabetical order is not the correct order in a set
		// of entries, so this will show us the ordinal is being
		// set and used correctly in determining entry position.
		
		String value = FileUtils.getLinesAsArray("data/test/AttachmentsDirEntry.txt")[0];
		YamlEntry entry = new AttachmentsDirEntry(value);
		entrySet.add(entry);
		
		String[] valueArray = FileUtils.getLinesAsArray("data/test/AttachmentsGlobsEntry.txt");
		entry = new AttachmentsGlobsEntry(valueArray);
		entrySet.add(entry);
		
		valueArray = FileUtils.getLinesAsArray("data/test/CcRecipientsEntry.txt");
		entry = new CcRecipientsEntry(valueArray);
		entrySet.add(entry);
		
		value = FileUtils.getLinesAsArray("data/test/JobNameEntry.txt")[0];
		entry = new JobNameEntry(value);
		entrySet.add(entry);
		
		valueArray = FileUtils.getLinesAsArray("data/test/MessageEntry.txt");
		entry = new MessageEntry(valueArray);
		entrySet.add(entry);
		
		entry = new OutputEntry();
		entrySet.add(entry);
		
		value = FileUtils.getLinesAsArray("data/test/OutputNameEntry.txt")[0];
		entry = new OutputNameEntry(value);
		entrySet.add(entry);
		
		entry = new StartEntry();
		entrySet.add(entry);
		
		valueArray = FileUtils.getLinesAsArray("data/test/SubjectEntry.txt");
		entry = new SubjectEntry(valueArray[0], valueArray[1], valueArray[2]);
		entrySet.add(entry);
		
		valueArray = FileUtils.getLinesAsArray("data/test/ToRecipientsEntry.txt");
		entry = new ToRecipientsEntry(valueArray);
		entrySet.add(entry);
		
		// Test for size of all entries
		assertTrue(entrySet.size() == 10);
		
		// Test each entry's ordinal, we should be ordered now. Note,
		// the ordinal is explictly set by each class that overrides
		// YamlEntry.java.
		int index = 0;
		for(YamlEntry testEntry : entrySet) {
			int testOrdinal = testEntry.getOrdinal();
			assertTrue(index == testOrdinal);
			index++;
		}
		
	}

}
