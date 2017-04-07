package util;

/**
 * Created by rongyang_lu on 2017/4/7.
 */
public class ArrayUtil {
    /**
     * 把一个整形数组的长度扩充到length
     *
     * @param array  要扩充的整形数组
     * @param length 扩充后数组的长度
     * @return 扩充后的数组
     */
    public static int[] expandTo(int[] array, int length) {
        int[] expandedArray = new int[length];
        System.arraycopy(array,0,expandedArray,0,array.length);
        return expandedArray;
    }

    /**
     * 把一个boolean数组的长度扩充到length
     *
     * @param array  要扩充的boolean数组
     * @param length 扩充后数组的长度
     * @return 扩充后的数组
     */
    public static boolean[] expandTo(boolean[] array, int length) {
        boolean[] expandedArray = new boolean[length];
        System.arraycopy(array,0,expandedArray,0,array.length);
        return expandedArray;
    }
}
