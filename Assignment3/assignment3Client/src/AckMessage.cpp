//
// Created by dapool on 07/01/19.
//

#include "AckMessage.h"

AckMessage::AckMessage(): Message(10), ackmsgOpcode(0) {}
AckMessage::AckMessage(short _ackmsgOpcode): Message(10), ackmsgOpcode(_ackmsgOpcode) {}
AckMessage::AckMessage(const AckMessage &message): Message(10), ackmsgOpcode(message.ackmsgOpcode)  {}

AckMessage& AckMessage::operator=(const AckMessage &message) {
    ackmsgOpcode = message.ackmsgOpcode;
    return *this;
}


short AckMessage::getMsgOpcode() {
    return ackmsgOpcode;
}
