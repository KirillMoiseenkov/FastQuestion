package CreationShip.demo;

import CreationShip.demo.nio.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientAnswerOnQuestion {



    public static void main(String[] args) throws IOException {

        Client client = new Client();


        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));


        for(;;){
        client.sendGetMessage(stdIn);
        }

    }

}
