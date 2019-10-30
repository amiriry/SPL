//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_KEYBOARDLISTENER_H
#define ASSIGNMENT3CLIENT_KEYBOARDLISTENER_H

#include "ConnectionHandler.h"
#include "BidiProtocol.h"

class KeyboardListener{
private:
    BidiProtocol* aProtocol;
    ConnectionHandler* connHand;
public:
    KeyboardListener(ConnectionHandler *connHand, BidiProtocol* aProtocol);
    KeyboardListener(const KeyboardListener& otherKL);
    virtual ~KeyboardListener();
    KeyboardListener operator=(const KeyboardListener& otherKL);
    void operator()();
    void handle();
};

#endif //ASSIGNMENT3CLIENT_KEYBOARDLISTENER_H


