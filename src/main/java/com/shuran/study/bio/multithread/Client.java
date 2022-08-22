package com.shuran.study.bio.multithread;

import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket s=new Socket("127.0.0.1",8888);
        new Thread(new ClientReadRunnable(s)).start();
        new Thread(new ClientWriteRunnable(s)).start();
    }


    public static class ClientReadRunnable implements Runnable{

        private Socket socket;
        BufferedReader reader;

        public ClientReadRunnable(Socket s) throws IOException{
            socket=s;
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run(){
            String content;
            while(true){
                try {
                    if ((content = reader.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(content);
            }
        }
    }

    public static class ClientWriteRunnable implements Runnable{
        private Socket socket;

        public ClientWriteRunnable(Socket s){
            socket=s;
        }

        @Override
        public void run() {
            try {
                PrintStream ps=new PrintStream(socket.getOutputStream());

                String line;
                BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
                while((line=reader.readLine())!=null){
                    //send to server
                    ps.println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


}
