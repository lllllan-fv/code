import java.util.List;
import java.util.Vector;

public class VectorDemo {

    public static void main(String[] args) {
        List<Integer> list = new Vector<>();
    }

    /**
     * 指定的 capacityIncrement 是增量，如果指定过增量，每次按照该大小进行扩容，否则每次扩容一倍
     * int newCapacity = oldCapacity + ((capacityIncrement > 0) ? capacityIncrement : oldCapacity);
     */
}
