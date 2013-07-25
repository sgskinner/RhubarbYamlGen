package org.sgs.rhubarb.yaml;

public class YamlFormatsTemplate {
	
	String primaryLineFormat;
	String additionalLinesFormat; // For 1-to-Many entries, e.g., "To:" recipients
	
	
	public YamlFormatsTemplate(String primaryLine, String additionalLineStartFormat){
		this(primaryLine);
		this.additionalLinesFormat = additionalLineStartFormat;
	}
	
	public YamlFormatsTemplate(String primaryLine){
		this.primaryLineFormat = primaryLine;
	}

	public String getPrimaryLine() {
		return primaryLineFormat;
	}

	public String getSecondaryLine() {
		if(additionalLinesFormat == null){
			throw new RuntimeException("Additional lines format does not exist, please use two arg constructor!");
		}
		return additionalLinesFormat;
	}

}
