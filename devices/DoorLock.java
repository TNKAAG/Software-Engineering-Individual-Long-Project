package devices;

public class DoorLock extends AbstractDevice {

    public DoorLock(String name) {
        super(name);
    }

    @Override
    public void turnOn() {
        powerOn = true;
        notifyObservers(name + " LOCKED");
    }

    @Override
    public void turnOff() {
        powerOn = false;
        notifyObservers(name + " UNLOCKED");
    }

    @Override
    public String getStatus() {
        return name + " [DoorLock] - " + (powerOn ? "LOCKED" : "UNLOCKED");
    }
}