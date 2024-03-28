package com.purrer.gentools;

import com.purrer.gentools.crossing.Crossings;
import com.purrer.gentools.crossing.CrossingConfig;
import com.purrer.gentools.entities.AllelePair;
import com.purrer.gentools.interfaces.Crossing;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrossingsFactoryTest {

    @Test
    public void testConfigBasedCrossing() {
        CrossingConfig config = new CrossingConfig();
        config.addAllelePairs(
                new AllelePair("E", "e"),
                new AllelePair("A", "a"),
                new AllelePair("G", "g"),
                new AllelePair("Cr", "cr"),
                new AllelePair("Z", "z"),
                new AllelePair("D", "d"),
                new AllelePair("Ch", "ch"),
                new AllelePair("Rn", "rn")
        );

        Crossing crossing = Crossings.create(config);
        Map<String, Integer> punnettSquare = crossing.crossing("EeggCrcr", "eeGgcrcr");

        assertEquals(8, punnettSquare.get("eeGgcrcr"));
        assertEquals(8, punnettSquare.get("eeGgCrcr"));
    }

    @Test
    public void testDefaultCrossing() {
        Crossing defaultCrossing = Crossings.createDefaultCrossing();
        Map<String, Integer> crossing = defaultCrossing.crossing(
                "aaBbCC",
                "AAbBcc"
        );

        assertEquals(3, crossing.size());
        assertEquals(16, crossing.get("AaBBCc"));
        assertEquals(32, crossing.get("AaBbCc"));
        assertEquals(16, crossing.get("AabbCc"));
    }

    @Test
    public void testCrossingConfigWithDuplicateAlleles() {
        CrossingConfig config = new CrossingConfig();
        config.addAllelePair(new AllelePair("A", "a"));
        assertThrows(IllegalStateException.class, () -> config.addAllelePair(new AllelePair("B", "a")));
    }

}
