//
// Created by dapool on 04/11/18.
//

//vegeterian dish with smallest id, after the most expensive beverage
#include "Customer.h"
#include <iostream>
#include <string>
#include <vector>
using namespace std;

VegetarianCustomer::VegetarianCustomer(std::string name, int id): Customer(name, id){
}

//VegetarianCustomer::VegetarianCustomer(const VegetarianCustomer &other) {
//    Customer();
//    VegetarianCustomer(other.getName(), other.getId();
//}

// can be done more efficient becasue we know what he will order till he dies!!!!!(constant)
vector<int> VegetarianCustomer::order(const vector <Dish> &menu) {
    vector<int> temp = sortIdByPrice(menu);
    ordersSoFar.push_back(findFirstOfType(temp, menu, VEG, true));
    ordersSoFar.push_back(findFirstOfType(temp, menu, BVG, false));
    return ordersSoFar;
}

std::string VegetarianCustomer::toString() const {
}


//VegetarianCustomer::~VegetarianCustomer() {
//    delete myFavType;
//}