//
// Created by Amir Shkedy on 2019-01-01.
//

#ifndef CLIENT_ENCODERDECODER_H
#define CLIENT_ENCODERDECODER_H

#include <string>
#include <vector>
//#include "ConnectionHandler.h"


class EncoderDecoder{
public:
    EncoderDecoder();
    virtual ~EncoderDecoder();
    std::vector<char> encode(std::string line);
    std::vector<char> decodeNextMessage(short opcode);
    void pushBytesToVector(char *bytes, int size, std::vector<char> &vec);
    void shortToBytes(short num, char *bytesArray);
    short bytesToShort(char *bytes);
};


#endif //CLIENT_ENCODERDECODER_H

