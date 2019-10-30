//
// Created by yarinma on 04/11/2018.
//
#include "Action.h"
using namespace std;

BaseAction::BaseAction() {
    this->status = PENDING;
}

void BaseAction::complete() {
    this->status = COMPLETED;

}

void BaseAction::error(std::string errorMsg) {
    // If an action resulted in an error then the protected method error(std::string errorMsg) should be called
    //When an action results in an error, the program should print to the screen:
    //"Error: <error_message>"
    //More details about the actions will be provided in section 3.4.
    this->status = ERROR;
    cout << "Error: " << errorMsg << endl;
    this->errorMsg = errorMsg;
}

void BaseAction::act(Restaurant &restaurant) {

}

