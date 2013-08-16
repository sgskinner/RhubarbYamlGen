package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;
import org.sgs.rhubarb.yaml.YamlFormatsTemplate;

public class MessageEntry extends YamlEntry {

	// Each arg should be one line of the message, no newline needed
	public MessageEntry(String...args) {
		super(5, YamlFormatConstants.MESSAGE_ENTRY_TEMPLATE, args);
		if(args == null || args.length < 1){
			throw new RuntimeException("Must supply at least one line for message!");
		}
	}

	@Override
	public String getFormattedEntry() {
		
		YamlFormatsTemplate formatsTemplate = super.getFormatsTemplate();
		String msgLabelFormat = formatsTemplate.getRootFormat();
		String msgLineFormat = formatsTemplate.getChildFormat();
		
		// Build our result
		StringBuffer sb = new StringBuffer();
		
		// Use formatting to add system newline, no other args though, this is just a label
		sb.append(String.format(msgLabelFormat));
		
		// Loop through all lines of message, and format them for new
		// lines and for proper indentation
		for(String line : super.getArgs()){
			line = line.replace("[\r\n]+$", ""); // we want our own newlines, not yours!
			line = line.trim(); // allow enforcement of our own indentation in next step
			sb.append(String.format(msgLineFormat, line));
		}
		
		return sb.toString();
	}

}
