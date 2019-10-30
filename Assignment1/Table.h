#ifndef TABLE_H_
#define TABLE_H_

#include <vector>
#include "Customer.h"
#include "Dish.h"

typedef std::pair<int, Dish> OrderPair;

class Table{
public:
    Table(int t_capacity);                          //done
    int getCapacity() const;                        //done
    void addCustomer(Customer* customer);           //done
    void removeCustomer(int id);                    //done
    Customer* getCustomer(int id);                  //done
    std::vector<Customer*>& getCustomers();         //done
    std::vector<OrderPair>& getOrders();            //done
    void order(const std::vector<Dish> &menu);      //done
    void openTable();                               //done - need to be checked
    void closeTable();                              //done
    int getBill();                                  //done
    bool isOpen();

    // 09.11.2018 - Copy constructor and destructor
    //~Table();
    Table(const Table &otherTable);
    // will be needed afterwards
    Table& operator=(const Table& other);

    //done
private:
    int capacity;
    bool open;
    std::vector<Customer*> customersList;
    std::vector<OrderPair> orderList; //A list of pairs for each order in a table - (customer_id, Dish)

};


#endif