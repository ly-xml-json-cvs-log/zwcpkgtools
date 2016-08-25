/**   
* @Title: NiiwooEvent.java
* @Package com.niiwoo.event
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2016-8-3 下午2:05:23
* @version V1.0   
*/


package com.niiwoo.event;

import java.util.EventObject;

/**
 * @ClassName: NiiwooEvent
 * @Description: 事件
 * @author seven
 * @date 2016-8-3 下午2:05:23
 * 
 */

public abstract class NiiwooEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public NiiwooEvent(Object object){
		super(object);
	}
}
