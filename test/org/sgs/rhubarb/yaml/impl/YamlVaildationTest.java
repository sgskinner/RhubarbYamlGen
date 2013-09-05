package org.sgs.rhubarb.yaml.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.sgs.rhubarb.yaml.YamlBridge;

public class YamlVaildationTest {
	
	@Test
	public void testYamlValidity() {
		YamlBridge yaml = new YamlBridge();
		File yamlDir = new File("data/output/final_yaml");
		Set<File> fileSet = new TreeSet<File>(new FileNameComparator());
		fileSet.addAll(Arrays.asList(yamlDir.listFiles()));
		
		
		for(File file : fileSet){
			try {
				yaml.validate(file);
			} catch (Exception e) {
				System.out.printf("'%s'failed validation.%n", file.getName());
				System.out.printf("%s%n", e.getMessage());
				throw e;
			}
		}
	}
}
