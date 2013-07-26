package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class CcRecipientsEntry extends ToRecipientsEntry {
	public CcRecipientsEntry(String...args){
		super(YamlFormatConstants.CC_RECIPIENT_ENTRY_TEMPLATE, args);
	}
}
