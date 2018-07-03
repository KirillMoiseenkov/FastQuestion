package CreationShip.demo.NIO;

import CreationShip.demo.DemoApplication;
import CreationShip.demo.NIO.comunic.Reader;
import CreationShip.demo.NIO.comunic.Writer;
import CreationShip.demo.NIO.worcker.Connection;
import CreationShip.demo.service.MessageService;
import CreationShip.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

@Service
public class Server extends Thread {

    private static ThreadLocal<Integer> countConnection = new ThreadLocal<>();
    private static InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private QuestionService questionService;


    private ServerSocketChannel serverSocket = ServerSocketChannel.open();
    private int allocate;
    private Map<SelectionKey, Connection> connectionMap = new HashMap<>();


    public Server(int allocate, InetSocketAddress inetSocketAddress) throws IOException {

        this.allocate = allocate;
        if (inetSocketAddress == null)
            inetSocketAddress = new InetSocketAddress("localhost", 8080);
        serverSocket.bind(inetSocketAddress);
        serverSocket.configureBlocking(false);

    }

    public Server() throws IOException {

        this.allocate = 64;

        serverSocket.bind(inetSocketAddress);
        serverSocket.configureBlocking(false);
    }

    public static Integer getCountConnection() {
        return countConnection.get();
    }

    public void setAllocate(int allocate) {
        this.allocate = allocate;
    }

    @Override
    public void run() {

        Selector selector;
        ByteBuffer buffer;
        countConnection.set(0);


        try {
            selector = Selector.open();
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            buffer = ByteBuffer.allocate(allocate);
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

        try {

            while (true) {

                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {

                    SelectionKey key = iter.next();

                    if (key.isAcceptable()) {

                        countConnection.set(countConnection.get() + 1);
                        System.out.println("connectioSize = " + countConnection.get() + " thread is" + Thread.currentThread());
                        register(selector, serverSocket);

                        logger.info("new Connector");

                    }

                    if (key.isValid() && key.isReadable()) {

                        read(buffer, key, selector);

                    }

                    if (key.isValid() && key.isWritable()) {

                        write(buffer, key, selector);

                    }

                    iter.remove();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_WRITE);

    }

    private void read(ByteBuffer buffer, SelectionKey key, Selector selector) {

        Connection connection;

        if (!connectionMap.containsKey(key)) {

            connectionMap.put(key, new Connection(messageService, questionService));
            connection = connectionMap.get(key);
        }

        connection = connectionMap.get(key);

        if (connection.getReader() == null) {
            Reader reader = null;
            reader = new Reader(buffer, key, selector);
            reader.enableWriteMode(true);
            connection.setReader(reader);
        }

        connection.read();

    }

    private void write(ByteBuffer buffer, SelectionKey key, Selector selector) {

        Connection connection;

        if (!connectionMap.containsKey(key)) {
            connectionMap.put(key, new Connection(messageService, questionService));

            connection = connectionMap.get(key);
        }

        connection = connectionMap.get(key);

        if (connection.getWriter() == null) {
            Writer writer = null;
            writer = new Writer(buffer, key, selector);
            writer.enableReadMode(true);
            connection.setWriter(writer);
        }

        connection.write();

    }



}