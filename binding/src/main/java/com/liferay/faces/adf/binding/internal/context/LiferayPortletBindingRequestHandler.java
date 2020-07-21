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
package com.liferay.faces.adf.binding.internal.context;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import oracle.adf.model.BindingContext;
import oracle.adf.model.portlet.PortletBindingRequestHandler;

import oracle.adf.share.ADFContext;
import oracle.adf.share.portlet.PortletADFContext;


/**
 * @author  Neil Griffin
 */
public class LiferayPortletBindingRequestHandler extends PortletBindingRequestHandler {

	private PortletContext portletContext;
	private PortletRequest portletRequest;
	private PortletResponse portletResponse;

	public LiferayPortletBindingRequestHandler(PortletContext portletContext, PortletRequest portletRequest,
		PortletResponse portletResponse) {

		super(portletContext, portletRequest, portletResponse);
		this.portletContext = portletContext;
		this.portletRequest = portletRequest;
		this.portletResponse = portletResponse;
	}

	@Override
	protected BindingContext createBindingContext(ADFContext adfContext) {
		BindingContext bindingContext = super.createBindingContext(adfContext);
		bindingContext.remove("_customeventdispatcher_class_name_");

		return bindingContext;
	}
}
