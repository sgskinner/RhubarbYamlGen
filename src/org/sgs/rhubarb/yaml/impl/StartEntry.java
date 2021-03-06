package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlFormatsTemplate;
import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class StartEntry extends YamlEntry {

	
	
	public StartEntry() {
		super(0, new YamlFormatsTemplate(YamlFormatConstants.FIRST_ENTRY_TEMPLATE.getRootFormat()), new String[]{});
	}

	@Override
	protected String getFormattedEntry() {
		String format = super.getFormatsTemplate().getRootFormat();
		return String.format(format);
	}

}
