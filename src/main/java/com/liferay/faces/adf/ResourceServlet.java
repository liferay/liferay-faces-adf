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
package com.liferay.faces.adf;

import javax.servlet.http.HttpServletRequest;


/**
 * The following should be placed in the WEB-INF/web.xml descriptor (instead of the normal Trinidad {@link
 * org.apache.myfaces.trinidad.webapp.ResourceServlet}) in order to activate the workaround described in {@link
 * com.liferay.faces.adf.internal.ExternalContextADFImpl}:
 *
 * <pre>
    <servlet>
        <servlet-name>resources</servlet-name>
        <servlet-class>com.liferay.faces.adf.ResourceServlet</servlet-class>
    </servlet>
 *     </pre>
 *
 * @author  Neil Griffin
 */
public class ResourceServlet extends org.apache.myfaces.trinidad.webapp.ResourceServlet {

	@Override
	protected String getResourcePath(HttpServletRequest httpServletRequest) {
		String resourcePath = super.getResourcePath(httpServletRequest);

		if (resourcePath != null) {
			int pos = resourcePath.indexOf(".adfcss");

			if (pos > 0) {
				resourcePath = resourcePath.substring(0, pos) + ".css" + resourcePath.substring(pos + 7);
			}
		}

		return resourcePath;
	}
}
