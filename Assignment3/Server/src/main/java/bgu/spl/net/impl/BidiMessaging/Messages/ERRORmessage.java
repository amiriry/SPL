package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class ERRORmessage extends Message {
    short errorOpcode;

    public ERRORmessage(short errorOpcode) {
        super((short) 11);
        this.errorOpcode = errorOpcode;
    }

    public short getErrorOpcode() {
        return errorOpcode;
    }
}
