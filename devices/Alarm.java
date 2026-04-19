package devices;

public class Alarm extends AbstractDevice {

    public enum AlarmState {
        ARMED, DISARMED, TRIGGERED
    }

    private AlarmState state;

    public Alarm(String name) {
        super(name);
        this.state = AlarmState.DISARMED;
    }

    @Override
    public void turnOn() {
        state = AlarmState.ARMED;
        powerOn = true;
        notifyObservers(name + " ARMED");
    }

    @Override
    public void turnOff() {
        state = AlarmState.DISARMED;
        powerOn = false;
        notifyObservers(name + " DISARMED");
    }

    public void trigger() {
        state = AlarmState.TRIGGERED;
        notifyObservers(name + " TRIGGERED!!!");
    }

    @Override
    public String getStatus() {
        return name + " [Alarm] - State: " + state;
    }
}