import controller.SmartHomeController;
import decorator.DeviceDecorator;
import decorator.EnergyMonitorDecorator;
import decorator.MotionDetectionDecorator;
import decorator.TimerDecorator;
import decorator.VoiceControlDecorator;
import devices.AC;
import devices.Device;
import devices.Fan;
import devices.Light;
import devices.Thermostat;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import model.Room;
import observer.Observer;
import strategy.AwayMode;
import strategy.EmergencyMode;

public class SmartHomeGUI extends JFrame {

    private SmartHomeController controller;
    private Device selectedDevice;
    private Room selectedRoom;

    private JTextArea logArea;
    private JLabel statusLabel;
    private JLabel selectedLabel;

    private JButton onButton;
    private JButton offButton;
    private JButton setLevelButton;
    private JButton motionButton;
    private JButton timerButton;
    private JButton voiceOnButton;
    private JButton voiceOffButton;
    private JButton energyButton;

    private JList<String> deviceList;
    private DefaultListModel<String> deviceModel;

    public SmartHomeGUI(SmartHomeController controller) {
        this.controller = controller;
        this.selectedDevice = null;
        this.selectedRoom = null;

        deviceModel = new DefaultListModel<>();
        deviceList = new JList<>(deviceModel);

        setTitle("Smart Home Controller");
        setSize(980, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JPanel statusPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        selectedLabel = new JLabel("Selected Device: None");
        statusLabel = new JLabel("Status: Waiting for selection...");
        statusPanel.add(selectedLabel);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);

        JPanel roomPanel = new JPanel(new BorderLayout());
        roomPanel.setBorder(BorderFactory.createTitledBorder("Rooms & Devices"));

        JPanel roomButtonsPanel = new JPanel(new GridLayout(6, 1, 8, 8));
        JButton bedroomBtn = new JButton("Bedroom");
        JButton bathroomBtn = new JButton("Bathroom");
        JButton kitchenBtn = new JButton("Kitchen");
        JButton livingBtn = new JButton("Living Room");
        JButton doorBtn = new JButton("Door Lock");
        JButton alarmBtn = new JButton("Alarm");
        roomButtonsPanel.add(bedroomBtn);
        roomButtonsPanel.add(bathroomBtn);
        roomButtonsPanel.add(kitchenBtn);
        roomButtonsPanel.add(livingBtn);
        roomButtonsPanel.add(doorBtn);
        roomButtonsPanel.add(alarmBtn);
        roomPanel.add(roomButtonsPanel, BorderLayout.NORTH);

        JScrollPane deviceScroll = new JScrollPane(deviceList);
        deviceScroll.setBorder(BorderFactory.createTitledBorder("Devices in Selected Room"));
        roomPanel.add(deviceScroll, BorderLayout.CENTER);

        add(roomPanel, BorderLayout.WEST);

        JPanel controlPanel = new JPanel(new GridLayout(10, 1, 8, 8));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Device Controls"));
        onButton = new JButton("Turn ON");
        offButton = new JButton("Turn OFF");
        setLevelButton = new JButton("Set Level");
        motionButton = new JButton("Simulate Motion");
        timerButton = new JButton("Schedule Timer OFF");
        voiceOnButton = new JButton("Voice: Turn ON");
        voiceOffButton = new JButton("Voice: Turn OFF");
        energyButton = new JButton("View Energy Usage");
        controlPanel.add(onButton);
        controlPanel.add(offButton);
        controlPanel.add(setLevelButton);
        controlPanel.add(motionButton);
        controlPanel.add(timerButton);
        controlPanel.add(voiceOnButton);
        controlPanel.add(voiceOffButton);
        controlPanel.add(energyButton);
        add(controlPanel, BorderLayout.CENTER);

        JPanel automationPanel = new JPanel(new GridLayout(6, 1, 8, 8));
        automationPanel.setBorder(BorderFactory.createTitledBorder("Automation & Sensors"));
        JButton awayBtn = new JButton("Activate Away Mode");
        JButton emergencyBtn = new JButton("Activate Emergency Mode");
        JButton intrusionBtn = new JButton("Simulate Intrusion");
        JButton tempDropBtn = new JButton("Simulate Temp Drop");
        automationPanel.add(awayBtn);
        automationPanel.add(emergencyBtn);
        automationPanel.add(intrusionBtn);
        automationPanel.add(tempDropBtn);
        add(automationPanel, BorderLayout.EAST);

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Live Activity Log"));
        add(logScroll, BorderLayout.SOUTH);

        bedroomBtn.addActionListener(e -> selectRoom(controller.getRoom("Bedroom")));
        bathroomBtn.addActionListener(e -> selectRoom(controller.getRoom("Bathroom")));
        kitchenBtn.addActionListener(e -> selectRoom(controller.getRoom("Kitchen")));
        livingBtn.addActionListener(e -> selectRoom(controller.getRoom("Living Room")));
        doorBtn.addActionListener(e -> selectDevice(controller.getDoorLock()));
        alarmBtn.addActionListener(e -> selectDevice(controller.getAlarm()));

        onButton.addActionListener(e -> {
            if (selectedDevice != null) {
                selectedDevice.turnOn();
                updateStatus();
            }
        });

        offButton.addActionListener(e -> {
            if (selectedDevice != null) {
                selectedDevice.turnOff();
                updateStatus();
            }
        });

        setLevelButton.addActionListener(e -> {
            if (selectedDevice == null) return;
            Device base = selectedDevice;
            if (selectedDevice instanceof DeviceDecorator) {
                base = ((DeviceDecorator) selectedDevice).unwrap();
            }
            if (base instanceof Light) {
                String value = JOptionPane.showInputDialog(this, "Set brightness (1-10):", "5");
                if (value != null) {
                    ((Light) base).setBrightness(Integer.parseInt(value));
                }
            } else if (base instanceof Fan) {
                String value = JOptionPane.showInputDialog(this, "Set speed (1-5):", "3");
                if (value != null) {
                    ((Fan) base).setSpeed(Integer.parseInt(value));
                }
            } else if (base instanceof AC) {
                String value = JOptionPane.showInputDialog(this, "Set cooling level (1-5):", "3");
                if (value != null) {
                    ((AC) base).setCoolingLevel(Integer.parseInt(value));
                }
            } else if (base instanceof Thermostat) {
                String value = JOptionPane.showInputDialog(this, "Set temperature (16-30):", "22");
                if (value != null) {
                    ((Thermostat) base).setTemperature(Integer.parseInt(value));
                }
            }
            updateStatus();
        });

        motionButton.addActionListener(e -> {
            if (selectedDevice instanceof MotionDetectionDecorator) {
                ((MotionDetectionDecorator) selectedDevice).detectMotion();
                updateStatus();
            }
        });

        timerButton.addActionListener(e -> {
            if (selectedDevice instanceof TimerDecorator) {
                ((TimerDecorator) selectedDevice).scheduleTurnOff(5);
            }
        });

        voiceOnButton.addActionListener(e -> {
            if (selectedDevice instanceof VoiceControlDecorator) {
                ((VoiceControlDecorator) selectedDevice).voiceCommand("turn on");
                updateStatus();
            }
        });

        voiceOffButton.addActionListener(e -> {
            if (selectedDevice instanceof VoiceControlDecorator) {
                ((VoiceControlDecorator) selectedDevice).voiceCommand("turn off");
                updateStatus();
            }
        });

        energyButton.addActionListener(e -> {
            if (selectedDevice instanceof EnergyMonitorDecorator) {
                double energy = ((EnergyMonitorDecorator) selectedDevice).getEnergyUsed();
                JOptionPane.showMessageDialog(this, String.format("Energy used: %.2f kWh", energy));
            }
        });

        awayBtn.addActionListener(e -> {
            controller.setMode(new AwayMode());
            controller.executeMode();
            updateStatus();
        });

        emergencyBtn.addActionListener(e -> {
            controller.setMode(new EmergencyMode());
            controller.executeMode();
            updateStatus();
        });

        intrusionBtn.addActionListener(e -> controller.getAlarm().trigger());
        tempDropBtn.addActionListener(e -> {
            Device thermostat = controller.getRoomDevices("Living Room").get(0); // Assuming first device is thermostat
            if (thermostat instanceof Thermostat) {
                ((Thermostat) thermostat).setTemperature(18);
                updateStatus();
            }
        });

        deviceList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = deviceList.getSelectedIndex();
                if (index >= 0 && selectedRoom != null) {
                    selectedDevice = selectedRoom.getDevices().get(index);
                    selectedLabel.setText("Selected Device: " + selectedDevice.getName());
                    updateStatus();
                }
            }
        });

        Observer guiObserver = message -> SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
        });
        controller.attachLoggerToAllDevices(guiObserver);

        setVisible(true);
    }

    private void selectDevice(Device device) {
        selectedDevice = device;
        if (device != null) {
            selectedLabel.setText("Selected Device: " + device.getName());
            updateStatus();
        }
    }

    private void selectRoom(Room room) {
        selectedRoom = room;
        deviceModel.clear();
        if (room != null) {
            for (Device device : room.getDevices()) {
                deviceModel.addElement(device.getName());
            }
        }
        selectedDevice = null;
        selectedLabel.setText("Selected Device: None");
        updateStatus();
    }

    private void updateStatus() {
        if (selectedDevice != null) {
            statusLabel.setText(selectedDevice.getStatus());
        }
    }
}
