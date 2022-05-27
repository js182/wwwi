package edu.fra.uas.net.client;
//import edu.fra.uas.net.Client;

import edu.fra.uas.net.Client;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello World");
        Client client = new Client("127.0.0.10", 65000, "127.0.0.8", 45632);
    }
}
