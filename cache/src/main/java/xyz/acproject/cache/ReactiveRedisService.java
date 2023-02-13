package xyz.acproject.cache;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jane
 * @ClassName ReactiveRedisService
 * @Description TODO
 * @date 2022/6/20 17:13
 * @Copyright:2022
 */
public interface ReactiveRedisService {
    //====================================================custom====================================================//
    Mono<String> flushDb();
    Flux<String>  scan(String pattern);
    //====================================================common====================================================//
    <T> Mono<T> get(Object key);
    Mono<Boolean> set(Object key,Object value);
    Mono<Boolean> set(Object key,Object value,long seconds);
    Mono<Long> deletes(Object... key);
    Mono<Boolean> deletesLike(String... key);
    Mono<Boolean> hasKey(Object key);
    Mono<Boolean> expire(Object key,long seconds);
    Mono<Long> getExpire(Object key);
    Mono<Boolean> rename(Object oldKey,Object newKey);
    Mono<Long> incr(Object key, long num);
    Mono<Long> decr(Object key, long num);

    //====================================================list====================================================//

    <T> Flux<T> lGet(Object key, long start, long end);

    Mono<Long> lSize(Object key);

    Mono<Boolean> lbPush(Object key,Object value);

    Mono<Boolean> lbPush(Object key,Object value,long seconds);

    Mono<Boolean> laPush(Object key,Object value);

    Mono<Boolean> laPush(Object key,Object value,long seconds);

    <T> Mono<Boolean> lbPushs(Object key,List<T> values);

    <T> Mono<Boolean> lbPushs(Object key,List<T> values,long seconds);

    <T> Mono<Boolean> laPushs(Object key,List<T> values);


    /**
     * 通常使用的集合插入
     *
     * @param key     关键
     * @param values  值
     * @param seconds 秒
     * @return boolean
     */
    <T> Mono<Boolean> laPushs(Object key,List<T> values,long seconds);

    Mono<Long> lRemove(Object key,long num,Object value);

    <T> Mono<T> lGetIndex(Object key, long index);

    Mono<Boolean> lUpdateIndex(Object key, long index, Object value);

    <T> Mono<T> lbPop(Object key);

    <T> Mono<T> lbPop(Object key,long seconds);

    <T> Mono<T>  laPop(Object key);

    <T> Mono<T> laPop(Object key,long seconds);

    <T> Mono<T> lbPopToAPush(Object sourceKey,Object toKey,Class<?> clazz);

    <T> Mono<T> lbPopToAPush(Object sourceKey, Object toKey, Class<?> clazz,long seconds);

    <T> Mono<T> laPopToBPush(Object sourceKey,Object toKey);

    <T> Mono<T> laPopToBPush(Object sourceKey, Object toKey,long seconds);

    Mono<Long> lbIndexOf(Object key,Object value);

    Mono<Long> laIndexOf(Object key,Object value);

    //====================================================hashmap====================================================//

    <T> Mono<T> hget(Object key,Object hKey);

    <T> Flux<T> hgetKeys(Object key);

    <T> Mono<List<T>> hgets(Object key, Collection<?> hKeys);

    <T,V> Flux<Map<T,V>> hmget(Object key);

    Mono<Boolean> hmPut(Object key,Object hKey,Object hValue);

    Mono<Boolean> hmPut(Object key,Object hKey,Object hValue,long seconds);

    Mono<Boolean> hmPuts(Object key,Map<?,?> map);

    Mono<Boolean> hmPuts(Object key,Map<?,?> map,long seconds);

    Mono<Long> hRemoves(Object key,Object... hKey);

    Mono<Boolean> hHasHkey(Object key,Object hKey);

    Mono<Long> hincr(Object key,Object hKey,long num);

    Mono<Long> hdecr(Object key,Object hKey,long num);

    Mono<Long> hsize(Object key);

    <T> Flux<T> htolist(Object key);

    Mono<Long> hlentoValue(Object key,Object hKey);

    <T,V> Flux<Map<T,V>> hmScan(Object key,String hKey,long num);

    //====================================================set====================================================//

    <T> Flux<T> sgets(Object key);

    Mono<Boolean> shasValue(Object key,Object value);

    Mono<Long> sPuts(Object key,Object... values);

    Mono<Long> sPuts(Object key,long seconds,Object... values);

    Mono<Long> sSize(Object key);

    Mono<Long> sRemoves(Object key,Object... values);

    <T> Flux<T> sDiffs(Object key,Collection<?> keys);

    <T> Flux<T> sIntersects(Object key,Collection<?> keys);

    <T> Flux<T> sUnions(Object key,Collection<?> keys);

    <T> Flux<T> sUnion(Object key,Object toKey);

    <T> Flux<T> sDiff(Object key,Object toKey);

    <T> Flux<T> sIntersect(Object key,Object toKey);


    <T>  Flux<T> sRandoms(Object key,long num);

    <T>  Mono<T> sRandom(Object key);

    <T> Flux<T> sDisRandoms(Object key,long num);

    <T> Flux<T> sScan(Object key,String value,long num);

    //====================================================zset====================================================//

    Mono<Boolean> zsPut(Object key,Object value,double score);

    Mono<Double> zsScore(Object key,Object value);

    Mono<Long> zsRank(Object key,Object value);

    Mono<Long> zsDESCRank(Object key,Object value);

    <T> Flux<T> zsgets(Object key,long start,long end);

    <T> Flux<T> zsDESCgets(Object key,double min,double max);

    Mono<Long> zsRemoves(Object key,Object... values);

    Mono<Long> zsRemoves(Object key,long start,long end);

    Mono<Long> zsRemoves(Object key,double min,double max);

    <T> Flux<T> zsDESCgets(Object key,long start,long end);

    Mono<Long> zsCount(Object key);

    Mono<Long> zsCount(Object key,double min,double max);

    Mono<Double> zsIncrScore(Object key,Object value,double score);

    Mono<Long> zsSize(Object key);

    Mono<Long> zsUnion(Object key,Object toKey,Object resultKey);

    Mono<Long> zsUnions(Object key,Collection<?> keys,Object resultKey);

    Mono<Long> zsIntersect(Object key,Object toKey,Object resultKey);

    Mono<Long> zsIntersects(Object key,Collection<?> keys,Object resultKey);
}
