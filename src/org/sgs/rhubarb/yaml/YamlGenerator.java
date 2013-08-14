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
import org.sgs.rhubarb.yaml.utils.FileUtils;



public class YamlGenerator {
	
	public static void generateJobYaml(JOBType job){
		
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
		
		int count = 0;
		for(Entry<String, String> entry : foundJobNamesMap.entrySet()) {
			String fileName = entry.getKey();
			String oldName = entry.getValue();
			for(JOBType job : allJobs){
				if(job.getJOBNAME().equals(oldName)){
					count++;
					System.out.printf("%s: %s%n", fileName, oldName);
					break;
				}
			}
		}
		
		System.out.printf("# found: %d%n", count);
		
		
	}

}
