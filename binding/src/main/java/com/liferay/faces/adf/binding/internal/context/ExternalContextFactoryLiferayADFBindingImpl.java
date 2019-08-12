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
package com.liferay.faces.adf.binding.internal.context;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;


/**
 * @author  Neil Griffin
 */
public class ExternalContextFactoryLiferayADFBindingImpl extends ExternalContextFactory {

	private ExternalContextFactory wrappedExternalContextFactory;

	public ExternalContextFactoryLiferayADFBindingImpl(ExternalContextFactory externalContextFactory) {
		this.wrappedExternalContextFactory = externalContextFactory;
	}

	@Override
	public ExternalContext getExternalContext(Object context, Object request, Object response) throws FacesException {
		return new ExternalContextLiferayADFBindingImpl(wrappedExternalContextFactory.getExternalContext(context,
					request, response));
	}
}