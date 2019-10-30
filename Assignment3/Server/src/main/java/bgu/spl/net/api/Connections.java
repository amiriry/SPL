package bgu.spl.net.api;

/**
 * Created by amirshkedy on 26/12/2018.
 */

import java.io.IOException;

public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);
}
