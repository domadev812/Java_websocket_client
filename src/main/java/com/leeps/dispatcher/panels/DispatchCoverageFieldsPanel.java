/**
 * Copyright 2017 LEEPS.Inc
 */
package com.leeps.dispatcher.panels;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.service.*;
import com.leeps.dispatcher.uidatamodel.*;
import de.craften.ui.swingmaterial.MaterialComboBox;
import de.craften.ui.swingmaterial.MaterialPanel;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private ArrayList<StateModel> listState = new ArrayList<StateModel>();
    private ArrayList<CityModel> listCity = new ArrayList<CityModel>();
    private ArrayList<StationModel> listStation = new ArrayList<StationModel>();
    private MaterialComboBox<String> coverageStateComboBox;
    private MaterialComboBox<String> coverageCityComboBox;
    private MaterialComboBox<String> coverageStationComboBox;

    private JLabel coverageAddressLabel;

    private JButton deleteRowButton;

    public DispatchCoverageFieldsPanel(AppWideCallsService pAppWideCallsService,
                                       CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory, JPanel pParentPanel) {

        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        parentPanel = pParentPanel;

        listState = appWideCallsService.getStateList();

        coverageStateComboBox = new MaterialComboBox<String>();
        coverageStateComboBox.setEditable(false);

        coverageCityComboBox = new MaterialComboBox<String>();
        coverageCityComboBox.setEditable(false);

        coverageStationComboBox = new MaterialComboBox<String>();
        coverageStationComboBox.setEditable(false);

        coverageAddressLabel = new JLabel();
        coverageAddressLabel.setFont(Roboto.BOLD.deriveFont(12.0f));
        coverageAddressLabel.setBackground(Color.WHITE);
        coverageAddressLabel.setOpaque(true);
        coverageAddressLabel.setBorder(new EmptyBorder(1, 20, 1, 20));

        coverageStateComboBox.removeAllItems();
        for (int i = 0; i < listState.size(); i++) {
            coverageStateComboBox.addItem(listState.get(i).getStateName());
        }

        coverageStationComboBox.insertItemAt(
                AppWideStrings.defaultComboString, 0);
        coverageCityComboBox.insertItemAt(
                AppWideStrings.defaultComboString, 0);

        coverageStateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                int index = coverageStateComboBox.getSelectedIndex();
                if(index == -1) return;
                StateModel selectedStateObject = listState.get(index);

                coverageCityComboBox.removeAllItems();
                listCity = appWideCallsService
                        .getCityList(selectedStateObject.getId());
                coverageCityComboBox.insertItemAt(
                        AppWideStrings.defaultComboString, 0);

                for (int i = 0; i < listCity.size(); i++) {
                    coverageCityComboBox.addItem(listCity.get(i).getCityName());
                }

                coverageStateComboBox.setBackground(Color.WHITE);
                coverageCityComboBox.setBackground(Color.WHITE);
                coverageStationComboBox.setBackground(Color.WHITE);
                coverageCityComboBox.setSelectedIndex(0);
                if(coverageStationComboBox.getItemCount() > 0)
                    coverageStationComboBox.setSelectedIndex(0);
            }
        });

        coverageCityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                int index = coverageCityComboBox.getSelectedIndex();
                if(index == 0 || index == -1) {
                    coverageStationComboBox.setSelectedIndex(0);
                    return;
                }
                index--;
                CityModel selectedCityObject = listCity.get(index);

                coverageStationComboBox.removeAllItems();
                listStation = appWideCallsService
                        .getStationList(selectedCityObject.getId());
                coverageStationComboBox.insertItemAt(
                        AppWideStrings.defaultComboString, 0);

                for (int i = 0; i < listStation.size(); i++) {
                    StationModel model = listStation.get(i);
                    coverageStationComboBox.addItem(model.getStationName());
                }

                coverageStateComboBox.setBackground(Color.WHITE);
                coverageCityComboBox.setBackground(Color.WHITE);
                coverageStationComboBox.setBackground(Color.WHITE);
                coverageStationComboBox.setSelectedIndex(0);
            }
        });

        coverageStationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                coverageStationComboBox.setBackground(Color.WHITE);
                coverageAddressLabel.setForeground(Color.BLACK);
                int index = coverageStationComboBox.getSelectedIndex();
                if(index == 0 || index == -1)
                {
                    coverageAddressLabel.setText("");
                    return;
                }
                index--;
                StationModel selectedStationObject = listStation.get(index);
                coverageAddressLabel.setText(selectedStationObject.getFullAddress());
            }
        });

        deleteRowButton = customizedUiWidgetsFactory.makeFam3IconButton(
                AppWideStrings.icon_delete_row, AppWideStrings.icon_delete_row_HoverHelpText, 20, 20);
        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleDeleteRowButtonClicked();
            }
        });
        deleteRowButton.setBorder(BorderFactory.createEmptyBorder());
        deleteRowButton.setBorderPainted(false);
        deleteRowButton.setContentAreaFilled(false);

        MaterialPanel coverageStatePanel = new MaterialPanel();
        MaterialPanel coverageCityPanel = new MaterialPanel();
        MaterialPanel coverageStationPanel = new MaterialPanel();
        MaterialPanel coverageAddressPanel = new MaterialPanel();


        coverageStatePanel.setLayout(new GridLayout(0, 1));
        coverageStatePanel.setBackground(Color.WHITE);

        coverageCityPanel.setBackground(Color.WHITE);
        coverageCityPanel.setLayout(new GridLayout(0, 1));

        coverageStationPanel.setBackground(Color.WHITE);
        coverageStationPanel.setLayout(new GridLayout(0, 1));

        coverageAddressPanel.setBackground(Color.WHITE);
        coverageAddressPanel.setLayout(new GridLayout(0, 1));

        coverageStatePanel.add(coverageStateComboBox);
        coverageCityPanel.add(coverageCityComboBox);
        coverageStationPanel.add(coverageStationComboBox);
        coverageAddressPanel.add(coverageAddressLabel);

        JPanel deleteRowPanel = new JPanel(new BorderLayout());
        deleteRowPanel.setBackground(AppWideStrings.panelBackgroundColor);
        deleteRowPanel.add(deleteRowButton, BorderLayout.CENTER);

        double[][] layoutSpec = new double[][]{{
                // Columns
                TableLayout.PREFERRED, // 0 "State"
                5, // 0 spacing
                140, // 2 CombBox state
                10, // 3 Label long state name
                10, // 4 spacing
                TableLayout.PREFERRED, // 5 "City"
                5, // 6 spacing
                170, // 7 CombBox city
                20, // 8 spacing
                TableLayout.PREFERRED, // 9 "Department / Station"
                5, // 10 spacing
                140, // 11 ComboBox Department / Station
                20, // 12 spacing
                TableLayout.PREFERRED, // 13 "Street Address"
                5, // 14 spacing
                TableLayout.FILL, // 15 TextField Street Address
                10, // 16 spacing
                20 // 17 Delete icon
        }, {
                // Rows
                40
        }};

        setBackground(AppWideStrings.panelBackgroundColor);
        setLayout(new TableLayout(layoutSpec));

        JLabel lblStateString = new JLabel(AppWideStrings.stateString);
        lblStateString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblStateString.setHorizontalAlignment(SwingConstants.RIGHT);

        add(lblStateString, "0,0");
        add(coverageStatePanel, "2,0");

        JLabel lblCityString = new JLabel(AppWideStrings.cityString);
        lblCityString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblCityString.setHorizontalAlignment(SwingConstants.RIGHT);

        add(lblCityString, "5,0");
        add(coverageCityPanel, "7,0");

        JLabel lblStationString = new JLabel(AppWideStrings.stationString);
        lblStationString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblStationString.setHorizontalAlignment(SwingConstants.RIGHT);

        add(lblStationString, "9,0");
        add(coverageStationPanel, "11,0");

        JLabel lblStreetString = new JLabel(AppWideStrings.streetString);
        lblStreetString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblStreetString.setHorizontalAlignment(SwingConstants.RIGHT);

        add(lblStreetString, "13,0");
        add(coverageAddressPanel, "15,0");
        add(deleteRowPanel, "17,0");
    }

    public void setFieldsEditable(boolean pEditable) {
        coverageStateComboBox.setEnabled(pEditable);
        coverageCityComboBox.setEnabled(pEditable);
        coverageStationComboBox.setEnabled(pEditable);
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
        coverageStateComboBox.setSelectedItem(model.getState_name());
        coverageStateComboBox.setBackground(Color.WHITE);

        coverageCityComboBox.setBackground(Color.WHITE);
        coverageCityComboBox.setSelectedItem(model.getCity_name());

        coverageStationComboBox.setBackground(Color.WHITE);
        coverageStationComboBox.setSelectedItem(model.getStation_name());

        coverageAddressLabel.setForeground(Color.BLACK);
        coverageAddressLabel.setText(model.getFull_address());
    }

    protected boolean areAllPoliceFireEmtRowsFieldsFilledIn() {
        return false;
    }

    public DispatchStationModel getDispatcherCoveredFields(int type) {

        int indState = coverageStateComboBox.getSelectedIndex();
        int indCity = coverageCityComboBox.getSelectedIndex();
        int indStation = coverageStationComboBox.getSelectedIndex();
        boolean allFieldsFilledIn = true;
        DispatchStationModel model = new DispatchStationModel();
        if(indState == -1)
            allFieldsFilledIn = false;

        if(indCity == -1 || indCity == 0)
            allFieldsFilledIn = false;

        if(indStation == -1 || indStation == 0)
            allFieldsFilledIn = false;

        if(!allFieldsFilledIn) model = null;
        else {
            StateModel modelState = listState.get(indState);
            CityModel modelCity = listCity.get(indCity - 1);
            StationModel modelStation = listStation.get(indStation - 1);

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

    public void setAllEditableFieldsRed(boolean pMakeRed) {
        if (pMakeRed)
            coverageAddressLabel.setForeground(Color.RED);
         else
            coverageAddressLabel.setForeground(Color.BLACK);
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
            coverageCityComboBox = null;
            coverageStationComboBox = null;
            coverageAddressLabel = null;
            deleteRowButton = null;
            setLayout(null);
        } catch (Exception ex) {
            System.out.println("Row deleted");
        }
    }
}