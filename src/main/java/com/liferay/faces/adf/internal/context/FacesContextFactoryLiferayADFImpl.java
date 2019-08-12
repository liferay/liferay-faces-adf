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


/**
 * @author  Neil Griffin
 */
public class FacesContextFactoryLiferayADFImpl extends FacesContextFactory {

	private FacesContextFactory wrappedFacesContextFactory;

	public FacesContextFactoryLiferayADFImpl(FacesContextFactory facesContextFactory) {
		this.wrappedFacesContextFactory = facesContextFactory;
	}

	@Override
	public FacesContext getFacesContext(Object request, Object response, Object context, Lifecycle lifecycle)
		throws FacesException {
		return new FacesContextLiferayADFImpl(wrappedFacesContextFactory.getFacesContext(request, response, context,
					lifecycle));
	}
}
