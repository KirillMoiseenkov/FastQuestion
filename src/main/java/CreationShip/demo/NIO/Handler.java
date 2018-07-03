package CreationShip.demo.NIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Handler {


    @Autowired
    private Server server;

    public void startServer(){
        server.run();
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.run();
    }

}
