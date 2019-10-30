#include <iostream>

#include "ConnectionHandler.h"

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

using namespace std;

ConnectionHandler::ConnectionHandler(string host, short port, EncoderDecoder _encdec): host_(host), port_(port), io_service_(),
                                    socket_(io_service_), encdec(_encdec){}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
            tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, '\n');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, '\n');
}

bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            getBytes(&ch, 1);
            frame.append(1, ch);
        }while (delimiter != ch);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    bool result=sendBytes(frame.c_str(),frame.length());
    if(!result) return false;
    return sendBytes(&delimiter,1);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

bool ConnectionHandler::sendString(string msgToEncode){
    std::vector<char> toSend = encdec.encode(msgToEncode);
    char *messageToSend;
    for(int i = 0; i < toSend.size(); i++){
        messageToSend[i] = static_cast<char>(toSend.at(i));
    }
    if(!sendBytes(messageToSend, toSend.size()))
        cout << "Disconnected somehow..... sad.... :(";

}

Message ConnectionHandler::decodeNextMesasge() {
    char* opcode = new char[2];
    vector<char> messageByChars;
    getBytes(opcode, 2);

//    Message *myMessage = nullptr;

    // there's a chance that this wouldn't work I don't know what would happen with msgBychar vector
    // how it is copied - check
    short opcodeShort = encdec.bytesToShort(opcode);
    switch(opcodeShort){
        case 9: {                   //Notification
            int zeroCounter = 0;
            char *readingChar = new char[1];
            getBytes(readingChar, 1);
            while (zeroCounter != 2) {
                if (readingChar[1] == '\0') {
                    zeroCounter++;
                    if (zeroCounter == 2) {
                            encdec.decodeNextMessage(opcodeShort, )
                    } else {
                        messageByChars.push_back(readingChar[1]);
                    }
                }
            }
        }
        case 10:{                   //Acknowledge
            break;
        }
        case 11:{                   // Error
            break;
        }
    }
    vector<char> returnArray = encdec.decodeNextByte(opcodeShort);
}

