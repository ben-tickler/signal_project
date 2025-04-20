package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating simulated patient health data.
 *
 * <p>Implementing classes should provide logic to generate specific types
 * of health-related data (e.g., ECG, blood pressure) for a given patient
 * and send it through a defined output strategy.
 *
 * <p>This abstraction allows multiple types of data generators to be used
 * interchangeably within the simulation framework.
 *
 * @see com.cardio_generator.outputs.OutputStrategy
 */
public interface PatientDataGenerator {

    /**
     * Generates health data for the specified patient and sends it via the provided output strategy.
     *
     * @param patientId      The unique ID of the patient for whom data is being generated.
     * @param outputStrategy The strategy used to output the generated data (e.g., console, file, TCP).
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
