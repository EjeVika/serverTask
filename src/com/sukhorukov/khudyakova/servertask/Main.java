package com.sukhorukov.khudyakova.servertask;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ТСД
 * Date: 12.07.13
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws IOException {
    //    String dirName = "111" ;
        String dirName = "";
        String outFileName = "index.html";
        if (args.length<1) {
            dirName = "testDir";
        }else{
            dirName = args[0];
        }


        File directory = new File(dirName);
        DirectoryContentWriter.createDirHTML(directory,outFileName);


    }
}
