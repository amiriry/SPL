package bgu.spl.net.srv;

import bgu.spl.net.api.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private ConnectionsImpl connections;
    private int connectionId;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol,
                                     ConnectionsImpl connections, int connectionId) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void run() {
        protocol.start(connectionId, connections);
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());

            out = new BufferedOutputStream(sock.getOutputStream());
            // note this
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    System.out.println("decoded " + nextMessage.toString());
                    protocol.process(nextMessage);
                }
            }
//            connections.disconnect(connectionId);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        in.close();
        out.close();
        sock.close();
    }

    // check - nothing here yet only need to be implemented so there will be no error
    @Override
    public void send(T msg) {
        try{
            byte[] arr = encdec.encode(msg);
            System.out.println(Arrays.toString(arr));
            out.write(arr);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
