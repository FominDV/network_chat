import java.util.Arrays;

import org.apache.logging.log4j.*;

public class TestingMethods {
    private static final Logger LOGGER = LogManager.getLogger(TestingMethods.class);

    public int[] getFormattedArray(int[] arr) {
        LOGGER.info("start method getFormattedArray");
        LOGGER.debug("input arr: " + Arrays.toString(arr));
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                LOGGER.info("Number 4 was found");
                return Arrays.copyOfRange(arr, ++i, arr.length);
            }
        }
        LOGGER.warn("There are not number 4");
        throw new RuntimeException("Number 4 was not found into this array");
    }

    public boolean hasOneAndFour(int[] arr) {
        LOGGER.info("start method hasOneAndFour");
        LOGGER.debug("input arr: " + Arrays.toString(arr));
        boolean hasOne = false, hasFour = false;
        for (int number : arr) {
            if (number == 1) hasOne = true;
            else if (number == 4) hasFour = true;
            if (hasOne && hasFour) {
                LOGGER.info("There are numbers 1 and 4");
                return true;
            }
        }
        LOGGER.warn("There are not 1 or 4");
        return false;
    }
}
