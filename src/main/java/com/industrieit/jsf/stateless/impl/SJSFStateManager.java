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

import java.io.IOException;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class SJSFStateManager extends com.sun.faces.application.StateManagerImpl {

	@Override
	public String getViewState(FacesContext context) {
		if (SJSFUtil.isPoolable(context.getViewRoot()))
		{
		     return SJSFStatics.STATELESS_VIEW_ID;
		}
		else 
		{
			//System.out.println("getview");
			return super.getViewState(context);
		}
	}

	@Override
	public boolean isSavingStateInClient(FacesContext context) {
		if (SJSFUtil.isPoolable(context.getViewRoot()))
		{
			return false;
		}
		else
		{
			//System.out.println("is ssclient");
			return super.isSavingStateInClient(context);
		}
	}

	@Override
	public Object saveView(FacesContext context) {
		if (SJSFUtil.isPoolable(context.getViewRoot()))
		{
			return "SJSF";
		}
		else
		{
			//System.out.println("save view");
			return super.saveView(context);
		}
	}


	@Override
	public void writeState(FacesContext context, Object state)
			throws IOException {
		
		if (SJSFUtil.isPoolable(context.getViewRoot()))
		{
	        ResponseWriter writer = context.getResponseWriter();
	        
	        writer.write("<input type=\"hidden\" name=\"javax.faces.ViewState\" id=\"javax.faces.ViewState\" value=\"");
	        writer.write(SJSFStatics.STATELESS_VIEW_ID);
	        writer.write("\" autocomplete=\"off\" />");	        
		}
		else
		{
			//System.out.println("write");
			super.writeState(context, state);
		}
	}

	
	@Override
	public void writeState(FacesContext context, SerializedView state)
			throws IOException {
		if (SJSFUtil.isPoolable(context.getViewRoot()))
		{
			//nothing
		}
		else
		{
			//System.out.println("write 2");
			super.writeState(context, state);
		}
	}

	public String getURI()
	{
		return SJSFURIBuilder.getURI();
	}
	
	@Override
	public UIViewRoot restoreView(FacesContext context, String viewId,
			String renderKitId) {
		UIViewRoot vr=null;
		if (SJSFUtil.isPoolable(context.getViewRoot()))
		{
			String uri=getURI();
			vr=SJSFStatePool.get(uri);
		}
		else
		{
			//System.out.println("restore view");
			vr= super.restoreView(context, viewId, renderKitId);
		}
		return vr;
	}	
	
}
