package xyz.acproject.utils.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jane
 * @ClassName ListUtils
 * @Description TODO
 * @date 2022/5/12 16:55
 * @Copyright:2022
 */
public class ListUtils {


    public static <T> List<T> merge(T... t) {
        List<T> list = new ArrayList<>();
        for (T t1 : t) {
            try {
                list.add(t1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static <T> List<T> merge(T[]... ts){
        List<T> list = new ArrayList<>();
        list.addAll(Arrays.stream(ts).flatMap(Arrays::stream).collect(Collectors.toList()));
        return list;
    }


    public static <T> List<T> merge(List<T>... ts){
        List<T> list = new ArrayList<>();
        for(List<T> t : ts){
            list.addAll(t);
        }
        return list;
    }
}
