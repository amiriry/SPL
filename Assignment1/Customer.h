#ifndef CUSTOMER_H_
#define CUSTOMER_H_

#include <vector>
#include <string>
#include "Dish.h"

class Customer{
public:
    Customer(std::string c_name, int c_id);	// done
    virtual std::vector<int> order(const std::vector<Dish> &menu)=0;	// done
    virtual std::string toString() const = 0;							// done
    std::string getName() const;										// done
    int getId() const;													// done


    std::vector<int> getOrdersSoFar();

    // rule of 5
    Customer(const Customer& other);
private:
    const std::string name;
    const int id;

protected:
	//kol ze shelanu

	// 09.11.2018 - not sure what that means but only after I put '*' it worked in customer
	std::vector<int> ordersSoFar;
	std::vector<int> sortIdByPrice(const std::vector<Dish> &specificMenu); // should be done with reference - need to be checked
	int findFirstOfType(std::vector<int> dishesIdByPrice , const std::vector<Dish> &menu, DishType type, bool cheapest);
	bool byPrice(Dish a, Dish b);
	std::string stringByEnum(DishType a);

};


// didn't wrote a destructor for that
class VegetarianCustomer : public Customer {
public:
	//vegeterian dish with smallest id, after the most expensive beverage
	VegetarianCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Dish> &menu);
    std::string toString() const;

    // rule of 5
    ~VegetarianCustomer();
    VegetarianCustomer(const VegetarianCustomer &other);
//    VegetarianCustomer& operator=(VegetarianCustomer other): Customer(other);
private:
	DishType myFavType = VEG;
};


// for now didn't wrote rule of 5
class CheapCustomer : public Customer {
public:
	//cheapest dish, next strategy is no order
	CheapCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Dish> &menu);
    std::string toString() const;

    // rule of 5
private:
};


class SpicyCustomer : public Customer {
public:
	//most expensive spicy dish, next strategy is most expensive beverege
	SpicyCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Dish> &menu);
    std::string toString() const;
    ~SpicyCustomer();
private:
	DishType myFavType = SPC;
};


class AlchoholicCustomer : public Customer {
public:
	//cheapest alcoholic beverege , which i didnt order before.
	AlchoholicCustomer(std::string name, int id);
    std::vector<int> order(const std::vector<Dish> &menu);
    std::string toString() const;
//    ~AlchoholicCustomer();
private:
	DishType myFavType=ALC;
};


#endif