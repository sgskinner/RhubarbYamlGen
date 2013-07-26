package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlEntry;
import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class AttachmentsDirEntry extends YamlEntry {

	public AttachmentsDirEntry(String dirPath) {
		super(8, YamlFormatConstants.ATTACHMENTS_DIR_ENTRY_TEMPLATE, new String[]{dirPath});
	}

	@Override
	protected String getFormattedEntry() {
		String format = super.getFormatsTemplate().getRootFormat(); 
		return String.format(format, getArgs()[0]);
	}
	
}
