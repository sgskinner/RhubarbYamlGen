package org.sgs.rhubarb.yaml.impl;

import java.util.Set;
import java.util.TreeSet;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class TargetEntry extends YamlEntry {

	private Set<YamlEntry> subEntries;
	
	public TargetEntry(int position, String targetName) {
		super(position, YamlFormatConstants.TARGET_NAME_ENTRY_TEMPLATE, targetName);
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
		String[] args = super.getArgs();
		String targetLine = String.format(targetLineFormat, args[0]);
		sb.append(targetLine);
		
		// Loop through all sub-entries, which end up as the YAML hash key/value pairs
		for(YamlEntry subEntry : subEntries){
			sb.append(subEntry.getFormattedEntry());
		}
		
		return sb.toString();
	}
	
	@Override
	public String toString(){
		return getFormattedEntry();
	}

}
