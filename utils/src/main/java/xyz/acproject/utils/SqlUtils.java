package xyz.acproject.utils;

/**
 * @author Admin
 * @ClassName SqlUtils
 * @Description TODO
 * @date 2023/1/12 17:19
 * @Copyright:2023
 */
public class SqlUtils {

    public static String findInSetParse(Object value,String sql_value){
        return StringsUtils.appendAll("FIND_IN_SET(",value,",substring(",sql_value,",2,LENGTH(",sql_value,")-2))");
    }
}
