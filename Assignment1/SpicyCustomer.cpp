//
// Created by dapool on 04/11/18.
//

//most expensive spicy dish, next strategy is most expensive beverege
#include "Customer.h"
#include <iostream>
#include <string>
#include <vector>
using namespace std;

SpicyCustomer::SpicyCustomer(std::string name, int id): Customer(name, id) {}

vector<int> SpicyCustomer::order(const vector <Dish> &menu) {
    vector<int> temp = sortIdByPrice(menu);
    if(ordersSoFar.size() == 0) {
        ordersSoFar.push_back(findFirstOfType(temp, menu, myFavType, false));
    }
    else{
        ordersSoFar.push_back(findFirstOfType(temp, menu, BVG, false));
    }

    return ordersSoFar;
}

std::string SpicyCustomer::toString() const {
}
