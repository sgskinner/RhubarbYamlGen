package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlFormatConstants;


public class AttachmentsGlobsEntry extends MultiLineEntry {
	public AttachmentsGlobsEntry(String...args) {
		super(9, YamlFormatConstants.ATTACHMENTS_GLOBS_ENTRY_TEMPLATE, args);
	}
}
