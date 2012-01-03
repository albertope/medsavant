/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewProjectDialog.java
 *
 * Created on Sep 19, 2011, 12:29:03 PM
 */
package org.ut.biolab.medsavant.view.dialog;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.ut.biolab.medsavant.MedSavantClient;
import org.ut.biolab.medsavant.controller.LoginController;
import org.ut.biolab.medsavant.controller.ReferenceController;
import org.ut.biolab.medsavant.db.model.Chromosome;

/**
 *
 * @author mfiume
 */
public class NewReferenceDialog extends javax.swing.JDialog {
    
    private DefaultTableModel model;

    /** Creates new form NewProjectDialog */
    public NewReferenceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Create a reference");
        initComponents();
        this.getRootPane().setDefaultButton(this.button_ok);
        this.setLocationRelativeTo(parent);
         
        
        //setup table
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {  
                return true;        
            }  
        };
        model.addColumn("Contig Name");
        model.addColumn("Length");
        model.addColumn("Centromere Position");
        
        for(Chromosome c : Chromosome.getHg19Chromosomes()){
            model.addRow(new Object[]{c.getName(), c.getLength(), c.getCentromerepos()});
        }
        
        table.setModel(model);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        
        //setup addRowButton
        addRowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addRowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addRow();
            }
        });
        
        
        //setup clearButton
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearTable();
            }
        });
        
    }
    
    private void addRow(){
        model.addRow(new Object[3]);
        table.setModel(model);
    }
    
    private void clearTable(){
        for(int i = 0; i < model.getRowCount(); i++){
            for(int j = 0; j < model.getColumnCount(); j++){
                model.setValueAt(null, i, j);
            }
        }
        table.setModel(model);
    }
    
    private List<Chromosome> getContigs() throws NumberFormatException, Exception {
        List<Chromosome> result = new ArrayList<Chromosome>();
        List<String> names = new ArrayList<String>();
        for(int i = 0; i < model.getRowCount(); i++){
            
            //contig name
            String name = (String) model.getValueAt(i, 0);
            if(name == null || name.equals("")) continue;
            if(names.contains(name)){
                throw new Exception(); //can't have duplicates
            }
            
            //length
            long length = Long.parseLong(model.getValueAt(i, 1).toString());
            
            //centromere
            long centromere = Long.parseLong(model.getValueAt(i, 2).toString());
            if(centromere > length){
                throw new Exception(); //centromere can't be greater than length
            }

            names.add(name);
            result.add(new Chromosome(name, null, centromere, length));
        }
        return result;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        text_reference_name = new javax.swing.JTextField();
        button_cancel = new javax.swing.JButton();
        button_ok = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        clearButton = new javax.swing.JLabel();
        addRowButton = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        text_reference_url = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);

        jLabel1.setText("Reference Name: ");

        text_reference_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_reference_nameActionPerformed(evt);
            }
        });

        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_cancelActionPerformed(evt);
            }
        });

        button_ok.setText("OK");
        button_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_okActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table);

        jLabel2.setText("Contig Information:");

        clearButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        clearButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        clearButton.setText("clear");

        addRowButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        addRowButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        addRowButton.setText("add row");

        jLabel3.setText("Sequence URL (optional):");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(text_reference_name, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .add(jLabel1)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(button_ok)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(button_cancel))
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 272, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(text_reference_url, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 120, Short.MAX_VALUE)
                        .add(addRowButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(text_reference_name, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(text_reference_url, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(clearButton)
                    .add(addRowButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 338, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(button_cancel)
                    .add(button_ok))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void text_reference_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_reference_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_reference_nameActionPerformed

    private void button_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_cancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_button_cancelActionPerformed

    private void button_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_okActionPerformed
        try {
            String referenceName = this.text_reference_name.getText();
            String referenceUrl = this.text_reference_url.getText();
            List<Chromosome> contigs = getContigs();

            if (referenceName == null || referenceName.equals("")){
                JOptionPane.showMessageDialog(this, "Reference name required");
            } else if (MedSavantClient.ReferenceQueryUtilAdapter.containsReference(LoginController.sessionId, referenceName)) {
                JOptionPane.showMessageDialog(this, "Reference already exists");
            } else {
                ReferenceController.getInstance().addReference(referenceName, contigs, referenceUrl);
                this.dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "<HTML>There was a problem reading your contig values.<BR>"
                    + "Make sure there are no duplicate names, length and centromere contain only numbers, <BR>"
                    + "and centromere &lt; length.</HTML>");
        }

    }//GEN-LAST:event_button_okActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                NewReferenceDialog dialog = new NewReferenceDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addRowButton;
    private javax.swing.JButton button_cancel;
    private javax.swing.JButton button_ok;
    private javax.swing.JLabel clearButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable table;
    private javax.swing.JTextField text_reference_name;
    private javax.swing.JTextField text_reference_url;
    // End of variables declaration//GEN-END:variables
}
