package devices;

public class AC extends AbstractDevice {

    private int coolingLevel; // 1-5

    public AC(String name) {
        super(name);
        this.coolingLevel = 3;
    }

    @Override
    public void turnOn() {
        powerOn = true;
        notifyObservers(name + " turned ON (Cooling Level: " + coolingLevel + ")");
    }

    @Override
    public void turnOff() {
        powerOn = false;
        notifyObservers(name + " turned OFF");
    }

    public void setCoolingLevel(int level) {
        if (level < 1 || level > 5) {
            notifyObservers(name + " cooling level change FAILED (Invalid range)");
            return;
        }
        this.coolingLevel = level;
        notifyObservers(name + " cooling level set to " + level);
    }

    @Override
    public String getStatus() {
        return name + " [AC] - " + (powerOn ? "ON" : "OFF") + " | Cooling Level: " + coolingLevel;
    }
}
