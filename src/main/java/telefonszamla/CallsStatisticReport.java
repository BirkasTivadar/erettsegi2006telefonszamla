package telefonszamla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallsStatisticReport {

    private final List<Call> calls = new ArrayList<>();

    public void addCall(Call call) {
        calls.add(call);
    }

    public void loadFromFile(Path path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] times = line.split(" ");
                LocalTime start = LocalTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));
                LocalTime end = LocalTime.of(Integer.parseInt(times[3]), Integer.parseInt(times[4]), Integer.parseInt(times[5]));
                String phoneNumber = bufferedReader.readLine();

                calls.add(new Call(phoneNumber, start, end));
            }
        } catch (IOException ioException) {
            throw new IllegalStateException("Can not read file", ioException);
        }
    }

    public void writeToFile(Path path) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            for (Call call : calls) {
                bufferedWriter.write(call.phoneNumber()
                        .concat(" ")
                        .concat(String.valueOf(call.getLengthInMinutes()))
                        .concat(System.lineSeparator()));
            }
        } catch (IOException ioException) {
            throw new IllegalStateException("Can not write file", ioException);
        }
    }

    public List<Call> getCalls() {
        return new ArrayList<>(calls);
    }

    public Map<String, Long> getCallsGroupByTime() {
        long peakTimeCalls = calls.stream()
                .filter(Call::isPeakTimeCall)
                .count();
        long nightTimeCalls = calls.size() - peakTimeCalls;

        return new HashMap<>(Map.of("Csúcsidős hívások száma", peakTimeCalls, "Csúcsidőn kívüli hívások száma", nightTimeCalls));
    }

    public Map<String, Long> getCallsGroupByType() {
        long mobileCalls = calls.stream()
                .filter(Call::isMobileCall)
                .count();
        long phoneCalls = calls.size() - mobileCalls;

        return new HashMap<>(Map.of("Mobilos hívások száma", mobileCalls, "Vezetékes hívások száma", phoneCalls));
    }

    public double getPayableForPeakTimeCalls() {
        return calls.stream()
                .filter(Call::isPeakTimeCall)
                .mapToDouble(Call::getPayable)
                .sum();
    }
}
