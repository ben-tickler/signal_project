package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated blood saturation data for a specified patient.
 *
 * <p>This class simulates blood saturation levels for multiple patients by fluctuating
 * the saturation value within a healthy range (90% to 100%). The class maintains a baseline
 * saturation value for each patient and updates it with small variations.
 *
 * <p>The saturation value is then sent to the provided {@link OutputStrategy}.
 *
 * @see PatientDataGenerator
 * @see OutputStrategy
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    /** Random number generator for generating variation in blood saturation levels. */
    private static final Random random = new Random();

    /** Array to store the last saturation value for each patient. */
    private int[] lastSaturationValues;

    /**
     * Constructs a BloodSaturationDataGenerator for a specified number of patients.
     *
     * <p>Each patient's initial blood saturation value is set to a random value between
     * 95% and 100%.
     *
     * @param patientCount The number of patients to simulate data for.
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient (between 95 and 100)
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Random saturation value between 95 and 100
        }
    }

    /**
     * Generates a new blood saturation value for a specific patient and outputs it via the
     * provided output strategy.
     *
     * <p>The generated saturation value fluctuates slightly (+-1%) within the healthy range.
     * If the saturation value goes beyond the healthy range (90-100%), it is clamped to stay
     * within this range.
     *
     * @param patientId    The unique ID of the patient whose saturation value is being generated.
     * @param outputStrategy The strategy used to output the generated data (e.g., to console, file, TCP).
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate small fluctuations in blood saturation level (-1, 0, or 1)
            int variation = random.nextInt(3) - 1;
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Clamp the saturation value to stay within a healthy range (90% to 100%)
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;

            // Output the generated saturation value to the specified output strategy
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }
}
