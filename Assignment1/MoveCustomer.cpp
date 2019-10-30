//
// Created by shkedya@wincs.cs.bgu.ac.il on 11/13/18.
//

#include "Action.h"
#include "Table.h"
#include "Restaurant.h"
using namespace std;

MoveCustomer::MoveCustomer(int src, int dst, int customerId): srcTable(src), dstTable(dst), id(customerId) {
}

//need to check what type of end to put inside

void MoveCustomer::act(Restaurant &restaurant) {
    if(!(srcTable < restaurant.getNumOfTables() &&
       dstTable < restaurant.getNumOfTables() &&
       restaurant.getTable(srcTable)->getCustomer(id) != nullptr &&
       restaurant.getTable(dstTable)->isOpen() &&
       restaurant.getTable(dstTable)->getCapacity() - restaurant.getTable(dstTable)->getCustomers().size() > 0))
        error("Cannot move customer");
    else{
        Table* sourceTable = restaurant.getTable(srcTable);
        Table* destinationTable = restaurant.getTable(dstTable);
        Customer* customerToMove = sourceTable->getCustomer(id);
        destinationTable->addCustomer(customerToMove);
        sourceTable->removeCustomer(customerToMove->getId());
        complete();
    }
}

std::string MoveCustomer::toString() const {
    return string("here we are in move customer action");
}
