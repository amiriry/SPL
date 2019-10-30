//
// Created by dapool on 07/01/19.
//
#include "Message.h"

#ifndef ASSIGNMENT3CLIENT_ACKMESSAGE_H
#define ASSIGNMENT3CLIENT_ACKMESSAGE_H
class AckMessage: public Message{
private:
    short ackmsgOpcode;
public:
    AckMessage();           // not sure why is it needed
    AckMessage(short ackmsgOpcode);
    AckMessage(const AckMessage& message);
    AckMessage& operator=(const AckMessage& message);
    virtual ~AckMessage() = default;
    short getMsgOpcode();
};
#endif //ASSIGNMENT3CLIENT_ACKMESSAGE_H
