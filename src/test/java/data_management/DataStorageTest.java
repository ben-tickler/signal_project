package data_management;
import static org.junit.jupiter.api.Assertions.*;

import com.data_management.DataReader;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        // Stub DataReader (only needed if DataStorage constructor requires it)
        DataReader reader = new DataReader() {
            @Override
            public void readData(DataStorage dataStorage) throws IOException {

            }

            @Override
            public List<PatientRecord> readData() {
                return List.of(); // Return empty or dummy list
            }
        };

        // Create instance of DataStorage using the stub reader
        DataStorage storage = new DataStorage(reader);

        // Add mock data
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        // Retrieve and assert
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size(), "Should retrieve 2 records");
        assertEquals(100.0, records.get(0).getMeasurementValue(), "First record value mismatch");
    }
}
