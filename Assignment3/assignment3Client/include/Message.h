//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_MESSAGE_H
#define ASSIGNMENT3CLIENT_MESSAGE_H

#include <string>

class Message{
private:
    short opCode;
    std::string msgContent;
public:
    Message();              // check - note sure what that for?
    Message(short opcode);
    Message(short opcode, std::string msgContent);
    Message(const Message& message);
    virtual Message operator=(const Message& message);
    virtual ~Message() = default;
    short getOpcode();
    std::string getContent() const;
};

#endif //ASSIGNMENT3CLIENT_MESSAGE_H
