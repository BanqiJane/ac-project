package xyz.acproject.utils.random;

import java.util.*;

/**
 * @author Jane
 * @ClassName RandomArrayUtils
 * @Description from https://blog.csdn.net/xipiyou/article/details/100086858?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2~default~CTRLIST~default-1.essearch_pc_relevant&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2~default~CTRLIST~default-1.essearch_pc_relevant
 * @date 2022/3/23 15:08
 * @Copyright:2022
 */
public class RandomArrayUtils {
    private static Random r;

    /**
     * 获取随机子列表
     * @param source 原列表
     * @param limit 子列表长度
     * @param <T> 列表原类型
     * @return 子列表
     */
    public static <T> List<T> newRandomList(List<T> source, int limit) {
        if(source == null || source.size() == 0 || source.size() <= limit){
            return source;
        }

        Set<Integer> set = createRandomSet(source.size(), limit);
        Integer[] array = set.toArray(new Integer[0]);
        return new RandomList<>(source, array);
    }

    /**
     * 创建一个随机的有序下标Set
     * @param listSize 原列表长度
     * @param limit 子列表长度
     * @return 随机的下标Set
     */
    private static Set<Integer> createRandomSet(int listSize, int limit) {
        Random rnd = r;
        if (rnd == null)
            r = rnd = new Random();

        Set<Integer> set = new HashSet<>(limit);
        for (int i = 0; i < limit; i++) {
            int value = rnd.nextInt(listSize);
            if (!add(set, value, listSize)) {
                return set;
            }
        }
        return set;
    }

    /**
     * 往Set中添加一个随机值，如果有冲突，则取随机值+1
     */
    private static boolean add(Set<Integer> set, int value, int size) {
        if (set.size() == size) {
            return false;
        }

        if (!set.contains(value)) {
            return set.add(value);
        }

        int nextValue = value + 1;
        if (nextValue == size) {
            nextValue = 0;
        }
        return add(set, nextValue, size);
    }

    private static class RandomList<T> extends AbstractList<T> {
        final List<T> list;
        final Integer[] indexs;

        RandomList(List<T> list, Integer[] indexs) {
            this.list = list;
            this.indexs = indexs;
        }

        @Override
        public T get(int index) {
            if(index < 0 || index >= indexs.length)
                throw new IndexOutOfBoundsException("The start index was out of bounds: "
                        + index + " >= " + indexs.length);
            int start = indexs[index];
            return list.get(start);
        }

        @Override
        public int size() {
            return indexs.length;
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }
    }
}
