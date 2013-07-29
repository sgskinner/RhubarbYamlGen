package org.sgs.rhubarb.yaml.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;



public class FileUtils{
	
	
	public static String[] getLinesAsArray(String filename) {
		List<String> lineList = getLines(filename);
		return lineList.toArray(new String[lineList.size()]);
	}
	
    public static List<String> getLines(final String filename){
        List<String> lines = new LinkedList<String>();
        LineIterator lineIter = getLineIterator(filename);
        while(lineIter.hasNext()){
            lines.add(lineIter.next());
        }
        return lines;
    }


    public static LineIterator getLineIterator(final String fileName){
        return getLineIterator(new File(fileName));
    }


    public static LineIterator getLineIterator(final File file){
        return IOUtils.lineIterator(new FileReader(file));
    }


    public static void writeCollectionToFile(final String filename, final Collection<String> strings){
        Writer writer;
        try{
            writer = new FileWriter(new File(filename));
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        try{
            IOUtils.writeLines(strings, "\n", writer);
            writer.flush();
        }catch(IOException e){
            throw new RuntimeException(e);
        }finally{
            IOUtils.closeQuietly(writer);
        }
    }
}
