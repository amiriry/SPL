#ifndef RESTAURANT_H_
#define RESTAURANT_H_

#include <vector>
#include <iostream>
#include <string>
#include "Dish.h"
#include "Table.h"
#include "Action.h"


class Restaurant{		
public:
	Restaurant();
    Restaurant(const std::string &configFilePath);
    void start();
    int getNumOfTables() const;
    Table* getTable(int ind);
	const std::vector<BaseAction*>& getActionsLog() const; // Return a reference to the history of actions
    std::vector<Dish>& getMenu();
    int startusik = 0;
    int end = 0;

private:
    bool open;
    std::vector<Table*> tables;
    std::vector<Dish> menu;
    std::vector<BaseAction*> actionsLog;

    // shelanu
	std::vector<std::string> textParser(const std::string &cfg);
	std::vector<std::string> lineParser(std::string line, std::vector<std::string>, char parseBy);
	Customer* returnNewCustomerByType(std::string name, std::string type);
	int clientSoFar = 0;
	int getClientSoFar();
	void incrementClientSoFar();
	std::vector<Table*> getTables();
	// creating conifguration of restaurant
	void createDatabaseByConfig(const std::string &configFilePath);
	void createTablesByConfig(std::vector<int> tablesInformation);
	void createMenuByConfig(std::vector<std::string> dishesInformation);
	DishType createEnumByString(std::string type);
    std::string stringByEnum(DishType a);


};

#endif