//
// Created by shkedya@wincs.cs.bgu.ac.il on 11/14/18.
//

#include "Action.h"
#include "Table.h"
#include "Restaurant.h"
using namespace std;

PrintMenu::PrintMenu() {}

void PrintMenu::act(Restaurant &restaurant) {
    vector<Dish> menu = restaurant.getMenu();

    // this is a great solution for the enum problem should think about using it more
    string dishTypeStrings[4] = {"VEG", "SPC", "BVG", "ALC"};
    // every place the we calculate this condition we can do it better
    int menuSize = restaurant.getMenu().size();
    for(int i = 0; i < menuSize; i++){

        cout << menu.at(i).getName() << " "
             << dishTypeStrings[menu.at(i).getType()] << " "
             << menu.at(i).getPrice() << "NIS\n";
    }
}

std::string PrintMenu::toString() const {
    return string("this is PrintMenu command");
}
