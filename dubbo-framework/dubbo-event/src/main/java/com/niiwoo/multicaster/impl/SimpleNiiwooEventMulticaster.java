/**   
 * @Title: SimpleNiiwooEventMulticaster.java
 * @Package com.niiwoo.event
 * @Description: TODO(用一句话描述该文件做什么)
 * @author seven   
 * @date 2016-8-3 下午5:40:02
 * @version V1.0   
 */

package com.niiwoo.multicaster.impl;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niiwoo.event.NiiwooEvent;
import com.niiwoo.listener.NiiwooEventListener;

/**
 * @ClassName: SimpleNiiwooEventMulticaster
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author seven
 * @date 2016-8-3 下午5:40:02
 * 
 */

public class SimpleNiiwooEventMulticaster extends
		AbstractNiiwooEventMulticaster {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Executor executor;

	@Override
	public void multicastEvent(final NiiwooEvent event) {
		try {
			for (final NiiwooEventListener<NiiwooEvent> listener : getNiiwooEventListeners(event)) {
				Executor executor = getExecutor();
				if (executor != null) {
					executor.execute(new Runnable() {
						public void run() {
							listener.onNiwooEvent(event);
						}
					});
				} else {
					listener.onNiwooEvent(event);
				}
			}
		} catch (Exception e) {
			logger.error("处理" + event.getClass().getName() + "事件发生异常", e);
		}
	}
	
	public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

}
