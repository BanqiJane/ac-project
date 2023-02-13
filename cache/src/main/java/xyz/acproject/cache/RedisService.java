package xyz.acproject.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jane
 * @ClassName RedisService
 * @Description TODO
 * @date 2021/2/2 0:11
 * @Copyright:2021
 * @DOC  https://docs.spring.io/spring-data/redis/docs/2.5.x/api/
 */
public interface RedisService {
    //====================================================custom====================================================//
    void flushDb();
    Set<String> scan(String pattern);
    //====================================================common====================================================//
    <T> T get(Object key);
    boolean set(Object key,Object value);
    boolean set(Object key,Object value,long seconds);
    boolean deletes(Object... key);
    boolean deletesLike(String... key);
    boolean hasKey(Object key);
    boolean expire(Object key,long seconds);
    long getExpire(Object key);
    void rename(Object oldKey,Object newKey);
    long incr(Object key, long num);
    long decr(Object key, long num);

    //====================================================list====================================================//

    <T> List<T> lGet(Object key,long start,long end);

    long lSize(Object key);

    boolean lbPush(Object key,Object value);

    boolean lbPush(Object key,Object value,long seconds);

    boolean laPush(Object key,Object value);

    boolean laPush(Object key,Object value,long seconds);

    <T> boolean lbPushs(Object key,List<T> values);

    <T> boolean lbPushs(Object key,List<T> values,long seconds);

    <T> boolean laPushs(Object key,List<T> values);


    /**
     * 通常使用的集合插入
     *
     * @param key     关键
     * @param values  值
     * @param seconds 秒
     * @return boolean
     */
    <T> boolean laPushs(Object key,List<T> values,long seconds);

    long lRemove(Object key,long num,Object value);

    <T> T  lGetIndex(Object key, long index);

    boolean lUpdateIndex(Object key, long index, Object value);

    <T> T  lbPop(Object key);

    <T> T lbPop(Object key,long seconds);

    <T> T  laPop(Object key);

    <T> T laPop(Object key,long seconds);

    <T> T  lbPopToAPush(Object sourceKey,Object toKey,Class<?> clazz);

    <T> T lbPopToAPush(Object sourceKey, Object toKey, Class<?> clazz,long seconds);

    <T> T  laPopToBPush(Object sourceKey,Object toKey);

    <T> T laPopToBPush(Object sourceKey, Object toKey,long seconds);

    long lbIndexOf(Object key,Object value);

    long laIndexOf(Object key,Object value);

    //====================================================hashmap====================================================//

    <T> T hget(Object key,Object hKey);

    <T> Set<T> hgetKeys(Object key);

    <T> List<T> hgets(Object key, Collection<?> hKeys);

    <T,V> Map<T,V> hmget(Object key);

    boolean hmPut(Object key,Object hKey,Object hValue);

    boolean hmPut(Object key,Object hKey,Object hValue,long seconds);

    boolean hmPuts(Object key,Map<?,?> map);

    boolean hmPuts(Object key,Map<?,?> map,long seconds);

    long hRemoves(Object key,Object... hKey);

    boolean hHasHkey(Object key,Object hKey);

    long hincr(Object key,Object hKey,long num);

    long hdecr(Object key,Object hKey,long num);

    long hsize(Object key);

    <T> List<T> htolist(Object key);

    long hlentoValue(Object key,Object hKey);

    <T,V> Map<T,V> hmScan(Object key,String hKey,long num);

    //====================================================set====================================================//

    <T> Set<T> sgets(Object key);

    boolean shasValue(Object key,Object value);

    long sPuts(Object key,Object... values);

    long sPuts(Object key,long seconds,Object... values);

    <T> T sPop(Object key);

    long sSize(Object key);

    long sRemoves(Object key,Object... values);

    <T> Set<T> sDiffs(Object key,Collection<?> keys);

    <T> Set<T> sIntersects(Object key,Collection<?> keys);

    <T> Set<T> sUnions(Object key,Collection<?> keys);

    <T> Set<T> sUnion(Object key,Object toKey);

    <T> Set<T> sDiff(Object key,Object toKey);

    <T> Set<T> sIntersect(Object key,Object toKey);


    <T> List<T> sRandoms(Object key,long num);

    Object sRandom(Object key);

    <T> Set<T> sDisRandoms(Object key,long num);

    <T> Set<T> sScan(Object key,String value,long num);

    //====================================================zset====================================================//

    boolean zsPut(Object key,Object value,double score);

    Double zsScore(Object key,Object value);

    long zsRank(Object key,Object value);

    long zsDESCRank(Object key,Object value);

    <T> Set<T> zsgets(Object key,long start,long end);

    <T> Set<T> zsDESCgets(Object key,double min,double max);

    long zsRemoves(Object key,Object... values);

    long zsRemoves(Object key,long start,long end);

    long zsRemoves(Object key,double min,double max);

    <T> Set<T> zsDESCgets(Object key,long start,long end);

    long zsCount(Object key);

    long zsCount(Object key,double min,double max);

    Double zsIncrScore(Object key,Object value,double score);

    long zsSize(Object key);

    long zsUnion(Object key,Object toKey,Object resultKey);

    long zsUnions(Object key,Collection<?> keys,Object resultKey);

    long zsIntersect(Object key,Object toKey,Object resultKey);

    long zsIntersects(Object key,Collection<?> keys,Object resultKey);

}
