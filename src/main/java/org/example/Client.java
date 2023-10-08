package org.example;

//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * Hello world!
 *
 */
public class Client {
    private MqttClient client;
    public Client(String brokerUrl, String clientId) throws MqttException {
        client = new MqttClient(brokerUrl, clientId);
    }

    public void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }

    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        client.publish(topic, mqttMessage);
    }

    public void subscribe(String topic, IMqttMessageListener listener) throws MqttException {
        client.subscribe(topic, listener);
    }

    public static class MqttMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("Received message on topic '" + topic + "': " + new String(message.getPayload()));
        }
    }
    public static void main( String[] args )
    {
        String brokerUrl = "tcp://localhost:1883"; // MQTT broker URL
        String clientId = "JavaMqttClient"; // Client ID
        String topic = "example/topic"; // Topic to subscribe/publish to

        try {
            Client mqttClient = new Client(brokerUrl, clientId);
            mqttClient.connect();

            // Subscribe to a topic
            mqttClient.subscribe(topic, new MqttMessageListener());

            // Publish a message to the topic
            String message = "Hello, MQTT!";
            mqttClient.publish(topic, message);

            // Create a JFrame to capture key events (Ctrl+C)
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {}

                @Override
                public void keyPressed(KeyEvent e) {}
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
//                        try {
                            //mqttClient.disconnect();
//                        } catch (MqttException ex) {
//                            ex.printStackTrace();
//                        }
                        System.exit(0);
                    }
                }
            });
            frame.setSize(1, 1); // Set an almost invisible frame
            frame.setVisible(true);

            // Endless loop to keep the program running
            while (true) {
                try {
                    Thread.sleep(1000); // Sleep for a second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }

        } catch (MqttException e) {
            e.fillInStackTrace();
        }
    }

}
