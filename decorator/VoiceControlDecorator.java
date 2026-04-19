package decorator;

import devices.Device;
import observer.Logger;

public class VoiceControlDecorator extends DeviceDecorator {

    public VoiceControlDecorator(Device device) {
        super(device);
        Logger.getInstance().update(device.getName() + " upgraded with VOICE CONTROL simulation");
    }

    public void voiceCommand(String command) {
        Logger.getInstance().update("Voice command received for " + device.getName() + ": \"" + command + "\"");

        if (command.equalsIgnoreCase("turn on")) {
            device.turnOn();
        } else if (command.equalsIgnoreCase("turn off")) {
            device.turnOff();
        } else {
            Logger.getInstance().update("Voice command not recognized: " + command);
        }
    }
}
