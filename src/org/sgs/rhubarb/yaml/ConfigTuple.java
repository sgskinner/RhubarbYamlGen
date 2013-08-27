package org.sgs.rhubarb.yaml;

import org.sgs.controlm.JOBType;

public class ConfigTuple implements Comparable<ConfigTuple>{
	
	private String legacyJobName;
	private String newJobName;
	private JOBType job;
	
	public ConfigTuple(){
		//
	}

	public ConfigTuple(String legacyJobName, String newJobName, JOBType job){
		this.legacyJobName = legacyJobName;
		this.newJobName = newJobName;
		this.job = job;
	}
	
	public String getLegacyJobName() {
		return legacyJobName;
	}

	public void setLegacyJobName(String legacyJobName) {
		this.legacyJobName = legacyJobName;
	}

	public String getNewJobName() {
		return newJobName;
	}

	public void setNewJobName(String newJobName) {
		this.newJobName = newJobName;
	}

	public JOBType getJob() {
		return job;
	}

	public void setJob(JOBType job) {
		this.job = job;
	}

	@Override
	public int compareTo(ConfigTuple otherTuple) {
		return getLegacyJobName().compareTo(otherTuple.getLegacyJobName());
	}
	
	
}
