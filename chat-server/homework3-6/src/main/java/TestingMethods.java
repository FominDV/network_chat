import java.util.Arrays;

public class TestingMethods {

    public static int[] getFormattedArray(int[] arr){
        for (int i = arr.length-1; i >=0 ; i--) {
            if(arr[i]==4){
                return Arrays.copyOfRange(arr,++i,arr.length);
            }
        }
        throw new RuntimeException("Number 4 was not found into this array");
    }
}
