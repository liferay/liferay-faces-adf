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

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.portlet.PortletRequest;

import oracle.adf.share.ADFContext;


/**
 * @author  Neil Griffin
 */
public class FacesContextFactoryLiferayADFBaseImpl extends FacesContextFactory {

	private static Boolean initADFContext;

	static {

		boolean init = true;

		try {
			Class.forName("oracle.adfinternal.view.faces.config.rich.FacesDataBindingConfigurator");
			init = false;
		}
		catch (ClassNotFoundException e) {
			// Ignore
		}

		initADFContext = init;
	}

	private FacesContextFactory wrappedFacesContextFactory;

	public FacesContextFactoryLiferayADFBaseImpl(FacesContextFactory facesContextFactory) {
		this.wrappedFacesContextFactory = facesContextFactory;
	}

	@Override
	public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle)
		throws FacesException {

		ADFContext adfContext = null;

		if (initADFContext && (request instanceof PortletRequest)) {
			PortletRequest portletRequest = (PortletRequest) request;
			adfContext = ADFContext.initADFContext(context, portletRequest.getPortletSession(false), portletRequest,
					response);
		}

		return new FacesContextLiferayADFBaseImpl(wrappedFacesContextFactory.getFacesContext(context, request, response,
					lifecycle), adfContext);
	}
}
