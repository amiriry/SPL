#ifndef DISH_H_
#define DISH_H_

#include <string>

enum DishType{
    VEG, SPC, BVG, ALC
};

class Dish{
public:
//    Dish(int d_id, std::string d_name, int d_price, DishType d_type): id(d_id), name(d_name), price(d_price), type(d_type);
    Dish(int d_id, std::string d_name, int d_price, DishType d_type);                                                         //took the initalization from here and put it the cpp file
    int getId() const;                                                                                                        //done
    std::string getName() const;                                                                                              //done
    int getPrice() const;                                                                                                     //done
    DishType getType() const;                                                                                                 //done

    // 09.11.2018 - created a copy constructor and destructor
//    Dish(const Dish &otherDish);
//    Dish& operator=(Dish& other);
//    Dish(const Dish &other);

private:
	const int id;
    const std::string name;
    const int price;
    const DishType type;

};


#endif