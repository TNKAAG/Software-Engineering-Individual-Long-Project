package strategy;

import controller.SmartHomeController;

public interface AutomationMode {
    void execute(SmartHomeController controller);
    String getModeName();
}