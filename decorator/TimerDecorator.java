package decorator;

import devices.Device;
import observer.Logger;

public class TimerDecorator extends DeviceDecorator {

    public TimerDecorator(Device device) {
        super(device);
        Logger.getInstance().update(device.getName() + " upgraded with TIMER scheduling");
    }

    public void scheduleTurnOff(int seconds) {
        Logger.getInstance().update(device.getName() + " scheduled to turn OFF in " + seconds + " seconds.");

        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
                device.turnOff();
                Logger.getInstance().update(device.getName() + " turned OFF automatically by timer.");
            } catch (InterruptedException e) {
                Logger.getInstance().update(device.getName() + " timer interrupted.");
            }
        }).start();
    }
}