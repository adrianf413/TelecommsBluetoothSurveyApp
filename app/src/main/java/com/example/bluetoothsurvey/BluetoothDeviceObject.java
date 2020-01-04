package com.example.bluetoothsurvey;

public class BluetoothDeviceObject {

    String name;
    String address;

    public BluetoothDeviceObject(){}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof BluetoothDeviceObject)
        {
            isEqual = (this.name == ((BluetoothDeviceObject) object).name);
        }

        return isEqual;
    }

}
