package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class JobNameEntry extends YamlEntry {

	public JobNameEntry(String name) {
		super(2, YamlFormatConstants.JOB_NAME_ENTRY_TEMPLATE, name);
	}

	@Override
	protected String getFormattedEntry() {
		String format = super.yamlFormatTemplate.getPrimaryLine();
		String[] args = super.getArgs();
		return String.format(format, args[0]);
	}

}
