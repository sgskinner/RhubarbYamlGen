package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class AttachmentsDirEntry extends MultiLineEntry {

	public AttachmentsDirEntry(String...dirPaths) {
		super(8, YamlFormatConstants.ATTACHMENTS_DIR_ENTRY_TEMPLATE, dirPaths);
	}
	
}
