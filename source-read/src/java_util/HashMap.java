package java_util;

import sun.misc.SharedSecrets;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author Doug Lea
 * @author Josh Bloch
 * @author Arthur van Hoff
 * @author Neal Gafter
 * @see Object#hashCode()
 * @see Collection
 * @see Map
 * @see TreeMap
 * @see Hashtable
 * @since 1.2
 */

@SuppressWarnings({"all"})
public class HashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {

    private static final long serialVersionUID = 362498820763181265L;

    /**
     * 默认的初始容量 16，必须是2的幂次
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * 允许的最大容量 1 << 30
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认的加载因子，0.75
     * <p>
     * 干嘛用的？
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 链表树化的阈值
     * 当链表节点个数增加到该阈值之后转换成红黑树
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * 红黑树退化成链表的阈值
     * 当红黑树节点个数减少到该阈值之后退化成链表
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * 允许树化的数组阈值
     * 链表树化的两个条件：
     * <ul>
     *     <li>存放数据的数组大小不小于64</li>
     *     <li>该链表节点个数不小于8</li>
     * </ul>
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     * 存放键值对的节点
     */
    static class Node<K, V> implements Entry<K, V> {
        // hash和key都是不会改变的，使用final的好处是什么？
        final int hash;
        final K key;

        // value和next都没有final修饰，因为是可变的
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Entry<?, ?> e = (Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    /* ---------------- Static utilities -------------- */

    /**
     * https://www.cnblogs.com/shujiying/p/12357193.html
     * <p>
     * 在计算键值对存放索引的时候，要用hash和数组的大小进行运算：i = (n - 1) & hash
     * <p>
     * n 绝大多数情况小于2的16次方。所以始终是 hashcode 的低16位（甚至更低）参与运算。
     * <p>
     * (h = key.hashCode()) ^ (h >>> 16)，hashcode和自己的高16位进行运算，能够让计算得到hash更加散列
     *
     * @param key
     * @return hash
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * Returns x's Class if it is of the form "class C implements
     * Comparable<C>", else null.
     */
    static Class<?> comparableClassFor(Object x) {
        if (x instanceof Comparable) {
            Class<?> c;
            Type[] ts, as;
            Type t;
            ParameterizedType p;
            if ((c = x.getClass()) == String.class) // bypass checks
                return c;
            if ((ts = c.getGenericInterfaces()) != null) {
                for (int i = 0; i < ts.length; ++i) {
                    if (((t = ts[i]) instanceof ParameterizedType) &&
                            ((p = (ParameterizedType) t).getRawType() ==
                                    Comparable.class) &&
                            (as = p.getActualTypeArguments()) != null &&
                            as.length == 1 && as[0] == c) // type arg is c
                        return c;
                }
            }
        }
        return null;
    }

    /**
     * Returns k.compareTo(x) if x matches kc (k's screened comparable
     * class), else 0.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // for cast to Comparable
    static int compareComparables(Class<?> kc, Object k, Object x) {
        return (x == null || x.getClass() != kc ? 0 : ((Comparable) k).compareTo(x));
    }

    /**
     * 求一个不小于指定cap的最小2的幂次
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /* ---------------- Fields -------------- */

    /**
     * 存放数据的数组
     * <p>
     * 数组中的元素可能是链表和红黑树，上面的节点存放具体的键值对
     */
    transient Node<K, V>[] table;

    /**
     * Holds cached entrySet(). Note that AbstractMap fields are used
     * for keySet() and values().
     */
    transient Set<Entry<K, V>> entrySet;

    /**
     * 当前键值对的数量
     */
    transient int size;

    /**
     * 修改次数。用于迭代器快速失败 fail-fast
     */
    transient int modCount;

    /**
     * 数组需要扩容的阈值：容量 * 负载因子
     * <p>
     * 如果当前数组为空或者大小为零，则该阈值为默认初始容量 16
     *
     * @serial
     */
    int threshold;

    /**
     * 加载因子
     * <p>
     * 决定hashmap扩容的阈值，默认值为0.75
     *
     * @serial
     */
    final float loadFactor;

    /* ---------------- Public operations -------------- */

    /**
     * 创建一个空的hashmap。
     * 指定初始容量和加载因子
     *
     * @param initialCapacity 初始容量
     * @param loadFactor      加载因子
     * @throws IllegalArgumentException 如果指定的初始容量或者加载因子数值非法
     */
    public HashMap(int initialCapacity, float loadFactor) {

        // 初始容量不能小于零
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);

        // 初始容量要限制在 MAXIMUM_CAPACITY 范围内
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;

        // 加载因子不能小于零或者是无限大
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * 创建一个空的hashmap。
     * 指定初始容量，使用默认的加载因子0.75
     * <p>
     * 调用 {@link HashMap#HashMap(int, float)}
     *
     * @param initialCapacity 初始容量
     * @throws IllegalArgumentException 如果指定的初始容量非法
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 创建一个空的hashmap。
     * 使用默认的初始容量16和默认的加载因子0.75
     */
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    /**
     * 使用目标map中的键值对创建一个hashmap
     * <p>
     * 调用 {@link #putMapEntries(Map, boolean)}
     *
     * @param m 指定的集合map
     * @throws NullPointerException 如果map为null
     */
    public HashMap(Map<? extends K, ? extends V> m) {
        // 使用默认的加载因子 0.75
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }

    /**
     * 实现 {@link #putAll(Map)} 和 {@link #HashMap(Map)}。
     * <p>
     * 将目标集合中的所有键值对存放到自己的数组中。
     *
     * <ul>
     *     <li>如果当前数组还未创建，去计算一个合适的扩容阈值。((float) s / loadFactor) + 1.0F</li>
     *     <li>如果键值对数量超过了扩容阈值，进行扩容 {@link #resize()}</li>
     *     <li>遍历目标集合，将所有键值对存放到自己的数组中 {@link #putVal(int, Object, Object, boolean, boolean)}</li>
     * </ul>
     *
     * @param m     指定的集合 map
     * @param evict false表示数组还未创建（这个要干嘛？）
     */
    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        // 获取目标集合中的键值对数量
        int s = m.size();

        if (s > 0) {
            // 如果数组还未创建
            if (table == null) {
                // 去计算一个新的扩容阈值
                float ft = ((float) s / loadFactor) + 1.0F;
                int t = ((ft < (float) MAXIMUM_CAPACITY) ? (int) ft : MAXIMUM_CAPACITY);
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }

            // 如果数量超过当前hashmap的扩容阈值
            else if (s > threshold)
                resize();

            // 遍历m，将所有的键值对存放到当前的hashmap中
            for (Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    /**
     * @return 返回当前集合中键值对的数量
     */
    public int size() {
        return size;
    }

    /**
     * @return 返回当前集合是否为空 / 键值对的数量是否为零
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 返回指定的key对应的value
     * <p>
     * 调用 {@link #getNode(int, Object)} 获取包含key的键值对
     *
     * @see #put(Object, Object)
     */
    public V get(Object key) {
        Node<K, V> e;
        // 键值对为空表示集合中不包含key，返回null
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    /**
     * 实现 {@link #get(Object)} 和相关方法.
     * <p>
     * 根据数组的大小和key的hash计算得到索引
     * <ul>
     *     <li>首先检查以下头节点（可能是链表也可能是红黑树），如果命中了，就省去了调用其他方法的消耗</li>
     *     <li>
     *         如果链表或红黑树中不止一个节点，比较后续的节点
     *         <ul>
     *             <li>如果当前是一棵红黑树，调用红黑树下对应的方法 {@link TreeNode#getTreeNode(int, Object)}</li>
     *             <li>否则遍历链表中的所有节点进行判断</li>
     *         </ul>
     *     </li>
     * </ul>
     * 如果数组为空或者链表/红黑树中没有符合的节点，返回null
     *
     * @param hash key的hash
     * @param key  key
     * @return the 键值对
     */
    final Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab;
        Node<K, V> first, e;
        int n;
        K k;

        // 数组不为空并且大小不为零，根据hash和n计算对应的索引
        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {

            // 首先检查以下头节点（可能是链表也可能是红黑树），如果命中了，就省去了调用其他方法的消耗
            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
                return first;

            // 如果链表或红黑树中不止一个节点，比较后续的节点
            if ((e = first.next) != null) {

                // 如果当前是一棵红黑树，调用红黑树下对应的方法
                if (first instanceof TreeNode)
                    return ((TreeNode<K, V>) first).getTreeNode(hash, key);

                // 否则遍历链表中的所有节点
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    /**
     * @param key 指定的key
     * @return 返回集合中是否包含目标key
     * <p>
     * 调用 {@link #getNode(int, Object)}
     */
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key   键
     * @param value 值
     * @return 返回原来key对应的value
     * <p>
     * 调用 {@link #putVal(int, Object, Object, boolean, boolean)}
     */
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    /**
     * 实现 {@link #put(Object, Object)} 和相关方法
     * <p>
     * 1. 如果数组还未创建或大小为零，先创建数组。调用 {@link #resize()}
     * <p>
     * 2. 计算得到索引，如果索引处没有数据，直接存放新节点
     * <p>
     * 3. 如果索引处有数据，首先判断头节点，如果命中可以节省调用其他方法的消耗
     * <p>
     * 4. 如果未命中，并且当前是一棵红黑树，调用红黑树下对应的方法 {@link TreeNode#putTreeVal(HashMap, Node[], int, Object, Object)}
     * <p>
     * 5. 如果未命中，并且当前是一个链表，遍历链表中的所有节点。如果某个节点含有key，覆盖或者保留原来的value。如果没有节点包含key，则在链表末尾添加新节点。
     * 如果添加新节点之后链表中的节点数量达到可以树化的阈值，调用 {@link #treeifyBin(Node[], int)} 进行树化
     *
     * @param hash         key的哈希值
     * @param key          键
     * @param value        值
     * @param onlyIfAbsent 是否保留原来的value
     * @param evict        是否已创建数组
     * @return 返回原来key对应的value
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;

        // 如果数组尚未创建，或者大小为零，调用 resize()
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;

        // 计算得到索引，如果索引处还没有数据，直接放入键值对
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);

        else {
            Node<K, V> e;
            K k;

            // 首先判断头节点，如果命中可以节省调用其他方法的消耗
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;

            else if (p instanceof TreeNode)
                // 如果当前是一棵红黑树，调用红黑树对应的方法
                e = ((TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);

            else {
                // 否则遍历链表将该键值对插入
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        // 遍历结束发现没有key，在链表末尾插入键值对
                        p.next = newNode(hash, key, value, null);

                        // 如果插入之后链表中的数量到达可以树化的阈值，调用树化的方法
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }

                    // 找到存放key的节点，退出遍历
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }

            // 如果e不为null，表示找到了存放key的节点
            if (e != null) {
                V oldValue = e.value;

                // 如果参数中指定覆盖原来的value、或原来的value为null，则进行替换
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;

                // 替换后的操作，没有代码，不知道做了什么
                afterNodeAccess(e);

                // 返回原来的value
                return oldValue;
            }
        }

        ++modCount;
        // 如果整个集合中的键值对数量超过扩容阈值，进行扩容
        if (++size > threshold)
            resize();

        // 插入新节点后的操作，看不到代码，不知道做了什么
        afterNodeInsertion(evict);
        return null;
    }

    /**
     * 数组初始化或双倍扩容。
     * <p>
     * 创建数组（默认初始容量为16），或者为原来的数组进行两倍扩容。并计算新的扩容阈值。
     * <p>
     * 扩容之后将原来数组中的节点，重新分布到新的数组当中。遇到红黑树需要调用对应方法 {@link TreeNode#split(HashMap, Node[], int, int)}
     *
     * @return 扩容后的新数组
     */
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;

        // 原来数组的容量
        int oldCap = (oldTab == null) ? 0 : oldTab.length;

        // 原来数组的扩容阈值
        int oldThr = threshold;

        int newCap, newThr = 0;


        if (oldCap > 0) {
            // 如果原来的容量已经不小于允许的最大容量了
            if (oldCap >= MAXIMUM_CAPACITY) {
                // 更新扩容阈值，不再扩容，直接返回原数组
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }

            // 新容量为原来容量的两倍，需要的适合将新扩容阈值也变为原来的两倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1;
        }

        // 当前数组为空，但是扩容阈值不为零，将新的数组容量设置为原来的扩容阈值
        else if (oldThr > 0)
            newCap = oldThr;


        else {
            // 原来的数组为空，并且原来的扩容阈值也为零
            // 使用默认的初始容量 16，扩容阈值为容量 * 加载因子 = 16 * 0.75 = 12
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        // 如果此时新的扩容阈值为零
        if (newThr == 0) {
            // 根据容量计算扩容阈值 = 新的容量 * 加载因子
            float ft = (float) newCap * loadFactor;

            // 把扩容阈值现在在 Integer.MAX_VALUE 范围内
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ? (int) ft : Integer.MAX_VALUE);
        }

        // 更新扩容阈值
        threshold = newThr;

        // 扩容之后需要重新分布所有的键值对
        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                // 遍历原来的数组中的所有链表/红黑树
                if ((e = oldTab[j]) != null) {
                    // 置为null，方便GC
                    oldTab[j] = null;

                    // 如果链表是单个节点，直接放入到新的数组中
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;

                    else if (e instanceof TreeNode)
                        // 如果当前是一棵红黑树，调用红黑树下对应的方法
                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);

                    else {
                        // 此时的重新分布，最多会将一条链表拆成两条链表，并且不会影响到数组中的其他链表
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;

                        // 遍历链表中的所有节点，将他们分布到两条新的链表当中
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);

                        // 将新链表放入到新数组中
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }

        // 返回新的数组
        return newTab;
    }

    /**
     * 将某一链表进行树化。
     * <p>
     * 树化的两个条件：链表大小到达指定阈值、数组大小达到指定阈值。
     * <p>
     * 如果数组的大小不合规范，先对数组扩容，暂不树化
     * <p>
     * 将链表中的所有普通节点替换为特定的红黑树节点{@link TreeNode#replacementTreeNode(Node, Node)}，再对这棵新的红黑树进行排列{@link TreeNode#treeify(Node[])}
     */
    final void treeifyBin(Node<K, V>[] tab, int hash) {
        int n, index;
        Node<K, V> e;

        // 如果数组还未创建，或者数组大小还没到达可以树化的阈值。先扩容，暂不树化
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();

        else if ((e = tab[index = (n - 1) & hash]) != null) {
            // 确认索引处存在链表，对其进行树化
            TreeNode<K, V> hd = null, tl = null;

            // 遍历链表中的所有节点，将节点替换为特定的红黑树节点
            do {
                TreeNode<K, V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);

            // 对刚创建好的红黑树进行重新排列
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }

    /**
     * 将指定集合中的所有键值对，插入到当前集合中
     *
     * @param m 指定的集合
     * @throws NullPointerException 如果指定的集合为null
     *                              <p>
     *                              调用 {@link #putMapEntries(Map, boolean)}
     */
    public void putAll(Map<? extends K, ? extends V> m) {
        putMapEntries(m, true);
    }

    /**
     * @param key 要删除的key
     * @return 返回原来key对应的value
     * <p>
     * 调用 {@link #removeNode(int, Object, Object, boolean, boolean)}
     */
    public V remove(Object key) {
        Node<K, V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ? null : e.value;
    }

    /**
     * 实现 {@link #remove(Object)} 和相关方法。
     *
     * @param hash       key的hash
     * @param key        key
     * @param value      value
     * @param matchValue 是否必须整个键值对相对才能删除
     * @param movable    if false do not move other nodes while removing ？
     * @return 返回包含key的键值对
     */
    final Node<K, V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, index;

        if ((tab = table) != null && (n = tab.length) > 0 && (p = tab[index = (n - 1) & hash]) != null) {
            Node<K, V> node = null, e;
            K k;
            V v;

            // 首先判断头节点
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;

            else if ((e = p.next) != null) {
                // 如果当前是红黑树，调用红黑树下对应的方法
                if (p instanceof TreeNode)
                    node = ((TreeNode<K, V>) p).getTreeNode(hash, key);

                else {
                    // 遍历整个链表找到包含key的节点
                    do {
                        if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }

            // 如果能够找到包含key的节点
            // 不需要匹配value || key和value都相等
            if (node != null && (!matchValue || (v = node.value) == value || (value != null && value.equals(v)))) {
                // 如果当前是红黑树节点，调用红黑树下的方法
                if (node instanceof TreeNode)
                    ((TreeNode<K, V>) node).removeTreeNode(this, tab, movable);

                else if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;

                ++modCount;
                --size;

                // 删除节点后的操作，没有代码，不知道要做什么
                afterNodeRemoval(node);

                return node;
            }
        }

        return null;
    }

    /**
     * 清除集合中的所有键值对
     */
    public void clear() {
        Node<K, V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    /**
     * @param value 待查找的value
     * @return 返回集合中是否包含目标value
     */
    public boolean containsValue(Object value) {
        Node<K, V>[] tab;
        V v;
        if ((tab = table) != null && size > 0) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K, V> e = tab[i]; e != null; e = e.next) {
                    if ((v = e.value) == value || (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * @return 返回包含集合中所有key的set集合
     */
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
    }

    final class KeySet extends AbstractSet<K> {
        public final int size() {
            return size;
        }

        public final void clear() {
            HashMap.this.clear();
        }

        public final Iterator<K> iterator() {
            return new KeyIterator();
        }

        public final boolean contains(Object o) {
            return containsKey(o);
        }

        public final boolean remove(Object key) {
            return removeNode(hash(key), key, null, false, true) != null;
        }

        public final Spliterator<K> spliterator() {
            return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        public final void forEach(Consumer<? super K> action) {
            Node<K, V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K, V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.key);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * @return a view of the values contained in this map
     */
    public Collection<V> values() {
        Collection<V> vs = values;
        if (vs == null) {
            vs = new Values();
            values = vs;
        }
        return vs;
    }

    final class Values extends AbstractCollection<V> {
        public final int size() {
            return size;
        }

        public final void clear() {
            HashMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        public final boolean contains(Object o) {
            return containsValue(o);
        }

        public final Spliterator<V> spliterator() {
            return new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        public final void forEach(Consumer<? super V> action) {
            Node<K, V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K, V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.value);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }

    final class EntrySet extends AbstractSet<Entry<K, V>> {
        public final int size() {
            return size;
        }

        public final void clear() {
            HashMap.this.clear();
        }

        public final Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public final boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Entry<?, ?> e = (Entry<?, ?>) o;
            Object key = e.getKey();
            Node<K, V> candidate = getNode(hash(key), key);
            return candidate != null && candidate.equals(e);
        }

        public final boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                Entry<?, ?> e = (Entry<?, ?>) o;
                Object key = e.getKey();
                Object value = e.getValue();
                return removeNode(hash(key), key, value, true, true) != null;
            }
            return false;
        }

        public final Spliterator<Entry<K, V>> spliterator() {
            return new EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
        }

        public final void forEach(Consumer<? super Entry<K, V>> action) {
            Node<K, V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<K, V> e = tab[i]; e != null; e = e.next)
                        action.accept(e);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

    // Overrides of JDK8 Map extension methods

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true, true);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return removeNode(hash(key), key, value, true, true) != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Node<K, V> e;
        V v;
        if ((e = getNode(hash(key), key)) != null &&
                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
            e.value = newValue;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        Node<K, V> e;
        if ((e = getNode(hash(key), key)) != null) {
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if (mappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K, V>[] tab;
        Node<K, V> first;
        int n, i;
        int binCount = 0;
        TreeNode<K, V> t = null;
        Node<K, V> old = null;
        if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
            else {
                Node<K, V> e = first;
                K k;
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
            V oldValue;
            if (old != null && (oldValue = old.value) != null) {
                afterNodeAccess(old);
                return oldValue;
            }
        }
        V v = mappingFunction.apply(key);
        if (v == null) {
            return null;
        } else if (old != null) {
            old.value = v;
            afterNodeAccess(old);
            return v;
        } else if (t != null)
            t.putTreeVal(this, tab, hash, key, v);
        else {
            tab[i] = newNode(hash, key, v, first);
            if (binCount >= TREEIFY_THRESHOLD - 1)
                treeifyBin(tab, hash);
        }
        ++modCount;
        ++size;
        afterNodeInsertion(true);
        return v;
    }

    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        Node<K, V> e;
        V oldValue;
        int hash = hash(key);
        if ((e = getNode(hash, key)) != null &&
                (oldValue = e.value) != null) {
            V v = remappingFunction.apply(key, oldValue);
            if (v != null) {
                e.value = v;
                afterNodeAccess(e);
                return v;
            } else
                removeNode(hash, key, null, false, true);
        }
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K, V>[] tab;
        Node<K, V> first;
        int n, i;
        int binCount = 0;
        TreeNode<K, V> t = null;
        Node<K, V> old = null;
        if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
            else {
                Node<K, V> e = first;
                K k;
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
        }
        V oldValue = (old == null) ? null : old.value;
        V v = remappingFunction.apply(key, oldValue);
        if (old != null) {
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            } else
                removeNode(hash, key, null, false, true);
        } else if (v != null) {
            if (t != null)
                t.putTreeVal(this, tab, hash, key, v);
            else {
                tab[i] = newNode(hash, key, v, first);
                if (binCount >= TREEIFY_THRESHOLD - 1)
                    treeifyBin(tab, hash);
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return v;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (value == null)
            throw new NullPointerException();
        if (remappingFunction == null)
            throw new NullPointerException();
        int hash = hash(key);
        Node<K, V>[] tab;
        Node<K, V> first;
        int n, i;
        int binCount = 0;
        TreeNode<K, V> t = null;
        Node<K, V> old = null;
        if (size > threshold || (tab = table) == null ||
                (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & hash]) != null) {
            if (first instanceof TreeNode)
                old = (t = (TreeNode<K, V>) first).getTreeNode(hash, key);
            else {
                Node<K, V> e = first;
                K k;
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        old = e;
                        break;
                    }
                    ++binCount;
                } while ((e = e.next) != null);
            }
        }
        if (old != null) {
            V v;
            if (old.value != null)
                v = remappingFunction.apply(old.value, value);
            else
                v = value;
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            } else
                removeNode(hash, key, null, false, true);
            return v;
        }
        if (value != null) {
            if (t != null)
                t.putTreeVal(this, tab, hash, key, value);
            else {
                tab[i] = newNode(hash, key, value, first);
                if (binCount >= TREEIFY_THRESHOLD - 1)
                    treeifyBin(tab, hash);
            }
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return value;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Node<K, V>[] tab;
        if (action == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K, V> e = tab[i]; e != null; e = e.next)
                    action.accept(e.key, e.value);
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Node<K, V>[] tab;
        if (function == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K, V> e = tab[i]; e != null; e = e.next) {
                    e.value = function.apply(e.key, e.value);
                }
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    /* ------------------------------------------------------------ */
    // Cloning and serialization

    /**
     * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
     * values themselves are not cloned.
     *
     * @return a shallow copy of this map
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        HashMap<K, V> result;
        try {
            result = (HashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }

    // These methods are also used when serializing HashSets
    final float loadFactor() {
        return loadFactor;
    }

    final int capacity() {
        return (table != null) ? table.length :
                (threshold > 0) ? threshold :
                        DEFAULT_INITIAL_CAPACITY;
    }

    /**
     * Save the state of the <tt>HashMap</tt> instance to a stream (i.e.,
     * serialize it).
     *
     * @serialData The <i>capacity</i> of the HashMap (the length of the
     * bucket array) is emitted (int), followed by the
     * <i>size</i> (an int, the number of key-value
     * mappings), followed by the key (Object) and value (Object)
     * for each key-value mapping.  The key-value mappings are
     * emitted in no particular order.
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws IOException {
        int buckets = capacity();
        // Write out the threshold, loadfactor, and any hidden stuff
        s.defaultWriteObject();
        s.writeInt(buckets);
        s.writeInt(size);
        internalWriteEntries(s);
    }

    /**
     * Reconstitutes this map from a stream (that is, deserializes it).
     *
     * @param s the stream
     * @throws ClassNotFoundException if the class of a serialized object
     *                                could not be found
     * @throws IOException            if an I/O error occurs
     */
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        // Read in the threshold (ignored), loadfactor, and any hidden stuff
        s.defaultReadObject();
        reinitialize();
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new InvalidObjectException("Illegal load factor: " +
                    loadFactor);
        s.readInt();                // Read and ignore number of buckets
        int mappings = s.readInt(); // Read number of mappings (size)
        if (mappings < 0)
            throw new InvalidObjectException("Illegal mappings count: " +
                    mappings);
        else if (mappings > 0) { // (if zero, use defaults)
            // Size the table using given load factor only if within
            // range of 0.25...4.0
            float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
            float fc = (float) mappings / lf + 1.0f;
            int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
                    DEFAULT_INITIAL_CAPACITY :
                    (fc >= MAXIMUM_CAPACITY) ?
                            MAXIMUM_CAPACITY :
                            tableSizeFor((int) fc));
            float ft = (float) cap * lf;
            threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
                    (int) ft : Integer.MAX_VALUE);

            // Check Map.Entry[].class since it's the nearest public type to
            // what we're actually creating.
            SharedSecrets.getJavaOISAccess().checkArray(s, Entry[].class, cap);
            @SuppressWarnings({"rawtypes", "unchecked"})
            Node<K, V>[] tab = (Node<K, V>[]) new Node[cap];
            table = tab;

            // Read the keys and values, and put the mappings in the HashMap
            for (int i = 0; i < mappings; i++) {
                @SuppressWarnings("unchecked")
                K key = (K) s.readObject();
                @SuppressWarnings("unchecked")
                V value = (V) s.readObject();
                putVal(hash(key), key, value, false, false);
            }
        }
    }

    /* ------------------------------------------------------------ */
    // iterators

    abstract class HashIterator {
        Node<K, V> next;        // next entry to return
        Node<K, V> current;     // current entry
        int expectedModCount;  // for fast-fail
        int index;             // current slot

        HashIterator() {
            expectedModCount = modCount;
            Node<K, V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<K, V> nextNode() {
            Node<K, V>[] t;
            Node<K, V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

        public final void remove() {
            Node<K, V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }
    }

    final class KeyIterator extends HashIterator
            implements Iterator<K> {
        public final K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator
            implements Iterator<V> {
        public final V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashIterator
            implements Iterator<Entry<K, V>> {
        public final Entry<K, V> next() {
            return nextNode();
        }
    }

    /* ------------------------------------------------------------ */
    // spliterators

    static class HashMapSpliterator<K, V> {
        final HashMap<K, V> map;
        Node<K, V> current;          // current node
        int index;                  // current index, modified on advance/split
        int fence;                  // one past last index
        int est;                    // size estimate
        int expectedModCount;       // for comodification checks

        HashMapSpliterator(HashMap<K, V> m, int origin,
                           int fence, int est,
                           int expectedModCount) {
            this.map = m;
            this.index = origin;
            this.fence = fence;
            this.est = est;
            this.expectedModCount = expectedModCount;
        }

        final int getFence() { // initialize fence and size on first use
            int hi;
            if ((hi = fence) < 0) {
                HashMap<K, V> m = map;
                est = m.size;
                expectedModCount = m.modCount;
                Node<K, V>[] tab = m.table;
                hi = fence = (tab == null) ? 0 : tab.length;
            }
            return hi;
        }

        public final long estimateSize() {
            getFence(); // force init
            return (long) est;
        }
    }

    static final class KeySpliterator<K, V>
            extends HashMapSpliterator<K, V>
            implements Spliterator<K> {
        KeySpliterator(HashMap<K, V> m, int origin, int fence, int est,
                       int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }

        public KeySpliterator<K, V> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new KeySpliterator<>(map, lo, index = mid, est >>>= 1,
                            expectedModCount);
        }

        public void forEachRemaining(Consumer<? super K> action) {
            int i, hi, mc;
            if (action == null)
                throw new NullPointerException();
            HashMap<K, V> m = map;
            Node<K, V>[] tab = m.table;
            if ((hi = fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            } else
                mc = expectedModCount;
            if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                Node<K, V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p.key);
                        p = p.next;
                    }
                } while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super K> action) {
            int hi;
            if (action == null)
                throw new NullPointerException();
            Node<K, V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                while (current != null || index < hi) {
                    if (current == null)
                        current = tab[index++];
                    else {
                        K k = current.key;
                        current = current.next;
                        action.accept(k);
                        if (map.modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }

        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
        }
    }

    static final class ValueSpliterator<K, V>
            extends HashMapSpliterator<K, V>
            implements Spliterator<V> {
        ValueSpliterator(HashMap<K, V> m, int origin, int fence, int est,
                         int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }

        public ValueSpliterator<K, V> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new ValueSpliterator<>(map, lo, index = mid, est >>>= 1,
                            expectedModCount);
        }

        public void forEachRemaining(Consumer<? super V> action) {
            int i, hi, mc;
            if (action == null)
                throw new NullPointerException();
            HashMap<K, V> m = map;
            Node<K, V>[] tab = m.table;
            if ((hi = fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            } else
                mc = expectedModCount;
            if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                Node<K, V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p.value);
                        p = p.next;
                    }
                } while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super V> action) {
            int hi;
            if (action == null)
                throw new NullPointerException();
            Node<K, V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                while (current != null || index < hi) {
                    if (current == null)
                        current = tab[index++];
                    else {
                        V v = current.value;
                        current = current.next;
                        action.accept(v);
                        if (map.modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }

        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0);
        }
    }

    static final class EntrySpliterator<K, V>
            extends HashMapSpliterator<K, V>
            implements Spliterator<Entry<K, V>> {
        EntrySpliterator(HashMap<K, V> m, int origin, int fence, int est,
                         int expectedModCount) {
            super(m, origin, fence, est, expectedModCount);
        }

        public EntrySpliterator<K, V> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid || current != null) ? null :
                    new EntrySpliterator<>(map, lo, index = mid, est >>>= 1,
                            expectedModCount);
        }

        public void forEachRemaining(Consumer<? super Entry<K, V>> action) {
            int i, hi, mc;
            if (action == null)
                throw new NullPointerException();
            HashMap<K, V> m = map;
            Node<K, V>[] tab = m.table;
            if ((hi = fence) < 0) {
                mc = expectedModCount = m.modCount;
                hi = fence = (tab == null) ? 0 : tab.length;
            } else
                mc = expectedModCount;
            if (tab != null && tab.length >= hi &&
                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
                Node<K, V> p = current;
                current = null;
                do {
                    if (p == null)
                        p = tab[i++];
                    else {
                        action.accept(p);
                        p = p.next;
                    }
                } while (p != null || i < hi);
                if (m.modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }

        public boolean tryAdvance(Consumer<? super Entry<K, V>> action) {
            int hi;
            if (action == null)
                throw new NullPointerException();
            Node<K, V>[] tab = map.table;
            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
                while (current != null || index < hi) {
                    if (current == null)
                        current = tab[index++];
                    else {
                        Node<K, V> e = current;
                        current = current.next;
                        action.accept(e);
                        if (map.modCount != expectedModCount)
                            throw new ConcurrentModificationException();
                        return true;
                    }
                }
            }
            return false;
        }

        public int characteristics() {
            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
                    Spliterator.DISTINCT;
        }
    }

    /* ------------------------------------------------------------ */
    // LinkedHashMap support


    /*
     * The following package-protected methods are designed to be
     * overridden by LinkedHashMap, but not by any other subclass.
     * Nearly all other internal methods are also package-protected
     * but are declared final, so can be used by LinkedHashMap, view
     * classes, and HashSet.
     */

    // Create a regular (non-tree) node
    Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

    // For conversion from TreeNodes to plain nodes
    Node<K, V> replacementNode(Node<K, V> p, Node<K, V> next) {
        return new Node<>(p.hash, p.key, p.value, next);
    }

    // Create a tree bin node
    TreeNode<K, V> newTreeNode(int hash, K key, V value, Node<K, V> next) {
        return new TreeNode<>(hash, key, value, next);
    }

    // For treeifyBin
    TreeNode<K, V> replacementTreeNode(Node<K, V> p, Node<K, V> next) {
        return new TreeNode<>(p.hash, p.key, p.value, next);
    }

    /**
     * Reset to initial default state.  Called by clone and readObject.
     */
    void reinitialize() {
        table = null;
        entrySet = null;
        keySet = null;
        values = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }

    // Callbacks to allow LinkedHashMap post-actions
    void afterNodeAccess(Node<K, V> p) {
    }

    void afterNodeInsertion(boolean evict) {
    }

    void afterNodeRemoval(Node<K, V> p) {
    }

    // Called only from writeObject, to ensure compatible ordering.
    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        Node<K, V>[] tab;
        if (size > 0 && (tab = table) != null) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<K, V> e = tab[i]; e != null; e = e.next) {
                    s.writeObject(e.key);
                    s.writeObject(e.value);
                }
            }
        }
    }

    /* ------------------------------------------------------------ */
    // Tree bins

    /**
     * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
     * extends Node) so can be used as extension of either regular or
     * linked node.
     */
    static final class TreeNode<K, V> extends LinkedHashMap.Entry<K, V> {
        TreeNode<K, V> parent;  // red-black tree links
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;    // needed to unlink next upon deletion
        boolean red;

        TreeNode(int hash, K key, V val, Node<K, V> next) {
            super(hash, key, val, next);
        }

        /**
         * Returns root of tree containing this node.
         */
        final TreeNode<K, V> root() {
            for (TreeNode<K, V> r = this, p; ; ) {
                if ((p = r.parent) == null)
                    return r;
                r = p;
            }
        }

        /**
         * Ensures that the given root is the first node of its bin.
         */
        static <K, V> void moveRootToFront(Node<K, V>[] tab, TreeNode<K, V> root) {
            int n;
            if (root != null && tab != null && (n = tab.length) > 0) {
                int index = (n - 1) & root.hash;
                TreeNode<K, V> first = (TreeNode<K, V>) tab[index];
                if (root != first) {
                    Node<K, V> rn;
                    tab[index] = root;
                    TreeNode<K, V> rp = root.prev;
                    if ((rn = root.next) != null)
                        ((TreeNode<K, V>) rn).prev = rp;
                    if (rp != null)
                        rp.next = rn;
                    if (first != null)
                        first.prev = root;
                    root.next = first;
                    root.prev = null;
                }
                assert checkInvariants(root);
            }
        }

        /**
         * Finds the node starting at root p with the given hash and key.
         * The kc argument caches comparableClassFor(key) upon first use
         * comparing keys.
         */
        final TreeNode<K, V> find(int h, Object k, Class<?> kc) {
            TreeNode<K, V> p = this;
            do {
                int ph, dir;
                K pk;
                TreeNode<K, V> pl = p.left, pr = p.right, q;
                if ((ph = p.hash) > h)
                    p = pl;
                else if (ph < h)
                    p = pr;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                else if (pl == null)
                    p = pr;
                else if (pr == null)
                    p = pl;
                else if ((kc != null ||
                        (kc = comparableClassFor(k)) != null) &&
                        (dir = compareComparables(kc, k, pk)) != 0)
                    p = (dir < 0) ? pl : pr;
                else if ((q = pr.find(h, k, kc)) != null)
                    return q;
                else
                    p = pl;
            } while (p != null);
            return null;
        }

        /**
         * Calls find for root node.
         */
        final TreeNode<K, V> getTreeNode(int h, Object k) {
            return ((parent != null) ? root() : this).find(h, k, null);
        }

        /**
         * Tie-breaking utility for ordering insertions when equal
         * hashCodes and non-comparable. We don't require a total
         * order, just a consistent insertion rule to maintain
         * equivalence across rebalancings. Tie-breaking further than
         * necessary simplifies testing a bit.
         */
        static int tieBreakOrder(Object a, Object b) {
            int d;
            if (a == null || b == null ||
                    (d = a.getClass().getName().
                            compareTo(b.getClass().getName())) == 0)
                d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
                        -1 : 1);
            return d;
        }

        /**
         * Forms tree of the nodes linked from this node.
         */
        final void treeify(Node<K, V>[] tab) {
            TreeNode<K, V> root = null;
            for (TreeNode<K, V> x = this, next; x != null; x = next) {
                next = (TreeNode<K, V>) x.next;
                x.left = x.right = null;
                if (root == null) {
                    x.parent = null;
                    x.red = false;
                    root = x;
                } else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    for (TreeNode<K, V> p = root; ; ) {
                        int dir, ph;
                        K pk = p.key;
                        if ((ph = p.hash) > h)
                            dir = -1;
                        else if (ph < h)
                            dir = 1;
                        else if ((kc == null &&
                                (kc = comparableClassFor(k)) == null) ||
                                (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);

                        TreeNode<K, V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            x.parent = xp;
                            if (dir <= 0)
                                xp.left = x;
                            else
                                xp.right = x;
                            root = balanceInsertion(root, x);
                            break;
                        }
                    }
                }
            }
            moveRootToFront(tab, root);
        }

        /**
         * Returns a list of non-TreeNodes replacing those linked from
         * this node.
         */
        final Node<K, V> untreeify(HashMap<K, V> map) {
            Node<K, V> hd = null, tl = null;
            for (Node<K, V> q = this; q != null; q = q.next) {
                Node<K, V> p = map.replacementNode(q, null);
                if (tl == null)
                    hd = p;
                else
                    tl.next = p;
                tl = p;
            }
            return hd;
        }

        /**
         * Tree version of putVal.
         */
        final TreeNode<K, V> putTreeVal(HashMap<K, V> map, Node<K, V>[] tab, int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            TreeNode<K, V> root = (parent != null) ? root() : this;
            for (TreeNode<K, V> p = root; ; ) {
                int dir, ph;
                K pk;
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
                    return p;
                else if ((kc == null && (kc = comparableClassFor(k)) == null) || (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K, V> q, ch;
                        searched = true;
                        if (((ch = p.left) != null && (q = ch.find(h, k, kc)) != null) || ((ch = p.right) != null && (q = ch.find(h, k, kc)) != null))
                            return q;
                    }
                    dir = tieBreakOrder(k, pk);
                }

                TreeNode<K, V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    Node<K, V> xpn = xp.next;
                    TreeNode<K, V> x = map.newTreeNode(h, k, v, xpn);
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    xp.next = x;
                    x.parent = x.prev = xp;
                    if (xpn != null)
                        ((TreeNode<K, V>) xpn).prev = x;
                    moveRootToFront(tab, balanceInsertion(root, x));
                    return null;
                }
            }
        }

        /**
         * Removes the given node, that must be present before this call.
         * This is messier than typical red-black deletion code because we
         * cannot swap the contents of an interior node with a leaf
         * successor that is pinned by "next" pointers that are accessible
         * independently during traversal. So instead we swap the tree
         * linkages. If the current tree appears to have too few nodes,
         * the bin is converted back to a plain bin. (The test triggers
         * somewhere between 2 and 6 nodes, depending on tree structure).
         */
        final void removeTreeNode(HashMap<K, V> map, Node<K, V>[] tab, boolean movable) {
            int n;
            if (tab == null || (n = tab.length) == 0)
                return;

            int index = (n - 1) & hash;
            TreeNode<K, V> first = (TreeNode<K, V>) tab[index], root = first, rl;
            TreeNode<K, V> succ = (TreeNode<K, V>) next, pred = prev;
            if (pred == null)
                tab[index] = first = succ;
            else
                pred.next = succ;
            if (succ != null)
                succ.prev = pred;
            if (first == null)
                return;
            if (root.parent != null)
                root = root.root();

            if (root == null || (movable && (root.right == null || (rl = root.left) == null || rl.left == null))) {
                tab[index] = first.untreeify(map);  // too small
                return;
            }

            TreeNode<K, V> p = this, pl = left, pr = right, replacement;
            if (pl != null && pr != null) {
                TreeNode<K, V> s = pr, sl;
                while ((sl = s.left) != null) // find successor
                    s = sl;
                boolean c = s.red;
                s.red = p.red;
                p.red = c; // swap colors
                TreeNode<K, V> sr = s.right;
                TreeNode<K, V> pp = p.parent;
                if (s == pr) { // p was s's direct parent
                    p.parent = s;
                    s.right = p;
                } else {
                    TreeNode<K, V> sp = s.parent;
                    if ((p.parent = sp) != null) {
                        if (s == sp.left)
                            sp.left = p;
                        else
                            sp.right = p;
                    }
                    if ((s.right = pr) != null)
                        pr.parent = s;
                }
                p.left = null;
                if ((p.right = sr) != null)
                    sr.parent = p;
                if ((s.left = pl) != null)
                    pl.parent = s;
                if ((s.parent = pp) == null)
                    root = s;
                else if (p == pp.left)
                    pp.left = s;
                else
                    pp.right = s;
                if (sr != null)
                    replacement = sr;
                else
                    replacement = p;
            } else if (pl != null)
                replacement = pl;
            else if (pr != null)
                replacement = pr;
            else
                replacement = p;
            if (replacement != p) {
                TreeNode<K, V> pp = replacement.parent = p.parent;
                if (pp == null)
                    root = replacement;
                else if (p == pp.left)
                    pp.left = replacement;
                else
                    pp.right = replacement;
                p.left = p.right = p.parent = null;
            }

            TreeNode<K, V> r = p.red ? root : balanceDeletion(root, replacement);

            if (replacement == p) {  // detach
                TreeNode<K, V> pp = p.parent;
                p.parent = null;
                if (pp != null) {
                    if (p == pp.left)
                        pp.left = null;
                    else if (p == pp.right)
                        pp.right = null;
                }
            }
            if (movable)
                moveRootToFront(tab, r);
        }

        /**
         * Splits nodes in a tree bin into lower and upper tree bins,
         * or untreeifies if now too small. Called only from resize;
         * see above discussion about split bits and indices.
         *
         * @param map   the map
         * @param tab   the table for recording bin heads
         * @param index the index of the table being split
         * @param bit   the bit of hash to split on
         */
        final void split(HashMap<K, V> map, Node<K, V>[] tab, int index, int bit) {
            TreeNode<K, V> b = this;
            // Relink into lo and hi lists, preserving order
            TreeNode<K, V> loHead = null, loTail = null;
            TreeNode<K, V> hiHead = null, hiTail = null;
            int lc = 0, hc = 0;
            for (TreeNode<K, V> e = b, next; e != null; e = next) {
                next = (TreeNode<K, V>) e.next;
                e.next = null;
                if ((e.hash & bit) == 0) {
                    if ((e.prev = loTail) == null)
                        loHead = e;
                    else
                        loTail.next = e;
                    loTail = e;
                    ++lc;
                } else {
                    if ((e.prev = hiTail) == null)
                        hiHead = e;
                    else
                        hiTail.next = e;
                    hiTail = e;
                    ++hc;
                }
            }

            if (loHead != null) {
                if (lc <= UNTREEIFY_THRESHOLD)
                    tab[index] = loHead.untreeify(map);
                else {
                    tab[index] = loHead;
                    if (hiHead != null) // (else is already treeified)
                        loHead.treeify(tab);
                }
            }
            if (hiHead != null) {
                if (hc <= UNTREEIFY_THRESHOLD)
                    tab[index + bit] = hiHead.untreeify(map);
                else {
                    tab[index + bit] = hiHead;
                    if (loHead != null)
                        hiHead.treeify(tab);
                }
            }
        }

        /* ------------------------------------------------------------ */
        // Red-black tree methods, all adapted from CLR

        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root,
                                                TreeNode<K, V> p) {
            TreeNode<K, V> r, pp, rl;
            if (p != null && (r = p.right) != null) {
                if ((rl = p.right = r.left) != null)
                    rl.parent = p;
                if ((pp = r.parent = p.parent) == null)
                    (root = r).red = false;
                else if (pp.left == p)
                    pp.left = r;
                else
                    pp.right = r;
                r.left = p;
                p.parent = r;
            }
            return root;
        }

        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root,
                                                 TreeNode<K, V> p) {
            TreeNode<K, V> l, pp, lr;
            if (p != null && (l = p.left) != null) {
                if ((lr = p.left = l.right) != null)
                    lr.parent = p;
                if ((pp = l.parent = p.parent) == null)
                    (root = l).red = false;
                else if (pp.right == p)
                    pp.right = l;
                else
                    pp.left = l;
                l.right = p;
                p.parent = l;
            }
            return root;
        }

        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root,
                                                      TreeNode<K, V> x) {
            x.red = true;
            for (TreeNode<K, V> xp, xpp, xppl, xppr; ; ) {
                if ((xp = x.parent) == null) {
                    x.red = false;
                    return x;
                } else if (!xp.red || (xpp = xp.parent) == null)
                    return root;
                if (xp == (xppl = xpp.left)) {
                    if ((xppr = xpp.right) != null && xppr.red) {
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    } else {
                        if (x == xp.right) {
                            root = rotateLeft(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                } else {
                    if (xppl != null && xppl.red) {
                        xppl.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    } else {
                        if (x == xp.left) {
                            root = rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }

        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root,
                                                     TreeNode<K, V> x) {
            for (TreeNode<K, V> xp, xpl, xpr; ; ) {
                if (x == null || x == root)
                    return root;
                else if ((xp = x.parent) == null) {
                    x.red = false;
                    return x;
                } else if (x.red) {
                    x.red = false;
                    return root;
                } else if ((xpl = xp.left) == x) {
                    if ((xpr = xp.right) != null && xpr.red) {
                        xpr.red = false;
                        xp.red = true;
                        root = rotateLeft(root, xp);
                        xpr = (xp = x.parent) == null ? null : xp.right;
                    }
                    if (xpr == null)
                        x = xp;
                    else {
                        TreeNode<K, V> sl = xpr.left, sr = xpr.right;
                        if ((sr == null || !sr.red) &&
                                (sl == null || !sl.red)) {
                            xpr.red = true;
                            x = xp;
                        } else {
                            if (sr == null || !sr.red) {
                                if (sl != null)
                                    sl.red = false;
                                xpr.red = true;
                                root = rotateRight(root, xpr);
                                xpr = (xp = x.parent) == null ?
                                        null : xp.right;
                            }
                            if (xpr != null) {
                                xpr.red = (xp == null) ? false : xp.red;
                                if ((sr = xpr.right) != null)
                                    sr.red = false;
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateLeft(root, xp);
                            }
                            x = root;
                        }
                    }
                } else { // symmetric
                    if (xpl != null && xpl.red) {
                        xpl.red = false;
                        xp.red = true;
                        root = rotateRight(root, xp);
                        xpl = (xp = x.parent) == null ? null : xp.left;
                    }
                    if (xpl == null)
                        x = xp;
                    else {
                        TreeNode<K, V> sl = xpl.left, sr = xpl.right;
                        if ((sl == null || !sl.red) &&
                                (sr == null || !sr.red)) {
                            xpl.red = true;
                            x = xp;
                        } else {
                            if (sl == null || !sl.red) {
                                if (sr != null)
                                    sr.red = false;
                                xpl.red = true;
                                root = rotateLeft(root, xpl);
                                xpl = (xp = x.parent) == null ?
                                        null : xp.left;
                            }
                            if (xpl != null) {
                                xpl.red = (xp == null) ? false : xp.red;
                                if ((sl = xpl.left) != null)
                                    sl.red = false;
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateRight(root, xp);
                            }
                            x = root;
                        }
                    }
                }
            }
        }

        /**
         * Recursive invariant check
         */
        static <K, V> boolean checkInvariants(TreeNode<K, V> t) {
            TreeNode<K, V> tp = t.parent, tl = t.left, tr = t.right,
                    tb = t.prev, tn = (TreeNode<K, V>) t.next;
            if (tb != null && tb.next != t)
                return false;
            if (tn != null && tn.prev != t)
                return false;
            if (tp != null && t != tp.left && t != tp.right)
                return false;
            if (tl != null && (tl.parent != t || tl.hash > t.hash))
                return false;
            if (tr != null && (tr.parent != t || tr.hash < t.hash))
                return false;
            if (t.red && tl != null && tl.red && tr != null && tr.red)
                return false;
            if (tl != null && !checkInvariants(tl))
                return false;
            if (tr != null && !checkInvariants(tr))
                return false;
            return true;
        }
    }

}
