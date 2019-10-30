//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_BIDIPROTOCOL_H
#define ASSIGNMENT3CLIENT_BIDIPROTOCOL_H

#include "Message.h"
#include "ConnectionHandler.h"

class BidiProtocol{
private:
    ConnectionHandler* connectionHandler;
public:
    BidiProtocol(ConnectionHandler* connectionHandler);
    BidiProtocol(const BidiProtocol& myBidiProtocol);
    BidiProtocol &operator=(const BidiProtocol& myBidiProtocol);
    void process(Message* p);
};

#endif //ASSIGNMENT3CLIENT_BIDIPROTOCOL_H
