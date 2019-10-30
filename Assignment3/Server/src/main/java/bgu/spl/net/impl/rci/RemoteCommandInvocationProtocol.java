package bgu.spl.net.impl.rci;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.api.Connections;
import bgu.spl.net.api.MessagingProtocol;
import java.io.Serializable;

public class RemoteCommandInvocationProtocol<T> implements BidiMessagingProtocol<Serializable> {

    private T arg;

    public RemoteCommandInvocationProtocol(T arg) {
        this.arg = arg;
    }

    // I think that here also start does nothing - it only here because of the interface demands.
    @Override
    public void start(int connectionId, Connections<Serializable> connections) {

    }

    @Override
    public void process(Serializable msg) {
        // before it was returning the result of this command
//        return ((Command) msg).execute(arg);
        // now it should only do it
        ((Command) msg).execute(arg);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
