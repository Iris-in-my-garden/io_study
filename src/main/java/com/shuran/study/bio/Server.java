package com.shuran.study.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket();
        ss.bind(new InetSocketAddress("127.0.0.1",8888));
        while(true){
            Socket socket=ss.accept();
            new Thread(()->{
                handler(socket);
            }).start();
        }
    }

    private static void handler(Socket socket){
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while((msg=reader.readLine())!=null){
                System.out.println(msg);
                if(msg.equals("quit")){
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
