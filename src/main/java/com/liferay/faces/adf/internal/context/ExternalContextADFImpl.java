/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liferay.faces.adf.internal.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;


/**
 * <p>This class (along with {@link com.liferay.faces.adf.ResourceServlet} provides a workaround for an incompatibility
 * between ADF Faces and Liferay Portal. Specifically, ADF Faces will end up requesting the following CSS resource:</p>
 *
 * <pre>
   /o/contextpath/adf/styles/cache/simple-portlet-j6h26r--d-webkit-d-d-ltr-d--p-n-u.css?_xRequiresRewrite=true
 * </pre>
 *
 * <p>The problem is that the CSS-related Liferay Portal servlet filters will activate due to the ".css" suffix. When
 * this happens, Liferay Portal sees "/o/context-path" and assumes that the CSS resource will be loaded from the file
 * system under the context path. In other words, it does not consider the fact that the CSS resource might be coming
 * from a servlet such as the Trinidad {@link org.apache.myfaces.trinidad.webapp.ResourceServlet}. Because of this, a
 * 404 (Not Found) error is returned and the ResourceServlet is not invoked. The workaround is to change the suffix from
 * ".css" to ".adfcss" in order to bypass the Liferay servlet filters.</p>
 *
 * @author  Neil Griffin
 */
public class ExternalContextADFImpl extends ExternalContextWrapper {

	private ExternalContext wrappedExternalContext;

	public ExternalContextADFImpl(ExternalContext externalContext) {
		this.wrappedExternalContext = externalContext;
	}

	@Override
	public String encodeResourceURL(String url) {

		if ((url != null) && url.contains("/adf/styles/cache")) {
			int pos = url.indexOf(".css");

			if (pos > 0) {
				url = url.substring(0, pos) + ".adfcss" + url.substring(pos + 4);
			}
		}

		return super.encodeResourceURL(url);
	}

	@Override
	public ExternalContext getWrapped() {
		return wrappedExternalContext;
	}
}