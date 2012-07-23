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

package org.ut.biolab.medsavant.region;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;

import com.healthmarketscience.sqlbuilder.Condition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.ut.biolab.medsavant.MedSavantClient;
import org.ut.biolab.medsavant.controller.FilterController;
import org.ut.biolab.medsavant.login.LoginController;
import org.ut.biolab.medsavant.model.RegionSet;
import org.ut.biolab.medsavant.model.Filter;
import org.ut.biolab.medsavant.view.genetics.filter.FilterState;
import org.ut.biolab.medsavant.view.genetics.filter.FilterView;


/**
 *
 * @author mfiume
 */
public class RegionSetFilterView extends FilterView {
    private static final Log LOG = LogFactory.getLog(RegionSetFilterView.class);

    public static final String FILTER_NAME = "Region Set";
    public static final String FILTER_ID = "region_set";
    private static final String REGION_SET_NONE = "None";

    private Integer appliedID = null;

    private JComboBox regionsCombo;
    private JButton applyButton;

    public RegionSetFilterView(FilterState state, int queryID) throws SQLException, RemoteException {
        this(queryID);
        if (state.getValues().get("value") != null) {
            applyFilter(Integer.parseInt(state.getValues().get("value")));
        }
    }

    public RegionSetFilterView(int queryID) throws SQLException, RemoteException {
        super(FILTER_NAME, queryID);
        setLayout(new GridBagLayout());

        regionsCombo = new JComboBox();

        regionsCombo.addItem(REGION_SET_NONE);
        RegionSet[] geneLists = getDefaultValues();
        for (RegionSet set: geneLists) {
            regionsCombo.addItem(set);
        }

        applyButton = new JButton("Apply");
        applyButton.setEnabled(false);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyCurrentFilter();
            }
        });

        regionsCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyButton.setEnabled(true);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(regionsCombo, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(applyButton, gbc);
    }

    public final void applyFilter(int regionSetID) {
        for (int i = 0; i < regionsCombo.getItemCount(); i++) {
            if (regionsCombo.getItemAt(i) instanceof RegionSet && ((RegionSet)regionsCombo.getItemAt(i)).getID() == regionSetID) {
                regionsCombo.setSelectedIndex(i);
                applyCurrentFilter();
                return;
            }
        }
    }

    private RegionSet[] getDefaultValues() throws SQLException, RemoteException {
        return MedSavantClient.RegionSetManager.getRegionSets(LoginController.sessionId);
    }

    @Override
    public FilterState saveState() {
        Map<String, String> map = new HashMap<String, String>();
        if (appliedID != null) map.put("value", Integer.toString(appliedID));
        return new FilterState(Filter.Type.REGION_LIST, FILTER_NAME, FILTER_ID, map);
    }

    private void applyCurrentFilter() {
        applyButton.setEnabled(false);

        Filter f = new RegionSetFilter() {

            @Override
            public Condition[] getConditions() throws SQLException, RemoteException {

                if (regionsCombo.getSelectedItem().equals(REGION_SET_NONE)) {
                    return new Condition[0];
                }

                RegionSet regionSet = (RegionSet)regionsCombo.getSelectedItem();
                appliedID = regionSet.getID();

                return getConditions(MedSavantClient.RegionSetManager.getRegionsInSet(LoginController.sessionId, regionSet, Integer.MAX_VALUE));
            }

            @Override
            public String getName() {
                return FILTER_NAME;
            }


            @Override
            public String getID() {
                return FILTER_ID;
            }
        };
        LOG.info(String.format("Adding filter: %s.", f.getName()));
        FilterController.addFilter(f, getQueryID());
    }

    @Override
    public void resetView() {
        regionsCombo.setSelectedIndex(0);
    }
}