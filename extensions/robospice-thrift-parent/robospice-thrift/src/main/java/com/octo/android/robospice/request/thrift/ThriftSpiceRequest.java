package com.octo.android.robospice.request.thrift;

import org.apache.thrift.TBase;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * @author Vitaly Tsaplin
 *
 */
public abstract class ThriftSpiceRequest<T extends TBase<?, ?>> extends SpiceRequest<T> {

	private Context context;
	
    public ThriftSpiceRequest(Class<T> clz) {
        super(clz);
    }

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
