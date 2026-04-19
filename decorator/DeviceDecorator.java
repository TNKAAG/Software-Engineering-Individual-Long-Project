package decorator;

import devices.Device;

public abstract class DeviceDecorator implements Device {

    protected Device device;

    public DeviceDecorator(Device device) {
        this.device = device;
    }

    public Device getWrappedDevice() {
        return device;
    }

    public Device unwrap() {
        Device current = this.device;

        while (current instanceof DeviceDecorator) {
            current = ((DeviceDecorator) current).getWrappedDevice();
        }
        return current;
    }

    @Override
    public void turnOn() {
        device.turnOn();
    }

    @Override
    public void turnOff() {
        device.turnOff();
    }

    @Override
    public boolean isOn() {
        return device.isOn();
    }

    @Override
    public String getName() {
        return device.getName();
    }

    @Override
    public String getStatus() {
        return device.getStatus();
    }
}