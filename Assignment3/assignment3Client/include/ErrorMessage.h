//
// Created by dapool on 07/01/19.
//

#ifndef ASSIGNMENT3CLIENT_ERRORMESSAGE_H
#define ASSIGNMENT3CLIENT_ERRORMESSAGE_H
class ErrorMessage: public Message{
private:
    short msgOpcode;
public:
    ErrorMessage();     // what that for? - check
    ErrorMessage(short msgOpcde);
    ErrorMessage(const ErrorMessage& errorMessage);
    ErrorMessage& operator=(const ErrorMessage& errorMessage);
    virtual ~ErrorMessage() = default;
    short getMsgOpcode();
};


#endif //ASSIGNMENT3CLIENT_ERRORMESSAGE_H
