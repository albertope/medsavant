/**
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ut.biolab.medsavant.client.region;

import javax.swing.JPanel;

import org.ut.biolab.medsavant.shared.model.RegionSet;
import org.ut.biolab.medsavant.client.view.list.SimpleDetailedListModel;
import org.ut.biolab.medsavant.client.view.list.SplitScreenView;
import org.ut.biolab.medsavant.client.view.app.MultiSectionApp;
import org.ut.biolab.medsavant.client.view.app.AppSubSection;

/**
 *
 * @author mfiume
 */
public class RegionPage extends AppSubSection {

    private final RegionController controller;
    private SplitScreenView view;


    public RegionPage(MultiSectionApp parent) {
        super(parent, "Gene Lists");
        controller = RegionController.getInstance();
    }

    @Override
    public JPanel getView() {
        if (view == null) {
            view = new SplitScreenView(
                    new SimpleDetailedListModel("Gene List") {
                        @Override
                        public RegionSet[] getData() throws Exception {
                            return controller.getRegionSets().toArray(new RegionSet[0]);
                        }
                    },
                    new RegionDetailedView(pageName),
                    new RegionDetailedListEditor());
        }
        return view;
    }
}
