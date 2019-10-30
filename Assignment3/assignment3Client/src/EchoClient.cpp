#include <stdlib.h>
#include "ConnectionHandler.h"
#include <thread>
#include <boost/algorithm/string.hpp>


std::atomic<bool> out(false);


void user(ConnectionHandler &connectionHandler) {
    while(!std::cin.eof()) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        if (!connectionHandler.sendLine(line)) {
            break;
        }
        std::string signout = "signout";
        if(out.load()==true && boost::iequals(signout, line) ){
            // std::cout << "Working...\n" << std::endl;
            break;
        }
    }
}

void server(ConnectionHandler &connectionHandler) {
    while(1) {
        std::string answer;
        if (!connectionHandler.getLine(answer)) {
            break;
        }

        int len=answer.length();

        answer.resize(len-1);
        std::cout << answer<< std::endl;
        if (answer == "ACK signout succeeded") {
            std::cout << "Exiting...\n" << std::endl;
            connectionHandler.close();
            break;
        }
        if (answer == "ACK login succeeded") {
            out=true;
        }
    }
}

//int main (int argc, char *argv[]) {
//
//    if (argc < 3) {
//        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
//        return -1;
//    }
//    std::string host = argv[1];
//    short port = atoi(argv[2]);
//
//    ConnectionHandler connectionHandler(host, port);
//    if (!connectionHandler.connect()) {
//        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
//        return 1;
//    }
//
//    std::thread userThread(user,std::ref(connectionHandler));
//
//    while(1) {
//        std::string answer;
//        if (!connectionHandler.getLine(answer)) {
//            break;
//        }
//
//        int len=answer.length();
//
//        answer.resize(len-1);
//        std::cout << answer<< std::endl;
//        if (answer == "ACK signout succeeded") {
//            //std::cout << "Exiting...\n" << std::endl;
//            connectionHandler.close();
//            break;
//        }
//        if (answer == "ACK login succeeded") {
//            out=true;
//        }
//    }
//    //  std::thread serverThread(server,std::ref(connectionHandler));
//
//    userThread.join();//lehazirrrrrrrrrrrrrrrrrrrrrr
////    serverThread.join();std::cout << "2...\n" << std::endl;
//
//    return 0;
//}


