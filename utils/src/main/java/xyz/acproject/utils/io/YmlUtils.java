package xyz.acproject.utils.io;

import org.apache.commons.lang3.StringUtils;
import xyz.acproject.utils.SpringUtils;

/**
 * @author Jane
 * @ClassName YmlUtils
 * @Description TODO
 * @date 2021/10/22 11:14
 * @Copyright:2021
 */
public class YmlUtils {



    public static <T> T get(String url,T default_Return){
        String key = SpringUtils.getConfigByKey(url);
        if(StringUtils.isBlank(key)){
            return default_Return;
        }
        return (T)key;
    }


}
