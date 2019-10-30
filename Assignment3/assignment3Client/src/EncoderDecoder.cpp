//
// Created by Amir Shkedy on 2019-01-01.
//

#include "../include/EncoderDecoder.h"
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>
#include <iostream>


using namespace std;

EncoderDecoder::EncoderDecoder(){
}

EncoderDecoder::~EncoderDecoder() {}

vector<char> EncoderDecoder::encode(string line) {

    vector<string> splitinput;
    vector<char> toReturn;
    char *bArray = new char[2];
    char* zeroByte = new char(0); //does it gives zero as char*?

    boost::split(splitinput, line, boost::is_any_of(" "));
    if(splitinput[0].compare("REGISTER") == 0){
        if(splitinput.size() == 3){
            // push opcode
            char* oneArray = new char;
            shortToBytes(1, oneArray);
            pushBytesToVector(oneArray, 2, toReturn);

            // push name
            char* name = new char;
            strcpy(name, splitinput[1].c_str());
            pushBytesToVector(name, strlen(name), toReturn);

            // push zero
            pushBytesToVector(zeroByte, 1, toReturn);               //not sure that this is how it should be down

            // push name
            char* password = new char;
            strcpy(password, splitinput[2].c_str());
            pushBytesToVector(password, strlen(password), toReturn);
            pushBytesToVector(zeroByte, 1, toReturn);
        } else
            cout << "usage: LOGIN <username> <password>";
    }
    else if(splitinput[0].compare("LOGIN") == 0){
        if(splitinput.size() == 3){
            // push opcode
            shortToBytes(2, bArray);
            pushBytesToVector(bArray, 2, toReturn);

            // push name
            char* name = new char;
            strcpy(name, splitinput[1].c_str());
            pushBytesToVector(name, strlen(name), toReturn);

            // push zero
            pushBytesToVector(0, 1, toReturn);               //not sure that this is how it should be down

            // push name
            char* password = new char;
            strcpy(password, splitinput[2].c_str());
            pushBytesToVector(password, strlen(password), toReturn);
            pushBytesToVector(0, 1, toReturn);
        } else
            cout << "usage: REGISTER <username> <password>";
    }
    else if(splitinput[0].compare("LOGOUT") == 0){
        if(splitinput.size() == 1)
            shortToBytes((short) 3, bArray);
        else
            cout << "usgae: LOGOUT" << endl;

    }
    else if(splitinput[0].compare("FOLLOW") == 0){
        // split in 3 place is the String of follow/unfollow
        // its only one character - and the first character is the one needed
        short numToFollow = splitinput[2][0] - '0';

        // number fields before the list of users and the number of users
        if(splitinput.size() == 3 + numToFollow){
            // push opcode
            shortToBytes((short) 4, bArray);
            pushBytesToVector(bArray, 2, toReturn);

            //push follow/Unfollow
            pushBytesToVector(&splitinput[1][0], 1, toReturn); //notice: &c is a pointer for c

            // push number of users
            char* numOfUsersBytes = new char;
            strcpy(numOfUsersBytes, splitinput[2].c_str());
            pushBytesToVector(numOfUsersBytes, 2, toReturn);

            // push users
            for(int i = 0; i < numToFollow; i++){   // going only on users
                char* username = new char;
                strcpy(username, splitinput[3+i].c_str()); // user are from the forth place and forward
            }
            // push zero byte in the end
            pushBytesToVector(0, 1, toReturn);
        }
        else
            cout << "Usage: FOLLOW <0/1> <numOfUsers> <userName1> <UserName2> ....";
    }
    else if(splitinput[0].compare("POST") == 0){
        // push opcode
        shortToBytes((short) 5, bArray);
        pushBytesToVector(bArray, 2, toReturn);

        // push content
        for(int i = 1; i < splitinput.size(); i++) {
            char *word = new char;
            // don't know if it works - the split is with spaces - but when I sent it as a content I
            // want to have spaces
            if (i != splitinput.size() - 1)
                strcpy(word, (splitinput[i] + " ").c_str());
            else
                strcpy(word, splitinput[i].c_str());

            pushBytesToVector(word, strlen(word), toReturn);
        }

        // Zero in the end
        pushBytesToVector(0, 1, toReturn);
    }
    else if(splitinput[0].compare("PM") == 0){
        // push opcode
        shortToBytes((short) 5, bArray);
        pushBytesToVector(bArray, 2, toReturn);         // this can be done once

        char* username = new char;
        strcpy(username, splitinput[1].c_str());
        pushBytesToVector(username, strlen(username), toReturn);

        // push content
        for(int i = 1; i < splitinput.size(); i++) {
            char *word = new char;
            // don't know if it works - the split is with spaces - but when I sent it as a content I
            // want to have spaces
            if (i != splitinput.size() - 1)
                strcpy(word, (splitinput[i] + " ").c_str());
            else
                strcpy(word, splitinput[i].c_str());

            pushBytesToVector(word, strlen(word), toReturn);
        }
        // Zero in the end
        pushBytesToVector(0, 1, toReturn);
    }
    else if(splitinput[0].compare("USERLIST") == 0){
        if(splitinput.size() == 1)
            shortToBytes((short) 7, bArray);
        else
            cout << "input is invalid" << endl;
    }
    else if(splitinput[0].compare("STAT") == 0){
        // push opcode
        shortToBytes((short) 5, bArray);
        pushBytesToVector(bArray, 2, toReturn);         // this can be done once

        char* username = new char;
        strcpy(username, splitinput[1].c_str());
        pushBytesToVector(username, strlen(username), toReturn);

        // Zero in the end
        pushBytesToVector(0, 1, toReturn);
    } else
        cout << "Not a valid command";
    return toReturn;
}

vector<char> EncoderDecoder::decodeNextMessage(short opcode) {

}

void EncoderDecoder::pushBytesToVector(char *bytes, int size, vector<char> &myVec){
    for(int i = 0; i < size; i++){
        myVec.push_back(bytes[i]);
    }
}
// works with a reference so doesn't need a return value
void EncoderDecoder::shortToBytes(short num, char *bytesArray){
    bytesArray[0] = (char)((num  >> 8) & 0xFF);
    bytesArray[1] = (char)(num & 0xFF);
}

short EncoderDecoder::bytesToShort(char *bytes){
    short myshort = (short) ((bytes[0] & 0xff) << 8);
    myshort += (short) (bytes[1] & 0xff);
    return myshort;
}


