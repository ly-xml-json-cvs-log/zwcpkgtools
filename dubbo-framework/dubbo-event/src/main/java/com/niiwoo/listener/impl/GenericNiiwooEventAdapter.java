/**   
* @Title: GenericNiiwooEventAdapter.java
* @Package com.niiwoo.event
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-8-3 下午5:30:42
* @version V1.0   
*/


package com.niiwoo.listener.impl;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.GenericTypeResolver;

import com.niiwoo.event.NiiwooEvent;
import com.niiwoo.listener.NiiwooEventListener;
import com.niiwoo.listener.SmartNiiwooEventListener;

/**
 * @ClassName: GenericNiiwooEventAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author seven
 * @date 2016-8-3 下午5:30:42
 * 
 */

public class GenericNiiwooEventAdapter implements SmartNiiwooEventListener{
	private final NiiwooEventListener<NiiwooEvent> delegate;
	
	public GenericNiiwooEventAdapter(NiiwooEventListener<NiiwooEvent> delegate){
		this.delegate = delegate;
	}
	
	@Override
	public void onNiwooEvent(NiiwooEvent niiwooEvent) {
		this.delegate.onNiwooEvent(niiwooEvent);
	}

	@Override
	public int getOrder() {
		  return Integer.MAX_VALUE;
	}

	@Override
	public boolean supportsEventType(Class<? extends NiiwooEvent> eventType) {
        Class<?> typeArg = GenericTypeResolver.resolveTypeArgument(this.delegate.getClass(), NiiwooEventListener.class);
        if (typeArg == null || typeArg.equals(NiiwooEvent.class)) {
            Class<?> targetClass = AopUtils.getTargetClass(this.delegate);
            if (targetClass != this.delegate.getClass()) {
                typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, NiiwooEventListener.class);
            }
        }
        return (typeArg == null || typeArg.isAssignableFrom(eventType));
	}

}
