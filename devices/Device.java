package devices;

public interface Device {
    void turnOn();
    void turnOff();
    boolean isOn();

    String getName();
    String getStatus();
}