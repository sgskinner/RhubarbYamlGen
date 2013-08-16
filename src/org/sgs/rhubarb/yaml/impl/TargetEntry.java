package org.sgs.rhubarb.yaml.impl;

import java.util.Set;
import java.util.TreeSet;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatsTemplate;

public class TargetEntry extends YamlEntry {

	private String targetName;
	private Set<YamlEntry> subEntries;
	
	public TargetEntry(int position, String targetName) {
		super(position, new YamlFormatsTemplate("  %s:%n"));
		this.targetName = targetName;
		this.subEntries = new TreeSet<YamlEntry>();
	}

	public void addSubEntry(YamlEntry subEntry){
		subEntries.add(subEntry);
	}
	
	/*
	 * This should be the target name followed by all the target's
	 * sub-entries. 
	 */
	@Override
	public String getFormattedEntry() {
		
		StringBuffer sb = new StringBuffer();
		
		// Add target name line, this acts as a key to the hash of sub-entries on the YAML side
		String targetLineFormat = super.getFormatsTemplate().getRootFormat();
		String targetLine = String.format(targetLineFormat, targetName);
		sb.append(targetLine);
		
		// Loop through all sub-entries, which end up as the YAML hash key/value pairs
		for(YamlEntry subEntry : subEntries){
			sb.append(subEntry.getFormattedEntry());
		}
		
		return sb.toString();
	}

}
