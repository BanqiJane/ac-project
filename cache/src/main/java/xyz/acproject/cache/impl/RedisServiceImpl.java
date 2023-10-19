package xyz.acproject.cache.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;
import xyz.acproject.cache.RedisService;
import xyz.acproject.utils.StringsUtils;
import xyz.acproject.utils.collections.ArrayUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Jane
 * @ClassName RedisServiceImpl
 * @Description TODO
 * @date 2021/2/2 0:12
 * @Copyright:2021
 */
public class RedisServiceImpl implements RedisService {

    private static final Logger LOGGER = LogManager.getLogger(RedisServiceImpl.class);

    private RedisTemplate redisTemplate;

    private long second = 0;

    public void setSecond(long second) {
        this.second = second;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @return void
     * @Description 清空数据库缓存 注意！
     * @author Jane
     * @date 2021/5/25 17:42
     * @params []
     * @Copyright
     */
    public void flushDb() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
        } catch (Exception e) {
            LOGGER.error("redis-flushdb:{}", e);
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public Set<String> scan(String pattern) {
        return (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions()
                    .match(StringsUtils.startEndWithAppendSome(pattern,"*",null))
                    .count(10000).build())) {
                while (cursor.hasNext()) {
                    keysTmp.add(new String(cursor.next(), "Utf-8"));
                }
            } catch (Exception e) {
                LOGGER.error("redis-scan:{}", pattern);
                throw new RuntimeException(e);
            }
            return keysTmp;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        try {
            return key == null ? null : (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            LOGGER.error("redis-set:{}", key);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean set(Object key, Object value) {
        try {
            if (this.second > 0) {
                redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(this.second));
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            //日志
            LOGGER.error("redis-set:{},{}", key, value);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean set(Object key, Object value, long seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            //日志
            LOGGER.error("redis-set:{},{},{}", key, value, seconds);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean deletes(Object... key) {
        try {
            if (key != null && key.length > 0) {
            } else return false;
            if (key.length == 1) {
                return redisTemplate.delete(key[0]);
            } else {
                return redisTemplate.delete(CollectionUtils.arrayToList(key)) == key.length ? true : false;
            }
        } catch (Exception e) {
            LOGGER.error("redis-deletes:{}", ArrayUtils.toList(key));
            return false;
        }
    }

    /**
     * @return boolean
     * @Description 模糊删除 必须得配合scan方法使用 暂时只支持string类型 模糊需要加*
     * @author Jane
     * @date 2021/5/25 17:40
     * @params [key]
     * @Copyright
     */
    @SuppressWarnings("unchecked")
    public boolean deletesLike(String... key) {
        try {
            if (key != null && key.length > 0) {
            } else return false;
            if (key.length == 1) {
                Set<String> keys = scan(key[0]);
                return redisTemplate.delete(keys) == 1 ? true : false;
            } else {
                Set<String> keys = new HashSet<>();
                for (String s : key) {
                    keys.addAll(scan(s));
                }
                return redisTemplate.delete(keys) == key.length ? true : false;
            }
        } catch (Exception e) {
            LOGGER.error("redis-delets:{}", ArrayUtils.toList(key));
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasKey(Object key) {
        try {
            return key != null ? redisTemplate.hasKey(key) : false;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("redis-hasKey:{}", key);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean expire(Object key, long seconds) {
        try {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            //日志
            LOGGER.error("redis-expire:{},{}", key,seconds);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long getExpire(Object key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("redis-getExpire:{}",key);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void rename(Object oldKey, Object newKey) {
        try {
            redisTemplate.rename(oldKey, newKey);
        } catch (Exception e) {
            LOGGER.error("redis-rename:{},{}",  oldKey, newKey);
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long incr(Object key, long num) {
        try {
            return redisTemplate.opsForValue().increment(key, num);
        } catch (Exception e) {
            LOGGER.error("redis-incr:{},{}", key,num);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long decr(Object key, long num) {
        try {
            return redisTemplate.opsForValue().decrement(key, num);
        } catch (Exception e) {
            LOGGER.error("redis-decr:{},{}", key,num);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> lGet(Object key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            //日志
            LOGGER.error("redis-lget:{},{},{}", key, start, end);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long lSize(Object key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            //日志
            LOGGER.error("redis-lsize:{}", key);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean lbPush(Object key, Object value) {
        try {

            redisTemplate.opsForList().leftPush(key, value);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("redis-lpush:{},{}", key, value);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean lbPush(Object key, Object value, long seconds) {
        try {

            redisTemplate.opsForList().leftPush(key, value);

            this.expire(key, seconds);
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-lbPush:{},{},{}", key, value, seconds);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean laPush(Object key, Object value) {
        try {

            redisTemplate.opsForList().rightPush(key, value);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-laPush:{},{}", key,value);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean laPush(Object key, Object value, long seconds) {
        try {

            redisTemplate.opsForList().rightPush(key, value);
            this.expire(key, seconds);
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-laPush:{},{},{}", key, value, seconds);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean lbPushs(Object key, List<T> values) {
        try {

            redisTemplate.opsForList().leftPushAll(key, values);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-lbPushs:{},{}", key,values);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean lbPushs(Object key, List<T> values, long seconds) {
        try {

            redisTemplate.opsForList().leftPushAll(key, values);
            this.expire(key, seconds);
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-lbPushs:{},{},{}", key, values, seconds);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean laPushs(Object key, List<T> values) {
        try {

            redisTemplate.opsForList().rightPushAll(key, values);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-laPushs:{},{}", key,values);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean laPushs(Object key, List<T> values, long seconds) {
        try {

            redisTemplate.opsForList().rightPushAll(key, values);
            this.expire(key, seconds);
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-laPushs:{},{},{}", key, values,seconds);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long lRemove(Object key, long num, Object value) {
        try {
            long remove = redisTemplate.opsForList().remove(key, num, value);

            return remove;

        } catch (Exception e) {
            LOGGER.error("redis-lRemove:{},{},{}", key, num, value);
            e.printStackTrace();

            return 0;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lGetIndex(Object key, long index) {
        try {

            return (T) redisTemplate.opsForList().index(key, index);

        } catch (Exception e) {
            LOGGER.error("redis-lGetIndex:{},{}", key, index);
            e.printStackTrace();

            return null;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean lUpdateIndex(Object key, long index, Object value) {
        try {

            redisTemplate.opsForList().set(key, index, value);
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-lUpdateIndex:{},{},{}", key, index, value);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lbPop(Object key) {
        try {

            return (T) redisTemplate.opsForList().leftPop(key);

        } catch (Exception e) {
            LOGGER.error("redis-lbPop:{}", key);
            e.printStackTrace();

            return null;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lbPop(Object key,long seconds) {
        try {

            return (T) redisTemplate.opsForList().leftPop(key,seconds,TimeUnit.SECONDS);

        } catch (Exception e) {
            LOGGER.error("redis-lbPop:{},{}", key,seconds);
            e.printStackTrace();

            return null;

        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T laPop(Object key) {
        try {
            return (T) redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            LOGGER.error("redis-laPop:{}", key);
            e.printStackTrace();

            return null;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T laPop(Object key,long seconds) {
        try {
            return (T) redisTemplate.opsForList().rightPop(key,seconds,TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("redis-laPop:{}", e);
            e.printStackTrace();

            return null;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lbPopToAPush(Object sourceKey, Object toKey, Class<?> clazz) {
        try {
            T t = lbPop(sourceKey);
            ObjectMapper objectMapper = new ObjectMapper();
            this.laPush(toKey, objectMapper.convertValue(t, clazz));
            return t;
        } catch (Exception e) {
            LOGGER.error("redis-lbPopToAPush:{},{},{}", sourceKey, toKey, clazz);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T lbPopToAPush(Object sourceKey, Object toKey, Class<?> clazz,long seconds) {
        try {
            T t = lbPop(sourceKey,seconds);
            ObjectMapper objectMapper = new ObjectMapper();
            this.laPush(toKey, objectMapper.convertValue(t, clazz));
            return t;
        } catch (Exception e) {
            LOGGER.error("redis-lbPopToAPush:{},{},{},{}", sourceKey, toKey, clazz, seconds);
            e.printStackTrace();

            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T laPopToBPush(Object sourceKey, Object toKey) {
        try {
            return (T) redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, toKey);
        } catch (Exception e) {
            LOGGER.error("redis-laPopToBPush:{},{}", sourceKey, toKey);
            e.printStackTrace();

            return null;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T laPopToBPush(Object sourceKey, Object toKey,long seconds) {
        try {
            return (T) redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, toKey,seconds,TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("redis-laPopToBPush:{},{},{}",sourceKey,toKey,seconds);
            e.printStackTrace();

            return null;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long lbIndexOf(Object key, Object value) {
        try {
            return redisTemplate.opsForList().indexOf(key, value);
        } catch (Exception e) {
            LOGGER.error("redis-lbIndexOf:{},{}", key,value);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long laIndexOf(Object key, Object value) {
        try {
            return redisTemplate.opsForList().lastIndexOf(key, value);
        } catch (Exception e) {
            LOGGER.error("redis-laIndexOf:{},{}", key, value);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T hget(Object key, Object hKey) {
        try {
            return (T) redisTemplate.opsForHash().get(key, hKey);
        } catch (Exception e) {
            LOGGER.error("redis-hget:{},{}", key, hKey);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> hgetKeys(Object key) {
        try {
            return redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            LOGGER.error("redis-hgetKeys:{}", key);
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> hgets(Object key, Collection<?> hKeys) {
        try {
            return redisTemplate.opsForHash().multiGet(key, hKeys);
        } catch (Exception e) {
            LOGGER.error("redis-hgets:{},{}", key, hKeys);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, V> Map<T, V> hmget(Object key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            LOGGER.error("redis-hmget:{}", key);
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hmPut(Object key, Object hKey, Object hValue) {
        try {

            redisTemplate.opsForHash().put(key, hKey, hValue);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-hmPut:{},{},{}", key, hKey, hValue);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hmPut(Object key, Object hKey, Object hValue, long seconds) {

        try {

            redisTemplate.opsForHash().put(key, hKey, hValue);

            if (seconds > 0) {

                expire(key, seconds);

            }

            return true;

        } catch (Exception e) {
            LOGGER.error("redis-hmPut:{},{},{},{}", key, hKey, hValue, seconds);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hmPuts(Object key, Map<?, ?> map) {
        try {

            redisTemplate.opsForHash().putAll(key, map);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return true;

        } catch (Exception e) {
            LOGGER.error("redis-hmPuts:{},{}", key, map);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hmPuts(Object key, Map<?, ?> map, long seconds) {
        try {

            redisTemplate.opsForHash().putAll(key, map);

            if (seconds > 0) {

                expire(key, seconds);

            }

            return true;

        } catch (Exception e) {
            LOGGER.error("redis-hmPuts:{},{},{}", key, map, seconds);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long hRemoves(Object key, Object... hKey) {
        try {
            if (hKey != null && hKey.length > 0) {
            } else return 0;
            if (hKey.length == 1) {
                return redisTemplate.opsForHash().delete(key, hKey[0]);
            } else {
                return redisTemplate.opsForHash().delete(key, hKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("redis-hRemoves:{},{}", key, ArrayUtils.toList(hKey));
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hHasHkey(Object key, Object hKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hKey);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("redis-hHasHkey:{},{}", key, hKey);
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long hincr(Object key, Object hKey, long num) {
        try {
            return redisTemplate.opsForHash().increment(key, hKey, num);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("redis-hincr:{},{},{}", key, hKey, num);
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long hdecr(Object key, Object hKey, long num) {
        try {
            return redisTemplate.opsForHash().increment(key, hKey, -num);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("redis-hdecr:{},{},{}", key, hKey, num);
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long hsize(Object key) {
        try {
            return redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("redis-hsize:{}", key);
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> htolist(Object key) {

        try {
            return redisTemplate.opsForHash().values(key);
        } catch (Exception e) {
            LOGGER.error("redis-htolist:{}", key);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long hlentoValue(Object key, Object hKey) {
        try {
            return redisTemplate.opsForHash().lengthOfValue(key, hKey);
        } catch (Exception e) {
            LOGGER.error("redis-hlentoValue:{},{}", key, hKey);
            return 0;
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, V> Map<T, V> hmScan(Object key, String hKey, long num) {
        Cursor<Map.Entry<T, V>> cursor;
        if (StringUtils.isEmpty(hKey) && num <= 0) {
            cursor = redisTemplate.opsForHash().scan(key, ScanOptions.NONE);
        } else {
            if (num <= 0) {
                cursor = redisTemplate.opsForHash().scan(key, ScanOptions.scanOptions().match(StringsUtils.startEndWithAppendSome(hKey,"*",null)).build());
            } else {
                cursor = redisTemplate.opsForHash().scan(key, ScanOptions.scanOptions().match(StringsUtils.startEndWithAppendSome(hKey,"*",null)).count(num).build());
            }
        }
        Map<T, V> maps = new HashMap<>();
        try {
            while (cursor.hasNext()) {
                Map.Entry<T, V> entry = cursor.next();
                maps.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            //日志打印
            LOGGER.error("redis-hmScan:{},{},{}", key, hKey, num);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return maps;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sgets(Object key) {

        try {

            return redisTemplate.opsForSet().members(key);

        } catch (Exception e) {
            LOGGER.error("redis-sgets:{}",key);
            e.printStackTrace();

            return new HashSet<>();

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean shasValue(Object key, Object value) {
        try {

            return redisTemplate.opsForSet().isMember(key, value);

        } catch (Exception e) {
            LOGGER.error("redis-isMember:{},{}", key, value);
            e.printStackTrace();

            return false;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long sPuts(Object key, Object... values) {
        try {
            long a = redisTemplate.opsForSet().add(key, values);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return a;
        } catch (Exception e) {
            LOGGER.error("redis-sPuts:{},{}",key,ArrayUtils.toList(values));
            e.printStackTrace();

            return 0;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long sPuts(Object key, long seconds, Object... values) {
        try {
            long a = redisTemplate.opsForSet().add(key, values);
            this.expire(key, seconds);
            return a;
        } catch (Exception e) {
            LOGGER.error("redis-sPuts:{},{},{}", key, seconds, ArrayUtils.toList(values));
            e.printStackTrace();

            return 0;

        }
    }

    public <T> T sPop(Object key) {
        try {
            return (T)redisTemplate.opsForSet().pop(key);
        } catch (Exception e) {
            LOGGER.error("redis-sPop:{}", key);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long sSize(Object key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            LOGGER.error("redis-sSize:{}", key);
            e.printStackTrace();

            return 0;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long sRemoves(Object key, Object... values) {
        try {
            if (values != null && values.length > 0) {
            } else return 0;
            if (values.length == 1) {
                return redisTemplate.opsForSet().remove(key, values[0]);
            } else {
                return redisTemplate.opsForSet().remove(key, values);
            }
        } catch (Exception e) {
            LOGGER.error("redis-sRemoves:{},{}", key, ArrayUtils.toList(values));
            e.printStackTrace();

            return 0;

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sDiffs(Object key, Collection<?> keys) {
        try {
            return redisTemplate.opsForSet().difference(key, keys);
        } catch (Exception e) {
            LOGGER.error("redis-sDiffs:{},{}", key, keys);
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sIntersects(Object key, Collection<?> keys) {
        try {
            return redisTemplate.opsForSet().intersect(key, keys);
        } catch (Exception e) {
            LOGGER.error("redis-intersect:{},{}", key, keys);
            return new HashSet<>();
        }
    }

    @Override
    public <T> Set<T> sUnions(Object key, Collection<?> keys) {
        try {
            return redisTemplate.opsForSet().union(key, keys);
        } catch (Exception e) {
            LOGGER.error("redis-sUnions:{},{}", key, keys);
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sUnion(Object key, Object toKey) {
        try {
            return redisTemplate.opsForSet().union(key, toKey);
        } catch (Exception e) {
            LOGGER.error("redis-sUnion:{},{}", key, toKey);
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sDiff(Object key, Object toKey) {
        try {
            return redisTemplate.opsForSet().difference(key, toKey);
        } catch (Exception e) {
            LOGGER.error("redis-difference:{},{}", key, toKey);
            return new HashSet<>();
        }
    }

    @Override
    public <T> Set<T> sIntersect(Object key, Object toKey) {
        try {
            return redisTemplate.opsForSet().intersect(key, toKey);
        } catch (Exception e) {
            LOGGER.error("redis-sIntersect:{},{}", key, toKey);
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> sRandoms(Object key, long num) {
        try {
            return redisTemplate.opsForSet().randomMembers(key, num);
        } catch (Exception e) {
            LOGGER.error("redis-sRandoms:{},{}", key, num);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object sRandom(Object key) {
        try {
            return redisTemplate.opsForSet().randomMember(key);
        } catch (Exception e) {
            LOGGER.error("redis-sRandom:{}", key);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sDisRandoms(Object key, long num) {
        try {
            return redisTemplate.opsForSet().distinctRandomMembers(key, num);
        } catch (Exception e) {
            LOGGER.error("redis-sDisRandoms:{},{},{}", key, num);
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> sScan(Object key, String value, long num) {
        Cursor<T> cursor;
        if (StringUtils.isEmpty(value) && num <= 0) {
            cursor = redisTemplate.opsForSet().scan(key, ScanOptions.NONE);
        } else {
            if (num <= 0) {
                cursor = redisTemplate.opsForSet().scan(key, ScanOptions.scanOptions().match(value).build());
            } else {
                cursor = redisTemplate.opsForSet().scan(key, ScanOptions.scanOptions().match(value).count(num).build());
            }
        }
        Set<T> objects = new HashSet<>();
        try {
            while (cursor.hasNext()) {
                T t = cursor.next();
                objects.add(t);
            }
        } catch (Exception e) {
            LOGGER.error("redis-sScan:{},{},{}", key, value, num);
            e.printStackTrace();
            return null;
        }
        return objects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean zsPut(Object key, Object value, double score) {
        try {
            boolean flag = redisTemplate.opsForZSet().add(key, value, score);
            if (this.second > 0) {
                this.expire(key, this.second);
            }
            return flag;
        } catch (Exception e) {
            LOGGER.error("redis-zsPut:{},{},{}", key, value, score);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double zsScore(Object key, Object value) {
        try {
            return redisTemplate.opsForZSet().score(key, value);
        } catch (Exception e) {
            LOGGER.error("redis-zsScore:{},{}",  key, value);
            e.printStackTrace();
            return 0d;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsRank(Object key, Object value) {
        try {
            return redisTemplate.opsForZSet().rank(key, value);
        } catch (Exception e) {
            LOGGER.error("redis-zsRank:{},{}", key, value);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zsgets(Object key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            LOGGER.error("redis-zsgets:{},{},{}", key, start, end);
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsRemoves(Object key, Object... values) {
        try {
            if (values != null && values.length > 0) {
            } else return 0;
            if (values.length == 1) {
                return redisTemplate.opsForZSet().remove(key, values[0]);
            } else {
                return redisTemplate.opsForZSet().remove(key, values);
            }
        } catch (Exception e) {
            LOGGER.error("redis-zsRemoves:{},{}", key, ArrayUtils.toList(values));
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsRemoves(Object key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().removeRange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("redis-zsRemoves:{},{},{}", key, start, end);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zsDESCgets(Object key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("redis-zsDESCgets:{},{},{}", key, start, end);
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> zsDESCgets(Object key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        } catch (Exception e) {
            LOGGER.error("redis-zsDESCgets:{},{},{}", key, min, max);
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsRemoves(Object key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        } catch (Exception e) {
            LOGGER.error("redis-zsRemoves:{},{},{}", key, min, max);
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public long zsDESCRank(Object key, Object value) {
        try {
            return redisTemplate.opsForZSet().reverseRank(key, value);
        } catch (Exception e) {
            LOGGER.error("redis-zsDESCRank:{},{}", key, value);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsCount(Object key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            LOGGER.error("redis-zsCount:{}", key);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsCount(Object key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            LOGGER.error("redis-zsCount:{}", key);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsSize(Object key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            LOGGER.error("redis-zsSize:{}", key);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double zsIncrScore(Object key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, score);
        } catch (Exception e) {
            LOGGER.error("redis-zsIncrScore:{},{},{}", key, value, score);
            e.printStackTrace();
            return 0d;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsUnion(Object key, Object toKey, Object resultKey) {
        try {
            return redisTemplate.opsForZSet().unionAndStore(key, toKey, resultKey);
        } catch (Exception e) {
            LOGGER.error("redis-zsUnion:{},{},{}", key, toKey, resultKey);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsUnions(Object key, Collection<?> keys, Object resultKey) {
        try {
            return redisTemplate.opsForZSet().unionAndStore(key, keys, resultKey);
        } catch (Exception e) {
            LOGGER.error("redis-zsUnions:{},{},{}", key, keys, resultKey);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsIntersect(Object key, Object toKey, Object resultKey) {
        try {
            return redisTemplate.opsForZSet().intersectAndStore(key, toKey, resultKey);
        } catch (Exception e) {
            LOGGER.error("redis-zsIntersect:{},{},{}", key, toKey, resultKey);
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long zsIntersects(Object key, Collection<?> keys, Object resultKey) {
        try {
            return redisTemplate.opsForZSet().intersectAndStore(key, keys, resultKey);
        } catch (Exception e) {
            LOGGER.error("redis-zsIntersects:{},{},{}", key, keys, resultKey);
            e.printStackTrace();
            return 0;
        }
    }
}
