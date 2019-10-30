package bgu.spl.mics.application;

import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {
        String fileData = null;

        // creating a gson object to work with json
        Gson gson = new GsonBuilder().create();

        try {
            fileData = new String(Files.readAllBytes(Paths.get(args[0])));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GsonClass gsonObj=gson.fromJson(fileData,GsonClass.class);

        //--------------loading all data from json-------------------------------------------
        LinkedList<BookInventoryInfo> booksToStore = new LinkedList<>();

        // An index variable for future uses
        int i;

        // Going over inventory from json - creating the corresponding books
        for(GsonClass.GsonBook book: gsonObj.initialInventory){
            booksToStore.add(new BookInventoryInfo(book.bookTitle,book.price,book.amount));
        }
        // loading the books - index 0 creates new array in size that is
        // corresponding to bookToStore
        Inventory.getInstance().load(booksToStore.toArray(new BookInventoryInfo[0]));

        // Going over Delivery vehicles in json creating the array of vehicles
        LinkedList<DeliveryVehicle> deliveryVehiclesToStore = new LinkedList<>();
        for(GsonClass.ResourcesArr.GsonVehicle vehicles: gsonObj.initialResources[0].vehicles){
            deliveryVehiclesToStore.add(new DeliveryVehicle(vehicles.license,vehicles.speed));
        }
        ResourcesHolder.getInstance().load(deliveryVehiclesToStore.toArray(new DeliveryVehicle[0]));


        // Reference to Services in json
        GsonClass.ServicesArr myServices = gsonObj.services;

        int threadCounter = myServices.selling + myServices.inventoryService + myServices.logistics
                              + myServices.resourcesService + myServices.customers.length + 1; // 1 is time service

        int inventoryServicesNum = gsonObj.services.inventoryService;
        int resourceServicesNum= gsonObj.services.resourcesService;
        int sellingServicesNum = gsonObj.services.selling;
        int logisticsServicesNum = gsonObj.services.logistics;
        int apiServicesNum = gsonObj.services.customers.length;
        int timeSpeed = gsonObj.services.time.speed;
        int timeDuration = gsonObj.services.time.duration;

        // start countdown - for time service to start after all others
        CountDownLatch startThreadCount = new CountDownLatch(threadCounter-1);
        // end countdown - for all service to end gracefully
        CountDownLatch endThreadCount = new CountDownLatch(1);


        LinkedList<Customer> storeInitialCustomers = new LinkedList<>();
        LinkedList<APIService> apiServices = new LinkedList<>();
        i = 1;

        // Customer hash map to print
        HashMap<Integer, Customer> customersToPrint = new HashMap<>();

        // Go over customers in the json file and create customers object, BookOrderEvents for
        // and API services in correspondence
        for(GsonClass.InitialCustomers customer: gsonObj.services.customers){

            // Create instance of customer to add
            Customer customerToAdd = new Customer(customer.id ,customer.name, customer.address, customer.distance,
                    customer.creditCard.number, customer.creditCard.amount);


            storeInitialCustomers.add(customerToAdd);
            customersToPrint.put(customer.id, customerToAdd);

            // Creating list of BookOrderEvents for the current customer
            LinkedList<BookOrderEvent> customerOrderBooks = new LinkedList<>();
            // Orders in json file - pushed into list that will be sent to the API
            for(GsonClass.OrderSchedule order: customer.orderSchedule){
                customerOrderBooks.add(new BookOrderEvent(getBookByName(booksToStore, order.bookTitle),customerToAdd ,
                                                    order.tick));
            }

            // Sort the book by tick before sending it to API service
            customerOrderBooks.sort(Comparator.comparingInt(BookOrderEvent::getTick));
            ConcurrentLinkedQueue toSendQueue = new ConcurrentLinkedQueue(customerOrderBooks);


            int someTick = 0; // that is wrong!
            apiServices.add(new APIService("customer " + i, startThreadCount, endThreadCount,
                                             someTick, toSendQueue));
            i++;
        }

        // ----------------- finished loading -----------------------------------


        //---------------- Creating instances of services -----------------------

        // Inventory service
        LinkedList<InventoryService> inventoryServices = new LinkedList<>();
        for(i = 0; i < inventoryServicesNum; i++){
            inventoryServices.add(new InventoryService(startThreadCount, endThreadCount));
        }

        // ResourcesService
        LinkedList<ResourceService> resourceServices = new LinkedList<>();
        for(i = 0; i < resourceServicesNum; i++){
            resourceServices.add(new ResourceService(i, startThreadCount, endThreadCount));
        }

        // LogisticsService
        LinkedList<LogisticsService> logisticsServices = new LinkedList<>();
        for(i = 0; i < logisticsServicesNum; i++){
            logisticsServices.add(new LogisticsService(i, startThreadCount, endThreadCount));
        }

        // SellingService
        LinkedList<SellingService> sellingServices = new LinkedList<>();
        for(i = 0; i < sellingServicesNum; i++){
            sellingServices.add(new SellingService("selling " + i , startThreadCount, endThreadCount));
        }

        // Timer service
        TimeService timerService = new TimeService(myServices.time.speed, myServices.time.duration,
                startThreadCount, endThreadCount);


        // Creating the runner for all the services - As threads
        ExecutorService runner = Executors.newFixedThreadPool(threadCounter);


        // Starting all services
        //-----------------------

        // inventory service
        for(InventoryService myinvserv: inventoryServices)
            runner.execute(myinvserv);


        // Resource Service
        for(ResourceService myresserv: resourceServices)
            runner.execute(myresserv);

        // Logisitics Service
        for(LogisticsService mylogserv: logisticsServices)
            runner.execute(mylogserv);

        // Selling Service
        for(SellingService myselserv: sellingServices)
            runner.execute(myselserv);

        // API Service
        for(APIService myapiserv: apiServices){
            runner.execute(myapiserv);
        }

        // Timer service

        try {
            startThreadCount.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runner.execute(timerService);


        //waiting for all microservices to terminate themselves and to count down the latch.
        try {
            endThreadCount.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // shut down all threads
        runner.shutdown();

        // Print information needed to files
        Printer.print(customersToPrint, args[1]);
        Inventory.getInstance().printInventoryToFile(args[2]);
        MoneyRegister.getInstance().printOrderReceipts(args[3]);
        Printer.print(MoneyRegister.getInstance(), args[4]);

    }

    // method for finding a book in the inventory
    private static BookInventoryInfo getBookByName(LinkedList<BookInventoryInfo> booklist, String name){
        BookInventoryInfo retBook = null;
        for(BookInventoryInfo book: booklist){
            if(book.getBookTitle().equals(name))
                retBook = book;
        }
        return retBook;
    }
}
