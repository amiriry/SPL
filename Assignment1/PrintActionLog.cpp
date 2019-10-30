//
// Created by shkedya@wincs.cs.bgu.ac.il on 11/13/18.
//

#include "Action.h"
#include "Table.h"
#include "Restaurant.h"
using namespace std;

PrintActionsLog::PrintActionsLog() {}

void PrintActionsLog::act(Restaurant &restaurant) {
    vector<BaseAction *> allLog = restaurant.getActionsLog();
    for(int i = 0; i < allLog.size(); i++){
        cout << allLog.at(i)->toString() << endl;
    }
    complete();
}

std::string PrintActionsLog::toString() const {
    return string("This is the print log action");
}

