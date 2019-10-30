package bgu.spl.mics.application;

import java.io.*;

/**
 * Created by amirshkedy on 20/12/2018.
 */
public class Printer {
    public static <T extends Serializable> void print(T serializedObj, String fileName){
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(serializedObj);
            oos.close();
            fos.close();
        } catch (IOException e){
        }

    }
}
