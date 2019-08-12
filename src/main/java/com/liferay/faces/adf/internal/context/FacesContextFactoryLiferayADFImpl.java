/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
package com.liferay.faces.adf.internal.context;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.portlet.PortletRequest;

import oracle.adf.share.ADFContext;


/**
 * @author  Neil Griffin
 */
public class FacesContextFactoryLiferayADFImpl extends FacesContextFactory {

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

	public FacesContextFactoryLiferayADFImpl(FacesContextFactory facesContextFactory) {
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

		return new FacesContextLiferayADFImpl(wrappedFacesContextFactory.getFacesContext(context, request, response,
					lifecycle), adfContext);
	}
}
