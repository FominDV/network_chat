import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(Theories.class)
public class TestingMethodsTest {
    private static final Logger LOGGER = LogManager.getLogger();
    private TestingMethods testingMethods;

    @Before
    public void setUp() throws Exception {
        testingMethods = new TestingMethods();
    }

    @DataPoints("dataForHasOneAndFourTestForFalse")
    public static int[][] dataForHasOneAndFourTestForFalse = {{1, 1, 1, 1, 1}, {1, 5, 3, 2}, {4, 4, 4, 4, 4}};
    @DataPoints("dataForHasOneAndFourTestForTrue")
    public static int[][] dataForHasOneAndFourTestForTrue = {{1, 1, 4, 4, 1}, {4, 3, 2, 6, 1}, {0, -590, 33, 1, 4, 0}};
    @DataPoints("dataForGetFormattedArrayTestByException")
    public static int[][] dataForGetFormattedArrayTestByException = {{1, 345, -87, 2, 0}, {}};
    @DataPoints("dataForGetFormattedArray")
    public static int[][][] dataForGetFormattedArray = {
            {{1, 4, 3, 4, 5}, {5}},
            {{4, 1, 2, 3}, {1, 2, 3}},
            {{33, 645, 4}, {}}};

    @Theory
    public void getFormattedArrayTest(@FromDataPoints("dataForGetFormattedArray") int[][] data) {
        Assertions.assertEquals(Arrays.toString(data[1]), Arrays.toString(testingMethods.getFormattedArray(data[0])));
    }

    @Theory
    public void getFormattedArrayTestByException(@FromDataPoints("dataForGetFormattedArrayTestByException") int[] data) {
        try {
            testingMethods.getFormattedArray(data);
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertEquals("Number 4 was not found into this array", e.getMessage());
        }
    }

    @Theory
    public void hasOneAndFourTestForTrue(@FromDataPoints("dataForHasOneAndFourTestForTrue") int[] data) {
        Assertions.assertTrue(testingMethods.hasOneAndFour(data));
    }

    @Theory
    public void hasOneAndFourTestForFalse(@FromDataPoints("dataForHasOneAndFourTestForFalse") int[] data) {
        Assertions.assertFalse(testingMethods.hasOneAndFour(data));
    }
}