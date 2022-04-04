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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;

import oracle.adf.share.ADFContext;


/**
 * The purpose of this class is to decorate the {@link #getExternalContext()} method in order for compatibility with
 * Liferay Portal. The normal extension point for introducing a custom {@link ExternalContext} in to the
 * chain-of-delegation would be to implement an {@link javax.faces.context.ExternalContextFactory} and to register it in
 * META-INF/faces-config.xml. Unfortunately the Trinidad FacesContextFactoryImpl.CacheRenderKit class contains a
 * constructor that places its own FacesContextFactoryImpl.OverrideDispatch class (which extends ExternalContext) as the
 * outer-most class instance in the chain-of-delegation, thus ignoring the &lt;ordering&gt; element in
 * META-INF/faces-config.xml for the &lt;external-context-factory&gt;
 */
public class FacesContextLiferayADFBaseImpl extends FacesContextWrapper {

	private ADFContext adfContext;
	private FacesContext wrappedFacesContext;

	public FacesContextLiferayADFBaseImpl(FacesContext facesContext, ADFContext adfContext) {
		this.wrappedFacesContext = facesContext;
		this.adfContext = adfContext;
	}

	@Override
	public ExternalContext getExternalContext() {
		return new ExternalContextLiferayADFBaseOuterImpl(super.getExternalContext());
	}

	@Override
	public FacesContext getWrapped() {
		return wrappedFacesContext;
	}

	@Override
	public void release() {

		if (adfContext != null) {
			ADFContext.resetADFContext(adfContext);
		}

		super.release();
	}
}
