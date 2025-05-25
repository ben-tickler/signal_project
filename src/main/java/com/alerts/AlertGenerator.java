package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * The {@code AlertGenerator} class monitors patient data and generates alerts when predefined conditions are met.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     *
     * @param dataStorage the data storage system that provides access to patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions are met.
     * Triggers alerts for:
     * - HeartRate > 100
     * - BloodPressure > 140
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(Long.MIN_VALUE, Long.MAX_VALUE);

        for (PatientRecord record : records) {
            String recordType = record.getRecordType();
            double value = record.getMeasurementValue();
            long timestamp = record.getTimestamp();
            String patientId = String.valueOf(record.getPatientId());

            if (recordType.equalsIgnoreCase("HeartRate") && value > 100) {
                triggerAlert(new Alert(patientId, "High Heart Rate: " + value, timestamp));
            } else if (recordType.equalsIgnoreCase("BloodPressure") && value > 140) {
                triggerAlert(new Alert(patientId, "High Blood Pressure: " + value, timestamp));
            }
        }
    }

    /**
     * Triggers an alert. This could be expanded to notify staff, log to a file, etc.
     *
     * @param alert the alert to be triggered
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT for Patient ID: " + alert.getPatientId()
                + " | Condition: " + alert.getCondition()
                + " | Time: " + alert.getTimestamp());
    }
}
