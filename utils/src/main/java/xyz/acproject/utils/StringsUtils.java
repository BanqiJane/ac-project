package xyz.acproject.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jane
 * @ClassName StringsUtils
 * @Description TODO
 * @date 2022/4/25 12:28
 * @Copyright:2022
 */
public class StringsUtils {


    public static String appendAll(Object... strings) {
        StringBuilder sb = new StringBuilder();
        for (Object s : strings) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static String startWithAndAppend(String s,String start){
        if(StringUtils.startsWith(s, start)){
            return s;
        }else{
            return appendAll(start,s);
        }
    }

    public static String endWithAndAppend(String s,String end){
        if(StringUtils.endsWith(s, end)){
            return s;
        }else{
            return appendAll(s,end);
        }
    }

    public static String startEndWithAppendAny(String s,String se){
        return startWithAndAppend(endWithAndAppend(s,se),se);
    }

    public static String startEndWithAppendSome(String s,String se,Boolean isStart){
        boolean hasStart = false;
        boolean hasEnd = false;
        if(StringUtils.startsWith(s, se)) hasStart = true;
        if(StringUtils.endsWith(s, se)) hasEnd = true;
        if(hasStart||hasEnd){
            return s;
        }else{
            if(isStart==null){
                return startEndWithAppendAny(s,se);
            }else {
                if (isStart) {
                    return startWithAndAppend(s, se);
                } else {
                    return endWithAndAppend(s, se);
                }
            }
        }
    }
}
