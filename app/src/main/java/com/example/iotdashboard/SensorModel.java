package com.example.iotdashboard;


import android.util.Log;

import java.util.*;

public class SensorModel extends Observable {

    // declaring a list of integer
    public List<Integer> List;

    // constructor to initialize the list
    public void SensorModel(){
        // reserving the space for list elements
        List = new ArrayList<Integer>(3);

        // adding elements into the list
        List.add(0);
        List.add(0);
        List.add(0);
        System.out.println(List);
        Log.e("dnasndlasnfasf",List.get(0).toString());
    }

    // defining getter and setter functions

    // function to return appropriate count
    // value at correct index
    public int getValueAtIndex(final int the_index) throws IndexOutOfBoundsException{
        return List.get(the_index);
    }

    // function to make changes in the activity button's
    // count value when user touch it
    public void setValueAtIndex(final int the_index, Integer value) throws IndexOutOfBoundsException{
        List.set(the_index,value);

        setChanged();
        notifyObservers();
    }

}
