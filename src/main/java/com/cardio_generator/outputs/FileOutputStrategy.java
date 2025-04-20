package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the OutputStrategy interface to write patient health data to text files.
 *
 * <p>Each type of data (e.g., ECG, BloodPressure) is written to a separate file
 * inside the specified base directory. Files are appended to and named based on
 * the data label.
 *
 * <p>Thread-safe operation is ensured via a {@link ConcurrentHashMap} to avoid
 * re-creating file paths multiple times under concurrent access.
 *
 * @see OutputStrategy
 */
public class FileOutputStrategy implements OutputStrategy {

    /** The directory where output files will be stored. */
    private String baseDirectory;

    /**
     * A mapping between data labels (e.g., "ECG", "BloodPressure") and file paths.
     * Ensures consistent and thread-safe file references.
     */
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new FileOutputStrategy with the given base directory.
     *
     * @param baseDirectory The root directory where data files will be written.
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs patient data by writing it to a label-specific file within the base directory.
     *
     * <p>Each entry is appended in the format:
     * <pre>
     * Patient ID: <id>, Timestamp: <timestamp>, Label: <label>, Data: <data>
     * </pre>
     *
     * @param patientId The unique ID of the patient.
     * @param timestamp The time at which the data was generated (in milliseconds).
     * @param label     A string label indicating the type of data.
     * @param data      The data content as a string.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        String filePath = fileMap.computeIfAbsent(label,
                k -> Paths.get(baseDirectory, label + ".txt").toString());

        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                    patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
