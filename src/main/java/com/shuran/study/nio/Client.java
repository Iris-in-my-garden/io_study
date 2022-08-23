package com.shuran.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private Selector selector;
    private SocketChannel socketChannel;
    private final String ip="127.0.0.1";
    private final int port=8888;

    private Charset charset= StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        new Client().init();
    }

    public void init() throws IOException {
        selector=Selector.open();
        socketChannel=SocketChannel.open(new InetSocketAddress(ip,port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        new ClientReadThread().start();

        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNextLine()){
            String line= scanner.nextLine();
            socketChannel.write(charset.encode(line));
        }
    }

    private class ClientReadThread extends Thread{
        @Override
        public void run() {
            try {
                while (selector.select()>0){
                    for(SelectionKey key:selector.selectedKeys()){
                        selector.selectedKeys().remove(key);

                        if(key.isReadable()){
                            SocketChannel socketChannel1= (SocketChannel) key.channel();
                            ByteBuffer buffer=ByteBuffer.allocate(1024);
                            StringBuilder content= new StringBuilder();
                            while(socketChannel1.read(buffer)>0){
                                socketChannel1.read(buffer);
                                buffer.flip();
                                content.append(charset.decode(buffer));
                            }
                            System.out.println("content: "+ content);
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
