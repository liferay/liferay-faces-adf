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
