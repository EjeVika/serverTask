package com.sukhorukov.khudyakova.servertask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
        String dirName = ".";
        String outFileName = "index.html";
        Integer port=8080;
        try{
            if (args.length>0) {
                dirName = args[0];
                port = new Integer(args[1]);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Wrong number of arguments. Program is terminated.");
            System.exit(-1);
        }catch (NumberFormatException e){
            System.err.println("Wrong port. Program is terminated.");
            System.exit(-1);
        }

        File directory = new File(dirName);





        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket s = server.accept();
            System.out.println("Client accepted"+s.getInetAddress()+":"+s.getPort());
            ServerProcess sProc  = new ServerProcess(s,directory,outFileName);
            new Thread(sProc).start();
//            directory = sProc.getCurrentDir();
        }




    }
}
