//
// Created by dapool on 07/01/19.
//

#include "AckUserlistMessage.h"

AckUserlistMessage::AckUserlistMessage(): AckMessage(7), userNameList(""), numOfUsers(0) {}
AckUserlistMessage::AckUserlistMessage(short _numOfUsers, std::string _userNameList): AckMessage(7),
            numOfUsers(_numOfUsers), userNameList(std::move(_userNameList)) {}

AckUserlistMessage& AckUserlistMessage::operator=(const AckUserlistMessage &ackUserlistMessage) {
    userNameList = ackUserlistMessage.userNameList;
    numOfUsers = ackUserlistMessage.numOfUsers;
    return *this;
}

short AckUserlistMessage::getNumOfUsers() {
    return numOfUsers;
}

std::string AckUserlistMessage::getUserNameList() {
    return userNameList;
}