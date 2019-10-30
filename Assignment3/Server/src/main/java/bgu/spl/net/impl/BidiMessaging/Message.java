package bgu.spl.net.impl.BidiMessaging;

public abstract class Message {

    final short opCode; // why it should be final? - check

    public Message(short opCode){
        this.opCode = opCode;
    }

    public short getOpcode() {
        return opCode;
    }
}
