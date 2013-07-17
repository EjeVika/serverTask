package com.sukhorukov.khudyakova.servertask;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 13.07.13
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class ServerProcess implements Runnable{
    private Socket socket;
    private File directory;
    private File currentDir;
    private String outFileName;


    public ServerProcess(Socket socket,File directory, String outFileName) throws IOException {
        this.socket = socket;
        this.directory = directory;
        this.currentDir = directory;
        this.outFileName = outFileName;
    }

    @Override
    public void run() {

        InputStream in=null;
        OutputStream out=null;
        try{
            in = socket.getInputStream();
            out = socket.getOutputStream();

            StringBuilder sb = new StringBuilder();
            int c;
            while((c =in.read())!=-1 && c!=10 && c!=13){
                sb.append((char)c);
            }
            String data = sb.toString();
            String args[] = data.split(" ");
            String cmd = args[0].trim().toUpperCase();
            String wantedPath = args[1];
            System.out.println("wantedPath = "+wantedPath);

            switch (cmd){
                case "GET":get(wantedPath,out);break;
                case "HEAD":head();break;
                default: notImpl501();
            }


        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in!=null) in=null;
            if (out!=null) out=null;
        }

    }

    private void notImpl501() {

    }

    private void get(String wantedPath,OutputStream out) throws IOException {

        File temp = new File(wantedPath);
        File wantedFile = null;
        if (temp.isDirectory()){
            DirectoryContentWriter.createDirHTML(new File(directory.getCanonicalPath()+File.separator+temp),outFileName);
            wantedFile = new File(directory.getCanonicalPath()+File.separator+outFileName);
        }else if (temp.getName().equals(outFileName)){
            DirectoryContentWriter.createDirHTML(new File(directory.getCanonicalPath()+File.separator+temp.getParentFile()),outFileName);
            wantedFile = new File(directory.getCanonicalPath()+File.separator+temp);
        }else{
            wantedFile = new File(directory.getCanonicalPath()+File.separator+temp);
        }
     /*   String fileName = temp.getName();
        File wantedFile = new File(directory.getCanonicalPath()+File.separator+temp);
        if (fileName.equals(outFileName)){
            DirectoryContentWriter.createDirHTML(temp,outFileName);
            wantedFile = new File(directory.getCanonicalPath()+File.separator+temp);
        }  */
/*        System.out.println("temp = "+temp);
        System.out.println("temp.getCanonicalPath() = "+temp.getCanonicalPath());
        System.out.println("temp.getName() = "+temp.getName());
        System.out.println("currentDir="+currentDir);
        System.out.println("currentDir.getCanonicalPath() = "+currentDir.getCanonicalPath());
        System.out.println("currentDir.getAbsolutePath() = "+currentDir.getAbsolutePath());
        File wantedFile = new File(currentDir.getCanonicalPath()+File.separator+temp);
    //    File wantedFile = new File(".."+File.separator+temp);
        System.out.println("wantedPath = "+wantedPath);
        System.out.println("wantedFile = "+wantedFile);
        System.out.println("wantedFile.getCanonicalPath() = "+wantedFile.getCanonicalPath());
        if (wantedFile.isDirectory()){
            this.currentDir = wantedFile;
            System.out.println("currentDir = "+currentDir);
            DirectoryContentWriter.createDirHTML(wantedFile,outFileName);
            wantedFile = new File(wantedFile.getAbsolutePath()+File.separator+outFileName);
            System.out.println(wantedFile.getCanonicalPath());
        }
        //System.out.println(wantedPath);
        //    DirectoryContentWriter.createDirHTML(directory,outFileName);
        //    File indexFile = new File(directory.getAbsolutePath()+File.separator+outFileName);
*/
        out.write("HTTP/1.0 200 OK\r\n".getBytes());

        out.write("Content-Type: text; charset=UTF-8\r\n".getBytes());
        out.write(("Content-Length: " + wantedFile.length() + "\r\n").getBytes());

        out.write("\r\n".getBytes());


        out.flush();
        try (InputStream inIndex = new FileInputStream(wantedFile)){

            byte buf[] = new byte[4096];
            int count;
            while((count = inIndex.read(buf))>=0){
                out.write(buf, 0, count);
            }
        }
    }
    private void head(){

    }

}

