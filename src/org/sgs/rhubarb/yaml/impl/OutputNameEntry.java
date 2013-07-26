package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class OutputNameEntry extends YamlEntry {

	public OutputNameEntry(String outputName) {
		super(3, YamlFormatConstants.OUTPUT_NAME_ENTRY_TEMPLATE, outputName);
	}

	@Override
	protected String getFormattedEntry() {
		String format = super.getFormatsTemplate().getPrimaryLine();
		return String.format(format, super.getArgs()[0]);
	}

}
