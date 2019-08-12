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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * @author  Neil Griffin
 */
public class LiferayADFBaseLoggingInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> classSet, ServletContext servletContext) throws ServletException {

		Logger logger = null;

		try {
			Class<?> clazz = Class.forName("org.slf4j.bridge.SLF4JBridgeHandler");

			Method installMethod = clazz.getMethod("install");
			installMethod.invoke(null, null);

			logger = Logger.getLogger(getClass().getName());
			logger.setLevel(Level.INFO);
			logger.info("Enabled java.util.logging via SLF4J");
		}
		catch (ClassNotFoundException e) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			String dateTime = dateFormat.format(Calendar.getInstance().getTime());
			System.err.println(dateTime + " WARN [" + getClass().getSimpleName() +
				"] Unable to output ADF logs because the jul-to-slf4j bridge is not on the classpath. For more information, see: https://www.slf4j.org/legacy.html#jul-to-slf4j");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		ClassLoader tccl = Thread.currentThread().getContextClassLoader();

		URL resource = tccl.getResource("META-INF/logging.xml");

		if (resource != null) {

			if (logger != null) {
				logger.info("Parsing resource " + resource);
			}

			InputStream inputStream = null;

			SAXParserFactory factory = SAXParserFactory.newInstance();

			try {

				SAXParser saxParser = factory.newSAXParser();
				inputStream = resource.openStream();
				saxParser.parse(inputStream, new LiferayADFBaseLoggingHandler(logger));
			}
			catch (Exception e) {

				if (logger == null) {
					e.printStackTrace();
				}
				else {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			finally {

				if (inputStream != null) {

					try {
						inputStream.close();
					}
					catch (IOException e) {

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
	}
}
