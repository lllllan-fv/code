package collection;

import java.util.ArrayDeque;

public class ArrayDequeDemo {

    public static void main(String[] args) {
        System.out.println(-2 & 15);
        ArrayDeque<Integer> Q = new ArrayDeque<>();

        for (int i = 0; i < 100000; ++i) {
            Q.add(i);
        }
        Q.clear();
    }

    /**
     * 默认初始容量为 16
     */

    /**
     * 容量都是 2 的幂次
     * 因为队列是一个循环数组，下标计算涉及取余，对2的幂次取余有规律：x % y = x & (y - 1)
     */
    /*
    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // Find the best power of two to hold elements.
        // Tests "<=" because arrays aren't kept full.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        return initialCapacity;
    }
     */
}
