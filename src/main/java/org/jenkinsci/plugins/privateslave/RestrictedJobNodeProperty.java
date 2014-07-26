package org.jenkinsci.plugins.privateslave;

public class RestrictedJobNodeProperty {

	private String name;
	public RestrictedJobNodeProperty(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public String setName()
	{
		return name;
	}
}
