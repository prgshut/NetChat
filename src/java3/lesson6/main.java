package java3.lesson6;

import java.util.Arrays;

public class main {
    public static void main(String[] args) {
        int[] a={1,2,3,45,2,58,26,4,7,8,52,4};
        int[] b;
        ArrayFunk funk = new ArrayFunk();
        b=funk.arrayScreening(a);
        System.out.println(Arrays.toString(b));
    }
}
