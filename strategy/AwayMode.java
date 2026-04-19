package strategy;

import controller.SmartHomeController;
import devices.Device;
import devices.Thermostat;
import observer.Logger;

public class AwayMode implements AutomationMode {

    @Override
    public void execute(SmartHomeController controller) {

        Logger.getInstance().update("AWAY MODE ACTIVATED");

        // Turn off all room devices
        for (Device device : controller.getAllRoomDevices()) {
            device.turnOff();
        }

        // Lock door
        controller.getDoorLock().turnOn();

        // Arm alarm
        controller.getAlarm().turnOn();

        // Thermostat low power (set to 20°C if it exists)
        Device thermo = controller.getRoomDevice("Living Room");
        if (thermo instanceof Thermostat) {
            ((Thermostat) thermo).setTemperature(20);
        }

        Logger.getInstance().update("AWAY MODE COMPLETE");
    }

    @Override
    public String getModeName() {
        return "Away Mode";
    }
}