package org.sgs.rhubarb.yaml.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class FileReader extends Reader {
    private java.io.FileReader fileReader;

    public FileReader(final File file) {
        try{
            fileReader = new java.io.FileReader(file);
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException{
        fileReader.close();
    }

    @Override
    public boolean equals(final Object obj){
        return fileReader.equals(obj);
    }

    @Override
    public int hashCode(){
        return fileReader.hashCode();
    }

    @Override
    public void mark(final int readAheadLimit) throws IOException{
        fileReader.mark(readAheadLimit);
    }

    @Override
    public boolean markSupported(){
        return fileReader.markSupported();
    }

    @Override
    public int read() throws IOException{
        return fileReader.read();
    }

    @Override
    public int read(final char[] arg0, final int arg1, final int arg2) throws IOException{
        return fileReader.read(arg0, arg1, arg2);
    }

    @Override
    public int read(final char[] cbuf) throws IOException{
        return fileReader.read(cbuf);
    }

    @Override
    public int read(final CharBuffer target) throws IOException{
        return fileReader.read(target);
    }

    @Override
    public boolean ready() throws IOException{
        return fileReader.ready();
    }

    @Override
    public void reset() throws IOException{
        fileReader.reset();
    }

    @Override
    public long skip(final long arg0) throws IOException{
        return fileReader.skip(arg0);
    }

    @Override
    public String toString(){
        return fileReader.toString();
    }

}
