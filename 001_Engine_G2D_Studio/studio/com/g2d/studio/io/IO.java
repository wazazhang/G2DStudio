package com.g2d.studio.io;


public interface IO 
{
    public File createFile(String pathname) ;

    public File createFile(File parent, String child);
}
