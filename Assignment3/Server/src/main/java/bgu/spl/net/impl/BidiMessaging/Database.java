package bgu.spl.net.impl.BidiMessaging;

import bgu.spl.net.impl.BidiMessaging.Messages.POSTmessage;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Database {

    private UserList myUsers = new UserList();

    public boolean isRegistered(String name){
        boolean isRegistered = false;
        if(myUsers.getByName(name) != null)
            isRegistered = true;
        return isRegistered;
    }

    public boolean registerUser(String username, String password){
        boolean wasRegistered = false;
        if(myUsers.getByName(username) == null){
            myUsers.addIfNotExist(username, password);
            wasRegistered = true;
        }

        return  wasRegistered;
    }
    //check - change it!!!!!! - to remember
    public boolean isLoggedIn(int connId){
        boolean returnVal = false;
        if(myUsers.getById(connId) != null){
           returnVal = true;
        }
        return returnVal;
    }

    public boolean isPasswordCorrect(String name, String password){
        return myUsers.getByName(name).isPasswordCorrect(password);
    }

    public void logInUser(int connId, String name) {                 // I think that this should return true/false
        //check
        if (myUsers.getByName(name).getConnId() == -1)
            myUsers.getByName(name).logIn(connId);

    }

    public void logoutUser(int connId){
        myUsers.getById(connId).logout();
    }

    public boolean doesFollow(int connId, String name){
        if(myUsers.getById(connId).getFollowings().getByName(name) == null){
            return false;
        }

        return true;
    }

    public UserList getFollowings(int connId){
        return myUsers.getById(connId).getFollowings();
    }

    public UserList getFollowers(int connId){
        return myUsers.getById(connId).getFollowers();
    }

    public short getNumFollowings(int connId){
        return myUsers.getById(connId).followersSize();
    }

    public short getNumFollowers(int connId){
        return myUsers.getById(connId).followersSize();
    }


    public LinkedList<Message> getPostsByConnId(int connId){
        LinkedList<Message> posts = new LinkedList<>();
        for(Message current: myUsers.getById(connId).getPosts()){
            if(current instanceof POSTmessage){
                posts.add(current);
            }
        }
        return posts;
    }

    public void addMessage(String username, Message message){

        myUsers.getByName(username).addPosts(message);
    }

    public void addFollowing(int connId, String toFollow){
        myUsers.getById(connId).addFollowing(myUsers.getByName(toFollow));
    }

    public void unfollow(int connId, String toUnFollow){
        myUsers.getById(connId).unfollow(myUsers.getByName(toUnFollow));
    }

    public User getUserByConnId(int connId){
        return myUsers.getById(connId);
    }

    public User getConnIdByName(String name){
        return myUsers.getByName(name);
    }
}
