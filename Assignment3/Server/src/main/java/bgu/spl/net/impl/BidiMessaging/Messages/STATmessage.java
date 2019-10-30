package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class STATmessage extends Message {
    String userName;

    public STATmessage(String userName) {
        super((short) 8);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
