package java.medicalconsultation;

import consultamedica.FqUnit;
import consultamedica.Posology;
import consultamedica.TakingGuideline;
import consultamedica.dayMoment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TakingGuideline Tests")
public class TakingGuidelineTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor accepts valid parameters")
        void testConstructorWithValidParameters() {
            TakingGuideline tg = new TakingGuideline(
                dayMoment.BEFORELUNCH, 15.0f, 1.0f, 1.0f, FqUnit.DAY, "Con agua"
            );
            assertEquals(dayMoment.BEFORELUNCH, tg.getdMoment());
            assertEquals(15.0f, tg.getDuration());
            assertEquals("Con agua", tg.getInstructions());
        }

        @Test
        @DisplayName("Constructor throws exception when dayMoment is null")
        void testConstructorWithNullDayMoment() {
            assertThrows(Exception.class, () -> {
                new TakingGuideline(null, 15.0f, 1.0f, 1.0f, FqUnit.DAY, "Con agua");
            });
        }

        @Test
        @DisplayName("Constructor throws exception when duration is negative")
        void testConstructorWithNegativeDuration() {
            assertThrows(Exception.class, () -> {
                new TakingGuideline(dayMoment.BEFORELUNCH, -1.0f, 1.0f, 1.0f, FqUnit.DAY, "Con agua");
            });
        }

        @Test
        @DisplayName("Constructor throws exception when duration is zero")
        void testConstructorWithZeroDuration() {
            assertThrows(Exception.class, () -> {
                new TakingGuideline(dayMoment.BEFORELUNCH, 0.0f, 1.0f, 1.0f, FqUnit.DAY, "Con agua");
            });
        }

        @Test
        @DisplayName("Constructor throws exception when Posology null")
        void testConstructorWithNullPosology() {
            assertThrows(Exception.class, () -> {
                new TakingGuideline(dayMoment.BEFORELUNCH, 15.0f, 0.0f, 0.0f, null, "Con agua");
            });
        }

        @Test
        @DisplayName("Constructor accepts null instructions (optional)")
        void testConstructorWithNullInstructions() {
            assertDoesNotThrow(() -> {
                new TakingGuideline(dayMoment.BEFORELUNCH, 15.0f, 1.0f, 1.0f, FqUnit.DAY, null);
            });
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        private TakingGuideline tg;

        @BeforeEach
        void setUp() {
            tg = new TakingGuideline(dayMoment.BEFORELUNCH, 15.0f, 1.0f, 1.0f, FqUnit.DAY, "Con agua");
        }

        @Test
        @DisplayName("getDayMoment returns correct value")
        void testGetDayMoment() {
            assertEquals(dayMoment.BEFORELUNCH, tg.getdMoment());
        }

        @Test
        @DisplayName("getDuration returns correct value")
        void testGetDuration() {
            assertEquals(15.0f, tg.getDuration());
        }

        @Test
        @DisplayName("getPosology returns Posology object")
        void testGetPosology() {
            assertNotNull(tg.getPosology());
            assertEquals(1.0f, tg.getPosology().getDose());
        }

        @Test
        @DisplayName("getInstructions returns correct value")
        void testGetInstructions() {
            assertEquals("Con agua", tg.getInstructions());
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        private TakingGuideline tg;

        @BeforeEach
        void setUp() {
            tg = new TakingGuideline(dayMoment.BEFORELUNCH, 15.0f, 1.0f, 1.0f, FqUnit.DAY, "Con agua");
        }

        @Test
        @DisplayName("setDayMoment updates value correctly")
        void testSetDayMoment() {
            tg.setdMoment(dayMoment.AFTERLUNCH);
            assertEquals(dayMoment.AFTERLUNCH, tg.getdMoment());
        }

        @Test
        @DisplayName("setDayMoment throws exception when null")
        void testSetDayMomentNull() {
            assertThrows(Exception.class, () -> {
                tg.setdMoment(null);
            });
        }

        @Test
        @DisplayName("setDuration updates value correctly")
        void testSetDuration() {
            tg.setDuration(20.0f);
            assertEquals(20.0f, tg.getDuration());
        }

        @Test
        @DisplayName("setDuration throws exception when negative")
        void testSetDurationNegative() {
            assertThrows(Exception.class, () -> {
                tg.setDuration(-5.0f);
            });
        }

        @Test
        @DisplayName("setPosology updates Posology object")
        void testSetPosology() {
            Posology newPos = new Posology(2.0f, 2.0f, FqUnit.HOUR);
            tg.setPosology(newPos);
            assertEquals(2.0f, tg.getPosology().getDose());
        }

        @Test
        @DisplayName("setInstructions updates value correctly")
        void testSetInstructions() {
            tg.setInstructions("Con comida");
            assertEquals("Con comida", tg.getInstructions());
        }
    }
}