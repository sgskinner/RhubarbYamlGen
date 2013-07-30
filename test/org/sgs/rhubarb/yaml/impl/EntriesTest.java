package org.sgs.rhubarb.yaml.impl;

import static org.junit.Assert.assertTrue;

import java.util.List;
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
	

	/* 
	 * Note: The set used here was populated by alphabetical order, which is not
	 * the natural ordering we need for a set of entries. So this test
	 * will show us the ordinal is being set and used correctly in determining each
	 * entry position within an ordered data structure. This is important, as
	 * multiple entries create a complete config, and order of the entries in a
	 * config file matter.
	 */
	@Test
	public void testOrdinal() {
		
		// Get a hydrated set of YamlEntry objects in an ordered set 
		Set<YamlEntry> entrySet = buildCompleteEntrySet();
		
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
	
	/*
	 * Test all entries being pulled together, and a complete config
	 * being built. Compare this generated config against a known-
	 * good config on disk.
	 * 
	 */
	@Test
	public void testBuildConfig() {
		
		StringBuffer sb = new StringBuffer();
		Set<YamlEntry> entrySet = buildCompleteEntrySet();
		
		for(YamlEntry entry : entrySet) {
			sb.append(entry);
		}
		String actualResult = sb.toString();
		
		sb = new StringBuffer();
		List<String> lines = FileUtils.getLines("data/test/archibus_email.yaml");
		for(String line : lines) {
			sb.append(line + NEWLINE);
		}
		String expectedResult = sb.toString();
		
		assertTrue(actualResult.equals(expectedResult));
		
	}
	

	/*
	 * Helper to hydrate one type of each YamlEntry class,
	 * where encapsulated data is pulled from "data/test/*.txt".
	 * 
	 * We'll populate the set in alphabetical order to see each entry's ordinal
	 * affecting its place in an ordered data structure (the YamlEntry class
	 * implements the Comparable<YamlEntry> interface, and is simply sorted by
	 * ordinal).
	 * 
	 * (By the way, I do feel a little dirty saying "ordered set",
	 * but the class does exist in the API, so... O_o )
	 * 
	 */
	private Set<YamlEntry> buildCompleteEntrySet() {

		// Instantiate an ordered data structure
		Set<YamlEntry> entrySet = new TreeSet<YamlEntry>();

		// Now make one of each type Yaml
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

		return entrySet;
	}

}
