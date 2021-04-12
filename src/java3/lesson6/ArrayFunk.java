package java3.lesson6;

import java.util.Arrays;

public class ArrayFunk {

    public int[] arrayScreening (final int[] arr)  {
        final int MARK =4;
        int[] rez;
        int lastPosition = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==MARK){
                lastPosition=i;
            }
        }
        if (lastPosition==0){
            throw new  RuntimeException();
        }
        rez=new int[arr.length-(lastPosition+1)];
        rez= Arrays.copyOfRange(arr,lastPosition+1,arr.length);
        return rez;
    }

    public boolean isNumber(int[] arr){
        final int oneNumb=1;
        final int twoNumbe =4;
        for (int i : arr) {
            if (i ==oneNumb || i==twoNumbe){
                return true;
            }
        }
        return false;
    }
}
