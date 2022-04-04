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
package com.liferay.faces.adf.base.internal.render;

import java.io.Writer;

import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;

import org.apache.myfaces.trinidad.util.Service;


/**
 * @author  Neil Griffin
 */
public class RenderKitLiferayADFBaseImpl extends RenderKitWrapper implements Service.Provider {

	private RenderKit adfRichRenderKit;
	private RenderKit htmlRenderKit;

	public RenderKitLiferayADFBaseImpl(RenderKit adfRichRenderKit, RenderKit htmlRenderKit) {
		this.adfRichRenderKit = adfRichRenderKit;
		this.htmlRenderKit = htmlRenderKit;
	}

	@Override
	public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
		return htmlRenderKit.createResponseWriter(writer, contentTypeList, characterEncoding);
	}

	@Override
	public <T> T getService(Class<T> serviceClass) {

		if (adfRichRenderKit instanceof Service.Provider) {

			Service.Provider serviceProvider = (Service.Provider) adfRichRenderKit;

			return serviceProvider.getService(serviceClass);
		}

		return null;
	}

	@Override
	public RenderKit getWrapped() {
		return adfRichRenderKit;
	}
}
