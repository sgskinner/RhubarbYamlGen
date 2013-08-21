package org.sgs.rhubarb.yaml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.sgs.controlm.AUTOEDIT2Type;
import org.sgs.controlm.JOBType;
import org.sgs.controlm.XmlDriver;
import org.sgs.rhubarb.yaml.impl.AttachmentsDirEntry;
import org.sgs.rhubarb.yaml.impl.AttachmentsGlobsEntry;
import org.sgs.rhubarb.yaml.impl.CcRecipientsEntry;
import org.sgs.rhubarb.yaml.impl.JobNameEntry;
import org.sgs.rhubarb.yaml.impl.MessageEntry;
import org.sgs.rhubarb.yaml.impl.OutputEntry;
import org.sgs.rhubarb.yaml.impl.StartEntry;
import org.sgs.rhubarb.yaml.impl.SubjectEntry;
import org.sgs.rhubarb.yaml.impl.TargetEntry;
import org.sgs.rhubarb.yaml.impl.ToRecipientsEntry;
import org.sgs.rhubarb.yaml.utils.FileUtils;



public class YamlGenerator {
	
	private static String[] TEST_EMAILS = new String[]{"KATT_AUTOMATION_ADDRESS",
		   											   "JULIE_WINGATE_ADDRESS",
                                                       "HEATHER_LO_ADDRESS",
                                                       "JOSH_SHALOO_ADDRESS",
           											   "SCOTT_SKINNER_ADDRESS"};
	
	
	/*
	 * Generate generic YAML for *DLV* jobs and job-streams
	 */
	public static void generateGenericDlvYaml(){
		
		String outputDir = "data/output/generic_yaml/";
		
		Set<String> names = getNewDlvNames();
		
		Map<String, String> fileNameToConfigMap = getGenericYaml(names);
		
		for(Entry<String, String> entry : fileNameToConfigMap.entrySet()){
			String filename = entry.getKey();
			String configAsString = entry.getValue();
			FileUtils.writeStringToFile(outputDir + filename, configAsString);
		}

	}
	
	
	/*
	 * Generate generic YAML for *DLV* jobs and job-streams
	 */
	public static void generateCmdLineYaml(){
		
		String outputDir = "data/output/cmdline_yaml/";
		
		Set<String> names = getCmdLineNames();
		
		Map<String, String> fileNameToConfigMap = getGenericYaml(names);
		
		for(Entry<String, String> entry : fileNameToConfigMap.entrySet()){
			String filename = entry.getKey();
			String configAsString = entry.getValue();
			FileUtils.writeStringToFile(outputDir + filename, configAsString);
		}

	}
	
	
	public static void generateCmdLineFile(boolean replaceVariables){
		
		String outputDir = "data/input/";
		String filename = "cmdLineJobContents.txt";
		
		Map<String, String> oldToNewNameMap = getOldToNewNameMap();
		XmlDriver xmlDriver = new XmlDriver("data/input/controlm_prd_2013-07-22.xml");
		List<JOBType> jobs =  xmlDriver.getAllJobs();
		
		StringBuffer sb = new StringBuffer();
		for(JOBType job : jobs){

			String cmd = job.getCMDLINE();
			if(cmd == null){
				// Not all jobs have a CMDLINE element
				continue;
			}
			
			if (replaceVariables) {
					cmd = replaceTokens(job, cmd);
			}
			
			String legacyName = job.getJOBNAME();
			String newName = oldToNewNameMap.get(legacyName);
			if(newName == null){
				newName = "NONE";
			}
			String commandInfo = String.format("[%s => %s]:%n %s%n%n", legacyName, newName, cmd);
			sb.append(commandInfo);
			
		}//for
		
		FileUtils.writeStringToFile(outputDir + filename, sb.toString());
		
	}
	
	
	private static Set<String> getCmdLineNames() {
		
		Set<String> newCmdLineNames = new TreeSet<String>();
		
		XmlDriver xmlDriver = new XmlDriver("data/input/controlm_prd_2013-07-22.xml");
		List<JOBType> jobs = xmlDriver.getAllJobs();
		Map<String, String> oldNewNameMap = getOldToNewNameMap(); 
		
		for (JOBType job : jobs) {

			String cmd = job.getCMDLINE();
			if (StringUtils.isNotBlank(cmd)) {
				String oldName = job.getJOBNAME();
				String newName = oldNewNameMap.get(oldName);
				if(StringUtils.isNotBlank(newName)){
					newCmdLineNames.add(newName);
				}
			}

		}// for
		
		return newCmdLineNames;
	}
	
	
	/*
	 * Given a list of job/job-stream names, return a map
	 * of generic configs suitable for writing out to file --
	 * the map key is the filename, and the map value is
	 * the actual YAML config
	 */
	private static Map<String, String> getGenericYaml(Set<String> names){
		
		Map<String, String> fileNameToConfigMap = new HashMap<String, String>();
		
		for (String name : names) {
			
			Set<YamlEntry> entrySet = new TreeSet<YamlEntry>();
			
			YamlEntry entry = new StartEntry();
			entrySet.add(entry);
			entry = new StartEntry();
			entrySet.add(entry);
			
			entry = new JobNameEntry(name);
			entrySet.add(entry);
			
			entry = new OutputEntry();
			entrySet.add(entry);

			// TargetEntry: "job_start" ****************************************************************
			TargetEntry targetEntry = new TargetEntry(3, "start");
			
			entry = new SubjectEntry("DEV", name, "Start Notice");
			targetEntry.addSubEntry(entry);
			
			entry = new MessageEntry("The '" + name + "' process has started.");
			targetEntry.addSubEntry(entry);
			
			entry = new ToRecipientsEntry(TEST_EMAILS);
			targetEntry.addSubEntry(entry);
			
			entry = new CcRecipientsEntry(new String[] {});
			targetEntry.addSubEntry(entry);
			
			entry = new AttachmentsDirEntry(new String[]{});
			targetEntry.addSubEntry(entry);
			
			entry = new AttachmentsDirEntry(new String[]{});
			targetEntry.addSubEntry(entry);
			
			entry = new AttachmentsGlobsEntry(new String[]{});
			targetEntry.addSubEntry(entry);
			
			entrySet.add(targetEntry);
			
			
			// TargetEntry: "log" ****************************************************************
			targetEntry = new TargetEntry(4, "log");

			entry = new SubjectEntry("DEV", name, "Log Email");
			targetEntry.addSubEntry(entry);

			entry = new MessageEntry("Please find the attached log(s) for the  '" + name + "' process.");
			targetEntry.addSubEntry(entry);

			entry = new ToRecipientsEntry(TEST_EMAILS);
			targetEntry.addSubEntry(entry);

			entry = new CcRecipientsEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entry = new AttachmentsDirEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entry = new AttachmentsDirEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entry = new AttachmentsGlobsEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entrySet.add(targetEntry);
			
			
			// TargetEntry: "report" ****************************************************************
			targetEntry = new TargetEntry(5, "report");

			entry = new SubjectEntry("DEV", name, "Report Email");
			targetEntry.addSubEntry(entry);

			entry = new MessageEntry("Please find the attached report(s) for the  '" + name + "' process.");
			targetEntry.addSubEntry(entry);

			entry = new ToRecipientsEntry(TEST_EMAILS);
			targetEntry.addSubEntry(entry);

			entry = new CcRecipientsEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entry = new AttachmentsDirEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entry = new AttachmentsDirEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entry = new AttachmentsGlobsEntry(new String[] {});
			targetEntry.addSubEntry(entry);

			entrySet.add(targetEntry);
			

			
			StringBuffer sb = new StringBuffer();
			for (YamlEntry newEntry : entrySet) {
					sb.append(newEntry);
			}
			
			String configAsString= sb.toString();
			String filename = name.toLowerCase() + "_email.yaml";
			fileNameToConfigMap.put(filename, configAsString);
			
		}//for
		
		return fileNameToConfigMap;
	}
	
	
	// Helper method to replace tokens in a Control-M CMDLINE string
	private static String replaceTokens(JOBType job,  String commandString){
		
		String result = new String(commandString);
		
		// Hydrate map of key-value pairs built from "AUTOEDIT2" elements 
		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("%%JOBNAME", job.getJOBNAME());
		List<AUTOEDIT2Type> entries = job.getAUTOEDIT2();
		for(AUTOEDIT2Type entry : entries){
			String key = entry.getNAME();
			String value = entry.getValueAttribute();
			tokenMap.put(key, value);
		}
		
		// Replace each entry of tokenMap in commandString
		for(Entry<String, String> entry : tokenMap.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			if(result.contains(key)){
				result = result.replace(key, value);
			}
		}
		
		return result;
	}
	
	
	private static Map<String, String> getOldToNewNameMap(){
		Map<String, String> nameMap = new TreeMap<String, String>();
		List<String> lines = FileUtils.getLines("data/input/oldToNewJobNames.csv");
		for(String line : lines){
			String[] tokens = line.split(",");
			String key = tokens[0];
			String value = tokens[1];
			if(key.equals("--")){
				// we have no mapping for this legacy name, skip
				continue;
			}
			nameMap.put(key, value);
		}
		return nameMap;
	}
	
	
	private static Set<String> getNewDlvNames(){
		
		// Build unique set of job-stream names
		Set<String> names = new TreeSet<String>();
		List<String> lines = FileUtils.getLines("data/input/oldToNewJobNames.csv");
		for(String line : lines){
			
			// line[0] == old name
			// line[1] == new name, fully qualified
			String[] tokens = line.split(",");
			String wholeName = tokens[1];
			
			if(!wholeName.contains("-DLV-")){
				// Not a "batch_deliver" job, skip to check next
				continue;
			}
			
			// Capture the full job name
			names.add(wholeName);
			
			// The form of the new name is "ENV-JOB_STREAM-EXECUTABLE-JOB_NAME",
			// e.g.: "UAF-ARCHB-DLV-LOADRPT". This snippet will capture the
			// job-stream name.
			tokens = wholeName.split("-");
			String jobStreamName = tokens[1];
			names.add(jobStreamName);
		}
		
		return names;
	}
	
	
	
	public static void main(String[] sgs) {
		//YamlGenerator.generateStubYaml();
		//findMuttJobs(true);
		//generateCmdLineYaml();
		generateCmdLineFile(true);
	}

}
