//
// Created by shkedya@wincs.cs.bgu.ac.il on 11/13/18.
//

#include "Action.h"
#include "Table.h"
#include "Restaurant.h"
using namespace std;

Order::Order(int id): tableId(id) {
}

void Order::act(Restaurant &restaurant) {
    if (tableId >= restaurant.getNumOfTables() || !(restaurant.getTable(tableId)->isOpen())) {
        error("Table does not exist or is not open");
    }
    else {
        Table *myTable = restaurant.getTable(tableId);
        for (int i = 0; i < myTable->getCustomers().size(); i++)
            myTable->getCustomer(i)->order(restaurant.getMenu());
        complete();
    }

}

std::string Order::toString() const {
    return string("this is the Order class");
}

//
//int tableId = stoi(commandlineVector[numberOfFieldsSoFar+1]);
//if (tableId >= this->getNumOfTables() || !(this->getTable(tableId)->isOpen())) {
//cout << "Table does not exist or is not open";
//}
//Table *myTablel = this->getTable(tableId);
//for (int i = 0; i < myTablel->getCustomers().size(); i++) {
//myTablel->getCustomer(i)->order(menu);
//}
//
