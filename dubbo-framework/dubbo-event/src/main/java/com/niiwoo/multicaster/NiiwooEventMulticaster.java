/**   
 * @Title: NiiwooEventMulticaster.java
 * @Package com.niiwoo.event
 * @Description: TODO(用一句话描述该文件做什么)
 * @author seven   
 * @date 2016-8-3 下午2:18:38
 * @version V1.0   
 */

package com.niiwoo.multicaster;

import com.niiwoo.event.NiiwooEvent;
import com.niiwoo.listener.NiiwooEventListener;

/**
 * @ClassName: NiiwooEventMulticaster
 * @Description: 注册事件监听器，发布事件
 * @author seven
 * @date 2016-8-3 下午2:18:38
 * 
 */

public interface NiiwooEventMulticaster {

	/**
	 * 添加监听
	 */
	void addNiiwooListener(NiiwooEventListener<NiiwooEvent> listener);

	/**
	 * 删除监听
	 */
	void removeNiiwooListener(NiiwooEventListener<NiiwooEvent> listener);

	/**
	 * 删除所有监听
	 */
	void removeAllNiiwooListeners();

	/**
	 * 分发事件
	 */
	void multicastEvent(NiiwooEvent event);
}
