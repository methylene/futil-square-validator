/*
 * Originally written r. simic
 * 
 * (c) 2011 Industrie It Pty Ltd
 * http://www.industrieit.com/
 *
 * IndustrieIT Pty Ltd licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * The software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.industrieit.jsf.stateless.impl;

import java.util.LinkedList;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;

import org.slf4j.LoggerFactory;

public class SJSFCleanTask implements Runnable {

	private UIViewRoot root;
	private LinkedList<UIViewRoot> cache;
	private long timeIn;

	private static long lastDrop = System.currentTimeMillis();

	private static final int REISSUE_DELAY_MS = 20;
	private static final int DROP_PERIOD_BASE_MS = 60000;
	private static final int MIN_DROP_PERIOD_MS = 2000;

	public SJSFCleanTask(UIViewRoot root, LinkedList<UIViewRoot> cache) {
		if (root.getViewMap() != null) {
			root.getViewMap().clear();
		}
		this.timeIn = System.currentTimeMillis();
		this.root = root;
		this.cache = cache;
	}

	public void run() {
		try {
			if (root.getAttributes().get(SJSFStatics.INPOOL) == null) {
				root.getAttributes().put(SJSFStatics.INPOOL, System.currentTimeMillis());
			}
			try {
				final long del = REISSUE_DELAY_MS - (System.currentTimeMillis() - timeIn);
				if (del > 0) {
					//System.out.println("thread sleeping "+del);
					Thread.sleep(del);
				}
			} catch (final InterruptedException e) {
				//nuthin'
			}
			if (root.getChildCount() == 0) {
				//there is an issue where views come back empty at high load???
				//doesn't happen too often, but trying to track it down
				//we are checking and preventing these being put back in the cache
				LoggerFactory.getLogger(this.getClass()).warn("ERROR - view has come back empty");
				return;
			}
			if (cache.size() > SJSFStatics.NON_CONTRACT_BUFFER) {
				boolean drop = false;
				//allows slow leakage from cache
				synchronized (this.getClass()) {
					final long del = System.currentTimeMillis() - lastDrop;
					/*if (new Random().nextInt(1000)==0)
					{
						System.out.println("DROP "+cache.size()+" "+del+" "+calcDropPeriod());
					}*/
					if (del > calcDropPeriod()) {
						drop = true;
						lastDrop = System.currentTimeMillis();
					}
				}
				if (drop) {
					LoggerFactory.getLogger(this.getClass()).debug(
							"#### dropping view from cache, new size " + cache.size());
					//don't return to cache
					return;
				}
			}
			reset(root);
			synchronized (cache) {
				cache.addLast(root);
			}

		} catch (final Exception e) {
			//e.printStackTrace();
			LoggerFactory.getLogger(this.getClass()).error("error", e);
		}
	}

	/**
	 * the frequency of scaling back the cache increases the larger it gets from
	 * the min buffer size, to a maximum 10 times the base drop frequency
	 * this allows the cache to recover from a request lock of some sort
	 * @return drop period
	 */
	private long calcDropPeriod() {
		long mult = cache.size() / SJSFStatics.NON_CONTRACT_BUFFER;
		if (mult == 0) {
			mult = 1;
		}
		long dropIncrement = DROP_PERIOD_BASE_MS / mult;
		if (dropIncrement < MIN_DROP_PERIOD_MS) {
			dropIncrement = MIN_DROP_PERIOD_MS;
		}
		return dropIncrement;
	}

	private void reset(UIComponent c) {
		for (final UIComponent child : c.getChildren()) {
			if (child instanceof UIInput) {
				final UIInput i = (UIInput) child;
				i.resetValue();
			}
			reset(child);
		}
	}

}
