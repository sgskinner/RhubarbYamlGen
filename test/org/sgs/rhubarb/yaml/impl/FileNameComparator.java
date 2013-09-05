package org.sgs.rhubarb.yaml.impl;

import java.io.File;
import java.util.Comparator;

public class FileNameComparator implements Comparator<File>{

	@Override
	public int compare(File file, File otherFile) {
		String fileName = file.getName();
		String otherFileName = otherFile.getName();
		return fileName.compareTo(otherFileName);
	}

}
