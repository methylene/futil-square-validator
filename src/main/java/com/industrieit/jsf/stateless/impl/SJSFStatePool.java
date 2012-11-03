/*
 * Originally written r. simic
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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rits.cloning.Cloner;

public class SJSFStatePool {

	private static Map<String, LinkedList<UIViewRoot>> ppool=new Hashtable<String, LinkedList<UIViewRoot>>();
	private static Map<String,String> discriminatorMap=new Hashtable<String, String>();
	
	private static Logger log=LoggerFactory.getLogger(SJSFStatePool.class);
	private static ExecutorService exServ;
	
	static {
		ThreadFactory tf=new ThreadFactory() {
			
			public Thread newThread(Runnable r) {
				Thread t=new Thread(r);
				t.setDaemon(true);
				return t;
			}
		};
		
		BlockingQueue<Runnable> q=new  LinkedBlockingQueue<Runnable>(250);
		exServ=new ThreadPoolExecutor(1, 3, 30l, TimeUnit.SECONDS, q,tf,new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	public static synchronized UIViewRoot get(String uri)
	{
		String discrimValue=getDiscriminatorValue(uri, null);
		LinkedList<UIViewRoot> items=getPoolForKey(uri, discrimValue);

		UIViewRoot v=null;
		synchronized(items)
		{
			if (items.isEmpty())
			{
				return null;
			}
			if (items.size()==1)
			{
				try {
					//clone
					Cloner cloner=new Cloner();
					v=cloner.deepClone(items.getFirst());
					log.warn("cloning..."+v.getViewId()+".....");
				} catch (Exception e) {
					log.error("error force cloning view on postback",e);
				}
			}
			else
			{
				v= (UIViewRoot) items.removeFirst();
			}
		}
		//long intime=(Long) v.getAttributes().get(SJSFStatics.INPOOL);
		//System.out.println("delay "+(System.currentTimeMillis()-intime));
		return v;
	}
	
	public static synchronized void cache(String uri, UIViewRoot v)
	{
		String discrimValue=getDiscriminatorValue(uri, v);
		LinkedList<UIViewRoot> items=getPoolForKey(uri, discrimValue);
		SJSFCleanTask t=new SJSFCleanTask(v,items);
		exServ.submit(t);
	}

	public static synchronized void clearPool()
	{
		ppool=new HashMap<String, LinkedList<UIViewRoot>>();
	}
	
	private static LinkedList<UIViewRoot> getPoolForKey(String uri, String discrim)
	{
		String k=constructCompositeKey(uri, discrim);
		LinkedList<UIViewRoot> items=(LinkedList<UIViewRoot>) ppool.get(k);
		if (items==null)
		{
			items=new LinkedList<UIViewRoot>();
			ppool.put(k, items);
		}
		return items;
	}
	
	public static int getPoolCount(String uri)
	{
		String discrimValue=getDiscriminatorValue(uri, null);
		LinkedList<UIViewRoot> items=getPoolForKey(uri, discrimValue);
		return items.size();
	}
	
	private static String getDiscriminatorValue(String uri, UIViewRoot v)
	{
		FacesContext fc=FacesContext.getCurrentInstance();
		String cached=(String) fc.getAttributes().get(SJSFStatics.DISCRIMINATOR_ATTRIB_NAME);
		if (cached!=null)
		{
			return cached;
		}
		//get the disciminator
		String discrim=discriminatorMap.get(uri);
		String discrimValue="";
		if (discrim==null)
		{
			//System.out.println("initialising discrim for "+uri);
			if (v==null)
			{
				return "";
			}
			//not set yet
			discrim=(String) v.getAttributes().get(SJSFStatics.DISCRIMINATOR_ATTRIB_NAME);
			if (discrim==null)
			{
				return "";
			}
			else
			{
				discrim="#{"+discrim+"}";
			}
			discriminatorMap.put(uri, discrim );
		}
		if (discrim.length()!=0)
		{
			ValueExpression ve=ExpressionFactory.newInstance().createValueExpression(fc.getELContext(),discrim , String.class);
			discrimValue=(String)ve.getValue(fc.getELContext());
		}
		fc.getAttributes().put(SJSFStatics.DISCRIMINATOR_ATTRIB_NAME, discrimValue);
		return discrimValue;
	}
	
	private static String constructCompositeKey(String uri, String discriminator)
	{
		if (discriminator==null || discriminator.length()==0)
		{
			return uri;
		}
		else
		{
			StringBuilder sb=new StringBuilder();
			sb.append(uri).append("-").append(discriminator);
			return sb.toString();
		}
	}
	
	public static Map<String, Integer> getCacheStats()
	{
		HashMap<String, Integer> ret=new HashMap<String, Integer>();
		for (String k : ppool.keySet())
		{
			ret.put(k, ppool.get(k).size());
		}
		return ret;
	}

}
