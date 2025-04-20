package com.cardio_generator.outputs;

/**
 * Strategy interface for outputting patient health data.
 *
 * <p>Implementations of this interface define how generated health data
 * should be delivered or stored, such as writing to the console, files,
 * network sockets, or WebSocket clients.
 *
 * <p>This abstraction enables flexibility in selecting or switching between
 * different output methods without modifying the data generation logic.
 *
 * @see com.cardio_generator.outputs.ConsoleOutputStrategy
 * @see com.cardio_generator.outputs.FileOutputStrategy
 * @see com.cardio_generator.outputs.TcpOutputStrategy
 * @see com.cardio_generator.outputs.WebSocketOutputStrategy
 */
public interface OutputStrategy {

    /**
     * Outputs health data for a specific patient.
     *
     * @param patientId The unique ID of the patient whose data is being output.
     * @param timestamp The timestamp at which the data was generated (in milliseconds since epoch).
     * @param label     A label identifying the type of data (e.g., "ECG", "BloodPressure").
     * @param data      The actual data value being output, formatted as a string.
     */
    void output(int patientId, long timestamp, String label, String data);
}
