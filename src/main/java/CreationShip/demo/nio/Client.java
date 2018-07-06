package CreationShip.demo.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

public class Client {

    private static SocketChannel client;
    private static ByteBuffer buffer;
    private static Client instance;
    private static CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();

    private Client() {
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            buffer = ByteBuffer.allocate(64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGetMessage(BufferedReader stdIn) throws IOException {

        client.read(buffer);

        String response = new String(Arrays.copyOfRange(buffer.array(), 0, buffer.position()));

        System.out.println("response from send: " + response.replace(System.lineSeparator(), ""));

        buffer.flip();
        buffer.clear();

        String msg = stdIn.readLine();
        client.write(ByteBuffer.wrap(msg.getBytes()));
        }

    public static void getMessages() throws IOException {

        client.read(buffer);

        String responce = new String(Arrays.copyOfRange(buffer.array(),0, buffer.position()));

        System.out.println("response from get: " + responce.replace(System.lineSeparator(), ""));

        buffer.flip();
        buffer.clear();

    }


    public static String bb_to_str(ByteBuffer buffer){
        byte[] bytes;
        if(buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes);
    }

    public static void main(String[] args) throws IOException {

        Client echoClient = new Client();

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        for (; ; ) {
            echoClient.sendGetMessage(stdIn);
        }
    }

}