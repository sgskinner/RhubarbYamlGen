package org.sgs.rhubarb.yaml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
	
	private static String generateJobYaml(String filePrefix, JOBType job){
		// Instantiate an ordered data structure
		Set<YamlEntry> entrySet = new TreeSet<YamlEntry>();

		// Now make one of each type Yaml
		String value = "";//TODO: Find easy way to get this, its not clear in xml how to
		YamlEntry entry = new AttachmentsDirEntry(value);
		entrySet.add(entry);

		String[] valueArray = new String[]{};
		entry = new AttachmentsGlobsEntry(valueArray);
		entrySet.add(entry);

		valueArray = new String[]{};
		entry = new CcRecipientsEntry(valueArray);
		entrySet.add(entry);

		value = filePrefix;
		entry = new JobNameEntry(value);
		entrySet.add(entry);

		String message = "This is a generic message for the " + filePrefix + " job.";
		entry = new MessageEntry(message);
		entrySet.add(entry);

		entry = new OutputEntry();
		entrySet.add(entry);

		entry = new StartEntry();
		entrySet.add(entry);

		valueArray = new String[]{"DEV", filePrefix, "Automated Test Email"};
		entry = new SubjectEntry(valueArray[0], valueArray[1], valueArray[2]);
		entrySet.add(entry);
		
		entry = new ToRecipientsEntry(TEST_EMAILS);
		entrySet.add(entry);
		
		StringBuffer sb = new StringBuffer();
		for(YamlEntry newEntry : entrySet) {
			sb.append(newEntry);
		}
		String actualResult = sb.toString();
		
		return actualResult;
	}
	
	
	/*
	 * A filePrefix is a job name, job-stream name, group name, table-name, etc.
	 * The idea being, we generate a config file for any Control-M construct, even if
	 * the Control-M isn't currently wired to use the YAML config.
	 */
	public static void generateStubYaml(){
		
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
			TargetEntry targetEntry = new TargetEntry(0, "start");
			
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
			targetEntry = new TargetEntry(1, "log");

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
			targetEntry = new TargetEntry(2, "report");

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
			String filename = "data/output/" + name.toLowerCase() + "_email.yaml";
			FileUtils.writeStringToFile(filename, configAsString);
			
			
		}
		
	}
	
	
	/*
	 * This was an attempt at creating files for the 50 files Julie
	 * had identified for UAF testing in ctmtest. The method only produced
	 * 33 files, as the "new name" did not map one-to-one with the
	 * "old names". 
	 */
	public static void generateJuliesUafConfigs(){
		Map<String, String> oldToNewJobNames = new HashMap<String,String>();
		for(String line : FileUtils.getLines("data/input/oldToNewJobNames.csv")){
			String[] tokens = line.split(",");
			String oldName = tokens[0];
			String newName = tokens[1];
			if(oldName.startsWith("--")){
				continue;
			}
			oldToNewJobNames.put(oldName, newName);
		}
		
		
		Set<String> jobNameSet = new TreeSet<String>();
		jobNameSet.addAll(FileUtils.getLines("data/input/trialJobs.txt"));
		
		
		Map<String, String> foundJobNamesMap = new TreeMap<String, String>();
		for(String name : jobNameSet){
			for(Entry<String, String> entry : oldToNewJobNames.entrySet()){
				String oldName = entry.getKey();
				String newName = entry.getValue();
				if(newName.contains(name)){
					foundJobNamesMap.put(name, oldName);
				}
			}
		}
		
		XmlDriver xmlDriver = new XmlDriver("data/input/controlm_prd_2013-07-22.xml");
		List<JOBType> allJobs = xmlDriver.getAllJobs();
		
		Map<String, String> configMap = new HashMap<String, String>();
		for(Entry<String, String> entry : foundJobNamesMap.entrySet()) {
			String fileName = entry.getKey();
			String oldName = entry.getValue();
			for(JOBType job : allJobs){
				if(job.getJOBNAME().equals(oldName)){
					configMap.put(fileName, generateJobYaml(fileName, job));
					break;
				}
			}
		}
		
		for(Entry<String, String> entry : configMap.entrySet()){
			String filename = entry.getKey().toLowerCase() + "_email.yaml";
			FileUtils.writeStringToFile("data/output/" + filename, entry.getValue());
		}
	}
	
	public static void findMuttJobs(){
		XmlDriver xmlDriver = new XmlDriver("data/input/controlm_prd_2013-07-22.xml");
		List<JOBType> jobs =  xmlDriver.getAllJobs();
		int count = 0;
		for(JOBType job : jobs){
			String cmd = job.getCMDLINE();
			if(cmd != null && cmd.length() > 0){
				count++;
				System.out.printf("%s%n", cmd);
			}
		}
		System.out.printf("count: %d%n", count);
	}
	
	
	public static void main(String[] sgs) {
		YamlGenerator.generateStubYaml();
	}

}
