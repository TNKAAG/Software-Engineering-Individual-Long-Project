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
            Device base = device;

        if (device instanceof decorator.DeviceDecorator) {
            base = ((decorator.DeviceDecorator) device).unwrap();
        }

        if (base instanceof Light) {
            device.turnOn();   // turn on wrapper (good)
        } 
        else 
            {
        device.turnOff();
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