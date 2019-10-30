//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_NOTIFICATIONMESSAGE_H
#define ASSIGNMENT3CLIENT_NOTIFICATIONMESSAGE_H

#include "Message.h"

class NotificationMessage: public Message{
private:
    std::string postingUser;
    char type; // 0 - pm, 1 - post
    std::string content;
public:
    NotificationMessage(); // not sure what is this for - check
    NotificationMessage(std::string _postingUser, char _type, std::string _content);
    NotificationMessage(const NotificationMessage &notificationMessage);
    NotificationMessage& operator=(const NotificationMessage &notificationMessage);
    virtual ~NotificationMessage() = default;
    std::string getPostingUser();
    short getType();
    std::string getContent();
};


#endif //ASSIGNMENT3CLIENT_NOTIFICATIONMESSAGE_H
