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
package com.liferay.faces.adf.base.internal.context;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;

import com.liferay.faces.adf.base.servlet.ResourceServlet;


/**
 * <p>This class (along with {@link ResourceServlet} provides a workaround for an incompatibility between ADF Faces and
 * Liferay Portal. Specifically, ADF Faces will end up requesting the following CSS resource:</p>
 *
 * <pre>
   /o/contextpath/adf/styles/cache/simple-portlet-j6h26r--d-webkit-d-d-ltr-d--p-n-u.css?_xRequiresRewrite=true
 * </pre>
 *
 * <p>The problem is that the CSS-related Liferay Portal servlet filters will activate due to the ".css" suffix. When
 * this happens, Liferay Portal sees "/o/context-path" and assumes that the CSS resource will be loaded from the file
 * system under the context path. In other words, it does not consider the fact that the CSS resource might be coming
 * from a servlet such as the Trinidad {@link org.apache.myfaces.trinidad.webapp.ResourceServlet}. Because of this, a
 * 404 (Not Found) error is returned and the ResourceServlet is not invoked. The workaround is to change the suffix from
 * ".css" to ".adfcss" in order to bypass the Liferay servlet filters.</p>
 *
 * @author  Neil Griffin
 */
public class ExternalContextLiferayADFBaseInnerImpl extends ExternalContextWrapper {

	private static final String ACTION_PHASE_LIFECYCLE = "p_p_lifecycle=1&";
	private static final String VIEW_ID_RENDER_PARAMETER = "_facesViewIdRender=";

	private Map<String, String> requestHeaderMap;
	private ExternalContext wrappedExternalContext;

	public ExternalContextLiferayADFBaseInnerImpl(ExternalContext externalContext) {
		this.wrappedExternalContext = externalContext;
	}

	@Override
	public String encodeResourceURL(String url) {

		if ((url != null) && url.contains("/adf/styles/cache")) {
			int pos = url.indexOf(".css");

			if (pos > 0) {
				url = url.substring(0, pos) + ".adfcss" + url.substring(pos + 4);
			}
		}

		return super.encodeResourceURL(url);
	}

	@Override
	public String getInitParameter(String name) {

		String value = super.getInitParameter(name);

		if ("org.apache.myfaces.trinidad.UPLOAD_MAX_FILE_SIZE".equals(name)) {

			String liferayFacesValue = super.getInitParameter("com.liferay.faces.util.uploadedFileMaxSize");

			if (liferayFacesValue == null) {

				liferayFacesValue = super.getInitParameter("com.liferay.faces.bridge.uploadedFileMaxSize");

				if (liferayFacesValue == null) {
					liferayFacesValue = super.getInitParameter("javax.faces.UPLOADED_FILE_MAX_SIZE");
				}
			}

			if (value == null) {
				value = liferayFacesValue;
			}
			else if ((liferayFacesValue != null) && (Long.parseLong(value) > Long.parseLong(liferayFacesValue))) {
				value = liferayFacesValue;
			}
		}

		return value;
	}

	@Override
	public Map<String, String> getRequestHeaderMap() {

		if (requestHeaderMap == null) {

			Map<String, String> requestParameterMap = getRequestParameterMap();

			// If this is an ADF partial request (and not a standard JSF 2.x <f:ajax/> type of partial request) then
			// remove the "Faces-Request" header so that the
			// oracle.adfinternal.view.faces.context.PartialViewContextFactoryImpl.getPartialViewContext(FacesContext)
			// method will know to return an ADF Faces PartialViewContextImpl rather than the Trinidad
			// PartialViewContextImpl.
			if ("true".equals(requestParameterMap.get("_xFacesResource")) &&
					"true".equals(requestParameterMap.get("_xProcessAsRender")) &&
					"true".equals(requestParameterMap.get("Adf-Rich-Message"))) {
				requestHeaderMap = new HashMap<String, String>(super.getRequestHeaderMap());
				requestHeaderMap.remove("Faces-Request");
			}
			else {
				requestHeaderMap = super.getRequestHeaderMap();
			}
		}

		return requestHeaderMap;
	}

	@Override
	public ExternalContext getWrapped() {
		return wrappedExternalContext;
	}
}
