package strategy;

import controller.SmartHomeController;
import devices.Alarm;
import devices.Device;
import devices.Light;
import observer.Logger;

public class EmergencyMode implements AutomationMode {

    @Override
    public void execute(SmartHomeController controller) {

        Logger.getInstance().update("EMERGENCY MODE ACTIVATED!!!");

        // Turn ON all lights only
        for (Device device : controller.getAllRoomDevices()) {
            if (device instanceof Light) {
                device.turnOn();
            } else {
                device.turnOff(); // turn off AC/Fan/Thermostat etc.
            }
        }

        // Lock door
        controller.getDoorLock().turnOn();

        // Trigger alarm
        Alarm alarm = controller.getAlarm();
        alarm.trigger();

        Logger.getInstance().update("EMERGENCY MODE COMPLETE");
    }

    @Override
    public String getModeName() {
        return "Emergency Mode";
    }
}