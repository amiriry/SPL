//
// Created by dapool on 07/01/19.
//

#include "KeyboardListener.h"
#include "BidiProtocol.h"
#include <string>
#include <boost/algorithm/string.hpp>
using namespace std;

KeyboardListener::KeyboardListener(ConnectionHandler *_connHand, BidiProtocol *_protocol): aProtocol(_protocol),
                    connHand(_connHand){}


void KeyboardListener::operator()() {
    while(1){
        bool isLogout = false;

        // arguments for input
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        int len=line.length();

        vector<string> commandlineSplit;
        boost::split(commandlineSplit, line, boost::is_any_of(" "));
        string command = commandlineSplit[0];

        if(command == "LOGOUT")
            break;
        else
            connHand->sendString(line);


    }
}
