package hr.ivica.chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * ChatServer is a multi-threaded chat server. It listens on a predefined port
 * and creates threads for managing connected clients. It caches output streams
 * to connected clients, making it easier to "publish" an incoming message to
 * all connected clients
 * 
 * @author ivica
 */
public class ChatServer {
    private int port;
    private Map<Socket, PrintWriter> outputWriters;
    private static Logger logger = LogManager.getLogger(ChatServer.class.getName());
    
    /**
     * Initializes ChatServer 
     * @param port - port which will be used to listen for connections
     *          
     */
    public ChatServer(int port) {
        this.port = port;
        this.outputWriters = new ConcurrentHashMap<>();
    }
    
    public Map<Socket, PrintWriter> getOutputWriters() {
        return outputWriters;
    }
    
    /**
     * Starts the server by opening a server socket
     * and listening for connections
     */
    public void start() {
        try (ServerSocket listenSocket = new ServerSocket(port);) {
            logger.info("Server listening on port {}", listenSocket.getLocalPort());
            while(true) {
                Socket socket = listenSocket.accept();
                logger.info("Accepted connection on Socket" + socket.toString());
                
                // create a PrintWriter and cache it in a Map 
                outputWriters.put(socket, new PrintWriter(socket.getOutputStream(), true));
                logger.debug("Number of active connections: {}", getOutputWriters().size());
            }
        } catch(IOException e) {
            logger.fatal(e.toString());
        } 
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ChatServer server = new ChatServer(55555);
        server.start();
    }
}