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
package com.liferay.faces.adf.base.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.liferay.faces.adf.base.internal.context.ExternalContextLiferayADFBaseInnerImpl;

import oracle.adf.share.logging.ADFLogger;


/**
 * The following should be placed in the WEB-INF/web.xml descriptor (instead of the normal Trinidad {@link
 * org.apache.myfaces.trinidad.webapp.ResourceServlet}) in order to activate the workaround described in {@link
 * ExternalContextLiferayADFBaseInnerImpl}:
 *
 * <pre>
    &lt;servlet&gt;
        &lt;servlet-name&gt;resources&lt;/servlet-name&gt;
        &lt;servlet-class&gt;com.liferay.faces.adf.base.servlet.ResourceServlet&lt;/servlet-class&gt;
    &lt;/servlet&gt;
 *     </pre>
 *
 * @author  Neil Griffin
 */
public class ResourceServlet extends org.apache.myfaces.trinidad.webapp.ResourceServlet {

	private static final String FIND_PARENT_FORM =
		"while (domElement.nodeName != \"FORM\" && domElement.parentNode){domElement=domElement.parentNode;console.log('parent='+domElement);}";

	private static final String[] BG_JS_SEARCH_TOKENS = new String[] {
			"addElementToLayer(a,this.GetFloatingType(),this.GetFirstFormId())", "p=this.GetFirstFormId()",
			"GetFirstFormId=function(){var a=AdfPage.PAGE.getDomDocument();return a.forms&&0<a.forms.length&&a.forms[0].id?a.forms[0].id:null};"
		};

	private static final String[] BG_JS_REPLACE_TOKENS = new String[] {
			"addElementToLayer(a,this.GetFloatingType(),this.GetFirstFormId(a))", "p=this.GetFirstFormId(k)",
			"GetFirstFormId=function(domElement){console.log(domElement);" + FIND_PARENT_FORM + ";return domElement;};"
		};

	private static final String[] BOOT_JS_SEARCH_TOKENS = new String[] {
			"d=this.getDomDocument().forms[0];",
			"var e=this.getDomDocument().forms[0];AdfAssert.assertDomElement(e);e=e.elements[AdfDhtmlPage._STATE_PARAM_NAME];AdfAssert.assertDomElement(e);e.parentNode.innerHTML=\"\\x3cinput type\\x3d'hidden' name\\x3d'\"+AdfDhtmlPage._STATE_PARAM_NAME+\"' value\\x3d'\"+d.getAttribute(\"value\")+\"' /\\x3e\"",
		};

	private static final String[] BOOT_JS_REPLACE_TOKENS = new String[] {
			"domElement=document.getElementById(b[0]);" + FIND_PARENT_FORM + "d=domElement;",
			"var formElements=this.getDomDocument().forms;" + "for (var i=0; i < formElements.length; i++){" +
			"  var viewStateInput=formElements[i].elements[AdfDhtmlPage._STATE_PARAM_NAME];" +
			"  if (viewStateInput) {" + "    viewStateInput.value=d.getAttribute(\"value\");" + "  }" + "}"
		};

	private static final String[] CORE_JS_SEARCH_TOKENS = new String[] {
			"k.style.width=\"100%\";k.style.tableLayout=l?\"auto\":\"fixed\"",
			"k.style.width=\"100%\",k.style.tableLayout=l?\"auto\":\"fixed\"",
			"f.style.width=\"100%\";f.style.tableLayout=d?\"auto\":\"fixed\""
		};

	private static final String[] CORE_JS_REPLACE_TOKENS = new String[] {
			"k.style.tableLayout=l?\"auto\":\"fixed\"", "k.style.tableLayout=l?\"auto\":\"fixed\"",
			"f.style.tableLayout=d?\"auto\":\"fixed\""
		};

	private static final String[] RESIZE_JS_SEARCH_TOKENS = new String[] {
			"AdfDhtmlPanelGridLayoutPeer.prototype.needsResizeNotify=function(a){return!0};"
		};

	private static final String[] RESIZE_JS_REPLACE_TOKENS = new String[] {
			"AdfDhtmlPanelGridLayoutPeer.prototype.needsResizeNotify=function(a){return 0};"
		};

	ADFLogger logger = ADFLogger.createADFLogger(ResourceServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		IOException {

		String resourcePath = getResourcePath(request);

		// Example: "/afr/partition/unknown/n/default/opt/d/background-d6un3v.js"
		if (resourcePath.contains("/background-") && resourcePath.endsWith(".js")) {
			FilteredResponse filteredResponse = new FilteredResponse(response, BG_JS_SEARCH_TOKENS,
					BG_JS_REPLACE_TOKENS);
			super.doGet(request, filteredResponse);
			filteredResponse.flushBuffer();
		}

		// Example: "/afr/partition/webkit/n/default/opt/d/boot-q1lgy6.js"
		else if (resourcePath.contains("/boot-") && resourcePath.endsWith(".js")) {
			FilteredResponse filteredResponse = new FilteredResponse(response, BOOT_JS_SEARCH_TOKENS,
					BOOT_JS_REPLACE_TOKENS);
			super.doGet(request, filteredResponse);
			filteredResponse.flushBuffer();
		}

		// Example: "/afr/partition/webkit/n/default/opt/d/core-dxjz8o.js"
		else if (resourcePath.contains("/core-") && resourcePath.endsWith(".js")) {
			FilteredResponse filteredResponse = new FilteredResponse(response, CORE_JS_SEARCH_TOKENS,
					CORE_JS_REPLACE_TOKENS);
			super.doGet(request, filteredResponse);
			filteredResponse.flushBuffer();
		}

		// Example: "/afr/partition/unknown/n/default/opt/d/grid-iluwbs.js"
		else if (resourcePath.endsWith(".js")) {
			FilteredResponse filteredResponse = new FilteredResponse(response, RESIZE_JS_SEARCH_TOKENS,
					RESIZE_JS_REPLACE_TOKENS);
			super.doGet(request, filteredResponse);
			filteredResponse.flushBuffer();
		}
		else {
			super.doGet(request, response);
		}
	}

	@Override
	protected String getResourcePath(HttpServletRequest httpServletRequest) {
		String resourcePath = super.getResourcePath(httpServletRequest);

		if (resourcePath != null) {
			int pos = resourcePath.indexOf(".adfcss");

			if (pos > 0) {
				resourcePath = resourcePath.substring(0, pos) + ".css" + resourcePath.substring(pos + 7);
			}
		}

		return resourcePath;
	}

	private class FilteredResponse extends HttpServletResponseWrapper {

		private ServletOutputStream servletOutputStream;

		public FilteredResponse(HttpServletResponse httpServletResponse, String[] searchTokens,
			String[] replaceTokens) {
			super(httpServletResponse);

			try {
				this.servletOutputStream = new FilteringServletOutputStream(httpServletResponse.getOutputStream(),
						searchTokens, replaceTokens);
			}
			catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		@Override
		public void flushBuffer() throws IOException {
			servletOutputStream.flush();
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			return servletOutputStream;
		}
	}

	private class FilteringServletOutputStream extends ServletOutputStream {

		private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		private String[] searchTokens;
		private ServletOutputStream servletOutputStream;
		private String[] replaceTokens;

		public FilteringServletOutputStream(ServletOutputStream servletOutputStream, String[] searchTokens,
			String[] replaceTokens) {
			this.servletOutputStream = servletOutputStream;
			this.searchTokens = searchTokens;
			this.replaceTokens = replaceTokens;
		}

		@Override
		public void flush() throws IOException {

			String text = byteArrayOutputStream.toString();

			for (int i = 0; i < searchTokens.length; i++) {
				String searchToken = searchTokens[i];
				String replaceToken = replaceTokens[i];

				int pos = text.indexOf(searchToken);

				while (pos > 0) {

					text = text.substring(0, pos) + replaceToken + text.substring(pos + searchToken.length());

					pos = text.indexOf(searchToken);
				}
			}

			PrintWriter printWriter = new PrintWriter(servletOutputStream);
			printWriter.write(text);
			printWriter.flush();
		}

		@Override
		public String toString() {
			return byteArrayOutputStream.toString();
		}

		@Override
		public void write(int b) throws IOException {
			byteArrayOutputStream.write(b);
		}
	}
}
