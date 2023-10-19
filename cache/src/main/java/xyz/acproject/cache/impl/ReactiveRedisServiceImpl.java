package xyz.acproject.cache.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.acproject.cache.ReactiveRedisService;
import xyz.acproject.utils.StringsUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Jane
 * @ClassName ReactiveRedisServiceImpl
 * @Description TODO
 * @date 2022/6/20 17:09
 * @Copyright:2022
 */
@SuppressWarnings("unchecked")
public class ReactiveRedisServiceImpl implements ReactiveRedisService {

    private static final Logger LOGGER = LogManager.getLogger(ReactiveRedisServiceImpl.class);

    private ReactiveRedisTemplate reactiveRedisTemplate;

    private long second = 0;

    public void setSecond(long second) {
        this.second = second;
    }

    public String error = "error";

    public void setError(String error) {
        this.error = error;
    }

    public void setRedisTemplate(ReactiveRedisTemplate reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<String> flushDb() {
        return reactiveRedisTemplate.getConnectionFactory()
                .getReactiveConnection().serverCommands().flushDb().doOnError(e -> {
                    LOGGER.error("reactive_redis-flushDb:{}", e);
                    e.printStackTrace();
                }).onErrorReturn(error);
    }


    //暂时未使用
    @Override
    public Flux<String> scan(String pattern) {
//        return (Set<String>) reactiveRedisTemplate.execute((ReactiveRedisCallback<Set<String>>) connection -> {
//            Set<String> keysTmp = new HashSet<>();
//            Flux<ByteBuffer> flux = connection.keyCommands().scan(ScanOptions.scanOptions()
//                    .match(StringsUtils.startEndWithAppendSome(pattern,"*",null))
//                    .count(10000).build());
//
//            return keysTmp;
//        });

        return reactiveRedisTemplate.scan(ScanOptions.scanOptions().match(StringsUtils.startEndWithAppendSome(pattern, "*", null)).count(10000).build());
    }

    @Override
    public <T> Mono<T> get(Object key) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> set(Object key, Object value) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Mono<Boolean> set(Object key, Object value, long seconds) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(seconds));
    }

    @Override
    public Mono<Long> deletes(Object... key) {
        if (key != null && key.length > 0) {
        } else return Mono.just(0l);
        if (key.length == 1) {
            return reactiveRedisTemplate.delete(key[0]);
        } else {
            return reactiveRedisTemplate.delete(key);
        }
    }

    @Override
    public Mono<Boolean> deletesLike(String... key) {
        if (key == null || key.length < 1) return Mono.just(false);
        Flux.fromStream(Arrays.asList(key).stream().map(k->
                this.scan(k))).flatMap(Function.identity()).flatMap(this::deletes).subscribe();
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> hasKey(Object key) {
        if (key == null) return Mono.just(false);
        return reactiveRedisTemplate.hasKey(key);
    }

    @Override
    public Mono<Boolean> expire(Object key, long seconds) {
        if (key == null) return Mono.just(false);
        return reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds));
    }

    @Override
    public Mono<Long> getExpire(Object key) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.getExpire(key);
    }

    @Override
    public Mono<Boolean> rename(Object oldKey, Object newKey) {
        if (oldKey == null || newKey == null) return Mono.empty();
        return reactiveRedisTemplate.rename(oldKey, newKey);
    }

    @Override
    public Mono<Long> incr(Object key, long num) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForValue().increment(key, num);
    }

    @Override
    public Mono<Long> decr(Object key, long num) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForValue().decrement(key, num);
    }

    @Override
    public <T> Flux<T> lGet(Object key, long start, long end) {
        if (key == null) return Flux.empty();
        return reactiveRedisTemplate.opsForList().range(key, start, end);
    }

    @Override
    public Mono<Long> lSize(Object key) {
        if (key == null) return Mono.just(0l);
        return reactiveRedisTemplate.opsForList().size(key);
    }

    @Override
    public Mono<Boolean> lbPush(Object key, Object value) {
        if (key == null || value == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public Mono<Boolean> lbPush(Object key, Object value, long seconds) {
        if (key == null || value == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().leftPush(key, value).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public Mono<Boolean> laPush(Object key, Object value) {
        if (key == null || value == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Mono<Boolean> laPush(Object key, Object value, long seconds) {
        if (key == null || value == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().rightPush(key, value).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public <T> Mono<Boolean> lbPushs(Object key, List<T> values) {
        if (key == null || CollectionUtils.isEmpty(values)) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public <T> Mono<Boolean> lbPushs(Object key, List<T> values, long seconds) {
        if (key == null || CollectionUtils.isEmpty(values)) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().leftPushAll(key, values).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public <T> Mono<Boolean> laPushs(Object key, List<T> values) {
        if (key == null || CollectionUtils.isEmpty(values)) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public <T> Mono<Boolean> laPushs(Object key, List<T> values, long seconds) {
        if (key == null || CollectionUtils.isEmpty(values)) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().rightPushAll(key, values).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public Mono<Long> lRemove(Object key, long num, Object value) {
        if (key == null) return Mono.just(0l);
        return reactiveRedisTemplate.opsForList().remove(key, num, value);
    }

    @Override
    public <T> Mono<T> lGetIndex(Object key, long index) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().index(key, index);
    }

    @Override
    public Mono<Boolean> lUpdateIndex(Object key, long index, Object value) {
        if (key == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public <T> Mono<T> lbPop(Object key) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().leftPop(key);
    }

    @Override
    public <T> Mono<T> lbPop(Object key, long seconds) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().leftPop(key).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public <T> Mono<T> laPop(Object key) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().rightPop(key);
    }

    @Override
    public <T> Mono<T> laPop(Object key, long seconds) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().rightPop(key).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public <T> Mono<T> lbPopToAPush(Object sourceKey, Object toKey, Class<?> clazz) {
        if (sourceKey == null || toKey == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().leftPop(sourceKey).map(value -> this.laPush(toKey, value).thenReturn(value));
    }

    @Override
    public <T> Mono<T> lbPopToAPush(Object sourceKey, Object toKey, Class<?> clazz, long seconds) {
        if (sourceKey == null || toKey == null) return Mono.empty();
        return lbPopToAPush(sourceKey, toKey, clazz).then(reactiveRedisTemplate.expire(toKey, Duration.ofSeconds(seconds)));
    }

    @Override
    public <T> Mono<T> laPopToBPush(Object sourceKey, Object toKey) {
        if (sourceKey == null || toKey == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey, toKey);
    }

    @Override
    public <T> Mono<T> laPopToBPush(Object sourceKey, Object toKey, long seconds) {
        if (sourceKey == null || toKey == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey, toKey, Duration.ofSeconds(seconds));
    }

    @Override
    public Mono<Long> lbIndexOf(Object key, Object value) {
        if (key == null || value == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().indexOf(key, value);
    }

    @Override
    public Mono<Long> laIndexOf(Object key, Object value) {
        if (key == null || value == null) return Mono.empty();
        return reactiveRedisTemplate.opsForList().lastIndexOf(key, value);
    }

    @Override
    public <T> Mono<T> hget(Object key, Object hKey) {
        if (key == null || hKey == null) return Mono.empty();
        return reactiveRedisTemplate.opsForHash().get(key, hKey);
    }

    @Override
    public <T> Flux<T> hgetKeys(Object key) {
        if (key == null) return Flux.empty();
        return reactiveRedisTemplate.opsForHash().keys(key);
    }

    @Override
    public <T> Mono<List<T>> hgets(Object key, Collection<?> hKeys) {
        if (key == null || CollectionUtils.isEmpty(hKeys)) return Mono.empty();
        return reactiveRedisTemplate.opsForHash().multiGet(key, hKeys);
    }

    @Override
    public <T, V> Flux<Map<T, V>> hmget(Object key) {
        if (key == null) return Flux.empty();
        return reactiveRedisTemplate.opsForHash().entries(key);
    }

    @Override
    public Mono<Boolean> hmPut(Object key, Object hKey, Object hValue) {
        if (key == null || hKey == null || hValue == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForHash().put(key, hKey, hValue);
    }

    @Override
    public Mono<Boolean> hmPut(Object key, Object hKey, Object hValue, long seconds) {
        if (key == null || hKey == null || hValue == null) return Mono.just(false);
        return hmPut(key, hKey, hValue).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public Mono<Boolean> hmPuts(Object key, Map<?, ?> map) {
        if (key == null || CollectionUtils.isEmpty(map)) return Mono.just(false);
        return reactiveRedisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public Mono<Boolean> hmPuts(Object key, Map<?, ?> map, long seconds) {
        if (key == null || CollectionUtils.isEmpty(map)) return Mono.just(false);
        return hmPuts(key, map).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public Mono<Long> hRemoves(Object key, Object... hKey) {
        if (key == null || hKey == null) return Mono.just(0l);
        return reactiveRedisTemplate.opsForHash().remove(key, hKey);
    }


    @Override
    public Mono<Boolean> hHasHkey(Object key, Object hKey) {
        if (key == null || hKey == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForHash().hasKey(key, hKey);
    }

    @Override
    public Mono<Long> hincr(Object key, Object hKey, long num) {
        if (key == null || hKey == null) return Mono.empty();
        return reactiveRedisTemplate.opsForHash().increment(key, hKey, num);
    }

    @Override
    public Mono<Long> hdecr(Object key, Object hKey, long num) {
        if (key == null || hKey == null) return Mono.empty();
        return reactiveRedisTemplate.opsForHash().increment(key, hKey, -num);
    }

    @Override
    public Mono<Long> hsize(Object key) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForHash().size(key);
    }

    @Override
    public <T> Flux<T> htolist(Object key) {
        if (key == null) return Flux.empty();
        return reactiveRedisTemplate.opsForHash().values(key);
    }

    @Override
    @Deprecated
    public Mono<Long> hlentoValue(Object key, Object hKey) {
        return Mono.just(0l);
    }

    @Override
    public <T, V> Flux<Map<T, V>> hmScan(Object key, String hKey, long num) {
        if (key == null || hKey == null) return Flux.empty();
        return reactiveRedisTemplate.opsForHash().scan(key, ScanOptions.scanOptions().match(StringsUtils.startEndWithAppendSome(hKey, "*", null)).count(num).build());
    }

    @Override
    public <T> Flux<T> sgets(Object key) {
        if (key == null) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().members(key);
    }

    @Override
    public Mono<Boolean> shasValue(Object key, Object value) {
        if (key == null || value == null) return Mono.just(false);
        return reactiveRedisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Mono<Long> sPuts(Object key, Object... values) {
        if (key == null || values == null || values.length < 1) return Mono.just(0l);
        return reactiveRedisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Mono<Long> sPuts(Object key, long seconds, Object... values) {
        if (key == null || values == null || values.length < 1) return Mono.just(0l);
        return sPuts(key, values).then(reactiveRedisTemplate.expire(key, Duration.ofSeconds(seconds)));
    }

    @Override
    public Mono<Long> sSize(Object key) {
        if (key == null) return Mono.just(0l);
        return reactiveRedisTemplate.opsForSet().size(key);
    }

    @Override
    public Mono<Long> sRemoves(Object key, Object... values) {
        if (key == null || values == null || values.length < 1) return Mono.just(0l);
        return reactiveRedisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public <T> Flux<T> sDiffs(Object key, Collection<?> keys) {
        if (key == null || CollectionUtils.isEmpty(keys)) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().difference(key, keys);
    }

    @Override
    public <T> Flux<T> sIntersects(Object key, Collection<?> keys) {
        if (key == null || CollectionUtils.isEmpty(keys)) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().intersect(key, keys);
    }

    @Override
    public <T> Flux<T> sUnions(Object key, Collection<?> keys) {
        if (key == null || CollectionUtils.isEmpty(keys)) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().union(key, keys);
    }

    @Override
    public <T> Flux<T> sUnion(Object key, Object toKey) {
       if (key == null || toKey == null) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().union(key, toKey);
    }

    @Override
    public <T> Flux<T> sDiff(Object key, Object toKey) {
        if (key == null || toKey == null) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().difference(key, toKey);
    }

    @Override
    public <T> Flux<T> sIntersect(Object key, Object toKey) {
        if (key == null || toKey == null) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().intersect(key,toKey);
    }

    @Override
    public <T> Flux<T> sRandoms(Object key, long num) {
        if (key == null) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().randomMembers(key, num);
    }

    @Override
    public <T> Mono<T> sRandom(Object key) {
        if (key == null) return Mono.empty();
        return reactiveRedisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public <T> Flux<T> sDisRandoms(Object key, long num) {
        if (key == null) return Flux.empty();
        return reactiveRedisTemplate.opsForSet().distinctRandomMembers(key,num);
    }

    @Override
    public <T> Flux<T> sScan(Object key, String value, long num) {
        if(key==null||value==null)return Flux.empty();
        return reactiveRedisTemplate.opsForSet().scan(key, ScanOptions.scanOptions().match(StringsUtils.startEndWithAppendSome(value, "*", null)).count(num).build());
    }

    @Override
    public Mono<Boolean> zsPut(Object key, Object value, double score) {
        if(key==null||value==null)return Mono.just(false);
        return reactiveRedisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Mono<Double> zsScore(Object key, Object value) {
        if(key==null||value==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().score(key, value);
    }

    @Override
    public Mono<Long> zsRank(Object key, Object value) {
        if(key==null||value==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().rank(key, value);
    }

    @Override
    public Mono<Long> zsDESCRank(Object key, Object value) {
        if(key==null||value==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    @Override
    public <T> Flux<T> zsgets(Object key, long start, long end) {
        if(key==null)return Flux.empty();
        return reactiveRedisTemplate.opsForZSet().range(key,Range.closed(start,end));
    }

    @Override
    public <T> Flux<T> zsDESCgets(Object key, double min, double max) {
        if(key==null)return Flux.empty();
        return reactiveRedisTemplate.opsForZSet().reverseRangeByScore(key,Range.closed(min,max));
    }

    @Override
    public Mono<Long> zsRemoves(Object key, Object... values) {
        if(key==null||values==null||values.length<1)return Mono.just(0l);
        return reactiveRedisTemplate.opsForZSet().remove(key, values);
    }

    @Override
    public Mono<Long> zsRemoves(Object key, long start, long end) {
        if(key==null)return Mono.just(0l);
        return reactiveRedisTemplate.opsForZSet().removeRange(key,Range.closed(start,end));
    }

    @Override
    public Mono<Long> zsRemoves(Object key, double min, double max) {
        if(key==null)return Mono.just(0l);
        return reactiveRedisTemplate.opsForZSet().removeRangeByScore(key,Range.closed(min,max));
    }

    @Override
    public <T> Flux<T> zsDESCgets(Object key, long start, long end) {
        if(key==null)return Flux.empty();
        return reactiveRedisTemplate.opsForZSet().reverseRange(key,Range.closed(start,end));
    }

    @Override
    @Deprecated
    public Mono<Long> zsCount(Object key) {
        if(key==null)return Mono.empty();
        return Mono.just(0l);
    }

    @Override
    public Mono<Long> zsCount(Object key, double min, double max) {
        if(key==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().count(key,Range.closed(min,max));
    }


    @Override
    public Mono<Double> zsIncrScore(Object key, Object value, double score) {
        if(key==null||value==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    @Override
    public Mono<Long> zsSize(Object key) {
        if(key==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().size(key);
    }

    @Override
    public Mono<Long> zsUnion(Object key, Object toKey, Object resultKey) {
        if(key==null||toKey==null||resultKey==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().unionAndStore(key, toKey, resultKey);
    }

    @Override
    public Mono<Long> zsUnions(Object key, Collection<?> keys, Object resultKey) {
        if(key==null||CollectionUtils.isEmpty(keys)||resultKey==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().unionAndStore(key,keys,resultKey);
    }

    @Override
    public Mono<Long> zsIntersect(Object key, Object toKey, Object resultKey) {
        if(key==null||toKey==null||resultKey==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().intersectAndStore(key, toKey, resultKey);
    }

    @Override
    public Mono<Long> zsIntersects(Object key, Collection<?> keys, Object resultKey) {
        if(key==null||CollectionUtils.isEmpty(keys)||resultKey==null)return Mono.empty();
        return reactiveRedisTemplate.opsForZSet().intersectAndStore(key,keys,resultKey);
    }
}
