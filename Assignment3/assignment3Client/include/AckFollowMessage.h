//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_ACKFOLLOWMESSAGE_H
#define ASSIGNMENT3CLIENT_ACKFOLLOWMESSAGE_H

#include "AckMessage.h"
class AckFollowMessage: public AckMessage{
private:
    short numOfUsers;
    std::string userNameList;
public:
    AckFollowMessage();
    AckFollowMessage(short numOfUsers, std::string userNameList);
    AckFollowMessage(const AckFollowMessage& ackFollowMessage);
    AckFollowMessage& operator=(const AckFollowMessage& ackFollowMessage);
    virtual ~AckFollowMessage()= default;
    short getNumOfUsers();
    std::string getUserNameList();

};

#endif //ASSIGNMENT3CLIENT_ACKFOLLOWMESSAGE_H
