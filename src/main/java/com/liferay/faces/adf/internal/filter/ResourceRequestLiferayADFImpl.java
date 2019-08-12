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
package com.liferay.faces.adf.internal.filter;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.filter.ResourceRequestWrapper;


/**
 * @author  Neil Griffin
 */
public class ResourceRequestLiferayADFImpl extends ResourceRequestWrapper {

	private Map<String, String[]> parameterMap = null;

	public ResourceRequestLiferayADFImpl(ResourceRequest resourceRequest) {
		super(resourceRequest);

		// The oracle.adfinternal.view.faces.renderkit.rich.FormRenderer.encodeAll(FacesContext, RenderingContext,
		// UIComponent, ClientComponent, FacesBean) method renders a parital action type of URL by calling the
		// oracle.adfinternal.view.faces.util.rich.RichPostbackUtils.encodeRichPortletUrl(String actionURL) method which
		// adds the "_xFacesResource", "_xProcessAsRender", and "Adf-Rich-Message" URL parameters in order to setup a
		// URL that can be used via XHR to invoke an ADF-style partial request. This is different than the standard JSF
		// 2.x <f:ajax/> type of request even though the XHR response is a <partial-response>...</partial-response>
		// document.
		if ("true".equals(resourceRequest.getParameter("_xFacesResource")) &&
				"true".equals(resourceRequest.getParameter("_xProcessAsRender")) &&
				"true".equals(resourceRequest.getParameter("Adf-Rich-Message"))) {

			// Trick the bridge into thinking that "?_jsfBridgeAjax=true&_jsfBridgePartial=true" was present on the
			// partial action URL. Normally this would be done by decorating the renderer, but ADF Faces didn't provide
			// an easy way to do that. See also
			// com.liferay.faces.adf.internal.context.ExternalContextLiferayADFInnerImpl.getRequestHeaderMap() for an
			// associated fix for helping ADF Faces determine whether or not an XHR is for an ADF partial request or a
			// standard JSF 2.x <f:ajax/> type of request.
			parameterMap = new HashMap<String, String[]>(resourceRequest.getParameterMap());
			parameterMap.put("_jsfBridgeAjax", new String[] { "true" });
			parameterMap.put("_jsfBridgePartial", new String[] { "true" });
		}
	}

	@Override
	public String getParameter(String name) {

		if ((parameterMap != null) && "_jsfBridgeAjax".equals(name)) {

			String[] values = parameterMap.get(name);

			if ((values != null) && (values.length > 0)) {
				return values[0];
			}

			return null;
		}

		return super.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {

		if (parameterMap == null) {
			return super.getParameterMap();
		}

		return parameterMap;
	}
}
