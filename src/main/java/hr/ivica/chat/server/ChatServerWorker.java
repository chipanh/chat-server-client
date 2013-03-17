/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.ivica.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author ivica
 */
public class ChatServerWorker implements Runnable{
    private Socket socket;
    private ChatServer server;
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(ChatServerWorker.class.getName());

    public ChatServerWorker(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                server.sendToAll(line);
            }
        } catch (IOException ex) {
            logger.error("Error handling client connection {} : {}", socket.toString(), ex.toString());
        } finally {
            logger.info("Closing client connection {}", socket.toString());
            server.closeConnection(socket);                       
        }
    }
    
    
}
