package com.sukhorukov.khudyakova.servertask;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ТСД
 * Date: 13.07.13
 * Time: 0:06
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryContentWriter {
    public static void processDirs(File directory,String htmlFileName) throws IOException {
        if(!directory.isDirectory()){
            return;
        }
        createDirHTML(directory,htmlFileName);

        for(File currentFile: directory.listFiles()){
            processDirs(currentFile,htmlFileName);
        }

    }
    public static void createDirHTML(File directory,String htmlFileName) throws IOException {
        if(directory.isDirectory()){
            File[] dirContent = directory.listFiles();
            int numberOfFiles = dirContent.length;
            List<File> arrayOfFiles = new ArrayList<>();
            List<File> arrayOfDirs = new ArrayList<>();
            for (int i=0;i<numberOfFiles;i++){
                if (dirContent[i].isDirectory()){
                    arrayOfDirs.add(dirContent[i]);
                }else{
                    arrayOfFiles.add(dirContent[i]);
                }
            }
            Collections.sort(arrayOfFiles);
            Collections.sort(arrayOfDirs);
            try (OutputStream out = new FileOutputStream(directory.getAbsolutePath()+File.separator+htmlFileName)){
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)) );

                writer.println("<html>");
                writer.println("<head>");
                writer.println("<title>"+directory.getName()+"</title>");
                writer.println("</head>");
                writer.println("<body>");
                writer.println("\t<table cellpadding=10>");
                writer.println("\t\t<tr>");
                writer.println("\t\t<td>NAME</td>");
                writer.println("\t\t<td>SIZE</td>");
                writer.println("\t\t</tr>");
                writer.println("\t\t<tr>");
                writer.println("\t\t<td>" +
                        "<a href = '../index.html'>..</a></td>");
                writer.println("\t\t<td></td>");
                writer.println("\t\t</tr>");

/*                for (int i=1;i<=numberOfFiles;i++){
                    if (dirContent[i-1].isDirectory()){
                        writer.println("\t\t<tr>");
                        writer.println("\t\t<td>" +
                                "<a href = \""+dirContent[i-1]+"\">"+dirContent[i-1].getName()+"</a></td>");
                        writer.println("\t\t<td></td>");
                        writer.println("\t\t</tr>");
                    }else{
                        writer.println("\t\t<tr>");
                        writer.println("\t\t<td>" +
                                "<a href = \""+dirContent[i-1]+"\">"+dirContent[i-1].getName()+"</a></td>");
                        writer.println("\t\t<td>"+dirContent[i-1].getUsableSpace()+"</td>");
                        writer.println("\t\t</tr>");
                    }
                }
*/
                for (int i=0; i<arrayOfDirs.size();i++){
                    writer.println("\t\t<tr>");
                    writer.println("\t\t<td>" +
                            "<a href = '"+arrayOfDirs.get(i).getName()+"/index.html'>"+arrayOfDirs.get(i).getName()+"</a></td>");
                    writer.println("\t\t<td></td>");
                    writer.println("\t\t</tr>");
                }
                for (int i=0; i<arrayOfFiles.size();i++){
                    writer.println("\t\t<tr>");
                    writer.println("\t\t<td>" +
                            "<a href = \""+arrayOfFiles.get(i).getName()+"\">"+arrayOfFiles.get(i).getName()+"</a></td>");
                    writer.println("\t\t<td>"+arrayOfFiles.get(i).length()+"</td>");
                    writer.println("\t\t</tr>");
                }
                writer.println("\t</table>");
                writer.println("</body>");
                writer.println("</html>");

                writer.flush();
            }
        }else{
            System.out.println("this path \""+directory.getPath()+"\" doesn't point to directory" );
        }
    }

}
