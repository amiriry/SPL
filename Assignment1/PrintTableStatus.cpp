//
// Created by shkedya@wincs.cs.bgu.ac.il on 11/14/18.
//

#include "Action.h"
#include "Table.h"
#include "Restaurant.h"
using namespace std;
PrintTableStatus::PrintTableStatus(int id): tableId(id) {}
void PrintTableStatus::act(Restaurant &restaurant) {
    Table* currentTable = restaurant.getTable(tableId);
    int bill = 0;
    // if the table is open
    if(currentTable->isOpen()){
        cout << "Table " << tableId << " status: open" << endl;
        // Go over customers
        vector<Customer *> currentTableCusList = currentTable->getCustomers();
        for(int i = 0; i < currentTableCusList.size(); i++){
            cout << currentTableCusList.at(i)->getId() << " " << currentTableCusList.at(i)->getName() << endl;
        }
        // Go over orders and count the bill
        for(int i = 0; i < currentTableCusList.size(); i++){
            vector<int> ordersSoFarPerCus = currentTableCusList.at(i)->getOrdersSoFar();
            for(int j = 0; j < ordersSoFarPerCus.size(); j++) {
                cout << restaurant.getMenu().at(ordersSoFarPerCus.at(j)).getName() << " "
                     << restaurant.getMenu().at(ordersSoFarPerCus.at(j)).getPrice() << "NIS "
                     << i << endl; // I think that it should work this is the id of the customer
                bill = bill + restaurant.getMenu().at(ordersSoFarPerCus.at(j)).getPrice();
            }
        }
        // Print the bill
        cout << "Current bill: " << bill << endl;
    }
        // if table is closed
    else
        cout << "Table " << tableId << " status: closed" << endl;
}
std::string PrintTableStatus::toString() const {
    return string("this is the PrintTableStatus command");
}


