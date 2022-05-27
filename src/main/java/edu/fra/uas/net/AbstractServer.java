package edu.fra.uas.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;

/**
 * A UDP Server.
 * 
 * <p> A {@code Server} can be used to receive requests and send responses. 
 * A {@code Server} is created through its constructor method.
 *
 * <p> Requests are received with the {@code onReceive()} method.
 * 
 * Responses are send, with the {@code sendResponse()} method.
 * 
 * The server should be closed after use, when it is no longer needed.
 */
public abstract class AbstractServer {

	private ServerThread serverThread;
	private static final int MAX_PORT_NUM = 0xFFFF;
	
	/**
	 * Creates a Server.
	 * 
	 * @param bindAddress  the local InetAddress the server will bind to
	 * @param port  the port number
	 * @throws IOException
	 */
	protected AbstractServer(String bindAddress, int port) throws IOException {
        if (port < 0 || port > MAX_PORT_NUM)
            throw new IllegalArgumentException("Server port value out of range: " + port);
		serverThread = new ServerThread(bindAddress, port);
		this.startServer();
	}
	
	/**
	 * Starts the server thread right after constructor.
	 */
	private void startServer() {
		serverThread.start();
	}
	
    /**
     * Receives a request. When this method returns, 
     * the {@code receivedData} parameter is filled with
     * the data received. The received request also contains the sender's
     * IP address {@code srcAddress}, and the port number {@code srcPort}
     * on the sender's machine.
     * <p>
     * This method is called when ever a request is received. 
     * If the received request message is longer than
     * 1500 bytes the message is truncated.
     * <p>
     * If there is a security manager, a packet cannot be received if the
     * security manager's {@code checkAccept} method
     * does not allow it.
     *
     * @param receivedData  the {@code byte[]} into which to place
     *                 the incoming data.
	 * @param srcAddress  contains the sender's IP address
	 * @param srcPort  contains the sender's port number 
	 */
	public abstract void onReceive(byte[] receivedData, String srcAddress, int srcPort);
	
	/**
     * Sends a response. The data bytes are send to the 
     * specified destination address {@code destAddress} with 
     * the specified port number {@code destPort}.
     *
	 * @param sendData  the {@code byte[]} to be sent.
	 * @param destAddress  the address the data to be sent to.
	 * @param destPort  the port number of the receiver of the response.
	 * @throws IOException
	 */
	public void sendResponse(byte[] sendData, String destAddress, int destPort) throws IOException {
        if (destPort < 0 || destPort > MAX_PORT_NUM)
            throw new IllegalArgumentException("Destination port value out of range: " + destPort);
		serverThread.send(sendData, destAddress, destPort);
	}
	
    /**
     * Closes this server's socket.
     * <p>
     * Any thread currently blocked in the {@code onReceive()} method upon this socket
     * will throw a {@link SocketException}.
     *
     * <p> If this socket has an associated channel then the channel is closed
     * as well.
     */
	public void stopAndClose() {
		this.serverThread.stopAndClose();
	}

	/**
	 * This Thread object hold a single server socket connection.
	 */
	private class ServerThread extends Thread {
		
	    private DatagramSocket socket;
	    private static final int BUFFER_SIZE = 1500;
	    private byte[] buf = new byte[BUFFER_SIZE];
	    
	    /**
	     * Creates a single ServerThread.
	     * 
	     * @param bindAddress  the local InetAddress the server will bind to
	     * @param port  the port number
	     * @throws IOException
	     */
	    ServerThread(String bindAddress, int port) throws IOException {
			InetAddress inetAddress = InetAddress.getByName(bindAddress);
			SocketAddress socketAddress=new InetSocketAddress(inetAddress, port);  
	        socket = new DatagramSocket(socketAddress);
	    }
	    
	    /**
	     * Reads the input from the socket, 
	     * submits the input to the {@code recv()} method
	     * and closes the if {@code stopAndClose()} is called.
	     * socket.
	     */
	    @Override
	    public void run() {	    	
	    	boolean active = true;
	        while (active) {
	            try {
	                DatagramPacket packet = new DatagramPacket(buf, buf.length);
	                socket.receive(packet);
	                String srcAddress = packet.getAddress().getHostAddress();
	                int srcPort = packet.getPort();
	                byte[] receivedData = Arrays.copyOf(packet.getData(), packet.getLength());	                
	                recv(receivedData, srcAddress, srcPort);
	            } catch(IOException e) {
	            	e.printStackTrace();
	            	active = false;
	            }
	        }
	    }
	    
	    /**
	     * Receives a request from a client and forwards the data received to the
	     * the {@code onReceive()} method.
	     * @param receivedData  the {@code byte[]} into which to place
	     *                 the incoming data.
	     * @param srcAddress  contains the sender's IP address
	     * @param srcPort  contains the sender's port number
	     */
	    private void recv(byte[] receivedData, String srcAddress, int srcPort) {
		    	onReceive(receivedData, srcAddress, srcPort);	    		
	    }
	    
		/**
	     * Sends a response. The data bytes are send to the 
	     * specified destination address {@code destAddress} with 
	     * the specified port number {@code destPort}.
	     *
		 * @param data  the {@code byte[]} to be sent.
		 * @param destAddress  the address the data to be sent to.
		 * @param destPort  the port number of the receiver of the response.
		 * @throws IOException
		 */
	    protected void send(byte[] data, String destAddress, int destPort) throws IOException {
	    	if(data == null)
	    		return;
	        if (data.length > BUFFER_SIZE)
	            throw new IllegalArgumentException("Data to send is to big: " + data.length 
	            		+ ". Maximal size: " + BUFFER_SIZE);
	    	DatagramPacket packet = new DatagramPacket(
	    			data, data.length, InetAddress.getByName(destAddress), destPort);
	    	socket.send(packet);
	    }
	    
	    /**
	     * Closes this server's socket.
	     * <p>
	     * Any thread currently blocked in the {@code onReceive()} method upon this socket
	     * will throw a {@link SocketException}.
	     *
	     * <p> If this socket has an associated channel then the channel is closed
	     * as well.
	     */
	    public void stopAndClose() {
	    	socket.close();
	    }
	    
	}
}
