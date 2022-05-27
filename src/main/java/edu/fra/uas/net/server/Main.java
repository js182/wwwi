package edu.fra.uas.net.server;

import edu.fra.uas.net.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Test");
        //Test client = new Test("127.0.0.10", 65000, "127.0.0.8", 45632);
        Server s = new Server("127.0.0.1",40);
    }
}
