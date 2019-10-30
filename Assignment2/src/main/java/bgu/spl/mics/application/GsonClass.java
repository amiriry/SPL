package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class GsonClass {
    public GsonBook[] initialInventory;
    public class GsonBook{
        public String bookTitle;
        public int amount;
        public int price;
    }


    public ResourcesArr[] initialResources;
    public class ResourcesArr {
        public GsonVehicle[] vehicles;
        public class GsonVehicle {
            public int license;
            public int speed;
        }
    }

    public ServicesArr services;
    public class ServicesArr {
        public int selling;
        public int inventoryService;
        public int logistics;
        public int resourcesService;
        public Time time;
        public InitialCustomers[] customers;
    }

    public class Time{
        public int speed;
        public int duration;
    }

    public class InitialCustomers{
        public int id;
        public String name;
        public String address;
        public int distance;
        public CreditCard creditCard;
        public OrderSchedule[] orderSchedule; // The importnat stuff here was that it was not with parenthasis
    }

    public class OrderSchedule {
        public String bookTitle;
        public int tick;
    }
    public class CreditCard{
        public long number;
        public int amount;
    }





}
