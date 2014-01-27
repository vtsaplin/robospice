package com.octo.android.robospice.persistence.thrift;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.TSerializer;

import roboguice.util.temp.Ln;
import android.app.Application;

import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;

/**
 * @author Vitaly Tsaplin
 *
 */
public final class ThriftObjectPersister<T> extends InFileObjectPersister<T> {

    private TSerializer serializer = new TSerializer();
    private TDeserializer deserializer = new TDeserializer();
	
    public ThriftObjectPersister(Application application, Class<T> clazz, File cacheFolder) throws CacheCreationException {
        super(application, clazz, cacheFolder);
    }

    public ThriftObjectPersister(Application application, Class<T> clazz) throws CacheCreationException {
        super(application, clazz);
    }

	@SuppressWarnings("unchecked")
	@Override
    protected T readCacheDataFromFile(File file) throws CacheLoadingException {
        try {
            byte [] bytes = IOUtils.toByteArray(new FileReader(file));
            TBase<TBase<?,?>, TFieldIdEnum> result = (TBase<TBase<?,?>, TFieldIdEnum>)getHandledClass().newInstance();
            deserializer.deserialize(result, bytes);
            return (T)result;
        } catch (FileNotFoundException e) {
            // Should not occur (we test before if file exists)
            // Do not throw, file is not cached
            Ln.w("file " + file.getAbsolutePath() + " does not exists", e);
            return null;
        } catch (Exception e) {
            throw new CacheLoadingException(e);
        }
    }

    @Override
    public T saveDataToCacheAndReturnData(final T data, final Object cacheKey) throws CacheSavingException {

        try {
            if (isAsyncSaveEnabled()) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            saveData(data, cacheKey);
                        } catch (TException e) {
                            Ln.e(e, "An error occured on saving request " + cacheKey + " data asynchronously");
                        } catch (IOException e) {
                            Ln.e(e, "An error occured on saving request " + cacheKey + " data asynchronously");
                        } catch (CacheSavingException e) {
                            Ln.e(e, "An error occured on saving request " + cacheKey + " data asynchronously");
                        }
                    };
                };
                t.start();
            } else {
                saveData(data, cacheKey);
            }
        } catch (CacheSavingException e) {
            throw e;
        } catch (Exception e) {
            throw new CacheSavingException(e);
        }
        return data;
    }

    @SuppressWarnings("unchecked")
	private void saveData(T data, Object cacheKey) throws IOException, CacheSavingException, TException {
        synchronized (getCacheFile(cacheKey).getAbsolutePath().intern()) {
			byte [] bytes = serializer.serialize((TBase<TBase<?,?>, TFieldIdEnum>)data);
        	IOUtils.write(bytes, new FileWriter(getCacheFile(cacheKey)));
        }
    }

}
