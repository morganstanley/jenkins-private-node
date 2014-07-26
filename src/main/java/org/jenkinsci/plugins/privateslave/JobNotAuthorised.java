package org.jenkinsci.plugins.privateslave;

import hudson.model.Node;
import hudson.model.queue.CauseOfBlockage;

public class JobNotAuthorised extends CauseOfBlockage{

    public final Node node;

    public JobNotAuthorised(Node node) {
        this.node = node;
    }

    @Override
    public String getShortDescription() {
        return "Job is not allowed to run on this node";
    }
}
