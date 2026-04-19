package decorator;

import devices.Device;
import observer.Logger;

public class MotionDetectionDecorator extends DeviceDecorator {

    public MotionDetectionDecorator(Device device) {
        super(device);
        Logger.getInstance().update(device.getName() + " upgraded with MOTION DETECTION");
    }

    public void detectMotion() {
        Logger.getInstance().update(device.getName() + " detected motion!");

        if (!device.isOn()) {
            Logger.getInstance().update(device.getName() + " auto-turning ON due to motion.");
            device.turnOn();
        }
    }
}