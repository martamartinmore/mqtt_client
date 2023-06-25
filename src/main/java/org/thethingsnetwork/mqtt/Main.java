package org.thethingsnetwork.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import java.sql.*;

public class Main implements MqttCallback {

    MqttClient client;
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/dataenergy";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "MySqL1234.";

    public Main() {
    }

    public static void main(String[] args) {
        new Main().doDemo();
    }

    public void doDemo() {
        try {
            String username   = "energy-server-consumption@ttn";
            String password   = "NNSXS.GZQ67JK64DLTDK2YCV24O3PHZNRXQAIDR7QXDLI.SGOV2JSZ7USDFSO37YNAXTXD3OOD7VZQNSWCJ7MTCJIPQ4BGEMHQ";
            String serverurl  = "tcp://eu1.cloud.thethings.network:1883";
            String clientId   = MqttClient.generateClientId();

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());

            client = new MqttClient(serverurl, clientId, null);
            System.out.println("Conectamos con el mqtt server");
            client.connect(options);
            client.setCallback(this);
            client.subscribe("#");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println(message);
        try {
            System.out.println("Empieza la conexion a la base de datos");
            // Establecer la conexión
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

            System.out.println("Conexion realizada");

            // Realizar operaciones en la base de datos
            Statement statement = conexion.createStatement();

            // Para futuros desarrollos, se deberá insertar aquí la decodificación de los datos del dispositivo final
            // y su procesamiento y envio a la base de datos correspondiente.

            // Ejemplo de funcionamiento correcto de la isnerción de datos en la base de datos cuando llega un
            // mensaje al cliente mqtt
            String sql_insert = "INSERT INTO salas VALUES (20, 'Sala_Prueba')";
            statement.executeUpdate(sql_insert);
            System.out.println("Se insertaron las filas correctamente ");

            // Cerrar la conexión
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }

}