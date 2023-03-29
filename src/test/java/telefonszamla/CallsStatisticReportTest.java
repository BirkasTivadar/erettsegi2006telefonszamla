package telefonszamla;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CallsStatisticReportTest {

    CallsStatisticReport callsStatisticReport;

    Path path;

    @TempDir
    public File temporaryFolder;

    @BeforeEach
    void init() {
        callsStatisticReport = new CallsStatisticReport();
        path = Path.of("src", "main", "resources", "hivasok.txt");
    }

    @Test
    void testAddCall() {
        assertEquals(Collections.EMPTY_LIST, callsStatisticReport.getCalls());

        callsStatisticReport.addCall(new Call("395682211", LocalTime.of(17, 55, 10), LocalTime.of(18, 19, 11)));

        assertEquals(1, callsStatisticReport.getCalls().size());
    }

    @Test
    void testLoadFromFile() {
        callsStatisticReport.loadFromFile(path);

        assertEquals(85, callsStatisticReport.getCalls().size());
    }

    @Test
    void writeToFile() throws IOException {
        Path writePath = new File(temporaryFolder, "test.txt").toPath();

        callsStatisticReport.loadFromFile(path);

        callsStatisticReport.writeToFile(writePath);

        List<String> test = Files.readAllLines(writePath);

        assertEquals(85, test.size());
    }

    @Test
    void testGetCallsGroupByTime() {
        callsStatisticReport.loadFromFile(path);

        assertEquals(63, callsStatisticReport.getCallsGroupByTime().get("Csúcsidős hívások száma"));
    }

    @Test
    void testGetCallsGroupByType() {
        callsStatisticReport.loadFromFile(path);

        assertEquals(27, callsStatisticReport.getCallsGroupByType().get("Mobilos hívások száma"));
    }

    @Test
    void testGetPayableForPeakTimeCalls() {
        callsStatisticReport.loadFromFile(path);

        assertEquals(12353.575, callsStatisticReport.getPayableForPeakTimeCalls(), 0.0001);
    }
}