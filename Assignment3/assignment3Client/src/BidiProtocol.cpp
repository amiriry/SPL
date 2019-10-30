//
// Created by dapool on 07/01/19.
//
#include "BidiProtocol.h"
#include "NotificationMessage.h"
#include "AckMessage.h"
#include "AckFollowMessage.h"
#include "AckUserlistMessage.h"
#include "AckStatMessage.h"
#include "ErrorMessage.h"


using namespace std;

BidiProtocol::BidiProtocol(ConnectionHandler *connectionHandler): connectionHandler(connectionHandler) {}
BidiProtocol::BidiProtocol(const BidiProtocol &myBidiProtocol):connectionHandler(myBidiProtocol.connectionHandler) {}

BidiProtocol &BidiProtocol::operator=(const BidiProtocol &myBidiProtocol) {
    connectionHandler = myBidiProtocol.connectionHandler;
    return *this;
}

void BidiProtocol::process(Message *message) {
    short opCode = message->getOpcode();
    switch (opCode){
        case 9:{
            NotificationMessage *notificationMessage = dynamic_cast<NotificationMessage *>(message);
            string type = "";
            if(notificationMessage->getType() == 1)
                type = "Public";
            else
                type = "PM";

            cout << "NOTIFICATION " << type  << " " << notificationMessage->getPostingUser() << " "
                    + notificationMessage->getContent();
        }
        case 10: {
            AckMessage *ackMessage = dynamic_cast<AckMessage *>(message);
            short ackMsgOpcode = ackMessage->getMsgOpcode();
            switch(ackMsgOpcode){
                case 2:{
                    // should have a parameter to notice that
                    cout << "ACK" << ackMsgOpcode << endl;
                    break;
                }
                case 3:{
                    connectionHandler->close();
                }
                case 4:{
                    // check - is it ok to use dynamic cast?
                    AckFollowMessage *ackFollowMessage = dynamic_cast<AckFollowMessage *>(message);
                    cout << "ACK " << ackMsgOpcode << " " << ackFollowMessage->getNumOfUsers() << " "
                         << ackFollowMessage->getUserNameList() << endl;
                }
                case 7:{
                    AckUserlistMessage *ackUserlistMessage = dynamic_cast<AckUserlistMessage *>(message);
                    cout << "ACK " << ackMsgOpcode << " " << ackUserlistMessage->getNumOfUsers() << " "
                         << ackUserlistMessage->getUserNameList() << endl;
                }
                case 8: {
                    AckStatMessage *ackStatMessage = dynamic_cast<AckStatMessage *>(message);
                    cout << "ACK " << ackMsgOpcode << " " << ackStatMessage->getNumOfPosts() << " "
                         << ackStatMessage->getNumOfFollowings() << " " << ackStatMessage->getNumOfFollowers() << endl;
                }

                // check - not sure that we need a default state here
                default:
                    cout << "ACK " << ackMsgOpcode << endl;
            }

        }
        case 11:{
          ErrorMessage *errorMessage = dynamic_cast<ErrorMessage *>(message);
          cout << "ERROR " << opCode << endl;
        }

        //check  - should check if there should be a default situation here and why should it happen
    }
}


