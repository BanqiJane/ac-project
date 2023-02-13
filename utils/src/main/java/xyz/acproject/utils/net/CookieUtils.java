package xyz.acproject.utils.net;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import xyz.acproject.lang.exception.DataException;

import java.util.*;

/**
 * @author Jane
 * @ClassName CookieUtils
 * @Description TODO
 * @date 2021/3/6 16:20
 * @Copyright:2021
 */
public class CookieUtils {


    public static String generate(Map<String,String> cookieMap){
        if(CollectionUtils.isEmpty(cookieMap)){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String,String> entry:cookieMap.entrySet()){
            if(!entry.getKey().equals(entry.getValue())) {
                stringBuilder.append(entry.getKey());
                stringBuilder.append("=");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("; ");
            }else{
                stringBuilder.append(entry.getKey());
                stringBuilder.append("; ");
            }
        }
        stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length());
        return stringBuilder.toString();
    }

    public static Map<String,String> parse(String cookie){
        if(StringUtils.isBlank(cookie)){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        String[] cookies = cookie.split("; ");
        for(String c:cookies){
            if(c.indexOf("=")>0){
                String[] child = c.split("=");
                map.put(child[0],child[1]);
            }else{
                map.put(c,c);
            }
        }
        return map;
    }

    public static Map<String,String> parse(List<String> cookies,String key) {
        if(CollectionUtils.isEmpty(cookies)){
            return null;
        }
        List<String> cookiesOuts = new ArrayList<>();
        cookiesOuts.addAll(cookies);
        if(StringUtils.isNotBlank(key)){
            for(Iterator<String> iterator=cookiesOuts.iterator();iterator.hasNext();){
                String cookie = iterator.next();
                if(!cookie.contains(key)){
                    iterator.remove();
                }
            }
        }
        Map<String,String> map = new HashMap<>();
        for(Iterator<String> iterator=cookies.iterator();iterator.hasNext();){
            String cookie = iterator.next();
            String[] cookieIns = cookie.split("; ");
            for(String c:cookieIns){
                if(c.indexOf("=")>0){
                    String[] child = c.split("=");
                    map.put(child[0],child[1]);
                }else{
                    map.put(c,c);
                }
            }
        }
        return map;
    }
}
