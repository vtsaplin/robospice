package com.octo.android.robospice;

import java.util.Set;

import android.app.Application;

import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.thrift.ThriftObjectPersisterFactory;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.thrift.ThriftSpiceRequest;

/**
 * @author Vitaly Tsaplin
 *
 */
public class ThriftSpiceService extends SpiceService {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void addRequest(CachedSpiceRequest<?> request, Set<RequestListener<?>> listRequestListener) {
		if (request.getSpiceRequest() instanceof ThriftSpiceRequest) {
			((ThriftSpiceRequest<?>) request.getSpiceRequest()).setContext(getApplicationContext());
		}
		super.addRequest(request, listRequestListener);
	}

	@Override
	public CacheManager createCacheManager(Application application) throws CacheCreationException {
		CacheManager cacheManager = new CacheManager();
		cacheManager.addPersister(new ThriftObjectPersisterFactory(application));
		return cacheManager;
	}
}
