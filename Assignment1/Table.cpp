//
// Created by yarinma on 04/11/2018.
//

#include "Table.h"
#include <iostream>
using namespace std;


Table::Table(int t_capacity) {
    capacity = t_capacity;
    open = false;
}

//Table::Table(const Table &otherTable) {
//    this->capacity = capacity;
//    this->open = open;
//    this->customersList = otherTable.customersList;
//    this->orderList = otherTable.orderList;
//}


//Table& Table::operator=(const Table &other) {
//    this->capacity = other.capacity;
//    this->open = other.open;
//    this->customersList = other.customersList;
//    this->orderList = other.orderList;
//
//    return *this;
//}


void Table::addCustomer(Customer *customer) {
    customersList.push_back(customer);
}

void Table::removeCustomer(int id) {
    //Done the same in getCustomer - should not use the same code twice - should be checked

    for (int i = 0; i < customersList.size() ; i++) {
        if(customersList.at(i)->getId() == id)
            customersList.erase(customersList.begin()+i);
    }
}

int Table::getCapacity() const {
    return capacity;
}

bool Table::isOpen() {
    return open;
}

Customer* Table::getCustomer(int id) {
    Customer* cust = nullptr;// if cust is null we need to check it on the client side
    for (int i = 0; i < customersList.size(); i++) {
        if (customersList.at(i)->getId() == id){
            cust = customersList.at(i);
        }
    }
    return cust;
}

int Table::getBill() {
    int billAmount = 0;
    for (int i = 0; i < customersList.size(); i++){
        billAmount = billAmount + this->orderList.at(i).second.getPrice();
    }

    return billAmount;
}

// 09.11.2018 - 15:25
// Go over list of customers, and use their specific order method to order their needs.
void Table::order(const vector<Dish> &menu) {
    for(int i = 0; i < orderList.size(); i++){
        customersList.at(i)->order(menu);
    }
}

void Table::closeTable() {
    if(!isOpen()){
        cout << " Table does not exist or is not open" << endl;
    }
    else{
        int bill = getBill();
        customersList.empty(); //Need to check if it creates a memory leak
        cout << "Table " <<   "was closed. Bill " << bill << "NIS ";
    }


}

void Table::openTable(){
    // need to be checked
    // I did it in 15:06 - 09.11.2018 - not sure that it is true
    this->open = true;
}

vector<OrderPair>& Table::getOrders() {
    return orderList;
}


std::vector<Customer*>& Table::getCustomers() {
    return customersList;
}

