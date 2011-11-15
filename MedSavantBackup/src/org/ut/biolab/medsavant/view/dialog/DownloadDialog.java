/*
 *    Copyright 2009-2011 University of Toronto
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

package org.ut.biolab.medsavant.view.dialog;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

import org.ut.biolab.medsavant.util.NetworkUtils;
import org.ut.biolab.medsavant.util.DownloadEvent;
import org.ut.biolab.medsavant.util.DownloadMonitor;
import org.ut.biolab.medsavant.util.MiscUtils;
import org.ut.biolab.medsavant.view.util.DialogUtils;

/**
 *
 * @author mfiume, tarkvara
 */
public class DownloadDialog extends JDialog implements DownloadMonitor {

    private boolean complete;
    private boolean cancelled;
    private String shortName;
    
    /** Successfully downloaded file (or null on failure). */
    private File downloadedFile;

    public DownloadDialog(Window parent, boolean modal) {
        super(parent, modal ? Dialog.ModalityType.APPLICATION_MODAL : Dialog.ModalityType.MODELESS);
        initComponents();
        setLocationRelativeTo(parent);

        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                askToDispose();
            }
        });

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel fileCaption = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        cancelButton = new javax.swing.JButton();
        fileLabel = new javax.swing.JLabel();
        javax.swing.JLabel destinationCaption = new javax.swing.JLabel();
        destinationLabel = new javax.swing.JLabel();
        stageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        fileCaption.setFont(fileCaption.getFont().deriveFont(fileCaption.getFont().getStyle() | java.awt.Font.BOLD));
        fileCaption.setText("Downloading: ");

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        fileLabel.setText("filename");

        destinationCaption.setFont(destinationCaption.getFont().deriveFont(destinationCaption.getFont().getStyle() | java.awt.Font.BOLD));
        destinationCaption.setText("To:");

        destinationLabel.setText("destination");

        stageLabel.setText("Starting download...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fileCaption)
                                .addComponent(destinationCaption))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(destinationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(stageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileCaption)
                    .addComponent(fileLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationCaption)
                    .addComponent(destinationLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(stageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(cancelButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        askToDispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel destinationLabel;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel stageLabel;
    // End of variables declaration//GEN-END:variables

    private void askToDispose() {
        if (!complete) {
            int result = DialogUtils.askYesNo("Cancel Download", "Are you sure you want to cancel?");
            if (result == DialogUtils.YES) {
                try {
                    downloadedFile.delete();
                } catch (Exception ignored) {
                }
                downloadedFile = null;
                dispose();
            }
        } else {
            dispose();
        }
    }

    /**
     * Set up the dialog, start the download process, and make it visible.
     */
    public void downloadFile(URL url, File destDir, String fileName) {
        shortName = fileName != null ? fileName : MiscUtils.getFilenameFromPath(url.getPath());
        setTitle("Downloading " + shortName);
        fileLabel.setText(url.toString());
        destinationLabel.setText(destDir.getPath());
        downloadedFile = new File(destDir, shortName);
        NetworkUtils.downloadFile(url, destDir, shortName, this);
        setVisible(true);
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @return the file which was downloaded, or null on error or cancellation.
     */
    public File getDownloadedFile() {
        return downloadedFile;
    }

    @Override
    public void handleEvent(DownloadEvent event) {
        switch (event.getType()) {
            case STARTED:
                break;
            case PROGRESS:
                int progress = (int)(event.getProgress() * 100.0);
                stageLabel.setText(String.format("Downloaded %d%%",  progress));
                progressBar.setValue(progress);
                break;
            case COMPLETED:
                dispose();
                ((Window)getParent()).toFront();
                if (cancelled) {
                    downloadedFile = null;
                }
                break;
            case FAILED:
                downloadedFile = null;
                DialogUtils.displayException("Download Error", String.format("<html>Download of <i>%s</i> failed.", shortName), event.getError());
                break;
        }
    }
}
