package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatsTemplate;

public class MultiLineEntry extends YamlEntry {

	// Allow subclasses to define different formats
	protected MultiLineEntry (int ordinal, YamlFormatsTemplate formatsTemplate, String...args){
		super(ordinal, formatsTemplate, args);
	}
	
	@Override
	protected String getFormattedEntry() {
		
		YamlFormatsTemplate formatsTemplate = super.getFormatsTemplate();
		String toLabelFormat = formatsTemplate.getRootFormat();
		String recipientEntryFormat = formatsTemplate.getChildFormat();
		
		// Build our result
		StringBuffer sb = new StringBuffer();
		
		// Use formatting to add system newline, no other args though, this is just a label
		sb.append(String.format(toLabelFormat));
		
		// Loop through all "to" recipients, and format each address for new
		// lines and for proper indentation/line-start
		for(String line : super.getArgs()){
			line = line.replace("[\r\n]+$", ""); // we want our own newlines, not yours!
			line = line.trim(); // allow enforcement of our own indentation in next step
			sb.append(String.format(recipientEntryFormat, line));
		}
		
		return sb.toString();
	}

}
