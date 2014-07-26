package org.jenkinsci.plugins.privateslave;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.kohsuke.stapler.QueryParameter;

import hudson.util.FormValidation;

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
