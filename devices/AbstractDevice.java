package devices;

import observer.Observer;
import observer.Subject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDevice implements Device, Subject {

    protected String name;
    protected boolean powerOn;
    private List<Observer> observers;

    public AbstractDevice(String name) {
        this.name = name;
        this.powerOn = false;
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public boolean isOn() {
        return powerOn;
    }

    @Override
    public String getName() {
        return name;
    }
}