package org.jenkinsci.plugins.privateslave;

import hudson.Extension;
import hudson.model.Node;
import hudson.model.Queue;
import hudson.model.queue.QueueTaskDispatcher;
import hudson.model.queue.CauseOfBlockage;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.util.DescribableList;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Extension
public class ValidNodeJobChecker extends QueueTaskDispatcher {
    private static Logger LOGGER = Logger.getLogger("ValidNodeJobChecker");

	@Override
	public CauseOfBlockage canTake(final Node node, Queue.BuildableItem item)
	{
		DescribableList<NodeProperty<?>,NodePropertyDescriptor> np = node.getNodeProperties();

		RestrictJobListNodeProperty p = np.get(RestrictJobListNodeProperty.class);
		if(p == null)
		{
			return null;
		}
        List<RestrictedJobNodeProperty> joblist = p.getJobList();
        if(joblist != null && joblist.size() > 0)
        {
            for(RestrictedJobNodeProperty rj:joblist)
            {
                if(item.task.getName().equals(rj.getName()) || item.task.getFullDisplayName().startsWith(rj.getName()))
                {
                    return null;
                }
                else
                {
                    if("".equals(rj.getName()))
                    {
                        continue;
                    }
               
                    try{
                        Pattern r = Pattern.compile(rj.getName());
                        Matcher fullm = r.matcher(item.task.getFullDisplayName());
                        Matcher shortm = r.matcher(item.task.getName());
                        if(fullm.find() || shortm.find())
                        {
                            return null;
                        }
                    }
                    catch(PatternSyntaxException ex)
                    {

                        continue;
                    }
                }
            }
        }
		return new JobNotAuthorised(node);
	}
}
