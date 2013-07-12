package com.sukhorukov.khudyakova.servertask;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: ТСД
 * Date: 13.07.13
 * Time: 0:06
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryContentWriter {
    public static void createDirHTML(File directory,String htmlFileName) throws IOException {
        if(directory.isDirectory()){
            File[] dirContent = directory.listFiles();
            int numberOfFiles = dirContent.length;
            try (OutputStream out = new FileOutputStream(htmlFileName)){
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
                        "<a href = \""+directory.getParent()+"\">..</a></td>");
                writer.println("\t\t<td></td>");
                writer.println("\t\t</tr>");
                for (int i=1;i<=numberOfFiles;i++){
                    if (dirContent[i-1].isDirectory()){
                     //   StringBuilder htmlName = new StringBuilder();
                     //   htmlName = htmlFileName.split(".")[0]+dirContent[i-1].toString()+".html";

                        String htmlName = htmlFileName.split("\\.")[0]+dirContent[i-1].getName()+".html";
                        System.out.println(htmlName);
                        DirectoryContentWriter.createDirHTML(dirContent[i-1],htmlName);
                        writer.println("\t\t<tr>");
                        writer.println("\t\t<td>" +
                                "<a href = \""+htmlName+"\">"+dirContent[i-1].getName()+"</a></td>");
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
