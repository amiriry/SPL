//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_ACKUSERLISTMESSAGE_H
#define ASSIGNMENT3CLIENT_ACKUSERLISTMESSAGE_H

#include "AckMessage.h"
#include <string>
class AckUserlistMessage: public AckMessage{
    short numOfUsers;
    std::string userNameList;
public:
    AckUserlistMessage();       // not sure why is this needed - check
    AckUserlistMessage(short numOfUsers, std::string userNameList);
    AckUserlistMessage(const AckUserlistMessage &ackUserlistMessage);
    AckUserlistMessage& operator=(const AckUserlistMessage& ackUserlistMessage);
    virtual  ~AckUserlistMessage() = default;
    short getNumOfUsers();
    std::string getUserNameList();
};

#endif //ASSIGNMENT3CLIENT_ACKUSERLISTMESSAGE_H
