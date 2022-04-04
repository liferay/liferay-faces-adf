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
