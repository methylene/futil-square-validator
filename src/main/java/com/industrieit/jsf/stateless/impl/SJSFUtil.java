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

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

public class SJSFUtil {

	public static boolean isPoolable(UIViewRoot vr)
	{
		boolean ret=false;
		if (vr!=null)
		{
			ret=vr.getAttributes().containsKey(SJSFStatics.POOLABLE);
		}
		return ret;
	}
	
	public static UIComponent getSJSFComponent(UIViewRoot vr)
	{
		for (UIComponent u : vr.getChildren())
	   {
		   if (u.getId().equals(SJSFStatics.SJSF_MARKER))
		   {
			   return u;
		   }
	   }
		return null;
	}
	
/*	public static String getDiscriminatorr(FacesContext fc)
	{
		//System.out.println("discriminator is "+disc);
		String disc=(String) fc.getAttributes().get(SJSFStatics.DISCRIMINATOR_ATTRIB_NAME);
		if (disc==null)
		{
			UIComponent c=SJSFUtil.getSJSFComponent(fc.getViewRoot());
			String d=(String) c.getAttributes().get(SJSFStatics.DISCRIMINATOR);
			if (d==null)
			{
				d="";
			}
			d=d.trim();
			fc.getAttributes().put(SJSFStatics.DISCRIMINATOR, d);
		}
		return disc;
	}*/
}
