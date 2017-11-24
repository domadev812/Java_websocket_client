/**
 * Copyright 2017 LEEPS.Inc
 */
package com.leeps.dispatcher.panels;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.service.*;
import com.leeps.dispatcher.uidatamodel.*;
import layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class DispatchCoverageFieldsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private JPanel parentPanel;

    private ArrayList<StateModel> mapState = new ArrayList<StateModel>();
    private ArrayList<CityModel> mapCity = new ArrayList<CityModel>();
    private ArrayList<StationModel> mapStation = new ArrayList<StationModel>();
    private JComboBox<String> coverageStateComboBox;
    private JLabel coverageStateNameLabel;
    private JComboBox<String> coverageCityComboBox;
    private JComboBox<String> coverageStationNameComboBox;

    private JLabel coverageAddressLabel;

    private JButton deleteRowButton;

    public DispatchCoverageFieldsPanel(AppWideCallsService pAppWideCallsService,
                                       CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory, JPanel pParentPanel) {

        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        parentPanel = pParentPanel;

//        mapState = appWideCallsService.getStateList();

        coverageStateComboBox = new JComboBox<String>();
        coverageStateComboBox.setEditable(false);
        coverageStateNameLabel = new JLabel();

        coverageCityComboBox = new JComboBox<String>();
        coverageCityComboBox.setEditable(false);

        coverageStationNameComboBox = new JComboBox<String>();
        coverageStationNameComboBox.setEditable(false);

        coverageAddressLabel = new JLabel();
        coverageStateComboBox.removeAllItems();
//        for (int i = 0; i < mapState.size(); i++) {
//            StateModel model = mapState.get(i);
//            coverageStateComboBox.addItem(model.getStateCode());
//        }

        for (int i = 0; i < 5; i++) {
            coverageStateComboBox.addItem("State " + i);
        }


        coverageCityComboBox.insertItemAt(
                AppWideStrings.defaultComboString, 0);
        for (int i = 0; i < 5; i++) {
            coverageCityComboBox.addItem("State " + i);
        }

        coverageStationNameComboBox.insertItemAt(
                AppWideStrings.defaultComboString, 0);
        for (int i = 0; i < 5; i++) {
            coverageStationNameComboBox.addItem("State " + i);
        }

//        coverageStateComboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent pE) {
//                int index = coverageStateComboBox.getSelectedIndex();
//                if(index == -1) return;
//                StateModel selectedStateObject = mapState.get(index);
//                String selectedStateString = selectedStateObject.getStateName();
//
//                coverageStateNameLabel.setText(selectedStateString);
//
//                coverageCityComboBox.removeAllItems();
//                mapCity = appWideCallsService
//                        .getCityList(selectedStateObject.getId());
//                coverageCityComboBox.insertItemAt(
//                        AppWideStrings.dispatchOperatorComboBoxSelectString, 0);
//
//                for (int i = 0; i < mapCity.size(); i++) {
//                    CityModel model = mapCity.get(i);
//                    coverageCityComboBox.addItem(model.getCityName());
//                }
//
//                coverageStateComboBox.setBackground(Color.WHITE);
//                coverageCityComboBox.setBackground(Color.WHITE);
//                coverageStationNameComboBox.setBackground(Color.WHITE);
//                coverageCityComboBox.setSelectedIndex(0);
//                if(coverageStationNameComboBox.getItemCount() > 0)
//                    coverageStationNameComboBox.setSelectedIndex(0);
//            }
//        });
//
//        coverageCityComboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent pE) {
//                int index = coverageCityComboBox.getSelectedIndex();
//                if(index == 0 || index == -1) {
//                    coverageStationNameComboBox.setSelectedIndex(0);
//                    return;
//                }
//                index--;
//                CityModel selectedCityObject = mapCity.get(index);
//
//                coverageStationNameComboBox.removeAllItems();
//                mapStation = appWideCallsService
//                        .getStationList(selectedCityObject.getId());
//                coverageStationNameComboBox.insertItemAt(
//                        AppWideStrings.dispatchOperatorComboBoxSelectString, 0);
//
//                for (int i = 0; i < mapStation.size(); i++) {
//                    StationModel model = mapStation.get(i);
//                    coverageStationNameComboBox.addItem(model.getStationName());
//                }
//
//                coverageStateComboBox.setBackground(Color.WHITE);
//                coverageCityComboBox.setBackground(Color.WHITE);
//                coverageStationNameComboBox.setBackground(Color.WHITE);
//                coverageStationNameComboBox.setSelectedIndex(0);
//            }
//        });
//
//        coverageStationNameComboBox.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent pE) {
//                coverageStationNameComboBox.setBackground(Color.WHITE);
//                coverageAddressLabel.setForeground(Color.BLACK);
//                int index = coverageStationNameComboBox.getSelectedIndex();
//                if(index == 0 || index == -1)
//                {
//                    coverageAddressLabel.setText("");
//                    return;
//                }
//                index--;
//                StationModel selectedStationObject = mapStation.get(index);
//                coverageAddressLabel.setText(selectedStationObject.getFullAddress());
//            }
//        });

        deleteRowButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_delete_row, AppWideStrings.icon_delete_row_HoverHelpText, 20, 20);
        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleDeleteRowButtonClicked();
            }
        });

        JPanel coverageStatePanel = new JPanel(new GridLayout(0, 1));
        JPanel coverageStateLabelPanel = new JPanel(new GridLayout(0, 1));
        JPanel coverageCityPanel = new JPanel(new GridLayout(0, 1));
        JPanel coverageStationNamePanel = new JPanel(new GridLayout(0, 1));
        JPanel coverageAddressPanel = new JPanel(new GridLayout(0, 1));

        coverageStateLabelPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, UIManager.getColor("Panel.background")),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        coverageAddressPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(5, 5, 5, 5, UIManager.getColor("Panel.background")),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        coverageStatePanel.setBackground(AppWideStrings.innerPanelBackgroundColor);
        coverageStateLabelPanel.setBackground(AppWideStrings.innerPanelBackgroundColor);
        coverageCityPanel.setBackground(AppWideStrings.innerPanelBackgroundColor);
        coverageStationNamePanel.setBackground(AppWideStrings.innerPanelBackgroundColor);
        coverageAddressPanel.setBackground(AppWideStrings.innerPanelBackgroundColor);

        coverageStatePanel.add(coverageStateComboBox);
        coverageStateLabelPanel.add(coverageStateNameLabel);
        coverageCityPanel.add(coverageCityComboBox);
        coverageStationNamePanel.add(coverageStationNameComboBox);
        coverageAddressPanel.add(coverageAddressLabel);

        JPanel deleteRowPanel = new JPanel(new BorderLayout());
        deleteRowPanel.setBackground(AppWideStrings.innerPanelBackgroundColor);
        deleteRowPanel.add(deleteRowButton, BorderLayout.CENTER);
        deleteRowPanel.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
        deleteRowPanel.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);

        double[][] layoutSpec = new double[][]{{
                // Columns
                TableLayout.PREFERRED, // 0 "State"
                2, // 0 spacing
                55, // 2 CombBox state
                100, // 3 Label long state name
                10, // 4 spacing
                TableLayout.PREFERRED, // 5 "City"
                2, // 6 spacing
                170, // 7 CombBox city
                10, // 8 spacing
                113, // 9 "Department / Station"
                2, // 10 spacing
                246, // 11 ComboBox Department / Station
                10, // 12 spacing
                TableLayout.PREFERRED, // 13 "Street Address"
                2, // 14 spacing
                TableLayout.FILL, // 15 TextField Street Address
                3, // 16 spacing
                20 // 17 Delete icon
        }, {
                // Rows
                TableLayout.PREFERRED
        }};

        setBackground(AppWideStrings.innerPanelBackgroundColor);
        setLayout(new TableLayout(layoutSpec));
        add(new JLabel(AppWideStrings.stateString), "0,0");
        add(coverageStatePanel, "2,0");
        add(coverageStateLabelPanel, "3,0");
        add(new JLabel(AppWideStrings.cityString), "5,0");
        add(coverageCityPanel, "7,0");
        add(new JLabel(AppWideStrings.stationString), "9,0");
        add(coverageStationNamePanel, "11,0");
        add(new JLabel(AppWideStrings.streetString), "13,0");
        add(coverageAddressPanel, "15,0");
        add(deleteRowPanel, "17,0");
    }

    public void setFieldsEditable(boolean pEditable) {
        coverageStateComboBox.setEnabled(pEditable);
        coverageCityComboBox.setEnabled(pEditable);
        coverageStationNameComboBox.setEnabled(pEditable);
        deleteRowButton.setEnabled(pEditable);
    }

    public void setTextFieldEditable(JTextField pTextField, boolean pEditable) {
        pTextField.setEditable(pEditable);
        pTextField.setBackground(pEditable ? Color.WHITE : Color.LIGHT_GRAY);
    }

    public void setComboBoxEditable(JComboBox<String> pComboBox, boolean pEditable) {
        pComboBox.setEnabled(pEditable);
    }

    public void setDispatcherCoveredFields(DispatchStationModel model) {
        coverageStateComboBox.setBackground(Color.WHITE);
        coverageStateComboBox.setSelectedItem(model.getState_code());

        coverageStateNameLabel.setText(model.getState_name());

        coverageCityComboBox.setBackground(Color.WHITE);
        coverageCityComboBox.setSelectedItem(model.getCity_name());

        coverageStationNameComboBox.setBackground(Color.WHITE);
        coverageStationNameComboBox.setSelectedItem(model.getStation_name());

        coverageAddressLabel.setForeground(Color.BLACK);
        coverageAddressLabel.setText(model.getFull_address());
    }

    protected boolean areAllPoliceFireEmtRowsFieldsFilledIn() {
        return false;
    }

    public DispatchStationModel getDispatcherCoveredFields(int type) {

        int indState = coverageStateComboBox.getSelectedIndex();
        int indCity = coverageCityComboBox.getSelectedIndex();
        int indStation = coverageStationNameComboBox.getSelectedIndex();
        boolean allFieldsFilledIn = true;
        DispatchStationModel model = new DispatchStationModel();
        if(indState == -1)
        {
            allFieldsFilledIn = false;
            coverageStateComboBox.setBackground(Color.RED);
        }

        if(indCity == -1 || indCity == 0)
        {
            allFieldsFilledIn = false;
            coverageCityComboBox.setBackground(Color.RED);
        }

        if(indStation == -1 || indStation == 0)
        {
            allFieldsFilledIn = false;
            coverageStationNameComboBox.setBackground(Color.RED);
        }

        if(!allFieldsFilledIn) model = null;
        else {
            StateModel modelState = mapState.get(indState);
            CityModel modelCity = mapCity.get(indCity - 1);
            StationModel modelStation = mapStation.get(indStation - 1);

            model.setId(0);
            model.setDispatch_id(0);
            model.setStation_id(modelStation.getId());
            model.setType(type);
            model.setFull_address(modelStation.getFullAddress());
            model.setStation_name(modelStation.getStationName());
            model.setCity_id(modelCity.getId());
            model.setCity_name(modelCity.getCityName());
            model.setState_id(modelState.getId());
            model.setState_name(modelState.getStateName());
            model.setState_code(modelState.getStateCode());
        }
        return model;
    }

    protected void setAllEditableFieldsRed(boolean pMakeRed) {
        if (pMakeRed) {
            coverageStateComboBox.setBackground(Color.RED);
            coverageCityComboBox.setBackground(Color.RED);
            coverageStationNameComboBox.setBackground(Color.RED);
            coverageAddressLabel.setForeground(Color.RED);
        } else {
            coverageStateComboBox.setBackground(Color.WHITE);
            coverageCityComboBox.setBackground(Color.WHITE);
            coverageStationNameComboBox.setBackground(Color.WHITE);
            coverageAddressLabel.setForeground(Color.BLACK);
        }
    }

    private void handleDeleteRowButtonClicked() {
        if (parentPanel == null) {
            return;
        }

        parentPanel.remove(this);
        parentPanel.revalidate();
        freeUpMemory();
    }

    public void freeUpMemory() {
        try {
            coverageStateComboBox = null;
            coverageStateNameLabel = null;
            coverageCityComboBox = null;
            coverageStationNameComboBox = null;
            coverageAddressLabel = null;
            deleteRowButton = null;
            setLayout(null);
        } catch (Exception ex) {
            System.out.println("Row deleted");
        }
    }
}