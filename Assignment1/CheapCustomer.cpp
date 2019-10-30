//
// Created by dapool on 04/11/18.
//

//cheapest dish, next strategy is no order
#include "Customer.h"
#include <iostream>
#include <string>
#include <vector>
using namespace std;

CheapCustomer::CheapCustomer(std::string name, int id): Customer(name, id) {}

vector<int> CheapCustomer::order(const vector <Dish> &menu) {
    if(ordersSoFar.size() == 0) {
        vector<int> temp = sortIdByPrice(menu);
        ordersSoFar.push_back(temp.at(0));
        return ordersSoFar;
    }
    else {
        return ordersSoFar;
    }
}

std::string CheapCustomer::toString() const {
}