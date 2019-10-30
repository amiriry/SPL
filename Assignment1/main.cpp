#include "Restaurant.h"
#include <iostream>
#include <fstream>
#include <sstream>
using namespace std;

Restaurant* backup = nullptr;

int main(int argc, char** argv){

    if(argc!=2){
        std::cout << "usage: rest <config_path>" << std::endl;
        return 0;
    }
    string configurationFile = argv[1];
    Restaurant rest(configurationFile);
    string userInput;
    rest.start();


    // 09.11.2018 - here I wrote just to see that it works - i deleted it now
    // because I think that all the restaurant works from start function

    if(backup!=nullptr){
    	delete backup;
    	backup = nullptr;
    }


   return 0;
}