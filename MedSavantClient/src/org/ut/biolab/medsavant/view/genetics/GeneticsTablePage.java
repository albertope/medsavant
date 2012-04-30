/*
 *    Copyright 2011-2012 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.ut.biolab.medsavant.view.genetics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.ut.biolab.medsavant.MedSavantClient;
import org.ut.biolab.medsavant.controller.FilterController;
import org.ut.biolab.medsavant.controller.LoginController;
import org.ut.biolab.medsavant.controller.ReferenceController;
import org.ut.biolab.medsavant.controller.ThreadController;
import org.ut.biolab.medsavant.db.FatalDatabaseException;
import org.ut.biolab.medsavant.db.NonFatalDatabaseException;
import org.ut.biolab.medsavant.model.Chromosome;
import org.ut.biolab.medsavant.listener.ReferenceListener;
import org.ut.biolab.medsavant.model.event.FiltersChangedListener;
import org.ut.biolab.medsavant.model.record.Genome;
import org.ut.biolab.medsavant.view.subview.SectionView;
import org.ut.biolab.medsavant.view.subview.SubSectionView;
import org.ut.biolab.medsavant.view.util.PeekingPanel;


/**
 *
 * @author mfiume
 */
public class GeneticsTablePage extends SubSectionView implements FiltersChangedListener, ReferenceListener {

    private JPanel panel;
    private TablePanel tablePanel;
    private GenomeContainer gp;
    private boolean isLoaded = false;
    private PeekingPanel genomeView;
    private Component[] settingComponents;

    public GeneticsTablePage(SectionView parent) {
        super(parent);
        FilterController.addFilterListener(this);
        ReferenceController.getInstance().addReferenceListener(this);
    }

    @Override
    public String getName() {
        return "Spreadsheet";
    }

    @Override
    public Component[] getSubSectionMenuComponents() {
        if (settingComponents == null) {
            settingComponents =  new Component[1];
            settingComponents[0] = PeekingPanel.getCheckBoxForPanel(genomeView,"Browser");
        }
        return settingComponents;
    }

    @Override
    public JPanel getView(boolean update) {

        if (panel == null || update) {
            ThreadController.getInstance().cancelWorkers(getName());
            setPanel();
        } else {
            tablePanel.updateIfRequired();
            gp.updateIfRequired();
        }
        return panel;
    }

    private void setPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        List<Chromosome> chrs = new ArrayList<Chromosome>();
        try {
            chrs = MedSavantClient.ChromosomeQueryUtilAdapter.getContigs(LoginController.sessionId, ReferenceController.getInstance().getCurrentReferenceId());
        } catch (SQLException ex) {
            Logger.getLogger(GeneticsTablePage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(GeneticsTablePage.class.getName()).log(Level.SEVERE, null, ex);
        }
        Genome g = new Genome(chrs);
        gp = new GenomeContainer(getName(), g);

        genomeView = new PeekingPanel("Genome", BorderLayout.SOUTH, (JComponent)gp, false,225);
        genomeView.setToggleBarVisible(false);
        panel.add(genomeView, BorderLayout.NORTH);

        tablePanel = new TablePanel(getName());
        panel.add(tablePanel, BorderLayout.CENTER);
    }

    @Override
    public void viewDidLoad() {
        isLoaded = true;
        tablePanel.updateIfRequired();
        gp.updateIfRequired();
    }

    @Override
    public void viewDidUnload() {
        ThreadController.getInstance().cancelWorkers(getName());
        if (tablePanel != null && !tablePanel.isInit()) {
            setUpdateRequired(true);
        }
        isLoaded = false;
    }

    public void updateContents() {
        ThreadController.getInstance().cancelWorkers(getName());
        if (tablePanel == null || gp == null) return;
        tablePanel.setUpdateRequired(true);
        gp.setUpdateRequired(true);
        if (isLoaded) {
            tablePanel.updateIfRequired();
            gp.updateIfRequired();
        }
    }

    @Override
    public void filtersChanged() throws SQLException, FatalDatabaseException, NonFatalDatabaseException {
        updateContents();
    }

    @Override
    public void referenceAdded(String name) {
    }

    @Override
    public void referenceRemoved(String name) {
    }

    @Override
    public void referenceChanged(String name) {
        updateContents();
    }
}