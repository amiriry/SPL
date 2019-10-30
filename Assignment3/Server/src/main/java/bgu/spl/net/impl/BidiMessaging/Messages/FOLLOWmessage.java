package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class FOLLOWmessage extends Message {
    byte follow; // 0 - unfollow, 1 - follow
    short numOfUsers;
    String userNameList;
    public FOLLOWmessage(byte follow, short numOfUsers, String userNameList) {
        super((short) 4);
        this.follow = follow;
        this.numOfUsers = numOfUsers;
        this.userNameList = userNameList;
    }

    public byte getFollowByte() {
        return follow;
    }

    public short getNumOfUsers() {
        return numOfUsers;
    }

    public String getUserNameList() {
        return userNameList;
    }
}
