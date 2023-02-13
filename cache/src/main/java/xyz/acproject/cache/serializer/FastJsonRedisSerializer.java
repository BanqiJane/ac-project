package xyz.acproject.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import xyz.acproject.utils.FastJsonUtils;

@SuppressWarnings("all")
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

	@Override
	public byte[] serialize(T t) throws SerializationException {
		// TODO 自动生成的方法存根
		if (t == null) {
			return new byte[0];
		}
		try {
			return JSON.toJSONBytes(t, SerializerFeature.WriteClassName);
		} catch (Exception ex) {
			throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		// TODO 自动生成的方法存根
		String data = new String(bytes);
		T result = (T) JSON.parse(data);
		return result;
	}

}
