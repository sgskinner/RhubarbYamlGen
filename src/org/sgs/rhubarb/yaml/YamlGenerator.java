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
import org.sgs.controlm.DOMAILType;
import org.sgs.controlm.JOBType;
import org.sgs.controlm.ONType;
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
	
	private static final String[] TEST_EMAILS = new String[]{"KATT_AUTOMATION_ADDRESS"};
	private static final String EMAIL_DELIM = ";";
	private static final Map<String, String> emailToTokenMap; 
	
	/* Complete set of possible "ON" @code values for elements
	 * having "DOMAIL" children. Control-M will, for
	 * now, continue to handle failure emails, and Rhubarb
	 * will take care of success emails.
	 *  
	 *  
	 *	"*Percent: 100  status: Ended OK*"                  Success
	 *	"OK"                                                Success
	 *
	 *	"*<result>true*"                                   	Deprecated
	 *	"*File does not exist*"                             Failure (only when doMail is present)
	 *	"*Time Limit for file watching was exceeded*"       Failure
	 *	"*failed*"                                          Failure
	 *	"NOTOK"                                             Failure
	 *	"*soap:Fault*"                                      Failure (also deprecated)
	 *	"RUNCOUNT = 12"                                     Failure (also deprecated)
	 *	"RUNCOUNT = 40"                                     Failure (also deprecated)
	 *	"RUNCOUNT = 60"                                     Failure (also deprecated)
	 *	"RUNCOUNT = 8"                                      Failure (also deprecated)
	 */
	private static Set<String> SUCCESS_CODES;
	static {
		SUCCESS_CODES = new TreeSet<String>();
		SUCCESS_CODES.add("*Percent: 100  status: Ended OK*");
		SUCCESS_CODES.add("OK");
		
		emailToTokenMap = new TreeMap<String, String>();
		List<String> lines = FileUtils.getLines("data/input/addresses_prd.yaml");
		lines.remove(0); // first line is "---"
		for(String line : lines){
			String[] tokens = line.split(": ");
			String token = tokens[0];
			String email = tokens[1];
			emailToTokenMap.put(email, token);
		}
	}

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
	

	public static void generateSuccessEmailYaml(){
		Set<ConfigTuple> successJobTuples = getSuccessJobTuples();
		for(ConfigTuple tuple : successJobTuples){
			String config = getSuccessEmailYaml(tuple);
			String filename = "data/output/success_yaml/" + tuple.getNewJobName().toLowerCase() + "_email.yaml";
			FileUtils.writeStringToFile(filename, config);
		}
	}
	
	
	private static Set<ConfigTuple> getSuccessJobTuples(){
		Set<ConfigTuple> successJobTuples = new TreeSet<ConfigTuple>();
		
		List<JOBType> jobs = getAllJobs();
		
		List<String> lines =  FileUtils.getLines("data/input/successEmailJobNameMap.csv");
		for(String line : lines){
			String[] tokens = line.split(",");
			String legacyName = tokens[0];
			String newSuccessName = tokens[1];
			
			if(newSuccessName.equals("NONE")){
				continue;
			}
			
			ConfigTuple tuple = new ConfigTuple(legacyName, newSuccessName, null);
			for(JOBType job : jobs){
				if(job.getJOBNAME().equals(legacyName)){
					tuple.setJob(job);
					successJobTuples.add(tuple);
				}
			}
		}
		
		return successJobTuples;
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
	
	
	private static List<JOBType> getAllJobs(){
		XmlDriver xmlDriver = new XmlDriver("data/input/controlm_prd_2013-07-22.xml");
		List<JOBType> jobs = xmlDriver.getAllJobs();
		return jobs;
	}
	
	
	private static Set<String> getCmdLineNames() {
		
		Set<String> newCmdLineNames = new TreeSet<String>();
		List<JOBType> jobs = getAllJobs();
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
			
			entry = new SubjectEntry("%%ENV", name, "Start Notice");
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

			entry = new SubjectEntry("%%ENV", name, "Log Email");
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

			entry = new SubjectEntry("%%ENV", name, "Report Email");
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
	
	
	private static DOMAILType extractSuccessDoMail(JOBType job){
		List<ONType> onTypes = job.getON();
		if (onTypes != null) {
			for (ONType onType : onTypes) {
				List<Object> doTypes = onType.getDOMAILOrDOOrDOCOND();
				if (doTypes != null) {
					for (Object o : doTypes) {
						if (o instanceof DOMAILType && SUCCESS_CODES.contains(onType.getCODE())) {
							DOMAILType doMail = (DOMAILType)o;
							return doMail;
						}
					}
				}
			}
		}
		return null;
	}
	

	private static String[] split(String string, String delim){
		if(string == null || string.length() == 0){
			return new String[]{};
		}
		
		String[] tokens = string.split(delim);
		String[] results = new String[tokens.length];
		int index = 0;
		for(String s : tokens){
			results[index] = s.trim();
			index++;
		}
		
		return results;
	}
	
	
	private static String[] substituteEmails(String[] emails){
		
		String[] results = new String[emails.length];
		int index = 0;
		for(String email : emails){
			String val = emailToTokenMap.get(email.toLowerCase());
			if(val == null){
				throw new RuntimeException("Unknown email: " + email);
			}
			results[index] = val;
			index++;
		}
		return results;
	}
	
	
	/*
	 * Hydrate as much of the YAML from the Control-M XML as possible
	 */
	private static String getSuccessEmailYaml(ConfigTuple configTuple) {

		// Setup to pull needed info from XML objects
		String newName = configTuple.getNewJobName();
		JOBType job = configTuple.getJob();
		DOMAILType doMail = extractSuccessDoMail(job);
		
		// All entries that will make a complete YAML config
		Set<YamlEntry> entrySet = new TreeSet<YamlEntry>();
		
		YamlEntry entry = new StartEntry();
		entrySet.add(entry);
		entry = new StartEntry();
		entrySet.add(entry);

		entry = new JobNameEntry(newName);
		entrySet.add(entry);

		entry = new OutputEntry();
		entrySet.add(entry);

		// TargetEntry: "start"
		// ****************************************************************
		TargetEntry targetEntry = new TargetEntry(3, "start");

		entry = new SubjectEntry("%%ENV", newName, "Start Notice");
		targetEntry.addSubEntry(entry);

		entry = new MessageEntry("");
		targetEntry.addSubEntry(entry);
		
		entry = new ToRecipientsEntry("KATT_AUTOMATION_ADDRESS");
		targetEntry.addSubEntry(entry);

		entry = new CcRecipientsEntry(new String[]{});
		targetEntry.addSubEntry(entry);

		entry = new AttachmentsDirEntry(new String[] {});
		targetEntry.addSubEntry(entry);

		entry = new AttachmentsGlobsEntry(new String[] {});
		targetEntry.addSubEntry(entry);

		entrySet.add(targetEntry);

		// TargetEntry: "log"
		// ****************************************************************
		targetEntry = new TargetEntry(4, "log");

		entry = new SubjectEntry("%%ENV", newName, "Logs Email");
		targetEntry.addSubEntry(entry);

		String msg = doMail.getMESSAGE();
		entry = new MessageEntry("Please find the attached log(s) for the  '" + newName + "' process.");
		targetEntry.addSubEntry(entry);

		entry = new ToRecipientsEntry("KATT_AUTOMATION_ADDRESS");
		targetEntry.addSubEntry(entry);

		entry = new CcRecipientsEntry(new String[] {});
		targetEntry.addSubEntry(entry);

		entry = new AttachmentsDirEntry("logs");
		targetEntry.addSubEntry(entry);

		entry = new AttachmentsGlobsEntry(new String[] {job.getJOBNAME().toLowerCase() + ".log"});
		targetEntry.addSubEntry(entry);

		entrySet.add(targetEntry);

		
		// TargetEntry: "success"
		// ****************************************************************
		targetEntry = new TargetEntry(5, "success");
		
		String subj = replaceTokens(job, doMail.getSUBJECT());
		entry = new SubjectEntry("%%ENV", newName, subj);
		targetEntry.addSubEntry(entry);

		msg = doMail.getMESSAGE();
		msg = replaceTokens(job, msg);
		entry = new MessageEntry(msg);
		targetEntry.addSubEntry(entry);
		
		String toRecipientString = replaceTokens(job, doMail.getDEST());
		String[] toRecipients = split(toRecipientString, EMAIL_DELIM);
		toRecipients = substituteEmails(toRecipients);
		entry = new ToRecipientsEntry(toRecipients);
		targetEntry.addSubEntry(entry);

		
		String ccRecipientString = doMail.getCCDEST();
		if(ccRecipientString != null){
			ccRecipientString = replaceTokens(job, doMail.getCCDEST());
			String[] ccRecipients = split(ccRecipientString, EMAIL_DELIM);;
			ccRecipients = substituteEmails(ccRecipients);
			entry = new CcRecipientsEntry(ccRecipients);
		}else{
			entry = new CcRecipientsEntry(new String[]{});
		}
		targetEntry.addSubEntry(entry);

		entry = new AttachmentsDirEntry("logs");
		targetEntry.addSubEntry(entry);

		entry = new AttachmentsGlobsEntry(job.getJOBNAME().toLowerCase() + ".log");
		targetEntry.addSubEntry(entry);

		entrySet.add(targetEntry);

		StringBuffer sb = new StringBuffer();
		for (YamlEntry newEntry : entrySet) {
			sb.append(newEntry);
		}

		return sb.toString();

	}
	
	
	private static String[] replaceTokens(JOBType job,  String[] strings){

		
		// Hydrate map of key-value pairs built from "AUTOEDIT2" elements 
		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("%%JOBNAME", job.getJOBNAME());
		List<AUTOEDIT2Type> entries = job.getAUTOEDIT2();
		for(AUTOEDIT2Type entry : entries){
			String key = entry.getNAME();
			String value = entry.getValueAttribute();
			tokenMap.put(key, value);
		}
		
		
		String[] results = new String[strings.length];
		for(int i = 0; i < strings.length; i++){
			results[i] = strings[i];
		}
		
		
		// Replace each entry of tokenMap in commandString
		for (int i = 0; i < strings.length; i++) {
			
			String string = strings[i];
			
			for (Entry<String, String> entry : tokenMap.entrySet()) {
				
				String key = entry.getKey();
				String value = entry.getValue();
				
				if (string.contains(key)) {
					results[i] = string.replace(key, value);
				}
			}

		}

		return results;
	}
	
	
	// Helper method to replace tokens in a Control-M CMDLINE string
	private static String replaceTokens(JOBType job,  String tokenString){

		String result = new String(tokenString);
		
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
	
	private static Map<String, String> getNewToOldNameMap(){
		Map<String, String> oldToNewNameMap = getOldToNewNameMap();
		Map<String, String> newToOldNameMap = new TreeMap<String, String>();
		for(Entry<String, String> entry : oldToNewNameMap.entrySet()){
			String legacyName = entry.getKey();
			String newName = entry.getValue();
			if(legacyName == null || legacyName.equals("--")){
				continue;
			}
			newToOldNameMap.put(newName, legacyName);
		}
		return newToOldNameMap;
	}
	
	/*
	 * Return all names for new Control-M jobs *and* job-streams
	 * that contain the string "-DLV-".
	 */
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
	
	
	/*
	 * Find all unique "ON" code attributes and print to stdout
	 */
	public static void printUniqueOnCodes(){
		
		Set<String> onCodeSet = new TreeSet<String>();
		
		List<JOBType> jobs = getAllJobs();
		for(JOBType job : jobs){
			List<ONType> onTypes = job.getON();
			if(onTypes != null){
				for(ONType onType : onTypes){
					List<Object>  doTypes = onType.getDOMAILOrDOOrDOCOND();
					if(doTypes != null){
						for(Object o : doTypes){
							if(o instanceof DOMAILType){
								onCodeSet.add(onType.getCODE());
								break;
							}
						}
					}
				}
			}
		}
		
		for(String code : onCodeSet){
			System.out.printf("\"%s\"%n", code);
		}
		
		
	}
	
	/*
	 * Find all jobs that have "DLV" in its title, and then
	 * only keep those that have a child DOMAIL element *and*
	 * a "success" code associated with the DOMAIL's parent.
	 */
	public static void printNewDlvJobNames(){
		
		Map<String, String> oldToNewNameMap = getOldToNewNameMap();
		
		// Loop through all jobs
		List<JOBType> jobs = getAllJobs();
		for(JOBType job : jobs){
			
			// Ensure job has "ON" elements
			List<ONType> onTypes = job.getON();
			if(onTypes != null){
				
				// Loop through all "ON" elements
				for(ONType onType : onTypes){
					
					// Get the "DO*" elements
					List<Object>  doTypes = onType.getDOMAILOrDOOrDOCOND();
					if(doTypes != null){
						
						// Look for DOMAIL elements, check that the "ON @code" is in "success" indicators
						for(Object o : doTypes){
							
							if(o instanceof DOMAILType){
								String code = onType.getCODE();
								if(SUCCESS_CODES.contains(code)){
									String legacyName = job.getJOBNAME();
									String newName = oldToNewNameMap.get(legacyName);
									if(newName == null){
										newName = "NONE";
									}
									System.out.printf("%s,%s%n", legacyName, newName);
								}//if success code
								
							}//if DOMAIL
							
						}//for doTypes
						
					}//if doType != null
					
				}//for onTypes
				
			}//if onTypes != null
			
		}//for jobs
		
		
	}//printNewDlvJobNames()
	
	public static void main(String[] sgs) {
		//YamlGenerator.generateStubYaml();
		//findMuttJobs(true);
		//generateCmdLineYaml();
		//generateCmdLineFile(true);
		//printUniqueOnCodes();
		//printNewDlvJobNames();
		generateSuccessEmailYaml();
	}

}
