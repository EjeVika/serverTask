package com.sukhorukov.khudyakova.servertask;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
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
    private String encoding;

    private String fileType;
    private long fileSize;
    private File wantedFile;
    private ByteArrayOutputStream  byteArray;


    public ServerProcess(Socket socket,File directory, String outFileName) throws IOException {
        this.socket = socket;
        this.directory = directory;
        this.outFileName = outFileName;
        this.encoding = "UTF-8";

        this.fileType = "";
        this.fileSize = 0;
        this.wantedFile = null;
        this.byteArray = new ByteArrayOutputStream();
    }

    @Override
    public void run() {
        try(InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream())
        {
            try{
                StringBuilder sb = new StringBuilder();
                int c;
                while((c =in.read())!=-1 && c!=10 && c!=13){       //reading the first line with command and its arguments
                    sb.append((char)c);
                }
    //            while(in.read()!=-1){}          // reading until the end of input stream
                String data = sb.toString();
                String args[] = data.split(" ");                // defining the command and arguments
                String cmd = args[0].trim().toUpperCase();
                String wantedPath = args[1];
                System.out.println("wanted Path = "+wantedPath);
            //    String fileType = "";
            //   long fileSize = 0;
            //    File wantedFile = null;
            //    ByteArrayOutputStream  byteArray = new ByteArrayOutputStream();
                preparation4Reply(wantedPath);
                switch (cmd){
                    case "GET":get(out);break;
                    case "HEAD":head(out);break;
                    default: returnError(out,501,"Not Implemented");
                }
            }catch (FileNotFoundException e){
                returnError(out,404,"Not Found");
                e.printStackTrace();
            }catch (ForbiddenException e){
                returnError(out,403,"Forbidden");
                e.printStackTrace();
            }catch (Exception e){
                returnError(out,500,"Internal Server Error");
                e.printStackTrace();
            }

         }catch (IOException e) {
            e.printStackTrace();
         }
    }

    private void preparation4Reply(String wantedPath) throws IOException, ForbiddenException {
        File temp = new File(directory.getCanonicalPath()+File.separator+wantedPath);
        String rootDirCanonPath = directory.getCanonicalPath();
        String tempCanonPath = temp.getCanonicalPath();
        if (tempCanonPath.startsWith(rootDirCanonPath)){
            if (temp.isDirectory()){
                File index = new File(temp, outFileName);
                if (index.canRead()){
                    wantedFile = new File(temp.getPath()+File.separator+outFileName);
                    fileSize = wantedFile.length();
                    fileType = "text";
                }else{
                    DirectoryContentWriter.createDirHTML(byteArray,
                            new File(temp.getPath()),outFileName);
                    fileSize = byteArray.size();
                    fileType = "text/html";
                }
            }else{
                wantedFile = temp;
                fileSize = wantedFile.length();
                FileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                fileType = fileTypeMap.getContentType(wantedFile);
            }
        }else{
            throw new ForbiddenException();
        }


    }


    private void get(OutputStream out) throws IOException {
        head(out);
        if (wantedFile!=null){
            try (InputStream in =new FileInputStream(wantedFile)){
                byte buf[] = new byte[4096];
                int count;
                while((count = in.read(buf))>=0){
                    out.write(buf, 0, count);
                }
            }
        }else{
            byteArray.writeTo(out);
        }

    }

    private void head(OutputStream out) throws IOException {

        out.write("HTTP/1.0 200 OK\r\n".getBytes());
        out.write(("Content-Type: "+fileType+"; charset="+encoding+"\r\n").getBytes());
        out.write(("Content-Length: " +fileSize+ "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }
    private void returnError(OutputStream out, int errorCode, String errorString)throws IOException {

        out.write(("HTTP/1.0 "+errorCode+" "+errorString+"\r\n").getBytes());
        out.write(("Content-Type: text/html; charset="+encoding+"\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }
}

