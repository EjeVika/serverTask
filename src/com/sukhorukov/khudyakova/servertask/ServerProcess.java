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
    private String outFileName;


    public ServerProcess(Socket socket,File directory, String outFileName) throws IOException {
        this.socket = socket;
        this.directory = directory;
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
                case "HEAD":head(wantedPath,out);break;
                default: notImpl501(out);
            }


        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in!=null) in=null;
            if (out!=null) out=null;
        }

    }

    private void notImpl501(OutputStream out) throws IOException {

        out.write("HTTP/0.9 501 Not Implemented\r\n".getBytes());
        out.write("Content-Type: text; charset=UTF-8\r\n".getBytes());
    //    out.write(("Content-Length: " + wantedFile.length() + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.flush();
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

        out.write("HTTP/0.9 200 OK\r\n".getBytes());
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
    //    out.flush();
    }

    private void head(String wantedPath, OutputStream out) throws IOException {
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
        out.write("HTTP/0.9 200 OK\r\n".getBytes());
        out.write("Content-Type: text; charset=UTF-8\r\n".getBytes());
        out.write(("Content-Length: " + wantedFile.length() + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }

}

