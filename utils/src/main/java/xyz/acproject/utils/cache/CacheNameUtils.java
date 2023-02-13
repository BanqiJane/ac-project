package xyz.acproject.utils.cache;

import org.apache.commons.lang3.StringUtils;
import xyz.acproject.utils.SpringUtils;

public class CacheNameUtils {

    public static String getKey(Class clz, String id) {
        return String.format("%s_%s_%s",getName(), clz.getName(), id);
    }

    public static String getKey(Class clz, long id) {
        return String.format("%s_%s_%d",getName(), clz.getName(), id);
    }

    public static String getKey(Class clz, int id) {
        return String.format("%s_%s_%d",getName(), clz.getName(), id);
    }

    public static String getKey(Class clz, int id,String suffix) {
        return String.format("%s_%s_%d_%s",getName(), clz.getName(), id,suffix);
    }

    public static String getKey(Class clz, String id,String suffix) {
        return String.format("%s_%s_%s_%s",getName(), clz.getName(), id,suffix);
    }
    public static String getKey(Class clz) {
        return String.format("%s_%s",getName(), clz.getName());
    }

    public static String getOneToManeyKey(Class one, Class many, long id) {
        return String.format("%s_%s_to_%s_%d",getName(), one.getName(), many.getName(), id);
    }
    public static String getManeyKey(Class one,long id,Class many,long twoId,String suffix) {
        return String.format("%s_%s_%d_to_%s_%d_%s",getName(), one.getName(), id,many.getName(),twoId,suffix);
    }


    public static String getOneToManeyKey(Class one, Class many, String id) {
        return String.format("%s_%s_to_%s_%s",getName() ,one.getName(), many.getName(), id);
    }


    private static String getName(){
        String key = SpringUtils.getConfigByKey("acproject.cache.key.name");
        if(StringUtils.isBlank(key)){
            return "";
        }
        return key;
    }
}
