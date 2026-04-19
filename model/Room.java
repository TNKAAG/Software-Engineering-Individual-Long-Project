package model;

import devices.Device;

public class Room {

    private String name;
    private Device device;

    public Room(String name, Device device) {
        this.name = name;
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void showRoomStatus() {
        System.out.println("Room: " + name + " -> " + device.getStatus());
    }
}