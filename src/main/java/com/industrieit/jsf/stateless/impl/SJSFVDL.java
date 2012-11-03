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

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.application.view.FaceletViewHandlingStrategy;

public class SJSFVDL extends FaceletViewHandlingStrategy {

		Logger log = LoggerFactory.getLogger(this.getClass());
	
	   @Override
	    public void buildView(FacesContext ctx, UIViewRoot view)
	    throws IOException {

		   if (!view.getAttributes().containsKey(SJSFStatics.BUILD_MARKER_KEY))
		   {
			   //System.out.println("build view");
			   super.buildView(ctx, view);
			   if (view.getChildCount()==0)
			   {
				   log.error("BUILD ERROR!!!!! - viewroot has no children");
			   }
			   //is it poolable?
			   boolean poolable=false;
			   String discriminator=null;
			   for (UIComponent u : view.getChildren())
			   {
				   if (u.getId().equals(SJSFStatics.SJSF_MARKER))
				   {
					   if (u.getAttributes().containsKey(SJSFStatics.STATELESS))
					   {
						   String str=""+u.getAttributes().get(SJSFStatics.STATELESS);
						   if (str!=null && str.trim().compareToIgnoreCase("true")==0)
						   {
							   poolable = true;
						   }
						   str=""+u.getAttributes().get(SJSFStatics.DISCRIMINATOR);
						   if (str!=null)
						   {
							   discriminator=str;
						   }
					   }
				   }
			   }
			   String uri=SJSFURIBuilder.getURI();
			   if (poolable)
			   {
				   //add a poolable marker
				   log.info("STATELESS JSF URI "+uri+" IS MARKED STATIC AND POOLABLE");
				   view.getAttributes().put(SJSFStatics.POOLABLE, Boolean.TRUE);
				   view.getAttributes().put(SJSFStatics.BUILD_MARKER_KEY, Boolean.TRUE);
				   if (discriminator!=null)
				   {
					   view.getAttributes().put(SJSFStatics.DISCRIMINATOR_ATTRIB_NAME,discriminator);
				   }
			   }
			   else
			   {
				   //System.out.println("!!! WARNING - URI "+uri+" IS NOT MARKED STATIC AND POOLABLE");			   
			   }
			   //mark as built
		   }
	    }

}
