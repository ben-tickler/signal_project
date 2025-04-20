package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated alert data for patients, triggering and resolving alerts
 * based on probabilistic behavior.
 *
 * <p>This class simulates health alerts for patients, where each alert has a 10% chance of
 * being resolved once triggered. Alerts are triggered with a probability influenced by
 * a Poisson process, controlled by the {@code lambda} parameter, representing the average
 * rate of alerts per period.
 *
 * <p>Alerts are output using the provided {@link OutputStrategy}.
 *
 * @see PatientDataGenerator
 * @see OutputStrategy
 */
public class AlertGenerator implements PatientDataGenerator {

    /** The random number generator used for generating alert-related data. */
    public static final Random RANDOM_GENERATOR = new Random();

    /** Array to track the state of alerts for each patient. False = resolved, True = triggered. */
    private boolean[] alertStates;

    /**
     * Constructs an AlertGenerator for a specified number of patients.
     *
     * <p>Each patient starts with their alert state set to resolved (false).
     *
     * @param patientCount The number of patients for which to generate alerts.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1]; // Initialize all alerts as resolved
    }

    /**
     * Generates a health alert for the specified patient and outputs it via the provided
     * output strategy.
     *
     * <p>If the alert is triggered, it will be marked as such and output. If the alert
     * is resolved (with a 90% chance), it will be marked as resolved and output.
     *
     * <p>Alert triggering follows a probabilistic model with a specified rate of alerts.
     *
     * @param patientId    The unique ID of the patient for whom the alert is generated.
     * @param outputStrategy The strategy used to output the alert data (e.g., to console, file, TCP).
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // If the alert is triggered for the patient, attempt to resolve it
            if (alertStates[patientId]) {
                // 90% chance to resolve the alert
                if (RANDOM_GENERATOR.nextDouble() < 0.9) {
                    alertStates[patientId] = false;

                    // Output the alert as "resolved"
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Lambda value controls the average rate of alerts per period
                double lambda = 0.1;

                // Calculate the probability of triggering an alert based on the Poisson distribution
                double probability = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < probability;

                // Trigger the alert with the calculated probability
                if (alertTriggered) {
                    alertStates[patientId] = true;

                    // Output the alert as "triggered"
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            // Log any errors that occur during alert generation
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
