package com.appify.vidstream.newWebApi;

import javax.inject.Inject;

import com.appify.vidstream.newWebApi.data.PropertyDataLoader;

public class PropertyHelper {
	private PropertyDataLoader propertyDataLoader;

	@Inject
	public PropertyHelper(PropertyDataLoader propertyDataLoader) {
		this.propertyDataLoader = propertyDataLoader;
	}

	public String getStringProperty(String propName, String defaultVal) {
		String propVal = this.propertyDataLoader.getProps().get(propName);
		return propVal != null ? propVal : defaultVal;
	}

	public Integer getIntProperty(String propName, Integer defaultVal) {
		String propVal = this.propertyDataLoader.getProps().get(propName);
		return propVal != null ? Integer.parseInt(propVal) : defaultVal;
	}

	public Boolean getBooleanProperty(String propName, Boolean defaultVal) {
		String propVal = this.propertyDataLoader.getProps().get(propName);
		return propVal != null ? Boolean.parseBoolean(propVal) : defaultVal;
	}

	public Double getDoubleProperty(String propName, Double defaultVal) {
		String propVal = this.propertyDataLoader.getProps().get(propName);
		return propVal != null ? Double.parseDouble(propVal) : defaultVal;
	}

	public Float getFloatProperty(String propName, Float defaultVal) {
		String propVal = this.propertyDataLoader.getProps().get(propName);
		return propVal != null ? Float.parseFloat(propVal) : defaultVal;
	}

}
