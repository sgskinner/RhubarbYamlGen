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
import org.sgs.rhubarb.yaml.impl.OutputNameEntry;
import org.sgs.rhubarb.yaml.impl.StartEntry;
import org.sgs.rhubarb.yaml.impl.SubjectEntry;
import org.sgs.rhubarb.yaml.impl.ToRecipientsEntry;
import org.sgs.rhubarb.yaml.utils.FileUtils;



public class YamlGenerator {
	
	public static String generateJobYaml(String filePrefix, JOBType job){
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

		value = "report";
		entry = new OutputNameEntry(value);
		entrySet.add(entry);

		entry = new StartEntry();
		entrySet.add(entry);

		valueArray = new String[]{"DEV", filePrefix, "Automated Test Email"};
		entry = new SubjectEntry(valueArray[0], valueArray[1], valueArray[2]);
		entrySet.add(entry);

		valueArray = new String[]{"katt-automation@list.arizona.edu",
	                               "jwingate@email.arizona.edu",
	                               "hlo@email.arizona.edu",
	                               "shaloo@email.arizona.edu",
	               					"sskinner@email.arizona.edu"};
		entry = new ToRecipientsEntry(valueArray);
		entrySet.add(entry);
		
		StringBuffer sb = new StringBuffer();
		for(YamlEntry newEntry : entrySet) {
			sb.append(newEntry);
		}
		String actualResult = sb.toString();
		
		return actualResult;
	}
	
	
	
	public static void main(String[] sgs) {
		
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

}
