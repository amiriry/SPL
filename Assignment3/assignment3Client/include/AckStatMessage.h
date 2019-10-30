//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_ACKSTATMESSAGE_H
#define ASSIGNMENT3CLIENT_ACKSTATMESSAGE_H

#include "AckMessage.h"

class AckStatMessage: public AckMessage{
private:
    short numOfPosts;
    short numOfFollowing;
    short numOfFollowers;

public:
    AckStatMessage();   // still not sure why is this needed - check
    AckStatMessage(short numOfPosts, short numOfFollowing, short numOfFollowers);
    AckStatMessage(const AckStatMessage& ackStatMessage);
    AckStatMessage& operator=(const AckStatMessage& ackStatMessage);
    virtual ~AckStatMessage() = default;
    short getNumOfPosts();
    short getNumOfFollowings();
    short getNumOfFollowers();
};

#endif //ASSIGNMENT3CLIENT_ACKSTATMESSAGE_H
