/**
 * Copyright (c) 2000-2021 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
package com.liferay.faces.adf.base.internal.render;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.context.RenderingContext;

import oracle.adf.view.rich.render.ClientComponent;


/**
 * @author  Neil Griffin
 */
public class PanelGridLayoutRenderer extends oracle.adfinternal.view.faces.renderkit.rich.PanelGridLayoutRenderer {

	private static final boolean DEBUG_HTML = false;

	@Override
	protected void encodeAll(FacesContext facesContext, RenderingContext renderingContext, UIComponent uiComponent,
		ClientComponent clientComponent, FacesBean facesBean) throws IOException {

		ResponseWriterWrapper responseWriterWrapper = new PanelGridLayoutResponseWriter(
				facesContext.getResponseWriter());

		facesContext.setResponseWriter(responseWriterWrapper);

		super.encodeAll(facesContext, renderingContext, uiComponent, clientComponent, facesBean);

		facesContext.setResponseWriter(responseWriterWrapper.getWrapped());
	}

	private static class PanelGridLayoutResponseWriter extends ResponseWriterWrapper {

		private int indent;
		private String styleClass;
		private int gridCellStackCount;
		private boolean writingDiv;
		private boolean writingGridCell;
		private boolean writingPanelGridLayout;
		private boolean writingRow;
		private ResponseWriter wrappedResponseWriter;
		private int panelGridLayoutStackCount;

		public PanelGridLayoutResponseWriter(ResponseWriter responseWriter) {
			this.wrappedResponseWriter = responseWriter;
		}

		@Override
		public void endElement(String name) throws IOException {

			if (styleClass != null) {
				_writeAttribute("class", styleClass, null);
				styleClass = null;
			}

			writingDiv = false;

			if (writingGridCell) {

				if (gridCellStackCount == 0) {
					writingGridCell = false;
				}
				else {
					gridCellStackCount--;
				}
			}

			if (writingPanelGridLayout) {

				if (panelGridLayoutStackCount == 0) {

					if (writingRow) {

						// Close the previous <div class="row">
						_endElement("div");
						writingRow = false;
					}

					writingPanelGridLayout = false;
				}
				else {
					panelGridLayoutStackCount--;
				}
			}

			_endElement(name);
		}

		@Override
		public ResponseWriter getWrapped() {
			return wrappedResponseWriter;
		}

		@Override
		public void startElement(String name, UIComponent uiComponent) throws IOException {

			if (styleClass != null) {
				_writeAttribute("class", styleClass, null);
				styleClass = null;
			}

			if (writingGridCell) {
				gridCellStackCount++;
			}

			if (writingPanelGridLayout) {
				panelGridLayoutStackCount++;
			}

			writingDiv = "div".equals(name);

			_startElement(name, uiComponent);
		}

		@Override
		public void writeAttribute(String name, Object value, String property) throws IOException {

			boolean ignore = false;

			if (value != null) {

				if (writingDiv) {

					if ("_afrc".equals(name)) {
						writingGridCell = true;

						String[] values = value.toString().split(" ");

						if (values.length >= 1) {

							if (Integer.valueOf(values[0]) == 1) {

								if (writingRow) {

									// Close the previous <div class="row">
									_endElement("div");
								}
								else {

									// Write class="row" on the previous <div>, even though that <div> was intended to
									// represent a grid cell (column).
									_writeAttribute("class", "row", null);

									writingRow = true;

									// Start a new <div class="col">
									_startElement("div", null);
								}
							}
						}

						styleClass = "col";
					}
					else if ("class".equals(name)) {

						if ("af_panelGridLayout".equals(value)) {
							writingPanelGridLayout = true;

							value = "container " + value.toString();
						}
					}
					else if ("style".equals(name)) {

						if (gridCellStackCount <= 1) {
							ignore = true;
						}
					}
				}

				if (!ignore) {

					if ("class".equals(name) && (styleClass != null)) {
						value = styleClass + " " + value.toString();
						styleClass = null;
					}

					_writeAttribute(name, value, property);
				}
			}
		}

		private void _endElement(String name) throws IOException {

			indent -= 2;

			if (DEBUG_HTML) {
				System.err.println("");

				for (int i = 0; i < indent; i++) {
					System.err.print(" ");
				}

				System.err.println("</" + name + ">");
			}

			super.endElement(name);
		}

		private void _startElement(String name, UIComponent uiComponent) throws IOException {

			if (DEBUG_HTML) {
				System.err.println("");

				for (int i = 0; i < indent; i++) {
					System.err.print(" ");
				}

				System.err.print("<" + name);
			}

			super.startElement(name, uiComponent);

			indent += 2;
		}

		private void _writeAttribute(String name, Object value, String property) throws IOException {

			if (DEBUG_HTML) {
				System.err.print(" " + name + "=\"" + value + "\"");
			}

			super.writeAttribute(name, value, property);
		}
	}
}
