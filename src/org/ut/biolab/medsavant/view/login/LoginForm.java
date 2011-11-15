/*
 *    Copyright 2011 University of Toronto
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

package org.ut.biolab.medsavant.view.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.mysql.jdbc.CommunicationsException;

import java.sql.SQLException;
import org.ut.biolab.medsavant.controller.LoginController;
import org.ut.biolab.medsavant.controller.SettingsController;
import org.ut.biolab.medsavant.MedSavantProgramInformation;
import org.ut.biolab.medsavant.db.exception.NonFatalDatabaseException;
import org.ut.biolab.medsavant.db.util.ConnectionController;
import org.ut.biolab.medsavant.model.event.LoginEvent;
import org.ut.biolab.medsavant.model.event.LoginListener;
import org.ut.biolab.medsavant.util.MiscUtils;
import org.ut.biolab.medsavant.view.dialog.AddDatabaseDialog;
import org.ut.biolab.medsavant.view.images.IconFactory;
import org.ut.biolab.medsavant.view.util.ViewUtil;

/**
 *
 * @author mfiume
 */
public class LoginForm extends JPanel implements LoginListener {
    private static final Logger LOG = Logger.getLogger(LoginForm.class.getName());

    private static class SpiralPanel extends JPanel {
        private final Image img;

        public SpiralPanel() {
            img = IconFactory.getInstance().getIcon(IconFactory.StandardIcon.LOGO).getImage();
        }
        
        @Override
        public void paintComponent(Graphics g) {
            //g.setColor(Color.black);
            //g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(img, this.getWidth()/2-img.getWidth(null)/2, this.getHeight()/2-img.getHeight(null)/2, null);
        }
    }

    /** Creates new form LoginForm */
    public LoginForm() {
        
        LoginController.addLoginListener(this);
        
        initComponents();
        
        rememberPasswordCheck.setVisible(false);
        autoSigninCheck.setVisible(false);
        
        userField.setText(LoginController.getUsername());
        passwordField.setText(LoginController.getPassword());

        userField.setText(SettingsController.getInstance().getUsername());
        if (SettingsController.getInstance().getRememberPassword()) {
            passwordField.setText(SettingsController.getInstance().getPassword());
        }
        rememberPasswordCheck.setSelected(SettingsController.getInstance().getRememberPassword());
        autoSigninCheck.setSelected(SettingsController.getInstance().getAutoLogin());

        versionInfoLabel.setText(MedSavantProgramInformation.getVersion() + " " + MedSavantProgramInformation.getReleaseType().toUpperCase());

        ViewUtil.clear(autoSigninCheck);
        ViewUtil.clear(rememberPasswordCheck);

        updateAutoSignInCheckBoxBasedOnPasswordCheckbox();

        statusLabel.setText("");
        titlePanel.add(Box.createVerticalGlue(),0); 
        
        spiralPanel.setLayout(new BorderLayout());
        spiralPanel.add(new SpiralPanel(),BorderLayout.CENTER); 
        
        detailsPanel.setVisible(false);
        this.button_create_db.setVisible(false);
        
        databaseField.setText(SettingsController.getInstance().getValue(SettingsController.KEY_DB_NAME));
        portField.setText(SettingsController.getInstance().getValue(SettingsController.KEY_DB_PORT));
        hostField.setText(SettingsController.getInstance().getValue(SettingsController.KEY_DB_HOST));
        
        
        this.setOpaque(false);
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
        java.awt.GridBagConstraints gridBagConstraints;

        titlePanel = new javax.swing.JPanel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        rememberPasswordCheck = new javax.swing.JCheckBox();
        autoSigninCheck = new javax.swing.JCheckBox();
        spiralPanel = new javax.swing.JPanel();
        versionInfoLabel = new javax.swing.JLabel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JToggleButton jToggleButton1 = new javax.swing.JToggleButton();
        detailsPanel = new javax.swing.JPanel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        hostField = new javax.swing.JTextField();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JSeparator jSeparator2 = new javax.swing.JSeparator();
        button_create_db = new javax.swing.JButton();
        loginButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        databaseField = new javax.swing.JTextField();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        titlePanel.setBackground(new java.awt.Color(217, 222, 229));
        titlePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titlePanel.setMaximumSize(new java.awt.Dimension(400, 32767));
        titlePanel.setMinimumSize(new java.awt.Dimension(400, 800));

        userField.setColumns(25);
        userField.setFont(new java.awt.Font("Arial", 1, 18));
        userField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                userFieldKeyPressed(evt);
            }
        });

        passwordField.setColumns(25);
        passwordField.setFont(new java.awt.Font("Arial", 0, 18));
        passwordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordFieldKeyPressed(evt);
            }
        });

        rememberPasswordCheck.setBackground(new java.awt.Color(255, 255, 255));
        rememberPasswordCheck.setText("Remember my password");
        rememberPasswordCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rememberPasswordCheckActionPerformed(evt);
            }
        });

        autoSigninCheck.setBackground(new java.awt.Color(255, 255, 255));
        autoSigninCheck.setText("Sign me in automatically");
        autoSigninCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoSigninCheckActionPerformed(evt);
            }
        });

        spiralPanel.setPreferredSize(new java.awt.Dimension(150, 150));

        javax.swing.GroupLayout spiralPanelLayout = new javax.swing.GroupLayout(spiralPanel);
        spiralPanel.setLayout(spiralPanelLayout);
        spiralPanelLayout.setHorizontalGroup(
            spiralPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        spiralPanelLayout.setVerticalGroup(
            spiralPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        versionInfoLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        versionInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        versionInfoLabel.setText("version information");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("username");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("password");

        jToggleButton1.setText("Connection Details");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        detailsPanel.setOpaque(false);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("hostname");

        hostField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hostField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hostFieldKeyPressed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("port");

        portField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        portField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                portFieldKeyPressed(evt);
            }
        });

        button_create_db.setText("Create Database");
        button_create_db.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_create_dbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(hostField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(portField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                    .addComponent(button_create_db, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                .addContainerGap())
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_create_db)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        loginButton.setBackground(new java.awt.Color(0, 0, 0));
        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        statusLabel.setText("Label");

        databaseField.setFont(new java.awt.Font("Arial", 1, 18));
        databaseField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        databaseField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                databaseFieldKeyPressed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("database");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(versionInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .addComponent(spiralPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .addComponent(userField, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(databaseField, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rememberPasswordCheck)
                    .addComponent(autoSigninCheck))
                .addContainerGap())
            .addComponent(detailsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addGap(171, 171, 171)
                .addComponent(loginButton)
                .addContainerGap())
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(spiralPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(versionInfoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(autoSigninCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rememberPasswordCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(loginButton)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(45, 45, 45, 45);
        add(titlePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void passwordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_passwordFieldKeyPressed

    private void rememberPasswordCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rememberPasswordCheckActionPerformed
        String value = SettingsController.booleanToString(rememberPasswordCheck.isSelected());
        SettingsController.getInstance().setValue(SettingsController.KEY_REMEMBER_PASSWORD, value);
        updateAutoSignInCheckBoxBasedOnPasswordCheckbox();
    }//GEN-LAST:event_rememberPasswordCheckActionPerformed

    private void autoSigninCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoSigninCheckActionPerformed
        String value = SettingsController.booleanToString(autoSigninCheck.isSelected());
        SettingsController.getInstance().setValue(SettingsController.KEY_AUTOLOGIN, value);
    }//GEN-LAST:event_autoSigninCheckActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        this.loginUsingEnteredUsernameAndPassword();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void userFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
}//GEN-LAST:event_userFieldKeyPressed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        detailsPanel.setVisible(!detailsPanel.isVisible());
        this.button_create_db.setVisible(!this.button_create_db.isVisible());
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void hostFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hostFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_hostFieldKeyPressed

    private void portFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_portFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_portFieldKeyPressed

    private void databaseFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_databaseFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_databaseFieldKeyPressed

    private void button_create_dbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_create_dbActionPerformed
        AddDatabaseDialog d = new AddDatabaseDialog(hostField.getText(), portField.getText(), databaseField.getText());
        d.setVisible(true);
        
    }//GEN-LAST:event_button_create_dbActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoSigninCheck;
    private javax.swing.JButton button_create_db;
    private javax.swing.JTextField databaseField;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JTextField hostField;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField portField;
    private javax.swing.JCheckBox rememberPasswordCheck;
    private javax.swing.JPanel spiralPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel versionInfoLabel;
    // End of variables declaration//GEN-END:variables

    private void loginUsingEnteredUsernameAndPassword() {
        
        final int port;
        try { port = Integer.parseInt(portField.getText()); }
        catch (Exception e) { portField.requestFocus(); return; }
        
        SettingsController.getInstance().setValue(SettingsController.KEY_DB_NAME, databaseField.getText());
        SettingsController.getInstance().setValue(SettingsController.KEY_DB_PORT, portField.getText());
        SettingsController.getInstance().setValue(SettingsController.KEY_DB_HOST, hostField.getText());
        
        ConnectionController.setDBName(SettingsController.getInstance().getValue(SettingsController.KEY_DB_NAME));
        ConnectionController.setPort(Integer.parseInt(SettingsController.getInstance().getValue(SettingsController.KEY_DB_PORT)));
        ConnectionController.setHost(SettingsController.getInstance().getValue(SettingsController.KEY_DB_HOST));
        
        statusLabel.setText("signing in...");
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.black);
        statusLabel.setEnabled(false);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginController.login(userField.getText(), passwordField.getText());
            }
        });
    }

    private void updateAutoSignInCheckBoxBasedOnPasswordCheckbox() {
        boolean rememberpw = rememberPasswordCheck.isSelected();
        boolean autosignin = autoSigninCheck.isSelected();
        if (!rememberpw) {
            autoSigninCheck.setEnabled(false);
            if (autosignin) {
                autoSigninCheck.setSelected(false);
                SettingsController.getInstance().setAutoLogin(false);
            }
        } else {
            autoSigninCheck.setEnabled(true);
        }
    }

    public void notifyOfUnsuccessfulLogin(Exception ex) {
        
        if (ex instanceof CommunicationsException) {
            setErrorStatus(MiscUtils.extractMySQLMessage((CommunicationsException)ex).toLowerCase());
        } else {
            if (ex instanceof SQLException) {
                if (ex.getMessage().startsWith("Access denied")) {
                    setErrorStatus("access denied");
                    return;
                }
            }
            LOG.log(Level.SEVERE, "Error accessing database.", ex);
            setErrorStatus("error accessing database");
        }
    }

    private void setErrorStatus(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.red);
        userField.requestFocus();
        loginButton.setEnabled(true);
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