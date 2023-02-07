package telefonszamla;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CallTest {

    private Call peakTimeCall;
    private Call nightTimeCall;

    @BeforeEach
    void init() {
        peakTimeCall = new Call("395682211", LocalTime.of(17, 55, 10), LocalTime.of(18, 19, 11));
        nightTimeCall = new Call("114571155", LocalTime.of(6, 40, 0), LocalTime.of(7, 1, 0));
    }

    @Test
    void testIsMobileCall() {
        assertTrue(peakTimeCall.isMobileCall());
        assertFalse(nightTimeCall.isMobileCall());
    }

    @Test
    void testIsPeakTimeCall() {
        assertTrue(peakTimeCall.isMobileCall());
        assertFalse(nightTimeCall.isMobileCall());
    }

    @Test
    void testGetTariffPerMinute() {
        assertEquals(69.175, peakTimeCall.getTariffPerMinute());
        assertEquals(15, nightTimeCall.getTariffPerMinute());
    }

    @Test
    void testGetLengthInMinutes() {
        assertEquals(25, peakTimeCall.getLengthInMinutes());
        assertEquals(22, nightTimeCall.getLengthInMinutes());
    }
}