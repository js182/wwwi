package edu.fra.uas.net;

import java.io.IOException;

public class Server extends AbstractServer{

    /**
     * Creates a Server.
     *
     * @param bindAddress the local InetAddress the server will bind to
     * @param port        the port number
     * @throws IOException
     */
    public Server(String bindAddress, int port) throws IOException {
        super(bindAddress, port);
    }
    
    public void onReceive(byte[] receivedData, String srcAddress, int srcPort) {
        //TODO: Implement method
    }
}
