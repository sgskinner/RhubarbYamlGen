package org.sgs.rhubarb.yaml;

public abstract class YamlEntry implements Comparable<YamlEntry>{
	
	private final int ordinal;
	private final YamlFormatsTemplate yamlFormatsTemplate;
	private String[] args;
	
	public YamlEntry(int ordinal, YamlFormatsTemplate yamlFormatsTemplate, String...args){
		this.ordinal = ordinal;
		this.yamlFormatsTemplate = yamlFormatsTemplate;
		this.args = args;
	}
	
	/*
	 * Override this with logic to populate yamlFormatTemplate with args and
	 * generate populated string to return (could be multi-line).
	 */
	protected abstract String getFormattedEntry();
	
	
	protected YamlFormatsTemplate getFormatsTemplate(){
		return yamlFormatsTemplate;
	}
	
	
	protected String[] getArgs(){
		return args;
	}
	
	
	public int getOrdinal() {
		return ordinal;
	}
	
	
	@Override
	public int compareTo(YamlEntry otherYamlLine) {
		if(this.getOrdinal() < otherYamlLine.getOrdinal()){
			return -1;
		}else if(this.getOrdinal() > otherYamlLine.getOrdinal()){
			return 1;
		}else{
			return 0;
		}
	}

}
