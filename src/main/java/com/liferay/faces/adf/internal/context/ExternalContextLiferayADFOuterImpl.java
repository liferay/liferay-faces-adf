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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.logging.Level;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.portlet.PortletRequest;

import com.liferay.faces.adf.ResourceServlet;

import oracle.adf.share.logging.ADFLogger;


/**
 * @author  Neil Griffin
 */
public class ExternalContextLiferayADFOuterImpl extends ExternalContextWrapper {

	private static final String ACTION_PHASE_LIFECYCLE = "p_p_lifecycle=1&";
	private static final String VIEW_ID_RENDER_PARAMETER = "_facesViewIdRender=";

	ADFLogger logger = ADFLogger.createADFLogger(ExternalContextLiferayADFOuterImpl.class);

	private ExternalContext wrappedExternalContext;

	public ExternalContextLiferayADFOuterImpl(ExternalContext externalContext) {
		this.wrappedExternalContext = externalContext;
	}

	@Override
	public ExternalContext getWrapped() {
		return wrappedExternalContext;
	}

	@Override
	public void redirect(String url) throws IOException {

		if (url != null) {

			if (url.contains(ACTION_PHASE_LIFECYCLE)) {

				String decodedURL = URLDecoder.decode(url, "UTF-8");

				int pos = decodedURL.indexOf(VIEW_ID_RENDER_PARAMETER);

				if (pos > 0) {
					String viewId = decodedURL.substring(pos + VIEW_ID_RENDER_PARAMETER.length());

					String requestContextPath = getRequestContextPath();

					String redirectURL = super.encodeRedirectURL(viewId, null);

					if (redirectURL.startsWith("/")) {
						redirectURL = requestContextPath + redirectURL;
					}
					else {
						redirectURL = requestContextPath + "/" + redirectURL;
					}

					url = super.encodeActionURL(redirectURL);

					PortletRequest portletRequest = (PortletRequest) getRequest();
					Object bridgeRequestScope = portletRequest.getAttribute(
							"com.liferay.faces.bridge.scope.internal.BridgeRequestScope");

					Class<?> bridgeRequestScopeClass = bridgeRequestScope.getClass();

					try {
						Method setRedirectOccurredMethod = bridgeRequestScopeClass.getMethod("setRedirectOccurred",
								new Class[] { boolean.class });

						setRedirectOccurredMethod.invoke(bridgeRequestScope, Boolean.TRUE);
					}
					catch (Exception e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				}
			}
		}

		super.redirect(url);
	}
}
