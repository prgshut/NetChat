package java3.lesson6;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class ArrayFunkTest {
    private static ArrayFunk funk=null;
    private int[] rez1;
    private boolean rez2;
    private int[] arr;
    private Class<? extends Exception> expectedException;
    public ArrayFunkTest(int[] arr, int[]rez1,Class<? extends Exception> expectedException, boolean rez2){
        this.arr=arr;
        this.rez1=rez1;
        this.rez2=rez2;
        this.expectedException=expectedException;
    }
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Parameters
    public static Collection test(){
        return Arrays.asList(new Object[][]{
                {new int[]{1,2,3,4,56,5,2,34,6,6,1} ,new int[] {56,5,2,34,6,6,1},null ,true},
                {new int[]{1,2,3,45,2,58,26,4,7,8,52,4} ,new int[] {},null ,true},
                {new int[]{1,2,3,56,5,2,34,6,6,1} ,new int[]{},RuntimeException.class ,true},
                {new int[]{1,2,3,4,56,5,2,34,6,6,1} ,new int[] {56,5,2,34,6,6,1},null ,true},
                {new int[]{1,2,3,4,56,5,2,34,6,6,1} ,new int[] {56,5,2,34,6,6,1},null ,true},
                {new int[]{1,2,3,4,56,5,2,34,6,6,1} ,new int[] {56,5,2,34,6,6,1},null ,true},
                {new int[]{1,2,3,4,56,5,2,34,6,6,1} ,new int[] {56,5,2,34,6,6,1},null ,true}
        });
    }


    @Test
    public void arrayScreening() {
        if (expectedException!=null){
            thrown.expect(expectedException);
        }
        Assert.assertArrayEquals(rez1,funk.arrayScreening(arr));
    }

    @Test
    public void isNumber() {
        Assert.assertEquals(rez2, funk.isNumber(arr));

    }
    @Before
    public void init(){
        System.out.println("init test");
        funk=new ArrayFunk();
    }
    @After
    public void tearDown() throws Exception{
        funk=null;
    }
}
