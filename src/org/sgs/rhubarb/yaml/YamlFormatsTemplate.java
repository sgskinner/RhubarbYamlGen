package org.sgs.rhubarb.yaml;

public class YamlFormatsTemplate {
	
	String rootFormat;
	String childFormat; // For 1-to-Many entries, e.g., "To:" recipients
	
	// Not all templates have more than just one format
	public YamlFormatsTemplate(String rootFormat){
		this.rootFormat = rootFormat;
	}
	
	// Some entries have mltiple lines that differ from the initial line
	// of the entry -- this constructor allows you to set both.
	public YamlFormatsTemplate(String rootFormat, String childFormat){
		this(rootFormat);
		this.childFormat = childFormat;
	}
	

	/*
	 * Most of the time this is a just a label, e.g. "to:\n", "cc:\n", "message: <\n", etc
	 */
	public String getRootFormat() {
		return rootFormat;
	}

	/*
	 * This format is only used for multi-line entries, e.g., this could be each line
	 * of a message body, or email attachment patterns(one per line), etc.
	 * 
	 * Care should be taken when using this format, as the input variable replacements
	 * should most likely be scrubbed of leading/trailing white space and newlines. This
	 * ensures that the desired indentation and the appropriate line terminators are
	 * present when this format is actual used.
	 * 
	 */
	public String getChildFormat() {
		if(childFormat == null){
			throw new RuntimeException("Additional child format does not exist, please use two arg constructor!");
		}
		return childFormat;
	}

}
