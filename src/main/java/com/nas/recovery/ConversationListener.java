/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nas.recovery;

import java.io.Serializable;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;

@Name("conversationListener")
@Scope(ScopeType.SESSION)
public class ConversationListener implements Serializable {
    @Logger
    private Log log;

    @In
    private Conversation conversation;

    @Observer(value="org.jboss.seam.beginConversation")
    public void observeConversationStart(){
    log.error("conversation#0 started", conversation.getId());
    }
    @Observer(value="org.jboss.seam.endConversation")
    public void observeConversationEnd(){
    log.error("conversation#0 ended", conversation.getId());
    }



}
