import controller.SmartHomeController;
import decorator.DeviceDecorator;
import decorator.EnergyMonitorDecorator;
import decorator.MotionDetectionDecorator;
import decorator.TimerDecorator;
import decorator.VoiceControlDecorator;
import devices.AC;
import devices.Alarm;
import devices.Device;
import devices.DoorLock;
import devices.Fan;
import devices.Light;
import devices.Thermostat;
import factory.DeviceFactory;
import java.util.List;
import java.util.Scanner;
import model.Room;
import observer.Logger;
import strategy.AwayMode;
import strategy.EmergencyMode;

public class Main {

    public static void main(String[] args) {

        SmartHomeController controller = SmartHomeController.getInstance();
        Logger logger = Logger.getInstance();

        // Hardcoded devices using Factory Pattern
        Device bedroomFan = DeviceFactory.createDevice("fan", "Bedroom Fan");
        Device bathroomLight = DeviceFactory.createDevice("light", "Bathroom Light");
        Device kitchenAC = DeviceFactory.createDevice("ac", "Kitchen AC");
        Device livingThermostat = DeviceFactory.createDevice("thermostat", "Living Room Thermostat");

        DoorLock doorLock = (DoorLock) DeviceFactory.createDevice("doorlock", "Main Door Lock");
        Alarm alarm = (Alarm) DeviceFactory.createDevice("alarm", "Home Alarm");

        // Add rooms
        Room bedroom = new Room("Bedroom");
        bedroom.addDevice(bedroomFan);
        bedroom.addDevice(DeviceFactory.createDevice("light", "Bedroom Light"));
        controller.addRoom(bedroom);

        Room bathroom = new Room("Bathroom");
        bathroom.addDevice(bathroomLight);
        controller.addRoom(bathroom);

        Room kitchen = new Room("Kitchen");
        kitchen.addDevice(kitchenAC);
        controller.addRoom(kitchen);

        Room livingRoom = new Room("Living Room");
        livingRoom.addDevice(livingThermostat);
        controller.addRoom(livingRoom);

        // Add system devices
        controller.setDoorLock(doorLock);
        controller.setAlarm(alarm);

        // Attach logger (Observer Pattern)
        controller.attachLoggerToAllDevices(logger);

        // Decorate some devices (Decorator Pattern demo)
        bathroomLight = new MotionDetectionDecorator(bathroomLight);
        kitchenAC = new TimerDecorator(new EnergyMonitorDecorator(kitchenAC));
        bedroomFan = new VoiceControlDecorator(bedroomFan);

        // Update controller room devices so automation modes use wrappers
        controller.updateRoomDevice("Bathroom", bathroomLight);
        controller.updateRoomDevice("Kitchen", kitchenAC);
        controller.updateRoomDevice("Bedroom", bedroomFan);

        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Run Console Version");
        System.out.println("2. Run GUI Version");
        int version = scanner.nextInt();

        if (version == 1) {
    // existing console menu loop

        while (true) {
            System.out.println("\n===== SMART HOME CONTROLLER =====");
            System.out.println("1. View Room Status");
            System.out.println("2. Control Room Device");
            System.out.println("3. Control System Devices (Door/Alarm)");
            System.out.println("4. Activate Automation Mode");
            System.out.println("5. View Logs");
            System.out.println("6. Simulate Sensor Event");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\n--- ROOM STATUS ---");
                    for (var room : controller.getAllRooms()) {
                        room.showRoomStatus();
                    }
                    System.out.println(doorLock.getStatus());
                    System.out.println(alarm.getStatus());
                    break;

                case 2:
                    System.out.println("\nChoose Room:");
                    System.out.println("1. Bedroom");
                    System.out.println("2. Bathroom");
                    System.out.println("3. Kitchen");
                    System.out.println("4. Living Room");

                    int roomChoice = scanner.nextInt();
                    scanner.nextLine();

                    String roomName = "";
                    if (roomChoice == 1) roomName = "Bedroom";
                    else if (roomChoice == 2) roomName = "Bathroom";
                    else if (roomChoice == 3) roomName = "Kitchen";
                    else if (roomChoice == 4) roomName = "Living Room";
                    else {
                        System.out.println("Invalid choice.");
                        break;
                    }

                    List<Device> roomDevices = controller.getRoomDevices(roomName);
                    if (roomDevices == null || roomDevices.isEmpty()) {
                        System.out.println("No devices in this room.");
                        break;
                    }

                    System.out.println("Choose Device:");
                    for (int i = 0; i < roomDevices.size(); i++) {
                        System.out.println((i+1) + ". " + roomDevices.get(i).getName());
                    }

                    int deviceChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (deviceChoice < 1 || deviceChoice > roomDevices.size()) {
                        System.out.println("Invalid choice.");
                        break;
                    }

                    Device selectedDevice = roomDevices.get(deviceChoice - 1);

                    Device baseDevice = selectedDevice;

                    if (selectedDevice instanceof DeviceDecorator) 
                        {
                        baseDevice = ((DeviceDecorator) selectedDevice).unwrap();
                        }

                    boolean controlling = true;

                    while (controlling) {
                        System.out.println("\n--- CONTROLLING: " + selectedDevice.getName() + " ---");
                        System.out.println(selectedDevice.getStatus());

                        System.out.println("1. Turn ON");
                        System.out.println("2. Turn OFF");

                        // Extra options depending on type
                        if (baseDevice instanceof Light) {
                            System.out.println("3. Set Brightness");
                        }
                        if (baseDevice instanceof Fan) {
                            System.out.println("3. Set Speed");
                        }
                        if (baseDevice instanceof AC) {
                            System.out.println("3. Set Cooling Level");
                        }
                        if (baseDevice instanceof Thermostat) {
                            System.out.println("3. Set Temperature");
                        }

                        // Decorator-based features
                        if (selectedDevice instanceof MotionDetectionDecorator) {
                            System.out.println("4. Simulate Motion Detection");
                        }
                        if (selectedDevice instanceof TimerDecorator) {
                            System.out.println("5. Schedule Turn OFF Timer");
                        }
                        if (selectedDevice instanceof VoiceControlDecorator) {
                            System.out.println("6. Voice Command");
                        }
                        if (selectedDevice instanceof EnergyMonitorDecorator) {
                            System.out.println("7. View Energy Usage");
                        }

                        System.out.println("0. Back to Main Menu");

                        System.out.print("Choose action: ");
                        int actionChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (actionChoice) {
                            case 1:
                                selectedDevice.turnOn();;
                                break;

                            case 2:
                                selectedDevice.turnOff();
                                break;

                            case 3:
                                // Handle "level" settings depending on device type
                                if (baseDevice instanceof Light) {
                                    System.out.print("Enter brightness (1-10): ");
                                    int b = scanner.nextInt();
                                    scanner.nextLine();
                                    ((Light) baseDevice).setBrightness(b);

                                } else if (baseDevice instanceof Fan) {
                                    System.out.print("Enter speed (1-5): ");
                                    int s = scanner.nextInt();
                                    scanner.nextLine();
                                    ((Fan) baseDevice).setSpeed(s);

                                } else if (baseDevice instanceof AC) {
                                    System.out.print("Enter cooling level (1-5): ");
                                    int c = scanner.nextInt();
                                    scanner.nextLine();
                                    ((AC) baseDevice).setCoolingLevel(c);

                                } else if (baseDevice instanceof Thermostat) {
                                    System.out.print("Enter temperature (16-30): ");
                                    int t = scanner.nextInt();
                                    scanner.nextLine();
                                    ((Thermostat) baseDevice).setTemperature(t);

                                } else {
                                    System.out.println("No adjustable setting available for this device.");
                                }
                                break;

                            case 4:
                                if (selectedDevice instanceof MotionDetectionDecorator) {
                                    ((MotionDetectionDecorator) selectedDevice).detectMotion();
                                } else {
                                    System.out.println("This device does not support motion detection.");
                                }
                                break;

                            case 5:
                                if (selectedDevice  instanceof TimerDecorator) {
                                    System.out.print("Enter seconds until auto OFF: ");
                                    int seconds = scanner.nextInt();
                                    scanner.nextLine();
                                    ((TimerDecorator) selectedDevice ).scheduleTurnOff(seconds);
                                } else {
                                    System.out.println("This device does not support timer scheduling.");
                                }
                                break;

                            case 6:
                                if (selectedDevice  instanceof VoiceControlDecorator) {
                                    System.out.print("Enter voice command (turn on / turn off): ");
                                    String cmd = scanner.nextLine();
                                    ((VoiceControlDecorator) selectedDevice).voiceCommand(cmd);
                                } else {
                                    System.out.println("This device does not support voice control.");
                                }
                                break;

                            case 7:
                                if (selectedDevice instanceof EnergyMonitorDecorator) {
                                    double energy = ((EnergyMonitorDecorator) selectedDevice).getEnergyUsed();
                                    System.out.println(selectedDevice.getName() + " energy used: " +
                                            String.format("%.2f", energy) + " kWh");
                                } else {
                                    System.out.println("This device does not support energy monitoring.");
                                }
                                break;

                            case 0:
                                controlling = false;
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                    }
                    break;
                    

                case 3:
                    System.out.println("\n--- SYSTEM DEVICES ---");
                    System.out.println("1. Door Lock (ON=Lock, OFF=Unlock)");
                    System.out.println("2. Alarm (ON=Arm, OFF=Disarm)");
                    int sysChoice = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("1. ON");
                    System.out.println("2. OFF");
                    int sysAction = scanner.nextInt();
                    scanner.nextLine();

                    if (sysChoice == 1) {
                        if (sysAction == 1) doorLock.turnOn();
                        else doorLock.turnOff();
                    } else if (sysChoice == 2) {
                        if (sysAction == 1) alarm.turnOn();
                        else alarm.turnOff();
                    }
                    break;

                case 4:
                    System.out.println("\n--- AUTOMATION MODES ---");
                    System.out.println("1. Away Mode");
                    System.out.println("2. Emergency Mode");
                    int modeChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (modeChoice == 1) controller.setMode(new AwayMode());
                    if (modeChoice == 2) controller.setMode(new EmergencyMode());

                    controller.executeMode();
                    break;

                case 5:
                    logger.showLogs();
                    break;

                case 6:
                    System.out.println("\n--- SENSOR EVENTS ---");
                    System.out.println("1. Motion Detected (Bathroom Light)");
                    System.out.println("2. Intrusion Detected (Trigger Alarm)");
                    System.out.println("3. Temperature Drop (Thermostat adjusts)");
                    int eventChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (eventChoice == 1 && bathroomLight instanceof MotionDetectionDecorator) {
                        ((MotionDetectionDecorator) bathroomLight).detectMotion();
                    } else if (eventChoice == 2) {
                        alarm.trigger();
                    } else if (eventChoice == 3 && livingThermostat instanceof Thermostat) {
                        ((Thermostat) livingThermostat).setTemperature(18);
                    } else {
                        System.out.println("Invalid event.");
                    }
                    break;

                case 7:
                    System.out.println("Exiting Smart Home Controller...");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
        } else {
            SmartHomeGUI gui = new SmartHomeGUI(controller);
        }
    }
}
