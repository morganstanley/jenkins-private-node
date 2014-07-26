package org.jenkinsci.plugins.privateslave;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import hudson.Extension;
import hudson.model.Node;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.util.FormValidation;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 *
 * @author Aaron Searle
 */
public class RestrictJobListNodeProperty extends NodeProperty<Node> {

     private List<RestrictedJobNodeProperty> jobList = new ArrayList<RestrictedJobNodeProperty>();
     private String nodeContact = "";

    @DataBoundConstructor
    public RestrictJobListNodeProperty(
            ArrayList<RestrictedJobNodeProperty> joblist_,String nodeContact_
    ) {

        this.jobList = joblist_;
        this.nodeContact = nodeContact_;
    }
    
    public List<RestrictedJobNodeProperty> getJobList()
    {
    	return jobList;
    }
    public String getNodeContact()
    {
        return nodeContact;
    }

    @Extension
    public static class DescriptorImpl extends NodePropertyDescriptor {

        public static final String JOB_LIST = "jobList";
        public static final String NODE_CONTACT = "nodeContact";

        @Override
        public boolean isApplicable(Class<? extends Node> nodeType) {

            return true;
        }

        @Override
        public NodeProperty<?> newInstance(
                final StaplerRequest req,
                final JSONObject formData
        ) throws FormException {
//TODO: Can't find where joblist is created or not for 'new' slave
            ArrayList<RestrictedJobNodeProperty> joblist = new ArrayList<RestrictedJobNodeProperty>();
            String nodeContact = "Not Set";
            try
            {
                nodeContact = formData.getString(NODE_CONTACT);
            }
            catch(JSONException e)
            {
            }
            boolean isobject = true;
            try
            {
            	JSONObject jsot = formData.getJSONObject(JOB_LIST);
            	joblist.add(new RestrictedJobNodeProperty(jsot.getString("RestrictedJobNodeProperty")));
            }
            catch(JSONException e) 
            {
            	isobject = false;
            	// Swallow because we want to try array as well 
            }
            if(!isobject)
            {           
                try
                {
            	JSONArray jsarr = formData.getJSONArray(JOB_LIST);	            
	            for (int i = 0; i < jsarr.size(); i++) {
	            	JSONObject jso = jsarr.getJSONObject(i);
	            	
	            	String name = jso.getString("RestrictedJobNodeProperty");
	            	joblist.add(new RestrictedJobNodeProperty(name));
	            	}
                }
                catch(JSONException e)
                {
                    //User hasn't specified any jobs, no big deal, they just
                    //can't run anything on that node anymore.
                }
            }


            return new RestrictJobListNodeProperty(joblist,nodeContact);
        }
        
    	public FormValidation doCheckName(@QueryParameter String value) {
    		if("".equals(value))
    		{
    			return FormValidation.error("Job pattern has not been provided");
    		}
    		try{
    			Pattern.compile(value);
    		}
    		catch(PatternSyntaxException ex)
    		{
    			return FormValidation.error("Regular expression/pattern is not valid");
    		}
    		
    		return FormValidation.ok();
    	}
        @Override
        public String getDisplayName() {

            return "Restrict jobs that run on this node";
        }
    }
}
