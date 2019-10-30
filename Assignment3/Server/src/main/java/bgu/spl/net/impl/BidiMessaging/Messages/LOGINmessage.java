package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class LOGINmessage extends Message {
    String name;
    String password;        // the password taken from the REGISTER command
    public LOGINmessage(String name, String password){
        super((short) 2);
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
