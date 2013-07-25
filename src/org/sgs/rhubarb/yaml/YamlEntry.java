package org.sgs.rhubarb.yaml;

public abstract class YamlEntry implements Comparable<YamlEntry>{
	
	private final int ordinal;
	protected final YamlFormatsTemplate yamlFormatTemplate;
	protected String[] args;
	
	public YamlEntry(int ordinal, YamlFormatsTemplate yamlFormatTemplate, String...args){
		this.ordinal = ordinal;
		this.yamlFormatTemplate = yamlFormatTemplate;
		this.args = args;
	}
	
	/*
	 * Override this with logic to populate yamlFormatTemplate with args and
	 * generate populated string to return (could be multi-line).
	 */
	protected abstract String getFormattedEntry();
	
	
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
