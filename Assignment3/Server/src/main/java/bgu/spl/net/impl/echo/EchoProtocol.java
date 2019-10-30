package bgu.spl.net.impl.echo;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.Connections;
import bgu.spl.net.api.MessagingProtocol;
import java.time.LocalDateTime;

public class EchoProtocol implements BidiMessagingProtocol<String> {

    private boolean shouldTerminate = false;

    // there is nothing to do here
    @Override
    public void start(int connectionId, Connections<String> connections) {

    }

    @Override
    public void process(String msg) {
        shouldTerminate = "bye".equals(msg);
        System.out.println("[" + LocalDateTime.now() + "]: " + msg);
        // To adhere to BidiMessagingProtocol - process should return nothing
    }

    // So createEcho is obsolete
    private String createEcho(String message) {
        String echoPart = message.substring(Math.max(message.length() - 2, 0), message.length());
        return message + " .. " + echoPart + " .. " + echoPart + " ..";
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
