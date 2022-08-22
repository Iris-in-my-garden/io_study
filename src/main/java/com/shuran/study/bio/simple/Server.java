package com.shuran.study.bio.simple;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {
        //创建server socket来监听客户端socket的连接请求
        try (ServerSocket ss = new ServerSocket()) {
            ss.bind(new InetSocketAddress("127.0.0.1", 8888));
            //循环接收客户端的请求
            while (true) {
                //接收到请求后，服务端会对应产生一个socket
                Socket socket = ss.accept();
                handler(socket);
            }
        }

    }

    private static void handler(Socket socket){
        try {
            PrintStream printStream=new PrintStream(socket.getOutputStream());
            printStream.println("SERVER HAS RECEIVED!");
            socket.close();
            printStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}
