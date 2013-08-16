package org.sgs.rhubarb.yaml.impl;

import org.sgs.rhubarb.yaml.YamlFormatConstants;

public class ToRecipientsEntry extends MultiLineEntry {
	public ToRecipientsEntry(String...args) {
		super(2, YamlFormatConstants.TO_RECIPIENTS_ENTRY_TEMPLATE, args);
	}
}
