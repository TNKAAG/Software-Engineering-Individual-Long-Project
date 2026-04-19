package factory;

import devices.*;

public class DeviceFactory {

    public static Device createDevice(String type, String name) {

        switch (type.toLowerCase()) {
            case "light":
                return new Light(name);

            case "fan":
                return new Fan(name);

            case "ac":
                return new AC(name);

            case "thermostat":
                return new Thermostat(name);

            case "doorlock":
                return new DoorLock(name);

            case "alarm":
                return new Alarm(name);

            default:
                throw new IllegalArgumentException("Unknown device type: " + type);
        }
    }
}