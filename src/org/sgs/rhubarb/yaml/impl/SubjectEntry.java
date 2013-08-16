package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class SubjectEntry extends YamlEntry {

	public SubjectEntry(String env, String jobName, String description) {
		super(0, YamlFormatConstants.SUBJECT_ENTRY_TEMPLATE, new String[]{env, jobName, description});
	}

	@Override
	public String getFormattedEntry() {
		String format = super.getFormatsTemplate().getRootFormat();
		return String.format(format, (Object[])super.getArgs());
	}

}
