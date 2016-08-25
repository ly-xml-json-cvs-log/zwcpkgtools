/**   
 * @Title: AbstractNiiwooEventMulticaster.java
 * @Package com.niiwoo.event
 * @Description: TODO(用一句话描述该文件做什么)
 * @author seven   
 * @date 2016-8-3 下午2:22:05
 * @version V1.0   
 */

package com.niiwoo.multicaster.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.niiwoo.event.NiiwooEvent;
import com.niiwoo.listener.NiiwooEventListener;
import com.niiwoo.listener.SmartNiiwooEventListener;
import com.niiwoo.listener.impl.GenericNiiwooEventAdapter;
import com.niiwoo.multicaster.NiiwooEventMulticaster;

/**
 * @ClassName: AbstractNiiwooEventMulticaster
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author seven
 * @date 2016-8-3 下午2:22:05
 * 
 */

public abstract class AbstractNiiwooEventMulticaster implements NiiwooEventMulticaster {
	private Set<NiiwooEventListener<NiiwooEvent>> niiwooEventListeners = new HashSet<>();

	private static final Map<Class<? extends NiiwooEvent>, ListenerRegistry> cachedNiiwooEventListeners = new HashMap<Class<? extends NiiwooEvent>, ListenerRegistry>();

	public Set<NiiwooEventListener<NiiwooEvent>> getNiiwooEventListeners() {
		return niiwooEventListeners;
	}

	public void setNiiwooEventListeners(Set<NiiwooEventListener<NiiwooEvent>> niiwooEventListeners) {
		this.niiwooEventListeners = niiwooEventListeners;
	}
	
	@Override
	public void addNiiwooListener(NiiwooEventListener<NiiwooEvent> listener) {
		getNiiwooEventListeners().add(listener);
	}

	@Override
	public void removeNiiwooListener(NiiwooEventListener<NiiwooEvent> listener) {
		getNiiwooEventListeners().remove(listener);
	}

	@Override
	public void removeAllNiiwooListeners() {
		getNiiwooEventListeners().clear();
	}

	protected List<NiiwooEventListener<NiiwooEvent>> getNiiwooEventListeners(NiiwooEvent event) {
		Class<? extends NiiwooEvent> eventType = event.getClass();
		if (!cachedNiiwooEventListeners.containsKey(eventType)) {
			LinkedList<NiiwooEventListener<NiiwooEvent>> allListeners = new LinkedList<>();
			if (niiwooEventListeners != null && niiwooEventListeners.size() > 0) {
				for (NiiwooEventListener<NiiwooEvent> niiwooEventListener : niiwooEventListeners) {
					if (supportsEvent(niiwooEventListener, eventType)) {
						allListeners.add(niiwooEventListener);
					}
				}
				sortNiiwooEventListener(allListeners);
			}
			ListenerRegistry listenerRegistry = new ListenerRegistry(
					allListeners);
			cachedNiiwooEventListeners.put(eventType, listenerRegistry);
		}
		return cachedNiiwooEventListeners.get(eventType).getNiiwooEventListeners();
	}

	protected boolean supportsEvent(NiiwooEventListener<NiiwooEvent> listener,
			Class<? extends NiiwooEvent> eventType) {
		SmartNiiwooEventListener smartListener = (listener instanceof SmartNiiwooEventListener ? (SmartNiiwooEventListener) listener
				: new GenericNiiwooEventAdapter(listener));
		return (smartListener.supportsEventType(eventType));
	}

	protected void sortNiiwooEventListener(List<NiiwooEventListener<NiiwooEvent>> niiwooEventListeners) {
		Collections.sort(niiwooEventListeners, new Comparator<NiiwooEventListener<NiiwooEvent>>() {
			public int compare(NiiwooEventListener<NiiwooEvent> o1, NiiwooEventListener<NiiwooEvent> o2) {
				if (o1.getOrder() > o2.getOrder()) {
					return 1;
				} else if (o1.getOrder() < o2.getOrder()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}

	private class ListenerRegistry {

		private List<NiiwooEventListener<NiiwooEvent>> niiwooEventListeners;

		public List<NiiwooEventListener<NiiwooEvent>> getNiiwooEventListeners() {
			return niiwooEventListeners;
		}

        private ListenerRegistry(List<NiiwooEventListener<NiiwooEvent>> niiwooEventListeners) {
            this.niiwooEventListeners = niiwooEventListeners;
        }
		
	}
}
