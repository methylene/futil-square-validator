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

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.sun.faces.application.view.MultiViewHandler;

public class SJSFViewHandler extends MultiViewHandler {

	@Override public UIViewRoot createView(FacesContext context, String viewId) {
		String uri = getURI();
		UIViewRoot vr = null;
		synchronized (this) {
			//System.out.println("vh create view");
			if (SJSFStatePool.getPoolCount(uri) > SJSFStatics.POST_BUFFER) {
				vr = SJSFStatePool.get(uri);
			}
			if (vr != null) {
				context.setViewRoot(vr);
			} else {
				vr = super.createView(context, viewId);
			}
		}
		return vr;
	}

	@Override public void initView(FacesContext context) throws FacesException {
		//System.out.println("vh init view");
		String uri = getURI();
		UIViewRoot vr = null;
		if (context.isPostback() || SJSFStatePool.getPoolCount(uri) > SJSFStatics.POST_BUFFER) {
			vr = SJSFStatePool.get(uri);
		}
		if (vr != null) {
			context.setViewRoot(vr);
		}
		super.initView(context);
	}

	@Override public void writeState(FacesContext context) throws IOException {

		if (SJSFUtil.isPoolable(context.getViewRoot())) {
			super.writeState(context);
		} else {
			//System.out.println("vh write state");
			super.writeState(context);
		}
	}

	private String getURI() {
		return SJSFURIBuilder.getURI();
	}

}
