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
package com.liferay.faces.adf.base.internal.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author  Neil Griffin
 */
public class LiferayADFBaseLoggingHandler extends DefaultHandler {

	private Logger logger;

	public LiferayADFBaseLoggingHandler(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if ("logger".equals(qName)) {
			String loggerName = attributes.getValue("name");
			String loggerLevel = attributes.getValue("level");
			Logger parsedLogger = Logger.getLogger(loggerName);

			Level level = null;

			try {
				parsedLogger.setLevel(Level.parse(loggerLevel));

				if (logger != null) {
					logger.log(Level.FINE, "logger name=" + loggerName + " level=" + loggerLevel);
				}
			}
			catch (IllegalArgumentException e) {

				if (logger == null) {
					e.printStackTrace();
				}
				else {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}
}
