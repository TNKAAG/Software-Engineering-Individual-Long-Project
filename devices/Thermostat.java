package devices;

public class Thermostat extends AbstractDevice {

    private int temperature; // 16-30

    public Thermostat(String name) {
        super(name);
        this.temperature = 22;
    }

    @Override
    public void turnOn() {
        powerOn = true;
        notifyObservers(name + " turned ON (Temperature: " + temperature + "°C)");
    }

    @Override
    public void turnOff() {
        powerOn = false;
        notifyObservers(name + " turned OFF");
    }

    public void setTemperature(int temp) {
        if (temp < 16 || temp > 30) {
            notifyObservers(name + " temperature change FAILED (Invalid range)");
            return;
        }
        this.temperature = temp;
        notifyObservers(name + " temperature set to " + temp + "°C");
    }

    @Override
    public String getStatus() {
        return name + " [Thermostat] - " + (powerOn ? "ON" : "OFF") + " | Temp: " + temperature + "°C";
    }
}