/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jboss.as.console.client.standalone.subsys;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.TreeItem;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.SubsystemGroup;
import org.jboss.as.console.client.shared.SubsystemGroupItem;
import org.jboss.as.console.client.shared.SubsystemMetaData;
import org.jboss.as.console.client.shared.model.SubsystemRecord;
import org.jboss.as.console.client.widgets.LHSHighlightEvent;
import org.jboss.as.console.client.widgets.LHSNavTree;
import org.jboss.as.console.client.widgets.LHSNavTreeItem;

import java.util.List;

/**
 * @author Heiko Braun
 * @date 5/24/11
 */
public class SubsystemTreeBuilder {

    public static void build(String parentPlace, final LHSNavTree subsysTree, List<SubsystemRecord> subsystems)
    {
        // build groups first
        for(SubsystemGroup group : SubsystemMetaData.getGroups().values())
        {
            final TreeItem groupTreeItem = new TreeItem(group.getName());

            for(SubsystemGroupItem groupItem : group.getItems())
            {
                for(SubsystemRecord subsys: subsystems)
                {
                    if(subsys.getTitle().equals(groupItem.getKey())
                            && groupItem.isDisabled()==false)
                    {
                        final String key = subsys.getTitle().toLowerCase().replace(" ", "_");
                        String token = parentPlace + key;
                        final LHSNavTreeItem link = new LHSNavTreeItem(groupItem.getName(), token);
                        link.setKey(key);

                        if(key.equals("datasources")) // the eventing currently doesn't work reliably
                        {
                            Timer t = new Timer() {
                                @Override
                                public void run() {
                                    groupTreeItem.setState(true);
                                    //link.setSelected(true);

                                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand(){
                                        @Override
                                        public void execute() {
                                            Console.MODULES.getEventBus().fireEvent(
                                                    new LHSHighlightEvent(subsysTree.getTreeId(), link.getText(), "profiles")
                                            );
                                        }
                                    });
                                }
                            };
                            t.schedule(500);
                        }

                        groupTreeItem.addItem(link);
                    }
                }
            }

            // skip empty groups
            if(groupTreeItem.getChildCount()>0)
                subsysTree.addItem(groupTreeItem);

        }
    }
}