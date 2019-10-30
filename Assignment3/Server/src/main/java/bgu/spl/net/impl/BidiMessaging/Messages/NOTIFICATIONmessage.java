package bgu.spl.net.impl.BidiMessaging.Messages;

import bgu.spl.net.impl.BidiMessaging.Message;

public class NOTIFICATIONmessage extends Message {
    char notificationType;
    String postingUser;
    String Content;

    public NOTIFICATIONmessage(char notificationType, String postingUser, String content) {
        super((short) 9);
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        Content = content;
    }

    public char getNotificationType() {
        return notificationType;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return Content;
    }
}
