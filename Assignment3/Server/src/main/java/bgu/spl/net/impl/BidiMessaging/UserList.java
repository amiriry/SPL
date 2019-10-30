package bgu.spl.net.impl.BidiMessaging;

import java.util.HashSet;
import java.util.Set;

//check - there is something with locks here

public class UserList {
    private Set<User> userlist = new HashSet<>();

    User getByName(String name){ //check - what happens when name is null? need to check before I get here
        User userToReturn = null;
        for(User currentUser: userlist){
            if(currentUser.getName().equals(name))
                userToReturn = currentUser;
        }

        return userToReturn;
    }

    // check - put break
    User getById(int connId){
        User userToReturn = null;
        for(User currentUser: userlist){
            if(currentUser.getConnId() == connId) {
                userToReturn = currentUser;
            }
        }

        return userToReturn;
    }

    // check - what happens if passwor is ""?
    boolean addIfNotExist(String username, String password){
        boolean wasAdded = false;
        if(!userlist.contains(getByName(username))){
            User toaddUser = new User(username, password);
            userlist.add(toaddUser);
        }

        return wasAdded;
    }

    boolean addUserToFollowingList(User toAdd){
        userlist.add(toAdd);
        return true;
    }

    public short size(){
        return (short)userlist.size();
    }

    boolean removeUserFromFollowing(User toRemove){
        userlist.remove(toRemove);
        return true;
    }

    public Set<User> getUserlist() {
        return userlist;
    }
}
