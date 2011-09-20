/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LoginForm.java
 *
 * Created on Jun 20, 2011, 11:11:22 AM
 */
package org.ut.biolab.medsavant.view.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.ut.biolab.medsavant.controller.LoginController;
import org.ut.biolab.medsavant.controller.SettingsController;
import main.ProgramInformation;
import org.ut.biolab.medsavant.exception.NonFatalDatabaseException;
import org.ut.biolab.medsavant.model.event.LoginEvent;
import org.ut.biolab.medsavant.model.event.LoginListener;
import org.ut.biolab.medsavant.view.MainFrame;
import org.ut.biolab.medsavant.view.images.IconFactory;
import org.ut.biolab.medsavant.view.util.ViewUtil;

/**
 *
 * @author mfiume
 */
public class LoginForm extends javax.swing.JPanel implements LoginListener {

    private static class SpiralPanel extends JPanel {
        private final Image img;

        public SpiralPanel() {
            img = IconFactory.getInstance().getIcon(IconFactory.StandardIcon.SPIRAL).getImage();
        }
        
        public void paintComponent(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(img, this.getWidth()/2-img.getWidth(null)/2, this.getHeight()/2-img.getHeight(null)/2, null);
        }
    }

    /** Creates new form LoginForm */
    public LoginForm() {
        
        
        LoginController.addLoginListener(this);
        
        initComponents();
        
        cb_rememberpassword.setVisible(false);
        cb_autosignin.setVisible(false);
        
        if (!ViewUtil.isMac()) {
            this.label_programtitle.setFont(new Font("Georgia", Font.BOLD, 80));
        }
        
        field_username.setText(LoginController.getUsername());
        field_password.setText(LoginController.getPassword());

        this.field_username.setText(SettingsController.getInstance().getUsername());
        if (SettingsController.getInstance().getRememberPassword()) {
            this.field_password.setText(SettingsController.getInstance().getPassword());
        }
        this.cb_rememberpassword.setSelected(SettingsController.getInstance().getRememberPassword());
        this.cb_autosignin.setSelected(SettingsController.getInstance().getAutoLogin());

        this.label_versioninformation.setText(ProgramInformation.getVersion() + " " + ProgramInformation.getReleaseType().toUpperCase());

        ViewUtil.clear(this.cb_autosignin);
        ViewUtil.clear(this.cb_rememberpassword);

        updateAutoSignInCheckBoxBasedOnPasswordCheckbox();

        label_status.setText("");
        this.panel_title.add(Box.createVerticalGlue(),0); 
        
        spiralPanel.setLayout(new BorderLayout());
        spiralPanel.add(new SpiralPanel(),BorderLayout.CENTER); 
        
        ptitle.add(Box.createHorizontalGlue(),0);
        ptitle.add(Box.createHorizontalGlue());         
        
        //this.setBorder(ViewUtil.getGiganticBorder());
        //this.setBackground(Color.white);
        this.setOpaque(false);

        //this.setOpaque(false);
        this.setMaximumSize(new Dimension(400, 400));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cb_autosignin = new javax.swing.JCheckBox();
        cb_rememberpassword = new javax.swing.JCheckBox();
        label_status = new javax.swing.JLabel();
        panel_logonholder = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        field_username = new javax.swing.JTextField();
        field_password = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        button_login = new javax.swing.JButton();
        panel_title = new javax.swing.JPanel();
        ptitle = new javax.swing.JPanel();
        label_programtitle = new javax.swing.JLabel() {
            public void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g2);
            }
        };
        jPanel4 = new javax.swing.JPanel();
        label_versioninformation = new javax.swing.JLabel();
        spiralPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cb_autosignin.setBackground(new java.awt.Color(255, 255, 255));
        cb_autosignin.setForeground(new java.awt.Color(204, 204, 204));
        cb_autosignin.setText("Sign me in automatically");
        cb_autosignin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_autosigninActionPerformed(evt);
            }
        });

        cb_rememberpassword.setBackground(new java.awt.Color(255, 255, 255));
        cb_rememberpassword.setForeground(new java.awt.Color(204, 204, 204));
        cb_rememberpassword.setText("Remember my password");
        cb_rememberpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_rememberpasswordActionPerformed(evt);
            }
        });

        label_status.setForeground(new java.awt.Color(102, 102, 102));
        label_status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_status.setText("status");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cb_autosignin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 596, Short.MAX_VALUE)
                        .addComponent(label_status))
                    .addComponent(cb_rememberpassword))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(cb_rememberpassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_autosignin)
                    .addComponent(label_status))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        panel_logonholder.setBackground(new java.awt.Color(0, 0, 0));
        panel_logonholder.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 200, 10, 200));
        panel_logonholder.setLayout(new javax.swing.BoxLayout(panel_logonholder, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(20, 25));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        panel_logonholder.add(jPanel2);

        field_username.setColumns(25);
        field_username.setFont(new java.awt.Font("Arial", 1, 18));
        field_username.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        field_username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                field_usernameKeyPressed(evt);
            }
        });
        panel_logonholder.add(field_username);

        field_password.setColumns(25);
        field_password.setFont(new java.awt.Font("Arial", 0, 18));
        field_password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        field_password.setAutoscrolls(false);
        field_password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                field_passwordKeyPressed(evt);
            }
        });
        panel_logonholder.add(field_password);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(20, 25));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        panel_logonholder.add(jPanel3);

        button_login.setBackground(new java.awt.Color(0, 0, 0));
        button_login.setText("Login");
        button_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_loginActionPerformed(evt);
            }
        });
        panel_logonholder.add(button_login);

        panel_title.setBackground(new java.awt.Color(0, 0, 0));
        panel_title.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel_title.setLayout(new javax.swing.BoxLayout(panel_title, javax.swing.BoxLayout.Y_AXIS));

        ptitle.setBackground(new java.awt.Color(255, 255, 255));
        ptitle.setOpaque(false);
        ptitle.setLayout(new javax.swing.BoxLayout(ptitle, javax.swing.BoxLayout.LINE_AXIS));

        label_programtitle.setFont(new java.awt.Font("Trebuchet MS", 1, 80));
        label_programtitle.setForeground(new java.awt.Color(255, 255, 255));
        label_programtitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_programtitle.setText("MedSavant");
        ptitle.add(label_programtitle);

        jPanel4.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(20, 25));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        ptitle.add(jPanel4);

        panel_title.add(ptitle);

        label_versioninformation.setFont(new java.awt.Font("Arial", 1, 18));
        label_versioninformation.setForeground(new java.awt.Color(204, 204, 204));
        label_versioninformation.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        label_versioninformation.setText("version information");
        panel_title.add(label_versioninformation);

        javax.swing.GroupLayout spiralPanelLayout = new javax.swing.GroupLayout(spiralPanel);
        spiralPanel.setLayout(spiralPanelLayout);
        spiralPanelLayout.setHorizontalGroup(
            spiralPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 815, Short.MAX_VALUE)
        );
        spiralPanelLayout.setVerticalGroup(
            spiralPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 234, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_logonholder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
            .addComponent(panel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
            .addComponent(spiralPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panel_title, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(spiralPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panel_logonholder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void field_usernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field_usernameKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_field_usernameKeyPressed

    private void field_passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field_passwordKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_field_passwordKeyPressed

    private void cb_rememberpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_rememberpasswordActionPerformed
        String value = SettingsController.booleanToString(this.cb_rememberpassword.isSelected());
        SettingsController.getInstance().setValue(SettingsController.KEY_REMEMBER_PASSWORD, value);
        updateAutoSignInCheckBoxBasedOnPasswordCheckbox();
    }//GEN-LAST:event_cb_rememberpasswordActionPerformed

    private void cb_autosigninActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_autosigninActionPerformed
        String value = SettingsController.booleanToString(this.cb_autosignin.isSelected());
        SettingsController.getInstance().setValue(SettingsController.KEY_AUTOLOGIN, value);
    }//GEN-LAST:event_cb_autosigninActionPerformed

    private void button_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_loginActionPerformed
        this.loginUsingEnteredUsernameAndPassword();
    }//GEN-LAST:event_button_loginActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_login;
    private javax.swing.JCheckBox cb_autosignin;
    private javax.swing.JCheckBox cb_rememberpassword;
    private javax.swing.JPasswordField field_password;
    private javax.swing.JTextField field_username;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel label_programtitle;
    private javax.swing.JLabel label_status;
    private javax.swing.JLabel label_versioninformation;
    private javax.swing.JPanel panel_logonholder;
    private javax.swing.JPanel panel_title;
    private javax.swing.JPanel ptitle;
    private javax.swing.JPanel spiralPanel;
    // End of variables declaration//GEN-END:variables

    private void loginUsingEnteredUsernameAndPassword() {
        this.label_status.setText("signing in...");
        this.label_status.setFont(new Font("Tahoma", Font.BOLD, 16));
        this.label_status.setForeground(Color.gray);
        this.button_login.setEnabled(false);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginController.login(field_username.getText(),field_password.getText());
            }
        });
        //new Thread(new RunLogin(this,this.field_username.getText(),this.field_password.getText())).start();
    }

    private void updateAutoSignInCheckBoxBasedOnPasswordCheckbox() {
        boolean rememberpw = this.cb_rememberpassword.isSelected();
        boolean autosignin = this.cb_autosignin.isSelected();
        if (!rememberpw) {
            this.cb_autosignin.setEnabled(false);
            if (autosignin) {
                this.cb_autosignin.setSelected(false);
                SettingsController.getInstance().setAutoLogin(false);
            }
        } else {
            this.cb_autosignin.setEnabled(true);
        }
    }

    public void notifyOfUnsuccessfulLogin(NonFatalDatabaseException ex) {
        System.out.println("Notified of exception " + ex.getMessage() + " of type " + ex.getExceptionType());
        if (!LoginController.isLoggedIn()) {
            if (ex.getExceptionType() == NonFatalDatabaseException.ExceptionType.TYPE_ACCESS_DENIED) {
                this.label_status.setText("login incorrect");
            } else if (ex.getExceptionType() == NonFatalDatabaseException.ExceptionType.TYPE_DB_CONNECTION_FAILURE
                    ) {
                this.label_status.setText("error accessing database");
            } else if (ex.getExceptionType() == NonFatalDatabaseException.ExceptionType.TYPE_UNKNOWN) {
                this.label_status.setText("login failure");
            }
            this.label_status.setFont(new Font("Arial", Font.BOLD, 15));
            this.label_status.setForeground(Color.red);
            this.field_username.requestFocus();
            this.button_login.setEnabled(true);
        }
    }

    public void loginEvent(LoginEvent evt) {
        switch(evt.getType()) {
            case LOGGED_IN:
                break;
            case LOGGED_OUT:
                break;
            case LOGIN_FAILED:
                notifyOfUnsuccessfulLogin(evt.getException());
                break;
        }
    }
}
