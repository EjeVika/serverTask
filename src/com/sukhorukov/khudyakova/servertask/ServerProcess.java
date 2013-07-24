package com.sukhorukov.khudyakova.servertask;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

/**

 */
class ServerProcess implements Runnable{
    private final Socket socket;
    private final File directory;
    private final String indexFileName;
    private final String encoding;

    private String fileType;
    private long fileSize;
    private File wantedFile;
    private ByteArrayOutputStream  byteArray;


    public ServerProcess(Socket socket,File directory, String indexFileName,String encoding) {
        this.socket = socket;
        this.directory = directory;
        this.indexFileName = indexFileName;
        this.encoding = encoding;

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
                /*String endOfRequest = "\r\n\r\n";
                StringBuilder sb = new StringBuilder();
                int c;
                while(!sb.toString().endsWith(endOfRequest)){       //reading full request
                    c=in.read();
                    sb.append((char) c);
                }
    //            System.out.println(sb.toString());

                String data = sb.toString();
                */
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String data = br.readLine();
                System.out.println(data);
                byte[] tmp = new byte[4096];
                int c;
                while (in.available()>0&&(in.read(tmp,0,in.available()))>0){}

                String args[] = data.split(" ");
                String cmd = args[0].trim().toUpperCase();      // defining the command
                String wantedPath = URLDecoder.decode(args[1],encoding);        // defining the path
                System.out.println("wanted Path = "+wantedPath);

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
                returnError(out, 500, "Internal Server Error");
                e.printStackTrace();
            }

         }catch (IOException e) {
            e.printStackTrace();
         }
    }

    private void preparation4Reply(String wantedPath) throws IOException, ForbiddenException {
        File temp = new File(directory.getCanonicalPath()+File.separator+wantedPath);
        System.out.println(temp.getCanonicalPath());
        if (!temp.canRead()){
            throw new FileNotFoundException();
        }else{
            String rootDirCanonPath = directory.getCanonicalPath();
            String tempCanonPath = temp.getCanonicalPath();
            if (tempCanonPath.startsWith(rootDirCanonPath)){
                if (temp.isDirectory()){
                    File index = new File(temp, indexFileName);
                    if (index.canRead()){
                        wantedFile = new File(temp.getPath()+File.separator+ indexFileName);
                        fileSize = wantedFile.length();
                        fileType = "text/html";
                    }else{
                        DirectoryContentWriter.createDirHTML(byteArray,
                                new File(temp.getPath()),encoding);
                        fileSize = byteArray.size();
                        fileType = "text/html";
                    }
                }else{
                    wantedFile = temp;
                    fileSize = wantedFile.length();
                    FileTypeMap fileTypeMap = new MimetypesFileTypeMap();
                    fileType = fileTypeMap.getContentType(wantedFile);
                //    System.out.println(fileType);

                }
            }else{
                throw new ForbiddenException();
            }
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
        out.flush();
    }

    private void head(OutputStream out) throws IOException {

        out.write("HTTP/1.0 200 OK\r\n".getBytes());
        out.write(("Content-Type: " +fileType+"; charset="+encoding+"\r\n").getBytes());
        out.write(("Content-Length: " +fileSize+ "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }
    private void returnError(OutputStream out, int errorCode, String errorString)throws IOException {

        out.write(("HTTP/1.1 "+errorCode+" "+errorString+"\r\n").getBytes());
        out.write(("Content-Type: text/html; charset="+encoding+"\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.flush();
    }
}

