//
// Created by Amir Shkedy on 2019-01-02.
//

#include <thread>
#include "EncoderDecoder.h"
#include "ConnectionHandler.h"
#include "BidiProtocol.h"
#include "Message.h"
#include "KeyboardListener.h"

int main(int argc, char *argv[]){
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);

//    EncoderDecoder encdec;
//check
    ConnectionHandler connectionHandler(host, port, encdec);
    BidiProtocol myProtocol(&connectionHandler); //check - need to add login

    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    KeyboardListener myKeyListener(&connectionHandler, &myProtocol);
    std::thread keyboardThread(myKeyListener);
    while(1){
        Message *myMessage = connectionHandler.
    }
//    BidiProtocol bidiProtocol(&connectionHandler);



    // keep doing nutil dead!
//    while(1){
        // an old implementation
        /*
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        int len=line.length();
        vector<char> mytry = encdec.encode(line);
        string mylinestring(mytry.begin(), mytry.end()+1);
        cout<< "this is my string " + mylinestring << endl;
        */

//        if (!connectionHandler.sendLine(mylinestring)) {
//            std::cout << "Disconnected. Exiting...\n" << std::endl;
//            break;
//        }

//        if (!connectionHandler.getLine(answer)) {
//            std::cout << "Disconnected. Exiting...\n" << std::endl;
//            break;
//        }
//    }
}
