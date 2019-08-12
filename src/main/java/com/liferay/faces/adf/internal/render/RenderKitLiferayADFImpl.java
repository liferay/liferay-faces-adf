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
package com.liferay.faces.adf.internal.render;

import java.io.Writer;

import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;

import org.apache.myfaces.trinidad.util.Service;
import org.apache.myfaces.trinidad.util.Service.Provider;


/**
 * @author  Neil Griffin
 */
public class RenderKitLiferayADFImpl extends RenderKitWrapper implements Service.Provider {

	private RenderKit adfRichRenderKit;
	private RenderKit htmlRenderKit;

	public RenderKitLiferayADFImpl(RenderKit adfRichRenderKit, RenderKit htmlRenderKit) {
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

			return ((Provider) adfRichRenderKit).getService(serviceClass);
		}

		return null;
	}

	@Override
	public RenderKit getWrapped() {
		return adfRichRenderKit;
	}
}
