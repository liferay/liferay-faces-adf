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
