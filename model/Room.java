package model;

import java.util.ArrayList;
import java.util.List;

import devices.Device;

public class Room {

    private String name;
    private List<Device> devices;

    public Room(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void removeDevice(Device device) {
        devices.remove(device);
    }

    public void showRoomStatus() {
        System.out.println("Room: " + name);
        for (Device device : devices) {
            System.out.println("  - " + device.getStatus());
        }
    }
}