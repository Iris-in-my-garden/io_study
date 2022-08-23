package com.shuran.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Server {
    private Selector selector;
    private final String ip="127.0.0.1";
    private final int port=8888;

    private Charset charset= StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        new Server().init();
    }

    public void init() throws IOException{
        selector=Selector.open();

        // open a unbind server socket channel instance
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(ip,port));

        // no blocking
        serverSocketChannel.configureBlocking(false);

        // for accept usage
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(selector.select()>0){
            for(SelectionKey key: selector.selectedKeys()){
                selector.selectedKeys().remove(key);

                // deal with accept operation
                if(key.isAcceptable()){
                    // accept connection
                    SocketChannel socketChannel=serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    // configure to receive other accept
                    key.interestOps(SelectionKey.OP_ACCEPT);
                }

                // deal with read operation
                if(key.isReadable()){
                    SocketChannel socketChannel=(SocketChannel) key.channel();
                    ByteBuffer buffer=ByteBuffer.allocate(1024);
                    StringBuilder content= new StringBuilder();

                    try {
                        while(socketChannel.read(buffer)>0){
                            buffer.flip();
                            content.append(charset.decode(buffer));
                        }
                        System.out.println("read contents: "+ content);
                        key.interestOps(SelectionKey.OP_READ);
                    } catch (IOException e){
                        key.cancel();
                        if(key.channel()!=null){
                            key.channel().close();
                        }
                    }
                    sendToAllClients(content.toString(), selector);

                }


            }
        }
    }

    private void sendToAllClients(String content, Selector selector) throws IOException {
        if(content!=null && content.length()>0){
            for(SelectionKey key : selector.keys()){
                Channel targetChannel=key.channel();
                if(targetChannel instanceof SocketChannel){
                    SocketChannel dest=(SocketChannel) targetChannel;
                    dest.write(charset.encode(content));
                }
            }
        }

    }
}
