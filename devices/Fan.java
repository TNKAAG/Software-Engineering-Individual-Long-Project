package devices;

public class Fan extends AbstractDevice {

    private int speed; // 1-5

    public Fan(String name) {
        super(name);
        this.speed = 3;
    }

    @Override
    public void turnOn() {
        powerOn = true;
        notifyObservers(name + " turned ON (Speed: " + speed + ")");
    }

    @Override
    public void turnOff() {
        powerOn = false;
        notifyObservers(name + " turned OFF");
    }

    public void setSpeed(int speed) {
        if (speed < 1 || speed > 5) {
            notifyObservers(name + " speed change FAILED (Invalid range)");
            return;
        }
        this.speed = speed;
        notifyObservers(name + " speed set to " + speed);
    }

    @Override
    public String getStatus() {
        return name + " [Fan] - " + (powerOn ? "ON" : "OFF") + " | Speed: " + speed;
    }
}