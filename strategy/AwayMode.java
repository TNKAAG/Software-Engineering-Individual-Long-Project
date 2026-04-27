package strategy;

import controller.SmartHomeController;
import devices.Device;
import devices.Thermostat;
import java.util.List;
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
        List<Device> livingDevices = controller.getRoomDevices("Living Room");
        for (Device d : livingDevices) 
            {
            Device base = d;

        if (d instanceof decorator.DeviceDecorator) 
        {
            base = ((decorator.DeviceDecorator) d).unwrap();
        }   

        if (base instanceof Thermostat) 
            {
                ((Thermostat) base).setTemperature(20);
                break;
            }
            }

        Logger.getInstance().update("AWAY MODE COMPLETE");
    }

    @Override
    public String getModeName() {
        return "Away Mode";
    }
}