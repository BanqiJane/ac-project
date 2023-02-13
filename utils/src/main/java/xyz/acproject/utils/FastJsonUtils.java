package xyz.acproject.utils;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.apache.commons.lang3.StringUtils;

/**
 * 依赖于fastjson包
 *
 * @author zjian
 * @version fastjsonTools v1.0
 */
public class FastJsonUtils {
    private static final SerializeConfig CONFIG = new SerializeConfig();

    private static final String FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";

    @SuppressWarnings("unused")
    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final SerializerFeature[] FEATURES = {

            SerializerFeature.WriteMapNullValue,

            SerializerFeature.WriteDateUseDateFormat,

            SerializerFeature.WriteNullListAsEmpty

    };


//	WriteMapNullValue

//	WriteDateUseDateFormat

    static {

        CONFIG.put(Date.class, new SimpleDateFormatSerializer(FORMAT_TIME));

    }


    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }

    public static boolean isHasJsonObj(String str) {
        return StringUtils.isBlank(str) ? false : StringUtils.containsAny(str.trim(), '{', '}');
    }

    public static boolean isJsonObj(String str) {
        return StringUtils.isBlank(str) ? false : StringUtils.startsWith(str.trim(),"{")&&StringUtils.endsWith(str.trim(),"}");
    }

    public static boolean isHasJsonArray(String str) {
        return StringUtils.isBlank(str) ? false : StringUtils.containsAny(str.trim(), '[', ']');
    }

    public static boolean isJsonArray(String str) {
        return StringUtils.isBlank(str) ? false : StringUtils.startsWith(str.trim(),"[")&&StringUtils.endsWith(str.trim(),"]");
    }



    public static <T> T parseObject(String json, Class<T> clazz) {

        try {

            T t = JSON.parseObject(json, clazz);

            return t;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static <T> List<T> parseList(String json, Class<T> clazz) {

        try {

            List<T> list = JSON.parseArray(json, clazz);

            return list;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    /**
     * 某人转为yyyy-MM-dd HH:mm:ss格式
     *
     * @param object
     * @return
     */

    public static String toJson(Object object) {

        try {

            String json = JSON.toJSONString(object, CONFIG, FEATURES);

            return json;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static JSONObject getAsJSONObjectFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getJSONObject(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static JSONArray getAsJSONArrayFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getJSONArray(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static String getAsStringFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getString(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static Integer getAsIntegerFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getInteger(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static Long getAsLongFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getLong(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static Double getAsDoubleFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getDouble(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static Boolean getAsBooleanFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getBoolean(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static Short getAsShortFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getShort(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static Byte getAsByteFromObject(String json, String key) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getByte(key);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static <T> T getAsObjectFromObject(String json, String key, Class<T> clazz) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            return jsonObject.getObject(key, clazz);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static <T> List<T> getAsListFromObject(String json, String key, Class<T> clazz) {

        try {

            JSONObject jsonObject = JSON.parseObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray(key);

            return jsonArray.toJavaList(clazz);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static <T> T toJavaObject(JSONObject jsonObject, Class<T> clazz) {

        try {

            T t = jsonObject.toJavaObject(clazz);

            return t;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static <T> List<T> toJavaList(JSONArray jsonArray, Class<T> clazz) {

        try {

            List<T> list = jsonArray.toJavaList(clazz);

            return list;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static JSONObject parseJSONObject(Object object) {

        try {

            return JSON.parseObject(toJson(object));

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    public static JSONArray parseJSONArray(Object object) {

        try {

            return JSON.parseArray(toJson(object));

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    //解决泛型问题 不能强制转换的方法

    public static <T> T generateAssignObject(Object source, Class<T> clazz) {

        try {

            T t = toJavaObject(parseJSONObject(source), clazz);

            return t;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    //解决泛型问题 不能强制转换的方法

    public static <T> List<T> generateAssignList(Object source, Class<T> clazz) {

        try {

            List<T> list = toJavaList(parseJSONArray(source), clazz);

            return list;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    //map转java对象
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        String jsonStr = JSONObject.toJSONString(map);
        return JSONObject.parseObject(jsonStr, beanClass);
    }

    //java对象转map
    public static Map<String, Object> objectToMap(Object obj) {
        if(obj==null)return new HashMap<>();
        String jsonStr = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(jsonStr);
    }


    /**
     * 获得列
     *
     * @param json json
     * @param col 关口
     * @return {@link String}
     */
    public static  <T> T getValue(String json, Class<T> clazz, String col) {
        if (StringUtils.isBlank(col)) {
            return null;
        }
        try {
            String[] values = col.split("\\.");
            JSONObject jsonObject = JSON.parseObject(json);
            int size = values.length;
            for (String val : values) {
                if (size > 1) {
                    jsonObject = (JSONObject) jsonObject.get(val);
                } else {
                    return jsonObject.getObject(val, clazz);
                }
                size--;
            }
        }catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据key获得对应的json的value 返回 hashmap 暂不支持 数组
     *
     * @param json json
     * @param cols 关口
     * @return {@link Map<String, Object>}
     */
    public static Map<String, Object> getValueMaps(String json, String... cols) {
        int len = cols.length;
        Map<String, Object> valueMap = new Hashtable<>();
        String[][] keyWord = new String[len][];
        for (int i = 0; i < len; i++) {
            keyWord[i] = cols[i].split("\\.");
        }
        for (int i = 0; i < len; i++) {
            try {
                JSONObject jsonObject = JSON.parseObject(json);
                for (int j = 0; j < keyWord[i].length - 1; j++) {
                    jsonObject = (JSONObject) jsonObject.get(keyWord[i][j]);
                }
                valueMap.put(keyWord[i][keyWord[i].length - 1], jsonObject.get(keyWord[i][keyWord[i].length - 1]));
                jsonObject = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return valueMap;
    }


    public static  <T> List<T> getLists(String json, Class<T> clazz, String col) {
        if (StringUtils.isBlank(col)) {
            return null;
        }
        try {
            String[] values = col.split("\\.");
            JSONObject jsonObject = JSON.parseObject(json);
            int size = values.length;
            for (String val : values) {
                if (size > 1) {
                    jsonObject = (JSONObject) jsonObject.get(val);
                } else {
                    return jsonObject.getJSONArray(val).toJavaList(clazz);
                }
                size--;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

/*
    */
/**
     * {
     *     "result":{ "msg":"",
     *       "list":[
     *          {"user":"123","pass":"111"}
     *          {"user":"456","pass":"222"}
     *        ]
     *     }
     * }
     *
     * 对于上面的json，，arrayPath为“result.list”,cols可以为user,pass
     * 最终返回的是user对应的值，或pass的值，cols对应的值
     *
     * @param json
     * @param arrayPath JSONArray的路径
     * @param cols JSONArray中对象的一些成员
     * @return JSONArray中对象的一些成员的value值
     *//*

    public String getColumns(String json, String arrayPath, String... cols) {
        int len = cols.length;
        String[] keyWord = arrayPath.split("\\.");
        StringBuffer colVals = new StringBuffer();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = null;
        for (int i = 0; i < keyWord.length - 1; i++) {
            jsonObject = (JSONObject) jsonObject.get(keyWord[i]);
        }
        jsonArray = (JSONArray) jsonObject.getJSONArray(keyWord[keyWord.length - 1]);//得到对应的JSONArray对象
        for (int j = 0; j < jsonArray.size(); j++) {
            JSONObject jj = JSON.parseObject(jsonArray.get(j).toString());
            for (int i = 0; i < cols.length; i++) {
                if (0 == i)
                    colVals.append(jj.get(cols[i]).toString());
                else
                    colVals.append("," + jj.get(cols[i]).toString());
            }
        }
        return colVals.toString();
    }
*/


}

