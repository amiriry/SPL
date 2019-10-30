package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class REGISTERmessage extends Message {
    String name;
    String password;
    public REGISTERmessage(String name, String password){
        super((short) 1);
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
