//
// Created by dapool on 07/01/19.
//
#include "AckStatMessage.h"


AckStatMessage::AckStatMessage(): AckMessage(8), numOfPosts(0), numOfFollowing(0), numOfFollowers(0) {}
AckStatMessage::AckStatMessage(short _numOfPosts, short _numOfFollowing, short _numOfFollowers): AckMessage(8),
            numOfPosts(_numOfPosts), numOfFollowing(_numOfFollowing), numOfFollowers(_numOfFollowers){}
AckStatMessage::AckStatMessage(const AckStatMessage& ackStatMessage): AckMessage(8),
                numOfPosts(ackStatMessage.numOfPosts), numOfFollowing(ackStatMessage.numOfFollowing),
                numOfFollowers(ackStatMessage.numOfFollowers){}

AckStatMessage& AckStatMessage::operator=(const AckStatMessage &ackStatMessage) {
    numOfPosts = ackStatMessage.numOfPosts;
    numOfFollowing = ackStatMessage.numOfFollowing;
    numOfFollowers = ackStatMessage.numOfFollowers;
    return *this;
}

short AckStatMessage::getNumOfPosts() {
    return numOfPosts;
}

short AckStatMessage::getNumOfFollowings() {
    return numOfFollowing;
}

short AckStatMessage::getNumOfFollowers() {
    return numOfFollowers;
}