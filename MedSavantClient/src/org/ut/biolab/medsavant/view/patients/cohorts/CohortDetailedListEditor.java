package org.ut.biolab.medsavant.view.patients.cohorts;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import org.ut.biolab.medsavant.MedSavantClient;
import org.ut.biolab.medsavant.controller.LoginController;
import org.ut.biolab.medsavant.model.Cohort;
import org.ut.biolab.medsavant.view.MedSavantFrame;
import org.ut.biolab.medsavant.view.dialog.CohortWizard;
import org.ut.biolab.medsavant.view.dialog.IndeterminateProgressDialog;
import org.ut.biolab.medsavant.view.list.DetailedListEditor;
import org.ut.biolab.medsavant.view.util.DialogUtils;

/**
 *
 * @author mfiume
 */
public class CohortDetailedListEditor extends DetailedListEditor {

    @Override
    public boolean doesImplementAdding() {
        return true;
    }

    @Override
    public boolean doesImplementDeleting() {
        return true;
    }

    @Override
    public void addItems() {
        new CohortWizard();
    }

    @Override
    public void editItems(Object[] results) {
    }

    @Override
    public void deleteItems(final List<Object[]> items) {

        int result;

        if (items.size() == 1) {
            String name = ((Cohort) items.get(0)[0]).getName();
            result = JOptionPane.showConfirmDialog(MedSavantFrame.getInstance(),
                    "Are you sure you want to remove " + name + "?\nThis cannot be undone.",
                    "Confirm", JOptionPane.YES_NO_OPTION);
        } else {
            result = JOptionPane.showConfirmDialog(MedSavantFrame.getInstance(),
                    "Are you sure you want to remove these " + items.size() + " cohorts?\nThis cannot be undone.",
                    "Confirm", JOptionPane.YES_NO_OPTION);
        }


        if (result == JOptionPane.YES_OPTION) {

            final IndeterminateProgressDialog dialog = new IndeterminateProgressDialog(
                    "Removing Cohort(s)",
                    items.size() + " cohort(s) being removed. Please wait.",
                    true);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    int numCouldntRemove = 0;
                    for (Object[] v : items) {
                        int id = ((Cohort) v[0]).getId();
                        try {
                            MedSavantClient.CohortQueryUtilAdapter.removeCohort(LoginController.sessionId, id);
                        } catch (Exception ex) {
                            numCouldntRemove++;
                            DialogUtils.displayErrorMessage("Couldn't remove " + ((Cohort) v[0]).getName(), ex);
                        }
                    }

                    dialog.close();
                    if (numCouldntRemove != items.size()) {
                        DialogUtils.displayMessage("Successfully removed " + (items.size() - numCouldntRemove) + " cohort(s)");
                    }
                }
            };
            thread.start();
            dialog.setVisible(true);
        }
    }
}