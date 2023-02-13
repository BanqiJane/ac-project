package xyz.acproject.utils;

/**
 * @author Jane
 * @ClassName NumberUtils
 * @Description TODO
 * @date 2022/5/7 10:19
 * @Copyright:2022
 */
public class NumberUtils {

    public static boolean notNullAndGt(Object obj, int num) {
        if (null==obj) {
            return false;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj) > num;
        }else if (obj instanceof Double) {
            return ((Double) obj) > num;
        }else if (obj instanceof Float) {
            return ((Float) obj) > num;
        }else if (obj instanceof Long) {
            return ((Long) obj) > num;
        }else if (obj instanceof Short) {
            return ((Short) obj) > num;
        }else if (obj instanceof Byte) {
            return ((Byte) obj) > num;
        }else if (obj instanceof Character) {
            return ((Character) obj) > num;
        }else if (obj instanceof String) {
            return ((String) obj).length() > num;
        }
        return false;
    }


    public static boolean notNullAndGe(Object obj, int num) {
        if (null==obj) {
            return false;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj) >= num;
        }else if (obj instanceof Double) {
            return ((Double) obj) >= num;
        }else if (obj instanceof Float) {
            return ((Float) obj) >= num;
        }else if (obj instanceof Long) {
            return ((Long) obj) >= num;
        }else if (obj instanceof Short) {
            return ((Short) obj) >= num;
        }else if (obj instanceof Byte) {
            return ((Byte) obj) >= num;
        }else if (obj instanceof Character) {
            return ((Character) obj) >= num;
        }else if (obj instanceof String) {
            return ((String) obj).length() >= num;
        }
        return false;
    }


    public static boolean notNullAndLt(Object obj, int num) {
        if (null==obj) {
            return false;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj) < num;
        }else if (obj instanceof Double) {
            return ((Double) obj) < num;
        }else if (obj instanceof Float) {
            return ((Float) obj) < num;
        }else if (obj instanceof Long) {
            return ((Long) obj) < num;
        }else if (obj instanceof Short) {
            return ((Short) obj) < num;
        }else if (obj instanceof Byte) {
            return ((Byte) obj) < num;
        }else if (obj instanceof Character) {
            return ((Character) obj) < num;
        }else if (obj instanceof String) {
            return ((String) obj).length() < num;
        }
        return false;
    }

    public static boolean notNullAndLe(Object obj, int num) {
        if (null==obj) {
            return false;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj) <= num;
        }else if (obj instanceof Double) {
            return ((Double) obj) <= num;
        }else if (obj instanceof Float) {
            return ((Float) obj) <= num;
        }else if (obj instanceof Long) {
            return ((Long) obj) <= num;
        }else if (obj instanceof Short) {
            return ((Short) obj) <= num;
        }else if (obj instanceof Byte) {
            return ((Byte) obj) <= num;
        }else if (obj instanceof Character) {
            return ((Character) obj) <= num;
        }else if (obj instanceof String) {
            return ((String) obj).length() <= num;
        }
        return false;
    }
}
