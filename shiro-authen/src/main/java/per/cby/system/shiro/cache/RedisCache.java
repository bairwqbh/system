package per.cby.system.shiro.cache;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import per.cby.frame.common.config.properties.SysProperties;
import per.cby.frame.common.util.SpringContextUtil;
import per.cby.frame.redis.handler.RedisHandler;
import per.cby.frame.redis.original.storage.value.SimpleRedisValueStorage;

/**
 * Redis缓存类
 * 
 * @author chenboyang
 *
 * @param <String> 键类型
 * @param <V>      值类型
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class RedisCache<K, V> implements Cache<K, V> {

    /**
     * 名称
     */
    private String name;

    /**
     * Redis操作器
     */
    private RedisHandler<String, V> redisHandler;

    /**
     * Redis值存储
     */
    private SimpleRedisValueStorage<V> redisValueStorage;

    /**
     * 构建Redis缓存
     */
    public RedisCache() {
        this(null);
    }

    /**
     * 构建Redis缓存
     * 
     * @param name 名称
     */
    public RedisCache(String name) {
        this(name, null);
    }

    /**
     * 构建Redis缓存
     * 
     * @param name         名称
     * @param redisHandler Redis操作器
     */
    public RedisCache(String name, RedisHandler<String, V> redisHandler) {
        this.name = name;
        this.redisHandler = redisHandler;
        init();
    }

    /**
     * Redis值存储接口
     * 
     * @return 存储接口
     */
    public SimpleRedisValueStorage<V> redisValueStorage() {
        return redisValueStorage;
    }

    /**
     * 初始化
     */
    private void init() {
        redisValueStorage = new SimpleRedisValueStorage<V>(name, redisHandler);
    }

    /**
     * 获取系统配置属性
     * 
     * @return 系统配置属性
     */
    private SysProperties sysProperties() {
        return SpringContextUtil.getBean(SysProperties.class);
    }

    /**
     * 获取过期时间
     * 
     * @return 过期时间
     */
    private long timeout() {
        return Optional.ofNullable(sysProperties()).map(SysProperties::getTimeout).orElse(30L);
    }

    /**
     * 当前键是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    public boolean has(K key) {
        return redisValueStorage.has((String) key);
    }

    @Override
    public void clear() throws CacheException {
        redisValueStorage.delete((Collection<String>) keys());
    }

    @Override
    public V get(K key) throws CacheException {
        redisValueStorage.handler().expire((String) key, timeout(), TimeUnit.MINUTES);
        return redisValueStorage.get((String) key);
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) redisValueStorage.handler().keys(redisValueStorage.keyPattern());
    }

    @Override
    public V put(K key, V value) throws CacheException {
        V old = get(key);
        redisValueStorage.set((String) key, value, timeout(), TimeUnit.MINUTES);
        return old;
    }

    @Override
    public V remove(K key) throws CacheException {
        V old = get(key);
        redisValueStorage.delete((String) key);
        return old;
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Collection<V> values() {
        return redisValueStorage.multiGet((Collection<String>) keys());
    }

}
