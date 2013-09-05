package org.sgs.rhubarb.yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.sgs.rhubarb.yaml.utils.FileUtils;
import org.yaml.snakeyaml.Yaml;

/*
 * A class to perform YAML deserialization, serialization, validation, and
 * manipulations. 
 */
public class YamlBridge {
	
	private Yaml yamlHandler;
	
	
	public YamlBridge(){
		yamlHandler = new Yaml();
	}
	
	
	public void vaildate(String filename){
		File file = new File(filename);
		validate(file);
	}
	
	public void validate(File file){
		read(file);
	}
	
	public Object read(File yamlFile){
		InputStream is = FileUtils.getFileInputStream(yamlFile);
		Object o = yamlHandler.load(is);
		return o;
	}
	
	public void write(String outputFilename, Object outputYaml){
		
		File file = new File(outputFilename);
		
		Writer writer = null;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			yamlHandler.dump(outputYaml, writer);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

}
