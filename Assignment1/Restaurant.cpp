//
// Created by yarinma on 04/11/2018.
//

#include "Restaurant.h"
#include <iostream>
#include <string>
#include <fstream>
#include <boost/algorithm/string/predicate.hpp>
#include <typeinfo>
#include "Action.h"

using namespace std;


Restaurant::Restaurant(const string &configFilePath) {
    createDatabaseByConfig(configFilePath);
    clientSoFar = 0;
}

Restaurant::Restaurant() {
    open = false;
    //tables = new vector<Table*>;
    // dishes = initialize a vector of dishes
    // BaseActions = initialize a vector of base actions
}

int Restaurant::getClientSoFar() {
    return clientSoFar;
}

const std::vector<BaseAction*>& Restaurant::getActionsLog() const {
    return actionsLog;
}

void Restaurant::incrementClientSoFar() {
    clientSoFar++;
}


void Restaurant::start() {


    int numberOfFieldsSoFar = 0, numberOfFieldsAfterCommand = 0;
    string userInput = "open 2 yarin,alc";
    vector<string> commandlineVector;
    cout << "hi enter command: \n";
//    getline(cin, userInput);
    commandlineVector = lineParser(userInput, commandlineVector, ' ');
    string commandName = commandlineVector[0];
    while(commandName != "cloaseall") {
        // syntax: open <table_id> <customer_1>,< customer_1_strategy> <customer_2>,<customer_2_ strategy>
        // open 2 shalom,veg yarin,veg amir,alc
        if (commandName == "open") {
            vector<Customer*> customersOfTable;
            for (int i = 2+numberOfFieldsSoFar; i < commandlineVector.size(); i++) {
                vector<string> pair;
                pair = lineParser(commandlineVector[i], pair, ',');
                customersOfTable.push_back(returnNewCustomerByType(pair.at(0), pair.at(1)));
                OpenTable *newTable = new OpenTable(stoi(commandlineVector[numberOfFieldsSoFar + 1]), customersOfTable);
                newTable->act(*this);
                actionsLog.push_back(newTable);
            }

        }
        else if (commandName == "order") {
            // syntax: order <table_id>
            // open 2 yarin,veg
            // order 2
            Order* newOrder = new Order(stoi(commandlineVector[numberOfFieldsSoFar+1]));
            newOrder->act(*this);
            actionsLog.push_back(newOrder);
        }
        else if (commandName == "move"){
            // syntax: move <origin_table_id> <dest_table_id> <customer_id>
            MoveCustomer* newMoveCus = new MoveCustomer(stoi(commandlineVector[numberOfFieldsSoFar+1]),
                                                        stoi(commandlineVector[numberOfFieldsSoFar+2]),
                                                        stoi(commandlineVector[numberOfFieldsSoFar+3]));
            newMoveCus->act(*this);
            actionsLog.push_back(newMoveCus);
        }
        else if (commandName == "close"){
            Close* newClose = new Close(stoi(commandlineVector[numberOfFieldsSoFar+1]));
            newClose->act(*this);
            actionsLog.push_back(newClose);
        }
        else if (commandName == "log") {
            PrintActionsLog* newActionLog = new PrintActionsLog;
            newActionLog->act(*this);
        }
        else if (commandName == "menu"){
            PrintMenu* newPrintMenu = new PrintMenu;
            newPrintMenu->act(*this);
        }
        else if (commandName == "status"){
            int idToStatus = stoi(commandlineVector[numberOfFieldsSoFar+1]);
            PrintTableStatus* newTableStatus = new PrintTableStatus(idToStatus);
            newTableStatus->act(*this);
        }
        //numberOfFieldsSoFar=numberOfFieldsSoFar+commandlineVector.size();
        cout << "hi enter command: \n";
        numberOfFieldsSoFar=commandlineVector.size();
        getline(cin, userInput);
        commandlineVector = lineParser(userInput, commandlineVector, ' ');
        numberOfFieldsAfterCommand = commandlineVector.size();
        // number of fields so far is counting the number of fields
        // commandlineVector is 0-based so the index of the next cell which is the command is "numberOfFieldsSoFar"
        commandName = commandlineVector[numberOfFieldsSoFar];
    }

}

vector<string> Restaurant::textParser(const string &cfg) {
    ifstream configfile(cfg);
    string line;
    int j;	    // number of characters - yarin wants to do this with iterator
    vector<string> allData;

    while(getline(configfile, line)){
        j=0;
        while(j < line.length()){
            if(line.at(j) == '#') {
                break; //found a comment - from j index to the end of line
            }
            else if(line.at(j) == ' ') {
                j++;
            }
            else{
                allData = lineParser(line.substr(j),allData, ',');

                break;
            }
        }
    }
    return allData;

}
//
vector<string> Restaurant::lineParser(string line, vector<string> parse, char parseBy) {
    string word = "";
    while (line.length() > 0) {
        if (line.at(0) != parseBy) {
            word = word + line.at(0);
            line=line.substr(1);
        } else {
            line=line.substr(1);
            parse.push_back(word);
            word = "";
            continue;
        }
    }
    if (word.length() > 0)
        parse.push_back(word);

    return parse;
}

Customer* Restaurant::returnNewCustomerByType(std::string name, std::string type) {
    if(boost::iequals(type,"VEG"))
        return new VegetarianCustomer(name,this->getClientSoFar());
    else  if(boost::iequals(type,"SPC"))
        return new SpicyCustomer(name,this->getClientSoFar());
    else  if(boost::iequals(type,"ALC"))
        return new AlchoholicCustomer(name,this->getClientSoFar());
    else
        return new CheapCustomer(name,this->getClientSoFar());




}

int Restaurant::getNumOfTables() const {
    return tables.size();
}

Table* Restaurant::getTable(int ind) {
    //notation: need to check first mayb for index out of range
   return tables.at(ind);
}

void Restaurant::createDatabaseByConfig(const std::string &configFilePath) {
    startusik++;
    vector<string> allData = textParser(configFilePath);
    vector<int> tablesData;
    vector<string> menuData;
    for(int i=0; i < allData.size() ; i++) {//in first entry in alldata there is numoftables
        if (i < stoi(allData.at(0)) + 1)
            tablesData.push_back(stoi(allData.at(i)));//encoding it as a int vector ahead
        else
            menuData.push_back(allData.at(i));

    }
    createMenuByConfig(menuData);
    createTablesByConfig(tablesData);
    end++;
}

void Restaurant::createMenuByConfig(std::vector<std::string> dishesInformation) {
    int id = 0;
    for(int i = 0; i < dishesInformation.size(); i=i+3){
        menu.push_back(*(new Dish(id,dishesInformation.at(i),stoi(dishesInformation.at(i+2)),createEnumByString(dishesInformation.at(i+1)))));
        id++;
    }
}

void Restaurant::createTablesByConfig(std::vector<int> tablesInformation) {
    for(int i = 0; i < tablesInformation.at(0); i++){
        tables.push_back(new Table(tablesInformation.at(i+1)));

    }
}

std::vector<Table*> Restaurant::getTables() {
    return tables;
}

DishType Restaurant::createEnumByString(std::string type) {
    DishType toReturn;
    if (boost::iequals(type, "VEG"))
        toReturn = VEG;
    else if (boost::iequals(type, "SPC"))
        toReturn = SPC;
    else if (boost::iequals(type, "ALC"))
        toReturn = ALC;
    else
        toReturn = BVG;

    return toReturn;
}



std::vector<Dish>& Restaurant::getMenu() {
    return menu;
}