#include "ConnectionHandler.h"
#include <iostream>
#include <fstream>
#include <boost/thread.hpp>
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>
#include <EncoderDecoder.h>

using namespace std;

//int main(int argc, char **argv)
//{
//
//    if (argc < 3) {
//        cerr << "Usage: " << argv[0] << " host port" << endl << endl;
//        return -1;
//    }
//    string host = argv[1];
//    short port = (short)atoi(argv[2]);
//
//    ConnectionHandler* connectionHandler = new ConnectionHandler(host, port);
//    if (!connectionHandler->connect()) {
//        cerr << "Cannot connect to " << host << ":" << port << endl;
//        return 1;
//    }
//
//
//
//    //
////    ClientState* state = new ClientState();
////
////    CommandLineListener commandLineListener(state, connectionHandler);
////    ServerResponseListener serverResponseListener(state, connectionHandler);
////    boost::thread t1(&CommandLineListener::run, &commandLineListener);
////    boost::thread t2(&ServerResponseListener::run, &serverResponseListener);
////    t1.join();
////    t2.join();
//
//    connectionHandler->close();
//    return 0;

//    string line = "REGISTER amir shkedy";
//    vector<string> splitInput;
//    boost::split(splitInput, line, boost::is_any_of(" "));
//    EncoderDecoder encdec;
//    vector<char> myvector;
//    char* oneArray = new char;
//    shortToBytes(1, oneArray);
//    pushBytesToVector(oneArray, 2, myvector);
//    cout << "top right there";
//}
//
//void pushBytesToVector(char *bytes, int size, vector<char> &myVec){
//    for(int i = 0; i < size; i++){
//        myVec.push_back(bytes[i]);
//    }
//}
//// works with a reference so doesn't need a return value
//void EncoderDecoder::shortToBytes(short num, char *bytesArray){
//    bytesArray[0] = (char)((num  >> 8) & 0xFF);
//    bytesArray[1] = (char)(num & 0xFF);
//}


