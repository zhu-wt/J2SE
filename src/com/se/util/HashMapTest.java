package com.se.util;

import org.junit.Test;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HashMap输出是无序的，这个无序不是说每次遍历的结果顺序不一样，而是说与插入顺序不一样。
 * <p>
 * a.遍历
 * <p>
 * b.按键排序
 * <p>
 * c.value去重
 * 对于HashMap而言，它的key是不能重复的，但是它的value是可以重复的，有的时候我们要将重复的部分剔除掉。
 * 方法一：将HashMap的key-value对调，然后赋值给一个新的HashMap，由于key的不可重复性，此时就将重复值去掉了。最后将新得到的HashMap的key-value再对调一次即可。
 * <p>
 * d.HashMap线程同步
 * 第一种：Map<Integer , String> hs = new HashMap<Integer , String>();
 * hs = Collections.synchronizedMap(hs);
 * 第二种：ConcurrentHashMap<Integer , String> hs = new ConcurrentHashMap<Integer , String>();
 *
 * @author Administrator
 */
public class HashMapTest {
    private String A;
    private int value;


    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("语文", 1);
        map.put("数学", 2);
        map.put("英语", 3);
        map.put("历史", 4);
        map.put("政治", 5);
        map.put("地理", 6);
        map.put("生物", 7);
        map.put("化学", 8);

        test(map);

        sortHashMapByKey(map);
    }

    public static void test(Map<String, Integer> map) {

        /****entrySet只是遍历了一次就把key和value都放到了entry中，效率更高***/
        for (Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        /************/
        Set<String> keySet = map.keySet();
        for (String str : keySet) {
            System.out.println("key="+str+", hashcode="+str.hashCode() + "; index="+(str.hashCode()&15));
        }
    }

    /**
     * b.按键排序
     * HashMap按键排序要比按值排序方法容易实现，而且方法很多，下面一一介绍。
     * 第一种：还是熟悉的配方还是熟悉的味道，用Collections的sort方法，只是更改一下比较规则。
     * 第二种：TreeMap是按键排序的，默认升序，所以可以通过TreeMap来实现。
     */
    public static void sortHashMapByKey(Map<String, Integer> hashmap) {
        System.out.println("按键排序后");
        //第一步：先创建一个TreeMap实例，构造函数传入一个Comparator对象。
        TreeMap<String, Integer> treeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // TODO Auto-generated method stub
                return o1.compareTo(o2);
            }
        });

        //第二步：将要排序的HashMap添加到我们构造的TreeMap中。
        treeMap.putAll(hashmap);
        for (Entry<String, Integer> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

    }

    @Override
    public boolean equals(Object obj) {

        // 如果为同一对象的不同引用,则相同
        if (this == obj) return true;

        // 如果传入的对象为空,则返回false
        if (obj == null) return false;

        // 如果两者属于不同的类型,不能相等
        if (getClass() != obj.getClass()) return false;

        // 类型相同, 比较内容是否相同(成员)
        HashMapTest sd = (HashMapTest) obj;
        return Objects.equals(A, sd.A) && value == sd.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(A, value);
    }

    /**
     * hashMap数据结构：jdk1.7（数组+链表）；jdk1.8(数组+链表+红黑树)[当链表元素大于阈值8时，链表转化为红黑树]
     *
     *hashMap和hashTable和concurrentHashMap区别
     * 1. 线程安全性：
     * hashMap非线程安全，hashTable和concurrentHashMap是线程安全的。hashTable是通过synchronize；concurrentHashMap是通过分段锁(segments)
     * 2.可以和value是否可null
     * hashTable的value为null时抛出空指针；concurrentHashMap的key或value为null时抛出空指针
     */
    public void test(){

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(null,null);

        Hashtable<Object, Object> hashtable = new Hashtable<>();
        hashtable.put(null,null);

        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>();

        concurrentHashMap.put(null,null);
    }

}
