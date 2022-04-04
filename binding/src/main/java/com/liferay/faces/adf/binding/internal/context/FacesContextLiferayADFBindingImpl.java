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
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;

import oracle.adf.model.BindingRequestHandler;

import oracle.adf.share.ADFContext;


/**
 * @author  Neil Griffin
 */
public class FacesContextLiferayADFBindingImpl extends FacesContextWrapper {

	private ADFContext adfContext;
	private ExternalContext externalContext;
	private FacesContext wrappedFacesContext;

	public FacesContextLiferayADFBindingImpl(FacesContext facesContext) {
		this.wrappedFacesContext = facesContext;
	}

	@Override
	public ExternalContext getExternalContext() {

		if (externalContext == null) {
			externalContext = new ExternalContextLiferayADFBindingImpl(super.getExternalContext());
		}

		return externalContext;
	}

	@Override
	public FacesContext getWrapped() {
		return wrappedFacesContext;
	}

	@Override
	public void release() {

		while (externalContext instanceof ExternalContextWrapper) {
			externalContext = ((ExternalContextWrapper) externalContext).getWrapped();

			if (externalContext instanceof ExternalContextLiferayADFBindingImpl) {
				ExternalContextLiferayADFBindingImpl externalContextLiferayADFInnerImpl =
					(ExternalContextLiferayADFBindingImpl) externalContext;

				BindingRequestHandler bindingRequestHandler =
					externalContextLiferayADFInnerImpl.getBindingRequestHandler();

				if (bindingRequestHandler != null) {
					bindingRequestHandler.endRequest();
				}

				break;
			}
		}

		super.release();
	}
}
