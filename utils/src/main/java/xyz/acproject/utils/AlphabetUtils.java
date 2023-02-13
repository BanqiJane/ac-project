package xyz.acproject.utils;

/**
 * @author Jane
 * @ClassName AlphabetUtils
 * @Description 字母类
 * @date 2021/6/9 16:41
 * @Copyright:2021
 */
public class AlphabetUtils {

    private AlphabetUtils() {
    }

    public static String lowerFirst(String key){
        char[] chars = key.toCharArray();
        chars[0] +=32;
        return String.valueOf(chars);
    }
    public static String upperFirst(String key){
        char[] chars = key.toCharArray();
        chars[0] -=32;
        return String.valueOf(chars);
    }
    //首字母转小写
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return lowerFirst(s);
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return upperFirst(s);
    }
}
