package com.sukhorukov.khudyakova.servertask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**

 */
public class Main {
    public static void main(String[] args) throws IOException {
        String dirName = ".";
        String indexFileName = "index.html";
        int port=8080;
        String encoding = "UTF-8";
        System.setProperty("file.encoding",encoding);

        if (args.length>0) {
            dirName = args[0];
                   }
        if (args.length>1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println(""+e);
            }
        }


        File directory = new File(dirName);

        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket s = server.accept();
            System.out.println("Client accepted : "+s.getInetAddress()+":"+s.getPort());
            ServerProcess sProc  = new ServerProcess(s,directory,indexFileName,encoding);
            new Thread(sProc).start();

        }
    }
}
