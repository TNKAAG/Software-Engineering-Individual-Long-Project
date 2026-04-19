package controller;

import devices.*;
import java.util.*;
import model.Room;
import observer.Logger;
import observer.Observer;
import strategy.AutomationMode;

public class SmartHomeController {

    private static SmartHomeController instance;

    private Map<String, Room> rooms;
    private DoorLock doorLock;
    private Alarm alarm;

    private AutomationMode currentMode;

    private SmartHomeController() {
        rooms = new HashMap<>();
    }

    public static SmartHomeController getInstance() {
        if (instance == null) {
            instance = new SmartHomeController();
        }
        return instance;
    }

    public void addRoom(Room room) {
        rooms.put(room.getName(), room);
    }

    public Room getRoom(String roomName) {
        return rooms.get(roomName);
    }

    public Device getRoomDevice(String roomName) {
        Room room = rooms.get(roomName);
        if (room == null) return null;
        return room.getDevice();
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public List<Device> getAllRoomDevices() {
        List<Device> devices = new ArrayList<>();
        for (Room room : rooms.values()) {
            devices.add(room.getDevice());
        }
        return devices;
    }

    public void setDoorLock(DoorLock doorLock) {
        this.doorLock = doorLock;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public DoorLock getDoorLock() {
        return doorLock;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void attachLoggerToAllDevices(Observer logger) {
        for (Device device : getAllRoomDevices()) {
            if (device instanceof AbstractDevice) {
                ((AbstractDevice) device).addObserver(logger);
            }
        }

        if (doorLock != null) doorLock.addObserver(logger);
        if (alarm != null) alarm.addObserver(logger);
    }

    public void setMode(AutomationMode mode) {
        this.currentMode = mode;
        Logger.getInstance().update("Automation mode set to: " + mode.getModeName());
    }

    public void executeMode() {
        if (currentMode == null) {
            Logger.getInstance().update("No automation mode selected.");
            return;
        }
        currentMode.execute(this);
    }

    public void updateRoomDevice(String roomName, Device newDevice) {
    Room room = rooms.get(roomName);
    if (room != null) {
        room.setDevice(newDevice);
    }
}
}