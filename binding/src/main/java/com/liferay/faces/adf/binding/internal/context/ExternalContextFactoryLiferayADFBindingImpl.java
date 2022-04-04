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
