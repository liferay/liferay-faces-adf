/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liferay.faces.adf.internal.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextWrapper;


/**
 * The purpose of this class is to decorate the {@link #getExternalContext()} method in order for compatibility with
 * Liferay Portal. The normal extension point for introducing a custom {@link ExternalContext} in to the
 * chain-of-delegation would be to implement an {@link javax.faces.context.ExternalContextFactory} and to register it in
 * META-INF/faces-config.xml. Unfortunately the Trinidad FacesContextFactoryImpl.CacheRenderKit class contains a
 * constructor that places its own FacesContextFactoryImpl.OverrideDispatch class (which extends ExternalContext) as the
 * outer-most class instance in the chain-of-delegation, thus ignoring the <ordering> element in
 * META-INF/faces-config.xml for the <external-context-factory>.
 */
public class FacesContextLiferayADFImpl extends FacesContextWrapper {

	private FacesContext wrappedFacesContext;

	public FacesContextLiferayADFImpl(FacesContext facesContext) {
		this.wrappedFacesContext = facesContext;
	}

	@Override
	public ExternalContext getExternalContext() {
		return new ExternalContextLiferayADFOuterImpl(super.getExternalContext());
	}

	@Override
	public FacesContext getWrapped() {
		return wrappedFacesContext;
	}
}