package edu.fra.uas.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * A UDP Client.
 * 
 * <p> A {@code Client} can be used to send requests and retrieve their responses. 
 * A {@code Client} is created through its constructor method.
 *
 * <p> Requests can be sent with a {@code sendRequest()} method.
 * <p><b>Request Example</b>
 * <pre>{@code  Client client = new Client("127.0.0.10", 65000, "127.0.0.8", 45632);
 * byte[] data = {(byte) 0x01, (byte) 0x20, (byte) 0x20, (byte) 0x3A,};
 * client.sendRequest(data);}</pre>
 * 
 * Responses are received, with the {@code receivedResponse()} method.
 * 
 * The client should be closed after use, when it is no longer needed.
 */
public class Client {

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private static final int TIMEOUT = 500;
    private static final int BUFFER_SIZE = 1500;
    private static final int MAX_PORT_NUM = 0xFFFF;
    
    /**
     * Creates a client.
     * 
     * @param serverAddress  the address of the server the client will send and receive data
     * @param serverPort  the port of the server the client will send and receive data
     * @param bindAddress  the local InetAddress the client will bind to
     * @param port  the port number
     * @throws SocketException
     * @throws UnknownHostException
     */
    public Client(String serverAddress, int serverPort, String bindAddress, int port) 
    		throws SocketException, UnknownHostException {
        if (serverPort < 0 || serverPort > MAX_PORT_NUM)
            throw new IllegalArgumentException("Server port value out of range: " + serverPort);
        if (port < 0 || port > MAX_PORT_NUM)
            throw new IllegalArgumentException("Port value out of range: " + port);
		InetAddress inetAddress = InetAddress.getByName(bindAddress);
		SocketAddress socketAddress=new InetSocketAddress(inetAddress, port);  
		socket = new DatagramSocket(socketAddress);
		this.serverAddress = InetAddress.getByName(serverAddress);
		this.serverPort = serverPort;
		socket.setSoTimeout(TIMEOUT);
    }
    
    /**
     * Sends a request. The data bytes are send to the specified server from the constructor method.
     *
     * @param      data  the {@code byte[]} to be sent.
     *
     * @exception  IOException  if an I/O error occurs.
     * @exception  SecurityException  if a security manager exists and its
     *             {@code checkMulticast} or {@code checkConnect}
     *             method doesn't allow the send.
     * @exception  PortUnreachableException may be thrown if the socket is connected
     *             to a currently unreachable destination. Note, there is no
     *             guarantee that the exception will be thrown.
     * @exception  java.nio.channels.IllegalBlockingModeException
     *             if this socket has an associated channel,
     *             and the channel is in non-blocking mode.
     * @exception  IllegalArgumentException if the socket is connected,
     *             and connected address and packet address differ.
     *
     * @see        java.net.DatagramPacket
     * @see        SecurityManager#checkMulticast(InetAddress)
     * @see        SecurityManager#checkConnect
     */
    public void sendRequest(byte[] data) throws IOException {
    	DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
    	socket.send(packet);
    }
    
    /**
     * Receives a response. When this method returns, 
     * the buffer is filled with the data received. 
     * <p>
     * This method blocks until a response is received.
     * If the received response is longer than 1500 bytes
     * the returned data is truncated.
     * <p>
     * If there is a security manager, a packet cannot be received if the
     * security manager's {@code checkAccept} method
     * does not allow it.
     *
     * @return     the incoming data.
     * @exception  IOException  if an I/O error occurs.
     * @exception  SocketTimeoutException  the timeout of 500 milliseconds has expired.
     * @exception  PortUnreachableException may be thrown if the socket is connected
     *             to a currently unreachable destination. Note, there is no guarantee that the
     *             exception will be thrown.
     * @exception  java.nio.channels.IllegalBlockingModeException
     *             if this socket has an associated channel,
     *             and the channel is in non-blocking mode.
     * @see        java.net.DatagramPacket
     * @see        java.net.DatagramSocket
     */
    public byte[] receivedResponse() throws IOException {
    	byte[] buffer = new byte[BUFFER_SIZE];
    	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
    	socket.receive(packet);
    	return Arrays.copyOf(packet.getData(), packet.getLength());
    }
    
    /**
     * Closes this client's socket.
     * <p>
     * Any thread currently blocked in the {@code receiveResponse()} method upon this socket
     * will throw a {@link SocketException}.
     *
     * <p> If this socket has an associated channel then the channel is closed
     * as well.
     */
    public void stopAndClose() {
    	socket.close();
    }
}
