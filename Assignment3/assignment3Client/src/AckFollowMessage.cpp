//
// Created by dapool on 07/01/19.
//

#include <AckMessage.h>
#include "AckFollowMessage.h"

AckFollowMessage::AckFollowMessage(): AckMessage(4), numOfUsers(0), userNameList(""){}
AckFollowMessage::AckFollowMessage(short numOfUsers, std::string userNameList): AckMessage(4), numOfUsers(numOfUsers), userNameList(userNameList){}

AckFollowMessage &AckFollowMessage::operator=(const AckFollowMessage &ackFollowMessage) {
    userNameList = ackFollowMessage.userNameList;
    numOfUsers = ackFollowMessage.numOfUsers;
    return *this;
}

short AckFollowMessage::getNumOfUsers() {
    return numOfUsers;
}

std::string AckFollowMessage::getUserNameList() {
    return userNameList;
}


