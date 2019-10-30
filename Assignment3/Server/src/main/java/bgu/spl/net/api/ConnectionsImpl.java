package bgu.spl.net.api;

import bgu.spl.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * This is the connection manager
 * It manages all the connections through the system.
 */
public class ConnectionsImpl<T> implements Connections<T> {

    // list of the new connection handlers for each active client
    // The ConnectionHandler in values of the map is interface - that makes
    // us can put here who ever implements ConnectionHandler -
    // which are BlockingConnectionHandler and NonBlockingConnectionHandler
    // I think that this is why this Connection interface can implement both Reactor and ThreadPerClient
    private HashMap<Integer, ConnectionHandler> connectionHandlers = new HashMap<>();

    // sends a message of type T to user that it's connection handlerId is connectionId
    @Override
    public boolean send(int connectionId, T msg) {
        // flag for sending happened or not
        boolean sent = false;
        if(connectionHandlers.containsKey(connectionId)) {
            ConnectionHandler myConnection = connectionHandlers.get(connectionId);
            myConnection.send(msg);
            sent = true;
        }
        return sent;
    }

    // Because it's part of the server implementation and not the protocol - it doesn't
    // know about the protocol - so sends message to all clients including those who haven't
    // finished connecting yet.
    @Override
    public void broadcast(T msg) {
        // going over all connection handlers - should not care about completing the login
        // by bgu protocol
        for(ConnectionHandler handle: connectionHandlers.values()){
            handle.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        try {
            // I am not sure what close is doing in this connection
            connectionHandlers.get(connectionId).close();
            connectionHandlers.remove(connectionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // return boolean - if addConnection to the list succeeded or not
    public boolean addConnectionection(int connecionId, ConnectionHandler conHand){
        connectionHandlers.putIfAbsent(connecionId, conHand);
        return true;
    }
}
