//
// Created by dapool on 07/01/19.
//

#include "NotificationMessage.h"

NotificationMessage::NotificationMessage(): Message(9), postingUser(nullptr), type(-1), content(""){}

NotificationMessage::NotificationMessage(std::string _postingUser, char _type, std::string _content): Message(9),
                postingUser(std::move(_postingUser)), type(_type) , content(_content) {}

NotificationMessage::NotificationMessage(const NotificationMessage &notificationMessage): Message(9),
    postingUser(notificationMessage.postingUser), type(notificationMessage.type),
    content(notificationMessage.content){}

NotificationMessage& NotificationMessage::operator=(const NotificationMessage &notificationMessage) {
    postingUser = notificationMessage.postingUser;
    type = notificationMessage.type;
    content = notificationMessage.content;
}

std::string NotificationMessage::getPostingUser() {
    return postingUser;
}

short NotificationMessage::getType() {
    return type;
}

std::string NotificationMessage::getContent() {
    return content;
}


