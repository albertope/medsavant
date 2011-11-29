/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ut.biolab.medsavant.view.patients.individual;

import au.com.bytecode.opencsv.CSVWriter;
import com.jidesoft.utils.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D.Float;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.ut.biolab.medsavant.controller.ProjectController;
import org.ut.biolab.medsavant.db.model.Cohort;
import org.ut.biolab.medsavant.db.util.query.CohortQueryUtil;
import org.ut.biolab.medsavant.db.util.query.PatientQueryUtil;
import org.ut.biolab.medsavant.log.ClientLogger;
import org.ut.biolab.medsavant.settings.DirectorySettings;
import org.ut.biolab.medsavant.view.component.CollapsablePanel;
import org.ut.biolab.medsavant.view.dialog.ComboForm;
import org.ut.biolab.medsavant.view.list.DetailedView;
import org.ut.biolab.medsavant.view.util.ViewUtil;
import pedviz.algorithms.Sugiyama;
import pedviz.graph.Graph;
import pedviz.loader.CsvGraphLoader;
import pedviz.view.GraphView2D;
import pedviz.view.NodeView;
import pedviz.view.rules.ShapeRule;
import pedviz.view.symbols.Symbol2D;
import pedviz.view.symbols.SymbolSexFemale;
import pedviz.view.symbols.SymbolSexMale;
import pedviz.view.symbols.SymbolSexUndesignated;

/**
 *
 * @author mfiume
 */
public class IndividualDetailedView extends DetailedView {

    private List<String> fieldNames;
    private IndividualDetailsSW sw;
    private final JPanel infoContent;
    private final JPanel infoDetails;
    private final JPanel menu;
    private int[] patientIds;
    private final JPanel pedigreeContent;
    private final JPanel pedigreeDetails;
    private PedigreeSW sw2;

    private class IndividualDetailsSW extends SwingWorker {
        private final int pid;

        public IndividualDetailsSW(int pid) {
            this.pid = pid;
        }
        
        @Override
        protected Object doInBackground() throws Exception {
            Object[] fieldValues = PatientQueryUtil.getPatientRecord(ProjectController.getInstance().getCurrentProjectId(), pid);
            return fieldValues;
        }
        
        @Override
        protected void done() {
            try {
                Object[] result = (Object[]) get();
                setPatientInformation(result);
            } catch (CancellationException ex){
                
            } catch (Exception ex) {
                ClientLogger.log(IndividualDetailedView.class, ex.getLocalizedMessage());
                return;
            }
        }
    }

    
    private class PedigreeSW extends SwingWorker {
        private final int pid;

        public PedigreeSW(int pid) {
            this.pid = pid;
        }
        
        @Override
        protected Object doInBackground() throws Exception {
            List<Object[]> results = PatientQueryUtil.getFamilyOfPatient(ProjectController.getInstance().getCurrentProjectId(), pid);
            
            File outfile = new File(DirectorySettings.getTmpDirectory() ,"pedigree" + pid + ".csv");
            
                CSVWriter w = new CSVWriter(new FileWriter(outfile),',',CSVWriter.NO_QUOTE_CHARACTER);
                w.writeNext(new String[] {Pedigree.FIELD_HOSPITALID,Pedigree.FIELD_MOM,Pedigree.FIELD_DAD,
                    Pedigree.FIELD_PATIENTID,Pedigree.FIELD_GENDER});
                for (Object[] row : results) {
                    String[] srow = new String[row.length];
                    for (int i = 0; i < row.length; i++) {
                        srow[i] = row[i].toString();
                    }
                    w.writeNext(srow);
                }
                w.close();
            return outfile;
        }
        
        @Override
        protected void done() {
            try {
                File pedigreeCSVFile = (File) get();
                showPedigree(pedigreeCSVFile);
                pedigreeCSVFile.delete();
            } catch (CancellationException ex){
                
            } catch (Exception ex) {
                ClientLogger.log(IndividualDetailedView.class, ex.getLocalizedMessage());
                return;
            }
        }
    }

    public synchronized void showPedigree(File pedigreeCSVFile) {
        
        pedigreeDetails.removeAll();
        pedigreeDetails.setLayout(new BorderLayout());
        
        //Step 1
	Graph graph = new Graph();
	CsvGraphLoader loader = new CsvGraphLoader(pedigreeCSVFile.getAbsolutePath(), ",");
	loader.setSettings(Pedigree.FIELD_HOSPITALID,Pedigree.FIELD_MOM,Pedigree.FIELD_DAD);
	loader.load(graph);

	//Step 2
	Sugiyama s = new Sugiyama(graph);
	s.run();

	//Step 3
	GraphView2D view = new GraphView2D(s.getLayoutedGraph());
        
        //view.highlight(nodes);
        //view.setSelectionEnabled(true);
        
        view.addRule(new ShapeRule(Pedigree.FIELD_GENDER, "2", new SymbolSexMale()));
	view.addRule(new ShapeRule(Pedigree.FIELD_GENDER, "1", new SymbolSexFemale()));
        view.addRule(new ShapeRule(Pedigree.FIELD_GENDER, "0", new SymbolSexUndesignated()));
	view.addRule(new ShapeRule(Pedigree.FIELD_GENDER, "null", new SymbolSexUndesignated()));
        
        view.addRule(new PedigreeBasicRule(patientIds));
        
        pedigreeDetails.add(view.getComponent(),BorderLayout.CENTER);
        
        pedigreeDetails.updateUI();
    }
    
    
    public static class HospitalSymbol extends Symbol2D {
        private final String hid;

                    public HospitalSymbol(String hid) {
                        this.hid = hid;
                    }
                    
                    @Override
                    public void drawSymbol(Graphics2D gd, Float position, float size, Color color, Color color1, NodeView nv) {
                        gd.setFont(new Font("Arial",Font.PLAIN,1));
                        FontMetrics fm = gd.getFontMetrics();
                        int width = fm.stringWidth(hid);
                        int height = fm.getMaxAscent();
                        
                        float startX = (float) position.getX()-size/2;// (float) (position.getX()-(double)width/2);
                        float startY = (float) (position.getY()+(double) size/2 +height);
                        
                        gd.drawString(hid, startX, startY);
                    }

                    @Override
                    public int getPriority() {
                        return 0;
                    }
                    
                }
    
    public synchronized void setPatientInformation(Object[] result) {

        System.out.println(fieldNames.size() + " vs " + result.length);
        for (int i = 0; i < fieldNames.size(); i++) {
            System.out.println(fieldNames.get(i) + " = " + result[i]);
        }
        
        String[][] values = new String[fieldNames.size()][2];
        for (int i = 0; i < fieldNames.size(); i++) {
            values[i][0] = fieldNames.get(i);
            values[i][1] = "";
            if(result[i] != null)
                values[i][1] = result[i].toString();
        }
        
        infoDetails.removeAll();
        infoDetails.setLayout(new BoxLayout(infoDetails,BoxLayout.Y_AXIS));
        
        infoDetails.add(ViewUtil.getKeyValuePairList(values));
        
        infoDetails.add(Box.createVerticalGlue());
        
        infoDetails.updateUI();
    }
    
    public IndividualDetailedView() {
        
        try {
            fieldNames = PatientQueryUtil.getPatientFieldAliases(ProjectController.getInstance().getCurrentProjectId());
        } catch (SQLException ex) {
            ClientLogger.log(IndividualDetailedView.class,ex.getLocalizedMessage(),Level.SEVERE);
        }
        
        JPanel viewContainer = (JPanel) ViewUtil.clear(this.getContentPanel());
        viewContainer.setLayout(new BorderLayout());
        
        JPanel infoContainer = ViewUtil.getClearPanel();
        ViewUtil.applyVerticalBoxLayout(infoContainer);
        
        viewContainer.add(ViewUtil.getClearBorderlessJSP(infoContainer),BorderLayout.CENTER);
        
        CollapsablePanel cp = new CollapsablePanel("Patient Information");
        infoContainer.add(cp);
        
        infoContainer.add(ViewUtil.getLargeSeparator());
        
        CollapsablePanel cpPed = new CollapsablePanel("Pedigree");
        infoContainer.add(cpPed);
        
        infoContainer.add(Box.createVerticalGlue());
        
        infoContent = cp.getContentPane();
        pedigreeContent = cpPed.getContentPane();
        
        infoDetails = ViewUtil.getClearPanel();
        pedigreeDetails = ViewUtil.getClearPanel();
        
        menu = ViewUtil.getClearPanel();
        
        menu.add(addIndividualsButton());
        menu.setVisible(false);
        infoContent.setLayout(new BorderLayout());
        pedigreeContent.setLayout(new BorderLayout());
        
        infoContent.add(infoDetails,BorderLayout.CENTER);
        pedigreeContent.add(pedigreeDetails,BorderLayout.CENTER);
        
        this.addBottomComponent(menu);
    }
    
    @Override
    public void setSelectedItem(Object[] item) {
        int patientId = (Integer) item[0];
        patientIds = new int[1];
        patientIds[0] = patientId;
        String hospitalId = (String) item[2];
        
        setTitle(hospitalId);
        
        infoDetails.removeAll();
        infoDetails.updateUI();
        
        if (sw != null) {
            sw.cancel(true);
        }
        sw = new IndividualDetailsSW(patientId);
        sw.execute();
        
        if (sw2 != null) {
            sw2.cancel(true);
        }
        
        sw2 = new PedigreeSW(patientId);
        sw2.execute();
        
        if(menu != null) menu.setVisible(true);
    }
    
    @Override
    public void setMultipleSelections(List<Object[]> items){
        patientIds = new int[items.size()];
        for(int i = 0; i < items.size(); i++){
            patientIds[i] = (Integer) items.get(i)[0];
        }
        setTitle("Multiple individuals (" + items.size() + ")");
        infoDetails.removeAll();
        infoDetails.updateUI();
    }
    
    private JButton addIndividualsButton(){
        JButton button = new JButton("Add individual(s) to cohort");
        button.setBackground(ViewUtil.getDetailsBackgroundColor());
        button.setOpaque(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(patientIds != null && patientIds.length > 0){
                    try {    
                        List<Cohort> cohorts = CohortQueryUtil.getCohorts(ProjectController.getInstance().getCurrentProjectId());                    
                        ComboForm form = new ComboForm(cohorts.toArray(), "Select Cohort", "Select which cohort to add to:");
                        Cohort selected = (Cohort) form.getSelectedValue();
                        if (selected == null) {
                            return;
                        }                        
                        CohortQueryUtil.addPatientsToCohort(patientIds, selected.getId());                            
                    } catch (SQLException ex) {
                        Logger.getLogger(IndividualDetailedView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    parent.refresh();                   
                }                   
            }
        }); 
        return button;
    }
    
}
