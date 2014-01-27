package com.octo.android.robospice.persistence.thrift;

import java.io.File;
import java.util.List;

import org.apache.thrift.TBase;

import android.app.Application;

import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;
import com.octo.android.robospice.persistence.file.InFileObjectPersisterFactory;

/**
 * @author Vitaly Tsaplin
 *
 */
public class ThriftObjectPersisterFactory extends InFileObjectPersisterFactory {

    public ThriftObjectPersisterFactory(Application application) throws CacheCreationException {
        this(application, null, null);
    }

    public ThriftObjectPersisterFactory(Application application, File cacheFolder)
        throws CacheCreationException {
        this(application, null, cacheFolder);
    }

    public ThriftObjectPersisterFactory(Application application, List<Class<?>> listHandledClasses) throws CacheCreationException {
        this(application, listHandledClasses, null);
    }

    public ThriftObjectPersisterFactory(Application application, List<Class<?>> listHandledClasses, File cacheFolder) throws CacheCreationException {
        super(application, listHandledClasses, cacheFolder);
    }

    @Override
    public boolean canHandleClass(Class<?> clazz) {
    	return TBase.class.isAssignableFrom(clazz);
    }

    @Override
    public <DATA> InFileObjectPersister<DATA> createInFileObjectPersister(Class<DATA> clazz, File cacheFolder) throws CacheCreationException {
        return new ThriftObjectPersister<DATA>(getApplication(), clazz, cacheFolder);
    }

}
