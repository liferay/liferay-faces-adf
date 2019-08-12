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
