//
// Created by shkedya@wincs.cs.bgu.ac.il on 11/13/18.
//

#include "Action.h"
#include "Table.h"
#include "Restaurant.h"
using namespace std;

Close::Close(int id): tableId(id) {
}

void Close::act(Restaurant &restaurant) {
    if(tableId < restaurant.getNumOfTables() && restaurant.getTable(tableId)->isOpen()) {
        Table *tableToClose = restaurant.getTable(tableId);
        int bill = 0;
        int numberOfCustomersInTable = tableToClose->getCustomers().size();
        vector<int> CurrentOrdersSoFar;
        int sizeOfCurrentOrderSoFarVector = tableToClose->getCustomer(0)->getOrdersSoFar().size();
        for (int i = 0; i < numberOfCustomersInTable; i++) {
            CurrentOrdersSoFar = tableToClose->getCustomer(i)->getOrdersSoFar();
            sizeOfCurrentOrderSoFarVector = CurrentOrdersSoFar.size();
            for (int j = 0; j < sizeOfCurrentOrderSoFarVector; j++) {
                bill = bill + restaurant.getMenu().at(CurrentOrdersSoFar.at(j)).getPrice();
            }
        }
        complete();
        cout << "Table " << tableId << " was closed. Bill " << bill << "NIS" << endl;
    }
    else{
        error("Table does not exist or is not open");
    }
}

std::string Close::toString() const {

}