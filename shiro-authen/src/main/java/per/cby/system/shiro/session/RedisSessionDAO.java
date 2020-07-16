package per.cby.system.shiro.session;

import java.io.Serializable;
import java.util.Optional;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import per.cby.system.shiro.cache.RedisCache;

/**
 * Redis会话存储DAO
 * 
 * @author chenboyang
 *
 */
@SuppressWarnings("unchecked")
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = super.doCreate(session);
		redisCache().put(sessionId, session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		return Optional.ofNullable(super.doReadSession(sessionId)).orElseGet(() -> super.getCachedSession(sessionId));
	}

	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);
		if (redisCache().has(session.getId())) {
			redisCache().put(session.getId(), session);
		}
	}

	@Override
	protected void doDelete(Session session) {
		super.doDelete(session);
		redisCache().remove(session.getId());
	}

	/**
	 * 获取Redis缓存
	 * 
	 * @return Redis缓存
	 */
	private <K, V> RedisCache<K, V> redisCache() {
		return (RedisCache<K, V>) getCacheManager().getCache(getActiveSessionsCacheName());
	}

}
