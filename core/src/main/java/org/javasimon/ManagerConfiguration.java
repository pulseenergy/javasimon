package org.javasimon;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Holds configuration for one Simon Manager. Configuration is read from the stream
 * and it is merged with any existing configuration read before. Method {@link #clear()}
 * must be used in order to reset this configuration object.
 * <p/>
 * Every {@link org.javasimon.Manager} holds its own configuration and programmer has
 * to take care of the initialization of the configuration. Default {@link org.javasimon.SimonManager}
 * is privileged and can be configured via file or resource when Java property {@code javasimon.config.file}
 * (constant {@link org.javasimon.SimonManager#PROPERTY_CONFIG_FILE_NAME})
 * or {@code javasimon.config.resource} (constant
 * {@link org.javasimon.SimonManager#PROPERTY_CONFIG_RESOURCE_NAME}) is used.
 * <p/>
 * <b>Structure of the configuration XML:</b>
 * <pre>{@literal
 * <simon-configuration>
 * ... TODO
 * </simon-configuration>}</pre>
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
public final class ManagerConfiguration {
	private Map<SimonPattern, SimonConfiguration> configs;

	private final Manager manager;

	/**
	 * Creates manager configuration for a specified manager.
	 *
	 * @param manager manager on whos behalf this configuration is created
	 */
	ManagerConfiguration(Manager manager) {
		this.manager = manager;
		clear();
	}

	/**
	 * Clears any previously loaded configuration.
	 */
	public void clear() {
		configs = new LinkedHashMap<SimonPattern, SimonConfiguration>();
	}

	/**
	 * Reads config from provided buffered reader. Package level because of tests.
	 *
	 * @param reader reader containing configuration
	 * @throws IOException thrown if problem occurs while reading from the reader
	 */
	public void readConfig(Reader reader) throws IOException {
		try {
			XMLStreamReader xr = XMLInputFactory.newInstance().createXMLStreamReader(reader);
			try {
				while (!xr.isStartElement()) {
					xr.next();
				}
				processStartElement(xr, "simon-configuration");
				while (true) {
					if (isStartTag(xr, "callback")) {
						manager.callback().addCallback(processCallback(xr));
					} else if (isStartTag(xr, "filter-callback")) {
						manager.callback().addCallback(processFilterCallback(xr));
					} else if (isStartTag(xr, "simon")) {
						processSimon(xr);
					} else {
						break;
					}
				}
				assertEndTag(xr, "simon-configuration");
			} finally {
				xr.close();
			}
		} catch (XMLStreamException e) {
			manager.callback().warning(null, e);
		} catch (SimonException e) {
			manager.callback().warning(e.getMessage(), e);
		}
	}

	private Callback processCallback(XMLStreamReader xr) throws XMLStreamException {
		Map<String, String> attrs = processStartElement(xr, "callback");
		String klass = attrs.get("class");
		if (klass == null) {
			klass = CompositeCallback.class.getName();
		}
		Callback callback;
		try {
			callback = (Callback) Class.forName(klass).newInstance();
		} catch (InstantiationException e) {
			throw new SimonException(e);
		} catch (IllegalAccessException e) {
			throw new SimonException(e);
		} catch (ClassNotFoundException e) {
			throw new SimonException(e);
		} catch (ClassCastException e) {
			throw new SimonException(e);
		}

		processSetAndCallbacks(xr, callback);
		processEndElement(xr, "callback");
		return callback;
	}

	private Callback processFilterCallback(XMLStreamReader xr) throws XMLStreamException {
		Map<String, String> attrs = processStartElement(xr, "filter-callback");
		String klass = attrs.get("class");
		if (klass == null) {
			klass = CompositeFilterCallback.class.getName();
		}
		FilterCallback callback;
		try {
			callback = (FilterCallback) Class.forName(klass).newInstance();
		} catch (InstantiationException e) {
			throw new SimonException(e);
		} catch (IllegalAccessException e) {
			throw new SimonException(e);
		} catch (ClassNotFoundException e) {
			throw new SimonException(e);
		} catch (ClassCastException e) {
			throw new SimonException(e);
		}

		while (isStartTag(xr, "rule")) {
			processRule(xr, callback);
		}
		processSetAndCallbacks(xr, callback);
		processEndElement(xr, "filter-callback");
		return callback;
	}

	private void processSetAndCallbacks(XMLStreamReader xr, Callback callback) throws XMLStreamException {
		while (isStartTag(xr, "set")) {
			processSet(xr, callback);
		}
		while (true) {
			if (isStartTag(xr, "callback")) {
				callback.addCallback(processCallback(xr));
			} else if (isStartTag(xr, "filter-callback")) {
				callback.addCallback(processFilterCallback(xr));
			} else {
				break;
			}
		}
	}

	private void processRule(XMLStreamReader xr, FilterCallback callback) throws XMLStreamException {
		String pattern = null;
		FilterCallback.Rule.Type type = FilterCallback.Rule.Type.SUFFICE;
		String condition = null;
		List<Callback.Event> events = new ArrayList<Callback.Event>();

		Map<String, String> attrs = processStartElement(xr, "rule");
		if (attrs.get("condition") != null) {
			condition = attrs.get("condition");
		}
		if (attrs.get("type") != null) {
			type = FilterCallback.Rule.Type.valueOf(toEnum(attrs.get("type")));
		}
		if (attrs.get("pattern") != null) {
			pattern = attrs.get("pattern");
		}
		if (attrs.get("events") != null) {
			String[] sa = attrs.get("events").trim().split(" *, *");
			for (String eventName : sa) {
				events.add(Callback.Event.forCode(eventName.toLowerCase()));
			}
		}
		if (isStartTag(xr, "condition")) {
			xr.next();
			condition = getText(xr);
			processEndElement(xr, "condition");
		}
		processEndElement(xr, "rule");
		callback.addRule(type, condition, pattern, events.toArray(new Callback.Event[events.size()]));
	}

	private void processSet(XMLStreamReader xr, Callback callback) throws XMLStreamException {
		Map<String, String> attrs = processStartElement(xr, "set", "property");
		setProperty(callback, attrs.get("property"), attrs.get("value"));
		processEndElement(xr, "set");
	}

	/**
	 * Sets the callback property.
	 *
	 * @param callback callback object
	 * @param property name of the property
	 * @param value value of the property
	 */
	private void setProperty(Callback callback, String property, String value) {
		try {
			if (value != null) {
				Method setter = callback.getClass().getMethod(setterName(property), String.class);
				setter.invoke(callback, value);
			} else {
				callback.getClass().getMethod(setterName(property)).invoke(callback);
			}
		} catch (NoSuchMethodException e) {
			throw new SimonException(e);
		} catch (IllegalAccessException e) {
			throw new SimonException(e);
		} catch (InvocationTargetException e) {
			throw new SimonException(e);
		}
	}

	private String setterName(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private void processSimon(XMLStreamReader xr) throws XMLStreamException {
		Map<String, String> attrs = processStartElement(xr, "simon", "pattern");
		String pattern = attrs.get("pattern");
		SimonState state = attrs.get("state") != null ? SimonState.valueOf(toEnum(attrs.get("state"))) : null;
		configs.put(new SimonPattern(pattern), new SimonConfiguration(state));
		processEndElement(xr, "simon");
	}

	/**
	 * Returns configuration for the Simon with the specified name.
	 *
	 * @param name Simon name
	 * @return configuration for that particular Simon
	 */
	SimonConfiguration getConfig(String name) {
		SimonState state = null;

		for (SimonPattern pattern : configs.keySet()) {
			if (pattern.matches(name)) {
				SimonConfiguration config = configs.get(pattern);
				if (config.getState() != null) {
					state = config.getState();
				}
			}
		}
		return new SimonConfiguration(state);
	}

	private String toEnum(String enumVal) {
		return enumVal.trim().toUpperCase().replace('-', '_');
	}

	// XML Utils

	private Map<String, String> processStartElement(XMLStreamReader reader, String elementName, String... requiredAttributes) throws XMLStreamException {
		Map<String, String> attrs = processStartElementPrivate(reader, elementName, requiredAttributes);
		reader.nextTag();
		return attrs;
	}

	private Map<String, String> processStartElementPrivate(XMLStreamReader reader, String elementName, String... requiredAttributes) throws XMLStreamException {
		assertStartTag(reader, elementName);
		Map<String, String> attrs = readAttributes(reader);
		for (String attr : requiredAttributes) {
			if (!attrs.containsKey(attr)) {
				throw new XMLStreamException("Attribute '" + attr + "' MUST be present (element: " + elementName + "). " + readerPosition(reader));
			}
		}
		return attrs;
	}

	private void assertStartTag(XMLStreamReader reader, String name) throws XMLStreamException {
		if (!reader.isStartElement()) {
			throw new XMLStreamException("Assert start tag - wrong event type " + reader.getEventType() + " (expected name: " + name + ") " + readerPosition(reader));
		}
		assertName(reader, "start tag", name);
	}

	private Map<String, String> readAttributes(XMLStreamReader reader) {
		Map<String, String> attributes = new LinkedHashMap<String, String>();
		int attrCount = reader.getAttributeCount();
		for (int i = 0; i < attrCount; i++) {
			attributes.put(reader.getAttributeName(i).toString(), reader.getAttributeValue(i));
		}
		return attributes;
	}

	private void assertName(XMLStreamReader reader, String operation, String name) throws XMLStreamException {
		if (!reader.getLocalName().equals(name)) {
			throw new XMLStreamException("Assert " + operation + " - wrong element name " + reader.getName().toString() + " (expected name: " + name + ") " + readerPosition(reader));
		}
	}

	private String readerPosition(XMLStreamReader reader) {
		return "[line: " + reader.getLocation().getLineNumber() + ", column: " + reader.getLocation().getColumnNumber() + "]";
	}

	private void assertEndTag(XMLStreamReader reader, String name) throws XMLStreamException {
		if (!reader.isEndElement()) {
			throw new XMLStreamException("Assert end tag - wrong event type " + reader.getEventType() + " (expected name: " + name + ") " + readerPosition(reader));
		}
		assertName(reader, "end tag", name);
	}

	private boolean isStartTag(XMLStreamReader reader, String name) {
		return reader.isStartElement() && reader.getLocalName().equals(name);
	}

	private void processEndElement(XMLStreamReader reader, String name) throws XMLStreamException {
		assertEndTag(reader, name);
		reader.nextTag();
	}

	private String getText(XMLStreamReader reader) throws XMLStreamException {
		StringBuilder sb = new StringBuilder();
		while (reader.isCharacters()) {
			sb.append(reader.getText());
			reader.next();
		}
		return sb.toString().trim();
	}
}