package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class OutputEntry extends YamlEntry {

	
	
	public OutputEntry() {
		super(1, YamlFormatConstants.OUTPUT_ENTRY_TEMPLATE, new String[]{});
	}

	@Override
	protected String getFormattedEntry() {
		// Pass through formatter to replace '%n'
		String format = super.getFormatsTemplate().getRootFormat(); 
		return String.format(format);
	}
	
}
