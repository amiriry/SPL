//
// Created by dapool on 07/01/19.
//

#include "Message.h"

Message::Message(): opCode(-1), msgContent("") {}
Message::Message(short _opcode, std::string _msgContent): opCode(_opcode), msgContent(_msgContent) {}
Message::Message(short opcode): opCode(opcode), msgContent("") {}
Message::Message(const Message &message): opCode(message.opCode), msgContent(message.msgContent) {}
Message Message::operator=(const Message &message) {
    //check  - every assignment operator - check if they equal - not sure that it should be done
    opCode = message.opCode;
    return *this;
}

short Message::getOpcode() {
    return this->opCode;
}

std::string Message::getContent() const {
    return msgContent;
}


