/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ut.biolab.medsavant.client.view.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.ut.biolab.medsavant.MedSavantClient;
import org.ut.biolab.medsavant.client.api.Listener;
import org.ut.biolab.medsavant.client.login.LoginController;
import org.ut.biolab.medsavant.client.project.ProjectController;
import org.ut.biolab.medsavant.client.reference.ReferenceController;
import org.ut.biolab.medsavant.client.util.ClientNetworkUtils;
import org.ut.biolab.medsavant.client.view.MedSavantFrame;
import org.ut.biolab.medsavant.client.view.notify.NotificationsPanel.Notification;
import org.ut.biolab.medsavant.client.view.app.builtin.task.BackgroundTaskWorker;
import org.ut.biolab.medsavant.client.view.app.builtin.task.TaskWorker;
import org.ut.biolab.medsavant.client.view.component.PlaceHolderTextField;
import org.ut.biolab.medsavant.client.view.component.RoundedPanel;
import org.ut.biolab.medsavant.client.view.dashboard.LaunchableApp;
import org.ut.biolab.medsavant.client.view.images.IconFactory;
import org.ut.biolab.medsavant.client.view.images.ImagePanel;
import org.ut.biolab.medsavant.client.view.util.DialogUtils;
import org.ut.biolab.medsavant.client.view.util.ViewUtil;
import org.ut.biolab.medsavant.shared.model.GeneralLog;
import org.ut.biolab.medsavant.shared.serverapi.VariantManagerAdapter;
import org.ut.biolab.medsavant.shared.util.ExtensionsFileFilter;
import org.ut.biolab.medsavant.shared.util.IOUtils;

/**
 *
 * @author mfiume
 */
public class VCFUploadApp implements LaunchableApp {

    private static final Log LOG = LogFactory.getLog(VCFUploadApp.class);
    int containerWidth = 400;
    private static VariantManagerAdapter variantManager = MedSavantClient.VariantManager;

    List<File> filesToImport;
    private JPanel fileListView;
    private static FileFilter fileFilter = new FileFilter() {

        @Override
        public boolean accept(File f) {
            if (f.isFile()) {
                if (f.getAbsolutePath().endsWith(".vcf") || IOUtils.isArchive(f)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "VCF file(s)";
        }
    };

    private JPanel settingsPanel;
    private JButton importButton;
    private JXCollapsiblePane dragDropContainer;
    private CardLayout cardLayout;
    private JCheckBox annovarCheckbox;
    private PlaceHolderTextField emailPlaceholder;

    public VCFUploadApp() {
        filesToImport = new ArrayList<File>();
    }

    private JPanel view;

    @Override
    public JPanel getView() {
        return view;
    }

    private void initView() {
        if (view == null) {
            view = new JPanel();

            view.setBackground(Color.white);
            view.setLayout(new BorderLayout());

            JPanel settingsCard = getSettingsPanel();
            view.add(settingsCard, BorderLayout.CENTER);
        }
    }

    private void initSettingsPanel() {

        settingsPanel = ViewUtil.getClearPanel();
        settingsPanel.setVisible(false);
        //settingsPanel.setOpaque(false);

        JPanel p = ViewUtil.getSubBannerPanel("");
        //p.setOpaque(false);
        p.setBackground(Color.white);
        MigLayout ml = new MigLayout("inset 10");
        p.setLayout(ml);

        p.add(ViewUtil.getSettingsHeaderLabel("Annotation"), "wrap");
        p.add(annovarCheckbox = new JCheckBox("perform gene-based variant annotation"), "wrap");
        annovarCheckbox.setSelected(true);
        annovarCheckbox.setFocusable(false);

        p.add(ViewUtil.getSettingsHeaderLabel("Notifications"), "wrap");

        emailPlaceholder = new PlaceHolderTextField();
        emailPlaceholder.setPlaceholder("email address");
        p.add(ViewUtil.getSettingsHelpLabel("Email notifications are sent upon completion"), "wrap");
        p.add(emailPlaceholder, "wrap, growx 1.0");

        settingsPanel.setBorder(BorderFactory.createEmptyBorder());
        settingsPanel.add(p);

    }

    private void addFileToImport(File f) {

        if (!fileFilter.accept(f)) {
            DialogUtils.displayMessage(String.format("File %s does not appear to be in the correct format", f.getName()));
            return;
        }

        if (filesToImport.contains(f)) {
            DialogUtils.displayMessage(String.format("File %s already listed for import", f.getName()));
            return;
        }

        filesToImport.add(f);
        refreshFileList();
    }

    @Override
    public void viewWillUnload() {
    }

    @Override
    public void viewWillLoad() {
        initView();
    }

    @Override
    public void viewDidUnload() {
    }

    @Override
    public void viewDidLoad() {
    }

    @Override
    public ImageIcon getIcon() {
        return IconFactory.getInstance().getIcon(IconFactory.StandardIcon.APP_IMPORTVCF);
    }

    @Override
    public String getName() {
        return "VCF Upload";
    }

    public static void main(String[] argv) {
        JFrame f = new JFrame();
        VCFUploadApp app = new VCFUploadApp();
        f.setPreferredSize(new Dimension(400, 400));
        f.setMinimumSize(new Dimension(400, 400));
        app.viewWillLoad();
        f.add(app.getView());
        f.pack();
        f.show();
        app.viewDidLoad();
    }

    private void refreshFileList() {
        fileListView.removeAll();
        MigLayout ml = new MigLayout("wrap 2");
        fileListView.setLayout(ml);

        for (final File f : this.filesToImport) {

            JButton b = ViewUtil.getIconButton(IconFactory.getInstance().getIcon(IconFactory.StandardIcon.CLOSE));
            fileListView.add(b);

            fileListView.add(new JLabel(f.getAbsolutePath()), String.format("width %s, center", containerWidth - b.getIcon().getIconWidth() - 5));

            b.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    filesToImport.remove(f);
                    refreshFileList();
                }

            });
        }

        fileListView.updateUI();

        importButton.setEnabled(!this.filesToImport.isEmpty());
        importButton.setSelected(!this.filesToImport.isEmpty());
        if (filesToImport.isEmpty()) {
            importButton.setText("Import");
        } else if (filesToImport.size() == 1) {
            importButton.setText("Import 1 file...");
        } else {
            importButton.setText(String.format("Import %s Files...", this.filesToImport.size()));
        }
    }

    private JPanel getSettingsPanel() {

        JPanel settingsCard = ViewUtil.getClearPanel();
        settingsCard.setLayout(new BorderLayout());

        JPanel container = ViewUtil.getClearPanel();
        MigLayout layout = new MigLayout("insets 30 200 30 200, fillx, hidemode 3");
        container.setLayout(layout);

        dragDropContainer = new JXCollapsiblePane();

        dragDropContainer.getContentPane().setBackground(Color.white);
        MigLayout dpMl = new MigLayout("wrap 1, center");

        dragDropContainer.getContentPane().setLayout(dpMl);

        RoundedPanel dp = new RoundedPanel(0);
        dp.setLayout(new MigLayout("wrap 1, center"));
        dp.setBackground(Color.white);
        dp.setOpaque(false);

        dp.setBorderDashed(true);
        dp.setDashThickness(3);

        ImagePanel ip = new ImagePanel(IconFactory.getInstance().getIcon(IconFactory.StandardIcon.IMPORT_VCF).getImage(), 300, 200);

        dp.add(ip);

        dp.setFocusable(false);
        int topBorder = 0;
        int sideBorder = 50;
        dp.setBorder(BorderFactory.createEmptyBorder(topBorder, sideBorder, topBorder, sideBorder));

        JLabel l = new JLabel("Drag and drop .vcf (or .vcf.gz) files to be uploaded");
        l.setForeground(new Color(100, 100, 100));
        dp.add(l);

        dragDropContainer.add(dp);

        dragDropContainer.add(l = new JLabel("or"), "center");
        l.setForeground(new Color(100, 100, 100));
        JButton chooseButton = new JButton("Choose...");
        chooseButton.setFocusable(false);
        dragDropContainer.add(chooseButton, "center");

        chooseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File[] files = DialogUtils.chooseFilesForOpen("Choose Variant Files", new ExtensionsFileFilter(new String[]{"vcf", "vcf.gz", "vcf.bz2"}), null);
                for (File f : files) {
                    addFileToImport(f);
                }
            }

        });

        container.add(dragDropContainer, "center, wrap");

        new FileDrop(dp, new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                for (File f : files) {
                    addFileToImport(f);
                }
            }   // end filesDropped
        }); // end FileDrop.Listener

        fileListView = ViewUtil.getClearPanel();
        container.add(fileListView, "wrap, center");

        JToggleButton advancedOptionsButton = ViewUtil.getSoftToggleButton("Advanced Options");//ViewUtil.getIconButton(IconFactory.getInstance().getIcon(IconFactory.StandardIcon.CONFIGURE));
        advancedOptionsButton.setToolTipText("Advanced Options");
        advancedOptionsButton.setFocusable(false);
        //container.add(advancedOptionsButton, "wrap, center");

        initSettingsPanel();

        container.add(settingsPanel, "wrap, center, width 100%");

        advancedOptionsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settingsPanel.setVisible(!settingsPanel.isVisible());
            }

        });

        importButton = new JButton("Import");
        importButton.setEnabled(false);
        importButton.setFocusable(false);
        JPanel bContainer = ViewUtil.getClearPanel();
        bContainer.setLayout(new MigLayout("fillx, insets 0"));
        bContainer.setPreferredSize(new Dimension(containerWidth, 24));

        bContainer.add(advancedOptionsButton, "left");
        bContainer.add(importButton, "right");

        container.add(bContainer, "wrap,center");
        container.add(settingsPanel, String.format("wrap, center, width %s", containerWidth));

        final VCFUploadApp instance = this;

        importButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                new BackgroundTaskWorker(instance, "Upload Variants") {

                    @Override
                    protected Void doInBackground() throws Exception {

                        final BackgroundTaskWorker instance = this;

                        this.addLog("Upload started");
                        
                        final Notification notification = this.getNotificationForWorker();
                        notification.setShowsProgress(true);
                        
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                               
                                MedSavantFrame.getInstance().showNotification(notification);
                            }
                        });

                        File[] copyOfFilesToImport = new File[filesToImport.size()];
                        int counter = 0;
                        for (File f : filesToImport) {
                            copyOfFilesToImport[counter++] = f;
                        }
                        clearFiles();

                        final int[] transferIDs = new int[copyOfFilesToImport.length];

                        int fileIndex = 0;

                        int numFiles = copyOfFilesToImport.length;
                        
                        for (File file : copyOfFilesToImport) {
                            LOG.info("Created input stream for file");
                            this.addLog("Uploading " + file.getName() + "...");
                            transferIDs[fileIndex++] = ClientNetworkUtils.copyFileToServer(file);
                            this.setTaskProgress(((double)fileIndex)/numFiles);
                            
                        }
                        this.addLog("Done uploading variants");

                        this.addLog("Queuing background import job...");

                        this.addLog("Annotating with Jannovar: " + annovarCheckbox.isSelected());
                        this.addLog("Emailing notifications to: " + emailPlaceholder.getText());
                        
                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    variantManager.uploadVariants(
                                            LoginController.getInstance().getSessionID(),
                                            transferIDs,
                                            ProjectController.getInstance().getCurrentProjectID(),
                                            ReferenceController.getInstance().getCurrentReferenceID(),
                                            new String[][]{}, false, emailPlaceholder.getText(), true, annovarCheckbox.isSelected());
                                    succeeded();
                                } catch (Exception ex) {
                                    LOG.error(ex);
                                    instance.addLog("Error: " + ex.getMessage());
                                    instance.setStatus(TaskStatus.ERROR);
                                    AppDirectory.getTaskManager().showErrorForTask(instance, ex);
                                }
                            }

                            private void succeeded() {
                                 AppDirectory.getTaskManager().showMessageForTask(instance,
                                "<html>Variants have been uploaded and are now being processed.<br/>"
                                + "You may view progress in the Server Log in the Task Manager<br/><br/>"
                                + "You may log out or continue doing work.</html>");
                            }

                        });

                        t.start();

                        this.addLog("Done");

                        this.setStatus(TaskStatus.FINISHED);

                        return null;
                    }

                    @Override
                    protected void showSuccess(Object result) {
                    }

                    @Override
                    protected void showFailure(Exception e) {
                    }

                }.start();
            }

        });

        JScrollPane p = ViewUtil.getClearBorderlessScrollPane(container);
        p.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        settingsCard.add(p, BorderLayout.CENTER);

        return settingsCard;
    }

    /**
     * Remove all files
     */
    private void clearFiles() {
        filesToImport.removeAll(filesToImport);
        refreshFileList();
    }

    @Override
    public void didLogout() {
    }

    @Override
    public void didLogin() {
    }
}
