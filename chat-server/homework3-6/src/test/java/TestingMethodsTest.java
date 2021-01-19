import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RunWith(Theories.class)
public class TestingMethodsTest {
    private TestingMethods testingMethods;

    @Before
    public void setUp() throws Exception {
        testingMethods = new TestingMethods();
    }

    @DataPoints
    public static int[][][] dataForGetFormattedArray = {
            {{1, 4, 3, 4, 5}, {5}},
            {{4, 1, 2, 3}, {1, 2, 3}},
            {{33, 645, 4}, {}}};

    @Theory
    public void getFormattedArray(int[][] data) {
        Assertions.assertEquals(Arrays.toString(data[1]), Arrays.toString(testingMethods.getFormattedArray(data[0])));
    }
    @Test
    public void getFormattedArrayTestByException(){
       try {
           testingMethods.getFormattedArray(new int[]{1,77,-100});
       }catch (RuntimeException e){
           Assertions.assertEquals("Number 4 was not found into this array", e.getMessage());
       }
    }

}