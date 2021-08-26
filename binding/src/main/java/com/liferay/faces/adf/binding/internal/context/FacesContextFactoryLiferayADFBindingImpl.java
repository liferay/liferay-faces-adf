/**
 * Copyright (c) 2000-2021 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
package com.liferay.faces.adf.binding.internal.context;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.portlet.PortletRequest;

import oracle.adf.share.ADFContext;


/**
 * @author  Neil Griffin
 */
public class FacesContextFactoryLiferayADFBindingImpl extends FacesContextFactory {

	private FacesContextFactory wrappedFacesContextFactory;

	public FacesContextFactoryLiferayADFBindingImpl(FacesContextFactory facesContextFactory) {
		this.wrappedFacesContextFactory = facesContextFactory;
	}

	@Override
	public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle)
		throws FacesException {

		return new FacesContextLiferayADFBindingImpl(wrappedFacesContextFactory.getFacesContext(context, request,
					response, lifecycle));
	}
}
