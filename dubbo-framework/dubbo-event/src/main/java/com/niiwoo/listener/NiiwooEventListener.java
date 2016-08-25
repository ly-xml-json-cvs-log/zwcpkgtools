/**   
* @Title: NiiwooEventListener.java
* @Package com.niiwoo.event
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-8-3 下午2:11:52
* @version V1.0   
*/


package com.niiwoo.listener;

import java.util.EventListener;

import com.niiwoo.event.NiiwooEvent;

/**
 * @ClassName: NiiwooEventListener
 * @Description: 事件监听
 * @author seven
 * @date 2016-8-3 下午2:11:52
 * 
 */

public interface NiiwooEventListener<E extends NiiwooEvent> extends EventListener{
	/**
	 * 响应事件
	 */
	void onNiwooEvent(E niiwooEvent);
	
    /**
     * 执行的顺序号
     *
     */
    int getOrder();
}
