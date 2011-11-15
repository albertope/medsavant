/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ut.biolab.medsavant.view.genetics.aggregates;

import java.sql.SQLException;
import org.ut.biolab.medsavant.db.exception.FatalDatabaseException;
import org.ut.biolab.medsavant.db.exception.NonFatalDatabaseException;
import org.ut.biolab.medsavant.db.model.BEDRecord;
import com.jidesoft.utils.SwingWorker;
import org.ut.biolab.medsavant.view.component.SearchableTablePanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.ut.biolab.medsavant.controller.FilterController;
import org.ut.biolab.medsavant.controller.ProjectController;
import org.ut.biolab.medsavant.controller.ReferenceController;
import org.ut.biolab.medsavant.db.model.RegionSet;
import org.ut.biolab.medsavant.db.util.query.RegionQueryUtil;
import org.ut.biolab.medsavant.db.util.query.VariantQueryUtil;
import org.ut.biolab.medsavant.model.event.FiltersChangedListener;
import org.ut.biolab.medsavant.view.util.ViewUtil;
import org.ut.biolab.medsavant.view.util.WaitPanel;

/**
 *
 * @author mfiume
 */
public class GeneListPanelGenerator implements AggregatePanelGenerator {
    private static final Logger LOG = Logger.getLogger(GeneListPanelGenerator.class.getName());

    private GeneListPanel panel;

    public GeneListPanelGenerator() {
    }

    public String getName() {
        return "Genomic Region";
    }

    public JPanel getPanel() {
        if (panel == null) {
            panel = new GeneListPanel();
        }
        return panel;
    }

    public void setUpdate(boolean update) {
        
        if (panel == null) { return; }
        
        if (update) {
            
        } else {
            panel.stopThreads();

        }
    }

    public class GeneListPanel extends JPanel implements FiltersChangedListener {

        private final JPanel banner;
        private final JComboBox geneLister;
        private final JButton goButton;
        private final JPanel tablePanel;
        private GeneAggregator aggregator;
        //private final JPanel content;
        private final TreeMap<BEDRecord, Integer> regionToVariantCountMap;
        private SearchableTablePanel stp;
        private GeneVariantIntersectionWorker gviw;
        private List<BEDRecord> currentGenes;
        private final TreeMap<BEDRecord, Integer> regionToIndividualCountMap;
        private GenePatientIntersectionWorker gpiw;
        private final JProgressBar progress;
        private int numbersRetrieved;
        private int limit = 10000;

        public GeneListPanel() {

            this.setLayout(new BorderLayout());
            banner = ViewUtil.getSubBannerPanel("Gene List");
            //banner.setLayout(new BoxLayout(banner,BoxLayout.X_AXIS));
            //banner.setBorder(ViewUtil.getMediumBorder());

            regionToVariantCountMap = new TreeMap<BEDRecord, Integer>();
            regionToIndividualCountMap = new TreeMap<BEDRecord, Integer>();

            geneLister = new JComboBox();

            goButton = new JButton("Aggregate");

            tablePanel = new JPanel();
            //tablePanel.setBackground(Color.white);
            tablePanel.setLayout(new BorderLayout());

            //banner.add(Box.createHorizontalGlue());
            //banner.add(new JLabel("> Gene list: "));
            banner.add(geneLister);
            banner.add(ViewUtil.getMediumSeparator());
            
            //banner.add(goButton);
            banner.add(Box.createHorizontalGlue());
            
            progress = new JProgressBar();
            progress.setStringPainted(true);
            
            banner.add(progress);
            

            this.add(banner, BorderLayout.NORTH);
            this.add(tablePanel, BorderLayout.CENTER);

            geneLister.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    showGeneAggregates((RegionSet) geneLister.getSelectedItem());
                }
            });

            //content = new JPanel();
            //content.setLayout(new BorderLayout());

            (new GeneListGetter()).execute();

            FilterController.addFilterListener(this);
        }

        private synchronized void updateGeneTable() {

            //this.add(content,BorderLayout.CENTER);

            List<String> columnNames = Arrays.asList(new String[]{"Name", "Chromosome", "Start", "End", "Variants", "Patients"});
            List<Class> columnClasses = Arrays.asList(new Class[]{String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class});
            stp = new SearchableTablePanel(GeneListPanelGenerator.class.getName(), columnNames, columnClasses, new ArrayList<Integer>(), limit, null) {
                @Override
                public void forceRefreshData(){
                    limit = stp.getRetrievalLimit();
                    updateGeneTable();
                }
            };

            regionToVariantCountMap.clear();
            regionToIndividualCountMap.clear();

            numbersRetrieved = 0;
            updateProgess();
            
            for (BEDRecord r : this.currentGenes) {
                regionToVariantCountMap.put(r, null);
                regionToIndividualCountMap.put(r, null);
            }
            
            tablePanel.removeAll();
            
            tablePanel.add(stp, BorderLayout.CENTER);

            tablePanel.updateUI();

            updateData();

            tablePanel.updateUI();

            if (gviw != null && !gviw.isDone()) {
                gviw.cancel(true);
            }
            
            if (gpiw != null && !gpiw.isDone()) {
                gpiw.cancel(true);
            }

            
            gviw = new GeneVariantIntersectionWorker(this.currentGenes);
            gviw.execute();
            
            //gpiw = new GenePatientIntersectionWorker(this.currentGenes);
            //gpiw.execute();

        }

        private void initGeneTable(List<BEDRecord> genes) {
            this.currentGenes = genes;
            updateGeneTable();
        }

        public synchronized void updateData() {
            
            List<Object[]> data = new ArrayList<Object[]>();

            int i = 0;
            for (BEDRecord r : regionToVariantCountMap.keySet()) {
                if(i >= limit) break;
                data.add(BEDToVector(r, regionToVariantCountMap.get(r), regionToIndividualCountMap.get(r)));
                i++;
            }
            
            //stp.updateData(data);
            stp.updateUI();
            stp.forceRefreshData();
        }

        public synchronized void updateBEDRecordVariantValue(BEDRecord br, int value) {
            regionToVariantCountMap.put(br, value);
            updateData();
                        incrementProgress();
        }
        
        public synchronized void updateBEDRecordPatientValue(BEDRecord br, int value) {
            regionToIndividualCountMap.put(br, value);
            updateData();
                        incrementProgress();
        }

        public void updateGeneListDropDown(List<RegionSet> geneLists) {
            for (RegionSet genel : geneLists) {
                geneLister.addItem(genel);
            }
        }

        private void showGeneAggregates(RegionSet geneList) {

            if (aggregator != null && !aggregator.isDone() && !aggregator.isCancelled()) {
                aggregator.cancel(true);
            }

            this.numbersRetrieved = 0;
            this.updateProgess();
            
            this.tablePanel.removeAll();
            this.tablePanel.add(new WaitPanel("Getting aggregate information"), BorderLayout.CENTER);
            tablePanel.updateUI();

            progress.setString("Getting " + geneList + " gene list");
            aggregator = new GeneAggregator(geneList);
            aggregator.execute();
        }

        private Object[] BEDToVector(BEDRecord r, int numVariants, int numIndividuals) {
            return new Object[] { r.getName(), r.getChrom(), r.getStart(), r.getEnd(), numVariants, numIndividuals };
        }

        public void filtersChanged() throws SQLException, FatalDatabaseException, NonFatalDatabaseException {

            if (this.gviw != null && !this.gviw.isDone()) {
                this.gviw.cancel(true);
            }
            
            if (this.gpiw != null && !this.gpiw.isDone()) {
                this.gpiw.cancel(true);
            }

            updateGeneTable();
        }
        
        private void incrementProgress() {
            numbersRetrieved++;
            updateProgess();
        }

        private void updateProgess() {
            int value = 0;
            if (regionToVariantCountMap.size() != 0) { 
                value = numbersRetrieved*100/2/Math.min(regionToVariantCountMap.keySet().size(), limit); 
            }
            value = Math.min(value, 100);
            progress.setValue(value);
            progress.setString(value + "% done ");
            
        }

        private void stopThreads() {
            
            try {
                this.gpiw.cancel(true);
            } catch (Exception e) {}
            
            try {
                this.gviw.cancel(true);
            } catch (Exception e) {}
            
            progress.setString("stopped");

        }

        private class GeneAggregator extends SwingWorker<List<BEDRecord>, BEDRecord> {

            private final RegionSet geneList;

            private GeneAggregator(RegionSet geneList) {
                this.geneList = geneList;
            }

            @Override
            protected List<BEDRecord> doInBackground() throws Exception {
                return RegionQueryUtil.getBedRegionsInRegionSet(geneList.getId(), limit);
            }

            @Override
            protected void done() {
                try {
                    initGeneTable(get());
                } catch (Exception x) {
                    // TODO: #90
                    LOG.log(Level.SEVERE, null, x);
                }
            }
        }

        private class GeneListGetter extends SwingWorker<List<RegionSet>, RegionSet> {

            @Override
            protected List<RegionSet> doInBackground() throws Exception {
                return RegionQueryUtil.getRegionSets();
            }

            @Override
            protected void done() {
                try {
                    updateGeneListDropDown(get());
                } catch (Exception x) {
                    // TODO: #90
                    LOG.log(Level.SEVERE, null, x);
                }
            }
        }

        private class GeneVariantIntersectionWorker extends SwingWorker {

            private final List<BEDRecord> records;

            public GeneVariantIntersectionWorker(List<BEDRecord> records) {
                this.records = records;
            }

            @Override
            protected Object doInBackground() throws Exception {
                for(int i = 0; i < Math.min(records.size(), limit); i++){
                    BEDRecord r = records.get(i);
                    int recordsInRegion = VariantQueryUtil.getNumVariantsInRange(
                            ProjectController.getInstance().getCurrentProjectId(), 
                            ReferenceController.getInstance().getCurrentReferenceId(), 
                            FilterController.getQueryFilterConditions(), 
                            r.getChrom(), 
                            r.getStart(), 
                            r.getEnd());
                    if (!Thread.interrupted()) {
                        updateBEDRecordVariantValue(r, recordsInRegion);
                    } else {
                        break;
                    }
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get();
                    gpiw = new GenePatientIntersectionWorker(records);
                    gpiw.execute();
                } catch (Exception x) {
                    LOG.log(Level.SEVERE, null, x);
                }
            }
        }
        
        private class GenePatientIntersectionWorker extends SwingWorker {

            private final List<BEDRecord> records;

            public GenePatientIntersectionWorker(List<BEDRecord> records) {
                this.records = records;
            }

            @Override
            protected Object doInBackground() throws Exception {
                for(int i = 0; i < Math.min(records.size(), limit); i++){
                    BEDRecord r = records.get(i);
                    int recordsInRegion = VariantQueryUtil.getNumPatientsWithVariantsInRange(
                            ProjectController.getInstance().getCurrentProjectId(), 
                            ReferenceController.getInstance().getCurrentReferenceId(), 
                            FilterController.getQueryFilterConditions(), 
                            r.getChrom(), 
                            r.getStart(), 
                            r.getEnd());
                    if (!Thread.interrupted()) {
                        updateBEDRecordPatientValue(r, recordsInRegion);
                    } else {
                        break;
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception x) {
                    LOG.log(Level.SEVERE, null, x);
                }
            }
        }
    }
}