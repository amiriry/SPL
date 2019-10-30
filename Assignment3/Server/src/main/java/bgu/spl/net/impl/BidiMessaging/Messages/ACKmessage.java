package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class ACKmessage extends Message {
    short msgOpcode;
    short numPosts;
    short numFollowing;
    short numOfUsers;
    byte followBytes;
    String userNameList;
    public ACKmessage(short msgOpcode) {
        super((short) 10);
        this.msgOpcode = msgOpcode;
    }
    // Acknowledge for Stat
    public ACKmessage(short numPosts, short numFollowing){
        super((short) 10);
        this.msgOpcode =  8; // STAT opcode
        this.numPosts = numPosts;
        this.numFollowing = numFollowing;
    }
    // Acknowledge for Userlist
    public ACKmessage(short numOfUsers, String userNameList){
        super((short) 10);
        this.msgOpcode = 7;
        this.numOfUsers = numOfUsers;
        this.userNameList = userNameList;
    }
    // Acknowledgement for Follow - check - need to check if its ok
    public ACKmessage(byte followBytes, short numOfUsers, String userNameList){
        super((short) 10);
        this.msgOpcode = 4; // follow opcode
        this.followBytes = followBytes;
        this.numOfUsers = numOfUsers;
        this.userNameList = userNameList;
    }

    public short getMsgOpcode() {
        return msgOpcode;
    }

    public short getNumPosts() {
        return numPosts;
    }

    public short getNumFollowing() {
        return numFollowing;
    }

    public short getNumOfUsers() {
        return numOfUsers;
    }

    public String getUserNameList() {
        return userNameList;
    }

    public byte getFollowByte() {
        return followBytes;
    }
}
