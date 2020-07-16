package per.cby.system.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import per.cby.frame.redis.handler.RedisHandler;

/**
 * Redis缓存管理器
 * 
 * @author chenboyang
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RedisCacheManager implements CacheManager {

	/**
	 * 会话Redis操作器
	 */
	private RedisHandler sessionRedisHandler;

	/**
	 * Redis连接工厂
	 */
	private RedisConnectionFactory redisConnectionFactory;

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new RedisCache<K, V>(name, getSessionRedisHandler());
	}

	/**
	 * 获取会话Redis操作器
	 * 
	 * @return 会话Redis操作器
	 */
	private <K, V> RedisHandler<K, V> getSessionRedisHandler() {
		if (sessionRedisHandler == null) {
			if (redisConnectionFactory != null) {
				sessionRedisHandler = new RedisHandler<K, V>(redisConnectionFactory);
				sessionRedisHandler.setKeySerializer(new StringRedisSerializer());
				sessionRedisHandler.setValueSerializer(new JdkSerializationRedisSerializer());
				sessionRedisHandler.afterPropertiesSet();
			}
		}
		return sessionRedisHandler;
	}

	@Autowired(required = false)
	public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
		this.redisConnectionFactory = redisConnectionFactory;
	}

}
