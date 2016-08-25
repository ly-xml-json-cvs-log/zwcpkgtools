/**   
* @Title: SmartNiiwooEventListener.java
* @Package com.niiwoo.event
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-8-3 下午5:28:32
* @version V1.0   
*/


package com.niiwoo.listener;

import com.niiwoo.event.NiiwooEvent;

/**
 * @ClassName: SmartNiiwooEventListener
 * @Description: 检查是否支持特定的事件
 * @author seven
 * @date 2016-8-3 下午5:28:32
 * 
 */

public interface SmartNiiwooEventListener extends NiiwooEventListener<NiiwooEvent>{
    /**
     * 是否支持此事件
     *
     * @param eventType
     * @return
     */
    boolean supportsEventType(Class<? extends NiiwooEvent> eventType);
}
