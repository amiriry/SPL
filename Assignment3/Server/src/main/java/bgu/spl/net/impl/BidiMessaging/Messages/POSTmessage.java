package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class POSTmessage extends Message {
    String contents;

    public POSTmessage(String contents) {
        super((short) 5);
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }
}
