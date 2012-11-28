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

package org.meth4j.jsf;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.industrieit.jsf.stateless.impl.SJSFStatePool;
import com.industrieit.jsf.stateless.impl.SJSFURIBuilder;
import com.industrieit.jsf.stateless.impl.SJSFUtil;

public class SJSFPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	public void beforePhase(PhaseEvent event) {

	}

	public void afterPhase(PhaseEvent event) {
		final FacesContext fc = event.getFacesContext();
		if (!fc.isPostback() && SJSFUtil.isPoolable(fc.getViewRoot())) {
			SJSFStatePool.cache(SJSFURIBuilder.getURI(), fc.getViewRoot());
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}