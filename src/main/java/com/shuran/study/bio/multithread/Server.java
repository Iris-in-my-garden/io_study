package com.shuran.study.bio.multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 把收到的消息转发给所有客户端
 */
public class Server {
    public static List<Socket> socketList=new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try(ServerSocket ss=new ServerSocket()){
            ss.bind(new InetSocketAddress("127.0.0.1", 8888));
            while(true){
                Socket socket=ss.accept();
                socketList.add(socket);
                new Thread(new ServerProcessRunnable(socket)).start();
            }

        }
    }


    public static class ServerProcessRunnable implements  Runnable{
        Socket socket;
        BufferedReader reader;

        public ServerProcessRunnable(Socket s) throws IOException{
            socket=s;
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try{
                String content=null;
                while ((content=readFromClient())!=null){
                    //send to every connected client
                    System.out.println("received content: "+content);
                    for(Socket s : Server.socketList){
                        PrintStream ps=new PrintStream(s.getOutputStream());
                        ps.println(content);

                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private String readFromClient(){
            try {
                return reader.readLine();
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }




}
