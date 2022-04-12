import java.util.Vector;

public class VectorDemo {

    public static void main(String[] args) {
        Vector<String> vector = new Vector<>();

        vector.add("hello");
//        下标不合法，但是源码中没有检查
//        System.out.println(vector.indexOf("hello", -2));
    }

    /**
     * 指定的 capacityIncrement 是增量，如果指定过增量，每次按照该大小进行扩容，否则每次扩容一倍
     * int newCapacity = oldCapacity + ((capacityIncrement > 0) ? capacityIncrement : oldCapacity);
     */
}
