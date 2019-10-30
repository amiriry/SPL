package bgu.spl.net.impl.BidiMessaging;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by amirshkedy on 31/12/2018.
 */
public class Network {
    // 1 - connectionId 2 - username
    private HashMap<Integer, String> loggedInUsers = new HashMap<>();
    // 1 - username, 2 - password
    private HashMap<String, String> registeredUsers = new HashMap<>();
    // 1- connectionId 2 - list of user he follows (saved by names)
    private HashMap<Integer, ArrayList<String>> connIdToListFollowing = new HashMap<>();
    // 1 - username, 2 - queue of posts
    private HashMap<String, Queue<String>> userToPosts = new HashMap<>();

    public void registerUser(String userame, String password){
        registeredUsers.put(userame, password);
    }

    public boolean isResgitered(String username){
        boolean returnValue = false;
        if(registeredUsers.containsKey(username))
            returnValue = true;
        return returnValue;
    }

    public void logInUser(int connId, String username){

        loggedInUsers.put(connId, username);
        connIdToListFollowing.put(connId, new ArrayList<>());
        //check
        System.out.println("I got login function and put this - " + Arrays.toString(connIdToListFollowing.get(connId).toArray()));
        userToPosts.put(registeredUsers.get(connId), new ConcurrentLinkedQueue<>());
//        System.out.println("I got login function and put this - " + Arrays.toString(userToPosts.get(registeredUsers)));
    }
    // checked by connection Id because the connection id is created when the handler is created
    // which is before the registration itself
    // you use start() function with connectionId and its not yet registered
    public boolean isLoggedIn(int connectionId){
        boolean retValue = false;
        if(loggedInUsers.containsKey(connectionId))
            retValue = true;
        return retValue;
    }


    public boolean isPasswordCorrect(String name, String password){
        boolean retValue = false;
        if(registeredUsers.get(name).equals(password))
            retValue = true;
        return retValue;
    }


    public void logoutUser(int connectionId){
        loggedInUsers.remove(connectionId);
    }

    public int numberOfUsers(){
        return loggedInUsers.size();
    }


    public String getNameByConnId(int connectionId){
        return loggedInUsers.get(connectionId);
    }

    public int getConnIdByName(String username){
        int connId = 0; // not sure that it's ok
        for(Map.Entry<Integer, String> value: loggedInUsers.entrySet()){
            if(value.getValue().equals(username))
                connId = value.getKey();
        }
        return connId;
    }

    public boolean doesFollow(int connectionId, String username){
        boolean retValue = false;
        if(connIdToListFollowing.get(connectionId).contains(username))
            retValue = true;
        return retValue;
    }


    public ArrayList<String> getFollowing(int connId){
        //check
        System.out.println(" this is what I am trying " + connIdToListFollowing.get(connId));
        return connIdToListFollowing.get(connId);
    }

    public short getNumberOfFollowing(int connectionId){
        return (short)connIdToListFollowing.get(connectionId).size();
    }

    public Queue<String> getPostsByConnId(int connId){
        String username = loggedInUsers.get(connId);
        //check
        System.out.println("I suppose to get here " + username);
        System.out.println(" and he's posts: " + userToPosts.get(username));
        return userToPosts.get(username);
    }

    public void addMessage(String username, String post){
        userToPosts.get(username).add(post);
    }

    public void addFollowing(int connId, String toFollow){
        connIdToListFollowing.get(connId).add(toFollow);
    }

    public void unfollow(int connId, String toUnFollow){
        connIdToListFollowing.get(connId).remove(toUnFollow);
    }



}


