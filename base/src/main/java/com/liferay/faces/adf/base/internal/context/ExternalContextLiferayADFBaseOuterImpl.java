/**
 * Copyright (c) 2000-2022 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.adf.base.internal.context;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.logging.Level;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.portlet.PortletRequest;

import oracle.adf.share.logging.ADFLogger;


/**
 * @author  Neil Griffin
 */
public class ExternalContextLiferayADFBaseOuterImpl extends ExternalContextWrapper {

	private static final String ACTION_PHASE_LIFECYCLE = "p_p_lifecycle=1&";
	private static final String VIEW_ID_RENDER_PARAMETER = "_facesViewIdRender=";

	ADFLogger logger = ADFLogger.createADFLogger(ExternalContextLiferayADFBaseOuterImpl.class);

	private ExternalContext wrappedExternalContext;

	public ExternalContextLiferayADFBaseOuterImpl(ExternalContext externalContext) {
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

					if (bridgeRequestScope != null) {
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
		}

		super.redirect(url);
	}
}
