//
// Created by dapool on 04/11/18.
//

//cheapest alcoholic beverege , which i didnt order before.
#include "Customer.h"
#include <iostream>
#include <string>
#include <vector>
#include <boost/algorithm/string/predicate.hpp>
using namespace std;

AlchoholicCustomer::AlchoholicCustomer(std::string name, int id): Customer(name, id) {}

// changed ordersSoFar to a pointer - so every dot was changed to ->
// I think that you don't need to use new to initalize this - so I changed it back to a vector and chanegd the arrows to dots
vector<int> AlchoholicCustomer::order(const vector <Dish> &menu) {
    int iD;
    vector<int> temp;
    if(ordersSoFar.size() == 0) {
        iD = findFirstOfType(sortIdByPrice(menu), menu, ALC, true);
        if(iD != -1) //im en alkohol az hu noten li minus 1
            ordersSoFar.push_back(iD);
    }

    // The case where there are only alchoholic beverages
    else if(findFirstOfType(sortIdByPrice(menu), menu, ALC, true) == -1){
        temp = sortIdByPrice(menu);
        ordersSoFar.push_back(temp.at(ordersSoFar.size()));
    }


    // other way to check:
    /*
     * Compare the number of alchoholic dishes you encounter in the ordered by dishes
     * with the orders so far - so you get to the next you didn't encounter
     */
    else{

        int fillInId = -1;
        int i = 0;
        for (i = 0; i < menu.size() && fillInId == -1; i++) {
            if (!(boost::iequals(stringByEnum(menu.at(i).getType()), "ALC")))
                fillInId = menu.at(i).getId();
        }

        temp = sortIdByPrice(menu);
        for (i = 0; i < temp.size() && fillInId != -1; i++) {
            if (temp.at(i) != ordersSoFar.at(ordersSoFar.size() - 1)) {
                temp.at(i) = fillInId;
            } else {
                temp.at(i) = fillInId;
                break;
            }
        }
        iD = findFirstOfType(temp, menu, ALC, true);
        if(iD != -1)
            ordersSoFar.push_back(iD);

    }
    return ordersSoFar;
}

std::string AlchoholicCustomer::toString() const {
}