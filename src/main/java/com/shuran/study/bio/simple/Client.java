package com.shuran.study.bio.simple;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line=reader.readLine();
        System.out.println("from server: "+line);
        reader.close();
        socket.close();
    }
}
