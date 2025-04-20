package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * An implementation of {@link OutputStrategy} that sends patient health data
 * to a connected client over a TCP socket.
 *
 * <p>This class sets up a TCP server on a given port, accepts a single client connection,
 * and then streams formatted patient data to the connected client.
 *
 * <p>Output is sent in CSV format: <code>patientId,timestamp,label,data</code>
 *
 * @see OutputStrategy
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Constructs a TcpOutputStrategy that opens a server socket on the specified port.
     *
     * <p>Once a client connects, the connection is accepted in a separate thread to avoid
     * blocking the main execution thread.
     *
     * @param port The port number on which to start the TCP server.
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept client connection asynchronously
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends patient health data to the connected TCP client, if one is connected.
     *
     * <p>The message is formatted as a single CSV line:
     * <code>patientId,timestamp,label,data</code>
     *
     * @param patientId The unique identifier for the patient.
     * @param timestamp The time the data was generated, in milliseconds since epoch.
     * @param label     The type of data (e.g., "ECG", "BloodPressure").
     * @param data      The actual data value as a string.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
