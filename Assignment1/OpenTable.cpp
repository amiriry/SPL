//
// Created by shkedya@wincs.cs.bgu.ac.il on 11/8/18.
//

#include "Action.h"
#include "Table.h"
#include "Restaurant.h"
using namespace std;

//Opens a given table and assigns a list of customers to it. If the table doesn't exist or is already open, this action
// should result in an error: “Table does not exist or is already open"

//i have TABLE ID HERE
//AND  A VECTOR OF CUSTOMERS(HERE)
//ERROR MSG AND STATUS ARE GIVEN BY BASEACTION
//constructor. act . and to string
//baseAction:complete,error

OpenTable::OpenTable(int id, std::vector<Customer *> &customersList): tableId(id), customers(customersList){
}

void OpenTable::act(Restaurant &restaurant) {
    if(tableId >= restaurant.getNumOfTables() || restaurant.getTable(tableId)->isOpen())
        error("Table does not exist or is already open");
    else{
        restaurant.getTable(tableId)->openTable();
        for(int i = 0; i < customers.size(); i++) {
            restaurant.getTable(tableId)->addCustomer(customers.at(i));
        }
        complete();
    }
}

std::string OpenTable::toString() const {
    return string("this is OpenTable to string function");
}



