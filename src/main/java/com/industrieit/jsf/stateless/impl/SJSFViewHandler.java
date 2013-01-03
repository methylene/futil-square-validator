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

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;

import com.sun.faces.application.view.MultiViewHandler;

public class SJSFViewHandler extends MultiViewHandler {

	private static final Logger LOG = getLogger(SJSFViewHandler.class);

	@Override public UIViewRoot createView(FacesContext context, String viewId) {
		final String uri = getURI(context);
		UIViewRoot vr = null;
		synchronized (this) {
			final int poolCount = SJSFStatePool.getPoolCount(uri);
			if (poolCount > SJSFStatics.POST_BUFFER) {
				vr = SJSFStatePool.getViewRoot(context, uri);
				if (LOG.isDebugEnabled()) {
					final Object[] olog = new Object[] { viewId, poolCount, context.getCurrentPhaseId() };
					LOG.debug("{}: SJSF cache hit in createView; poolCount={}; phase={}", olog);
				}
				context.setViewRoot(vr);
			} else {
				vr = super.createView(context, viewId);
				if (LOG.isDebugEnabled()) {
					final Object[] olog = new Object[] { viewId, poolCount, context.getCurrentPhaseId() };
					LOG.debug("{}: poolCount < POST_BUFFER in createView; poolCount={}; phase={}", olog);
				}
			}
		}
		return vr;
	}

	@Override public void initView(FacesContext context) throws FacesException {
		final String uri = getURI(context);
		final int poolCount = SJSFStatePool.getPoolCount(uri);
		UIViewRoot vr = null;
		if (context.isPostback() || poolCount > SJSFStatics.POST_BUFFER) {
			vr = SJSFStatePool.getViewRoot(context, uri);
			if (vr != null) {
				context.setViewRoot(vr);
				if (LOG.isDebugEnabled()) {
					final Object[] olog = new Object[] { vr.getViewId(), poolCount, context.getCurrentPhaseId() };
					LOG.debug("{}: SJSF cache hit in initView; poolCount={}; phase={}", olog);
				}
			} else {
				if (LOG.isDebugEnabled()) {
					final Object[] olog = new Object[] { uri, poolCount, context.getCurrentPhaseId() };
					LOG.debug("{}: SJSF cache miss in initView; poolCount={}", olog);
				}
			}
		}
		super.initView(context);
	}

	@Override public void writeState(FacesContext context) throws IOException {

		if (SJSFUtil.isPoolable(context.getViewRoot())) {
			super.writeState(context);
		} else {
			super.writeState(context);
		}
	}

	private String getURI(FacesContext context) {
		final String uri = SJSFURIBuilder.getURI();
//		LOG.debug("uri={}, viewId={}",uri, context.getViewRoot() == null ? "null":  context.getViewRoot().getViewId());
		return uri;
	}

}
