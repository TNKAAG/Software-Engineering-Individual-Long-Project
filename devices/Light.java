package devices;

public class Light extends AbstractDevice {

    private int brightness; // 1-10

    public Light(String name) {
        super(name);
        this.brightness = 5;
    }

    @Override
    public void turnOn() {
        powerOn = true;
        notifyObservers(name + " turned ON (Brightness: " + brightness + ")");
    }

    @Override
    public void turnOff() {
        powerOn = false;
        notifyObservers(name + " turned OFF");
    }

    public void setBrightness(int brightness) {
        if (brightness < 1 || brightness > 10) {
            notifyObservers(name + " brightness change FAILED (Invalid range)");
            return;
        }
        this.brightness = brightness;
        notifyObservers(name + " brightness set to " + brightness);
    }

    @Override
    public String getStatus() {
        return name + " [Light] - " + (powerOn ? "ON" : "OFF") + " | Brightness: " + brightness;
    }
}