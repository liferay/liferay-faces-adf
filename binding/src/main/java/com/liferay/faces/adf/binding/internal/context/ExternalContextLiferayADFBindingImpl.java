/**
 * Copyright (c) 2000-2020 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
package com.liferay.faces.adf.binding.internal.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import oracle.adf.model.BindingRequestHandler;


/**
 * @author  Neil Griffin
 */
public class ExternalContextLiferayADFBindingImpl extends ExternalContextWrapper {

	private BindingRequestHandler bindingRequestHandler;
	private ExternalContext wrappedExternalContext;

	public ExternalContextLiferayADFBindingImpl(ExternalContext externalContext) {
		this.wrappedExternalContext = externalContext;

		Object context = externalContext.getContext();

		if (context instanceof PortletContext) {
			this.bindingRequestHandler = new LiferayPortletBindingRequestHandler((PortletContext) context,
					(PortletRequest) externalContext.getRequest(), (PortletResponse) externalContext.getResponse());

			bindingRequestHandler.beginRequest();
		}
	}

	public BindingRequestHandler getBindingRequestHandler() {
		return bindingRequestHandler;
	}

	@Override
	public ExternalContext getWrapped() {
		return wrappedExternalContext;
	}
}
