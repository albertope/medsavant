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
package org.ut.biolab.medsavant.client.project;

import javax.swing.ImageIcon;
import org.ut.biolab.medsavant.client.cohort.CohortsPage;
import org.ut.biolab.medsavant.client.patient.IndividualsPage;
import org.ut.biolab.medsavant.client.region.RegionPage;
import org.ut.biolab.medsavant.client.variant.VariantFilesPage;
import org.ut.biolab.medsavant.client.view.images.IconFactory;
import org.ut.biolab.medsavant.client.view.app.MultiSectionApp;
import org.ut.biolab.medsavant.client.view.app.AppSubSection;

/**
 * Section which displays information about the current project.
 *
 * @author tarkvara
 */
public class ProjectsSection extends MultiSectionApp {

    private AppSubSection[] subSections;

    public ProjectsSection() {
        super("Project");//ProjectController.getInstance().getCurrentProjectName());
    }

    @Override
    public AppSubSection[] getSubSections() {
        if (subSections == null) {
            subSections = new AppSubSection[]{
                new ProjectSummaryPage(this),
                new IndividualsPage(this),
                new CohortsPage(this),
                new RegionPage(this),
                new VariantFilesPage(this)
            };
        }
        return subSections;
    }

    @Override
    public ImageIcon getIcon() {
        return IconFactory.getInstance().getIcon(IconFactory.StandardIcon.SECTION_OTHER);
    }
}
