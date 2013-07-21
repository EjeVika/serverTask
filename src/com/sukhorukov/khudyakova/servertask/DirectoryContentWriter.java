package com.sukhorukov.khudyakova.servertask;

import java.io.*;
import java.util.*;

/**

 */
class DirectoryContentWriter {
/*    public static void processDirs(File directory,String htmlFileName) throws IOException {
        if(!directory.isDirectory()){
            return;
        }
        createDirHTML(directory,htmlFileName);

        for(File currentFile: directory.listFiles()){
            processDirs(currentFile,htmlFileName);
        }

    }
*/
    public static void createDirHTML(ByteArrayOutputStream byteArray,File directory,String encoding)  {
        if(directory.isDirectory()){
            File[] dirContent = directory.listFiles();


            List<File> arrayOfFiles = new ArrayList<>();
            List<File> arrayOfDirs = new ArrayList<>();
            for (File aDirContent : dirContent) {
                if (aDirContent.isDirectory()) {
                    arrayOfDirs.add(aDirContent);
                } else {
                    arrayOfFiles.add(aDirContent);
                }
            }
            Collections.sort(arrayOfFiles);
            Collections.sort(arrayOfDirs);
       //     try (OutputStream out = new FileOutputStream(directory.getAbsolutePath()+File.separator+htmlFileName)){

            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(byteArray)) );

            writer.println("<html>");
            writer.println("<head>");
            writer.println("<meta charset="+encoding+">");
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
    //                "<a href = '../index.html'>..</a></td>");     // 4 recur version
                    "<a href = '../'>..</a></td>");
            writer.println("\t\t<td></td>");
            writer.println("\t\t</tr>");


            for (File aDir : arrayOfDirs) {
                writer.println("\t\t<tr>");
                writer.println("\t\t<td>" +
                        //                   "<a href = './"+arrayOfDirs.get(i).getName()+"/index.html'>"+arrayOfDirs.get(i).getName()+"</a></td>");
                        //                      4 recur version
                        "<a href = '" + aDir.getName() + "/'>" + aDir.getName() + "</a></td>");
                writer.println("\t\t<td></td>");
                writer.println("\t\t</tr>");
            }
            for (File aFile : arrayOfFiles) {
                writer.println("\t\t<tr>");
                writer.println("\t\t<td>" +
                        "<a href = \"" + aFile.getName() + "\">" + aFile.getName() + "</a></td>");
                writer.println("\t\t<td>" + aFile.length() + "</td>");
                writer.println("\t\t</tr>");
            }
                writer.println("\t</table>");
                writer.println("</body>");
                writer.println("</html>");

                writer.flush();
         //   }
        }else{
            System.out.println("this path \""+directory.getPath()+"\" doesn't point to directory" );
        }
    }

}
