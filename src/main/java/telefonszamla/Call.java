package telefonszamla;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public record Call(String phoneNumber, LocalTime start, LocalTime end) {

    private static final Set<String> MOBILE_PREFIXES = new HashSet<>(Set.of("39", "41", "71"));

    private static final double TARIFF_MOBILE_PEAK_TIME = 69.175;
    private static final double TARIFF_MOBILE_NIGHT_TIME = 30;
    private static final double TARIFF_PHONE_PEAK_TIME = 46.675;
    private static final double TARIFF_PHONE_NIGHT_TIME = 15;

    public boolean isMobileCall() {
        String prefix = phoneNumber.substring(0, 2);
        return MOBILE_PREFIXES.contains(prefix);
    }

    public boolean isPeakTimeCall() {
        return start.isAfter(LocalTime.of(7, 0, 0)) && start.isBefore(LocalTime.of(18, 0, 0));
    }

    public double getTariffPerMinute() {
        if (isPeakTimeCall()) {
            if (isMobileCall()) return TARIFF_MOBILE_PEAK_TIME;
            else return TARIFF_PHONE_PEAK_TIME;
        } else {
            if (isMobileCall()) return TARIFF_MOBILE_NIGHT_TIME;
            else return TARIFF_PHONE_NIGHT_TIME;
        }
    }

    public long getLengthInMinutes() {
        return start.until(end, ChronoUnit.MINUTES) + 1;
    }

    public double getPayable() {
        return getTariffPerMinute() * getLengthInMinutes();
    }
}
