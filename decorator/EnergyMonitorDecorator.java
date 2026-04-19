package decorator;

import devices.Device;
import observer.Logger;

public class EnergyMonitorDecorator extends DeviceDecorator {

    private double energyUsed; // simulated kWh

    public EnergyMonitorDecorator(Device device) {
        super(device);
        this.energyUsed = 0;
        Logger.getInstance().update(device.getName() + " upgraded with ENERGY MONITORING");
    }

    @Override
    public void turnOn() {
        super.turnOn();
        Logger.getInstance().update(device.getName() + " energy monitoring started.");
    }

    @Override
    public void turnOff() {
        super.turnOff();
        energyUsed += Math.random() * 2; // fake consumption
        Logger.getInstance().update(device.getName() + " energy used updated: " + String.format("%.2f", energyUsed) + " kWh");
    }

    public double getEnergyUsed() {
        return energyUsed;
    }
}
