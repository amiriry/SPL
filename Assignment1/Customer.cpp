//
// Created by yarinma on 04/11/2018.
//

#include "Customer.h"
#include <algorithm>
#include <string>
#include <iostream>
#include <cstring>
#include <boost/algorithm/string/predicate.hpp>

using namespace std;

Customer::Customer(std::string c_name, int c_id): id(c_id), name(c_name)  {
}

//Customer::Customer(const Customer &other) {
//    Customer(other.getName(), other.getId());
//}

string Customer::getName() const {
    return name;
}

int Customer::getId() const {
    return id;
}


// we need to do it better - lehalek et ze
vector<int> Customer::sortIdByPrice(const vector<Dish> &menu) {
    vector<int> sortedMenuByPrice;
    for(int i = 0; i < menu.size(); i++){
        sortedMenuByPrice.push_back(i);
    }

    for(int i = 0; i < menu.size(); i++){
        for(int j = i+1; j < menu.size(); j++){
            if(menu.at(sortedMenuByPrice.at(i)).getPrice() > menu.at(sortedMenuByPrice.at(j)).getPrice()) {
                int temp = sortedMenuByPrice.at(i);
                sortedMenuByPrice.at(i) = sortedMenuByPrice.at(j);
                sortedMenuByPrice.at(j) = temp;
            }
        }
    }
    return sortedMenuByPrice;

}

// need to write the return once
int Customer::findFirstOfType(std::vector<int> dishesIdByPrice, const std::vector<Dish> &menu, DishType type, bool cheapest) {
    int dishIDToReturn = -1;
    if(cheapest){
        for(int i=0; i < dishesIdByPrice.size(); i++) {
            if (boost::iequals(stringByEnum(menu.at(dishesIdByPrice.at(i)).getType()),stringByEnum(type))) {
                dishIDToReturn = menu.at(dishesIdByPrice.at(i)).getId();
                break;
            }

        }

    }
    else{
       for(int i=dishesIdByPrice.size()-1; i >=0 ; i--) {
           if (boost::iequals(stringByEnum(menu.at(dishesIdByPrice.at(i)).getType()),stringByEnum(type))) {
               dishIDToReturn = menu.at(dishesIdByPrice.at(i)).getId();
               break;
           }
       }
    }

    return dishIDToReturn;
}

std::string Customer::stringByEnum(DishType a) {
    string stringToReturn;
    if(a == DishType::ALC)
        stringToReturn = "ALC";
    else if(a == DishType::VEG)
        stringToReturn = "VEG";
    else if(a == DishType::SPC)
        stringToReturn = "SPC";
    else
        stringToReturn = "BVG";

    return stringToReturn;
}

vector<int> Customer::getOrdersSoFar()  {
    return ordersSoFar;
}



//Customer::~Customer() {
//}
