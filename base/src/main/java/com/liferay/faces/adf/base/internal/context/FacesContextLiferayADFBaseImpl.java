/**
 * Copyright (c) 2000-2020 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
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
