package com.leeps.dispatcher.dialogs;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.panels.DispatchCoverageFieldsPanel;
import com.leeps.dispatcher.service.*;
import com.leeps.dispatcher.uidatamodel.*;
import de.craften.ui.swingmaterial.MaterialComboBox;
import de.craften.ui.swingmaterial.MaterialPanel;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DispatcherProfileDialog extends BaseDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private boolean flagRowsField;

    int dispatcherID = 0;
    boolean dispatcherFlag = false;
    JSONObject jsonDispatch = new JSONObject();
    boolean activeFlag = true;

    private JPanel detailTopBottomFillPanel;
    private JPanel dispatcherInfoPanel;

    private JTextField dispatcherFullNameTextField;
    private JTextField dispatcherEmailTextField;
    private JTextField dispatcherIDTextField;

    private MaterialComboBox<String> dispatcherStateComboBox;
    private MaterialComboBox<String> dispatcherCityComboBox;
    private MaterialComboBox<String> dispatcherStationNameComboBox;
    private JLabel dispatchStreetAddressLabel;
    private JPanel dispatcherUnitPanel;


    private JPanel policeDepartmentsCoveredPanel;
    private JPanel fireDepartmentsCoveredPanel;
    private JPanel emtCoveredPanel;

    private JPanel policeStationsPanel;
    private JPanel fireStationsPanel;
    private JPanel emtStationsPanel;

    private JPanel footerPanel;
    private MaterialButton editProfileButton;
    private MaterialButton saveProfileButton;
    private MaterialButton deactiveProfileButton;

    private MaterialPanel coverageEmtAdd, coverageFireAdd, coveragePoliceAdd;
    private MaterialButton addPoliceRowButton;
    private MaterialButton addFireRowButton;
    private MaterialButton addEmtRowButton;
    private JLabel alertLabel;

    private ArrayList<DispatchStationModel> policeStationList;
    private ArrayList<DispatchStationModel> fireStationList;
    private ArrayList<DispatchStationModel> emtStationList;

    public DispatcherProfileDialog(JFrame parentFrame, int width, int height, AppWideCallsService pAppWideCallsService) {
        super(parentFrame, width, height);
        setBackButtonVisible(true);
        setBottomVisible(true);
        setTitle("DISPATCHER PROFILE");
        setUndecorated(true);

        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = new CustomizedUiWidgetsFactory();

        initComponents();
        setVisible(true);
    }

    private void initDispatcherInfoComponents() {
        MaterialPanel dispatchNamePanel = new MaterialPanel();
        MaterialPanel dispatchEmailPanel = new MaterialPanel();
        MaterialPanel dispatchIDPanel = new MaterialPanel();

        DropShadowBorder border = new DropShadowBorder(Color.BLACK, 2,
                0.3f, 2, false,
                false, true, true);

        dispatcherFullNameTextField = new JTextField("FIRSTNAME LASTNAME");
        dispatcherEmailTextField = new JTextField("EMAIL@MAIL.COM");
        dispatcherIDTextField = new JTextField("123456789");

        dispatchNamePanel.setLayout(new GridLayout(0, 1));
        dispatchNamePanel.add(dispatcherFullNameTextField);
        dispatcherFullNameTextField.setBorder(new EmptyBorder(1, 20, 1, 20));
        dispatcherFullNameTextField.setFont(Roboto.BOLD.deriveFont(12.0f));
        dispatcherFullNameTextField.setForeground(Color.BLACK);
        dispatchNamePanel.setBackground(Color.WHITE);

        dispatchEmailPanel.setLayout(new GridLayout(0, 1));
        dispatchEmailPanel.add(dispatcherEmailTextField);
        dispatcherEmailTextField.setBorder(new EmptyBorder(1, 20, 1, 20));
        dispatcherEmailTextField.setFont(Roboto.BOLD.deriveFont(12.0f));
        dispatcherEmailTextField.setForeground(Color.BLACK);
        dispatchEmailPanel.setBackground(Color.WHITE);

        dispatchIDPanel.setLayout(new GridLayout(0, 1));
        dispatchIDPanel.add(dispatcherIDTextField);
        dispatcherIDTextField.setBorder(new EmptyBorder(1, 20, 1, 20));
        dispatcherIDTextField.setFont(Roboto.BOLD.deriveFont(12.0f));
        dispatcherIDTextField.setForeground(Color.BLACK);
        dispatchIDPanel.setBackground(Color.WHITE);

        double[][] dispatcherInfoLayoutSpec = new double[][] { {
                // Columns
                23, // 0 no info icon for this one
                TableLayout.PREFERRED, // 1 "Dispatcher Info" or "Full Name"
                5, // 2 spacing
                240, // 3 TextField full name
                20, // 4 spacing
                TableLayout.PREFERRED, // 5 "Your Email"
                5, // 6 spacing
                240, // 7 TextField your eMail
                20, // 8 spacing
                TableLayout.PREFERRED, // 9 "Your ID"
                5, // 10 spacing
                140, // 11 TextField your id
                22 // 12 spacing on far right
        }, {
                // Rows
                TableLayout.PREFERRED, // 0 "Dispatcher Info"
                10,
                50, // 1 has Dispatcher Full name, Email, dispatcher ID
                10
        } };

        dispatcherInfoPanel = new JPanel(new TableLayout(dispatcherInfoLayoutSpec));
        JLabel lblInfoString = new JLabel(AppWideStrings.dispatchOperatorInfoString);
        lblInfoString.setFont(Roboto.BOLD.deriveFont(12.0f));
        dispatcherInfoPanel.add(lblInfoString, "1,0,11,0");
        //
        JLabel lblNameString = new JLabel(AppWideStrings.dispatchOperatorNameString);
        lblNameString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblNameString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherInfoPanel.add(lblNameString, "1,2");
        dispatcherInfoPanel.add(dispatchNamePanel, "3,2");
        //
        JLabel lblEmailString = new JLabel(AppWideStrings.dispatchOperatorEmailString);
        lblEmailString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblEmailString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherInfoPanel.add(lblEmailString, "5,2");
        dispatcherInfoPanel.add(dispatchEmailPanel, "7,2");
        //
        JLabel lblIDString = new JLabel(AppWideStrings.dispatchOperatorIDString);
        lblIDString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblIDString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherInfoPanel.add(lblIDString, "9,2");
        dispatcherInfoPanel.add(dispatchIDPanel, "11,2");
        dispatcherInfoPanel.setBackground(AppWideStrings.panelBackgroundColor);

        dispatcherStateComboBox = new MaterialComboBox<>();
        dispatcherStateComboBox.setBackground(Color.WHITE);

        dispatcherCityComboBox = new MaterialComboBox<String>();
        dispatcherCityComboBox.setBackground(Color.WHITE);

        dispatcherStationNameComboBox = new MaterialComboBox<String>();
        dispatcherStationNameComboBox.setEditable(false);
        dispatcherStationNameComboBox.setBackground(Color.WHITE);

        dispatchStreetAddressLabel = new JLabel(AppWideStrings.streetString);
        dispatchStreetAddressLabel.setFont(Roboto.BOLD.deriveFont(12.0f));
        dispatchStreetAddressLabel.setBackground(Color.WHITE);
        dispatchStreetAddressLabel.setOpaque(true);
        dispatchStreetAddressLabel.setBorder(new EmptyBorder(1, 20, 1, 20));

        for (int i = 0; i < 5; i++) {
            dispatcherStateComboBox.addItem("State " + i);
        }

        for (int i = 0; i < 5; i++) {
            dispatcherCityComboBox.addItem("City " + i);
        }

        for (int i = 0; i < 5; i++) {
            dispatcherStationNameComboBox.addItem("Station " + i);
        }

        MaterialPanel dispatchStatePanel = new MaterialPanel();
        MaterialPanel dispatchCityPanel = new MaterialPanel();
        MaterialPanel dispatchStationPanel = new MaterialPanel();
        MaterialPanel dispatchStreetAddressPanel = new MaterialPanel();

        //
        dispatchStatePanel.setLayout(new GridLayout(0, 1));
        dispatchStatePanel.setBackground(Color.WHITE);

        dispatchCityPanel.setBackground(Color.WHITE);
        dispatchCityPanel.setLayout(new GridLayout(0, 1));

        dispatchStationPanel.setBackground(Color.WHITE);
        dispatchStationPanel.setLayout(new GridLayout(0, 1));

        dispatchStreetAddressPanel.setBackground(Color.WHITE);
        dispatchStreetAddressPanel.setLayout(new GridLayout(0, 1));
        //
        dispatchStatePanel.add(dispatcherStateComboBox);
        dispatchCityPanel.add(dispatcherCityComboBox);
        dispatchStationPanel.add(dispatcherStationNameComboBox);
        dispatchStreetAddressPanel.add(dispatchStreetAddressLabel);

        double[][] dispatchStationLayoutSpec = new double[][] { {
                // Columns
                20, // 0 info icon
                3, // 1 spacing
                TableLayout.PREFERRED, // 2 "Dispatch Unit" or "State"
                5, // 3 spacing
                140, // 4 ComboBox state
                10,
                10, // 6 spacing
                TableLayout.PREFERRED, // 7 "City"
                5, // 8 spacing
                170, // 9 ComboBox city
                20, // 10 spacing
                TableLayout.PREFERRED, // 11 "Depart/Station String"
                5, // 12 spacing
                140, // 13 ComboBox station name
                20, // 14 spacing
                TableLayout.PREFERRED, // 15 "Street Address"
                5, // 16 spacing
                TableLayout.FILL, // 17 TextField Street Address
                22 // 18 spacing on far right
        }, {
                // Rows
                TableLayout.PREFERRED, // 0 "Dispatch Unit"
                0,
                40, // 1 State, City, Dispatch Unit Name, Street Address
                10
        } };

        dispatcherUnitPanel = new JPanel(new TableLayout(dispatchStationLayoutSpec));
        dispatcherUnitPanel.setBackground(AppWideStrings.panelBackgroundColor);

        JLabel lblDispatchInfoString = new JLabel(AppWideStrings.dispatchOperatorDispatchUnitLabelString);
        lblDispatchInfoString.setFont(Roboto.BOLD.deriveFont(12.0f));
        dispatcherUnitPanel.add(lblDispatchInfoString, "2,0,18,0");
        //
        JLabel lblStateString = new JLabel(AppWideStrings.stateString);
        lblStateString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblStateString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherUnitPanel.add(lblStateString, "2,2");
        dispatcherUnitPanel.add(dispatchStatePanel, "4,2");
        //
        JLabel lblCityString = new JLabel(AppWideStrings.cityString);
        lblCityString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblCityString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherUnitPanel.add(lblCityString, "7,2");
        dispatcherUnitPanel.add(dispatchCityPanel, "9,2");
        //
        JLabel lblStationString = new JLabel(AppWideStrings.stationString);
        lblStationString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblStationString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherUnitPanel.add(lblStationString, "11,2");
        dispatcherUnitPanel.add(dispatchStationPanel, "13,2");
        //
        JLabel lblStreetString = new JLabel(AppWideStrings.streetString);
        lblStreetString.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        lblStreetString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherUnitPanel.add(lblStreetString, "15,2");
        dispatcherUnitPanel.add(dispatchStreetAddressPanel, "17,2");

        try {
            String fullName = jsonDispatch.getString(KeyStrings.keyFirstName) + " " + jsonDispatch.getString(KeyStrings.keyLastName);
            String email = jsonDispatch.getString(KeyStrings.keyEmail);
            String userID = jsonDispatch.getString(KeyStrings.keyUserID);
            dispatcherFullNameTextField.setText(fullName.toUpperCase());
            dispatcherEmailTextField.setText(email.toUpperCase());
            dispatcherIDTextField.setText(userID.toUpperCase());
        } catch (JSONException e) {

        }
    }

    private void initDepartmentInfoComponents() {
        double[][] policeDepartmentsCoveredLayoutspec = new double[][] { {
                // Columns
                20, 3, TableLayout.PREFERRED, // Police Departments Covered
                TableLayout.FILL, // empty space to the right
                100, // Icon for add new row
                30
        }, {
                // Rows
                TableLayout.PREFERRED, // Police Departments Covered
                TableLayout.FILL, // GridLayout DispatchCoverageFieldsPanel list of Dept # - Dept name
                40
        } };

        policeDepartmentsCoveredPanel = new JPanel(new TableLayout(
                policeDepartmentsCoveredLayoutspec));
        policeDepartmentsCoveredPanel.setBackground(
                AppWideStrings.panelBackgroundColor);

        coveragePoliceAdd = new MaterialPanel();
        coveragePoliceAdd.setLayout(new GridLayout(0, 1));
        coveragePoliceAdd.setBackground(AppWideStrings.panelBackgroundColor);

        JLabel lblPoliceDepartmentInfoString = new JLabel(AppWideStrings.dispatchOperatorPoliceDepartmentsCoveredLabelString);
        lblPoliceDepartmentInfoString.setFont(Roboto.BOLD.deriveFont(12.0f));

        policeDepartmentsCoveredPanel.add(lblPoliceDepartmentInfoString, "2,0");

        addPoliceRowButton = new MaterialButton("ADD", new Color(47, 128, 237), Color.WHITE, new Color(9, 90, 220));
        addPoliceRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                DispatchStationModel model = new DispatchStationModel();
                addThisDispatcherCoveredFieldsToPoliceCoverage(model);
            }
        });
        coveragePoliceAdd.add(addPoliceRowButton);

        policeDepartmentsCoveredPanel.add(coveragePoliceAdd, "4,2");
        policeStationsPanel = new JPanel(new GridLayout(0, 1));
        policeDepartmentsCoveredPanel.add(policeStationsPanel, "2,1,5,1");

        double[][] fireDepartmentCoveredLayoutspec = new double[][] { {
                // Columns
                20, 3, TableLayout.PREFERRED, // Fire Departments Covered
                TableLayout.FILL, // empty space to the right
                100, // Icon for add new row
                30
        }, {
                // Rows
                TableLayout.PREFERRED, // Fire Departments Covered
                TableLayout.FILL, // GridLayout DispatchCoverageFieldsPanel list of Dept # - Dept name
                40
        } };

        fireDepartmentsCoveredPanel = new JPanel(new TableLayout(
                fireDepartmentCoveredLayoutspec));
        fireDepartmentsCoveredPanel.setBackground(
                AppWideStrings.panelBackgroundColor);

        coverageFireAdd = new MaterialPanel();
        coverageFireAdd.setLayout(new GridLayout(0, 1));
        coverageFireAdd.setBackground(AppWideStrings.panelBackgroundColor);

        JLabel lblFireDepartmentInfoString = new JLabel(AppWideStrings.dispatchOperatorFireDepartmentsCoveredLabelString);
        lblFireDepartmentInfoString.setFont(Roboto.BOLD.deriveFont(12.0f));
        fireDepartmentsCoveredPanel.add(lblFireDepartmentInfoString, "2,0");

        addFireRowButton = new MaterialButton("ADD", new Color(47, 128, 237), Color.WHITE, new Color(9, 90, 220));

        addFireRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                DispatchStationModel model = new DispatchStationModel();
                addThisDispatcherCoveredFieldsToFireCoverage(model);
            }
        });
        coverageFireAdd.add(addFireRowButton);

        fireDepartmentsCoveredPanel.add(coverageFireAdd, "4,2");
        fireStationsPanel = new JPanel(new GridLayout(0, 1));
        fireDepartmentsCoveredPanel.add(fireStationsPanel, "2,1,5,1");

        double[][] emtCoveredLayoutspec = new double[][] { {
                // Columns
                20, 3, TableLayout.PREFERRED, // EMT Units Covered
                TableLayout.FILL, // empty space to the right
                100, // Icon for add new row
                30
        }, {
                // Rows
                TableLayout.PREFERRED, // EMT Units Covered
                TableLayout.FILL, // GridLayout DispatchCoverageFieldsPanel list of Dept # - Dept name
                40
        } };

        emtCoveredPanel = new JPanel(new TableLayout(emtCoveredLayoutspec));
        emtCoveredPanel.setBackground(
                AppWideStrings.panelBackgroundColor);

        coverageEmtAdd = new MaterialPanel();
        coverageEmtAdd.setLayout(new GridLayout(0, 1));
        coverageEmtAdd.setBackground(AppWideStrings.panelBackgroundColor);

        JLabel lblEmtInfoString = new JLabel(AppWideStrings.dispatchOperatorEMTCoveredLabelString);
        lblEmtInfoString.setFont(Roboto.BOLD.deriveFont(12.0f));
        emtCoveredPanel.add(lblEmtInfoString, "2,0");

        addEmtRowButton = new MaterialButton("ADD", new Color(47, 128, 237), Color.WHITE, new Color(9, 90, 220));

        addEmtRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                DispatchStationModel model = new DispatchStationModel();
                addThisDispatcherCoveredFieldsToEmtCoverage(model);
            }
        });
        coverageEmtAdd.add(addEmtRowButton);

        emtCoveredPanel.add(coverageEmtAdd, "4,2");
        emtStationsPanel = new JPanel(new GridLayout(0, 1));
        emtCoveredPanel.add(emtStationsPanel, "2,1,5,1");
    }

    private void initFooterComponents() {
        double[][] footerLayoutspec = new double[][] { {
                // Columns
                20,  //0, Left Spacing
                80,  //1, Edit Button
                20,  //2, Spacing
                80,  //3, Save Button
                TableLayout.FILL,  //4, empty space to the right
                186,  //5, Deactivate Button
                20  //6, Right Spacing
        }, {
                12,
                36
        } };

        footerPanel = new JPanel(new TableLayout(footerLayoutspec));
        footerPanel.setOpaque(false);

        editProfileButton = new MaterialButton(AppWideStrings.buttonTextEditString, new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
        saveProfileButton = new MaterialButton(AppWideStrings.buttonTextSaveString, new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
        deactiveProfileButton = new MaterialButton("DEACTIVATE PROFILE", new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));

        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleEditButtonClicked();
            }
        });
        saveProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleSaveButtonClicked();
            }
        });
        deactiveProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                if(activeFlag) {
                    activeFlag = false;
                    deactiveProfileButton.setText(AppWideStrings.buttonTextActivateProfileString);
                }else{
                    activeFlag = true;
                    deactiveProfileButton.setText(AppWideStrings.buttonTextDeactivateProfileString);
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(KeyStrings.keyAction, KeyStrings.keyDispatchActive);
                    jsonObject.put(KeyStrings.keyDispatcherID, dispatcherID);
                    jsonObject.put(KeyStrings.keyActiveFlag, activeFlag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appWideCallsService.sendToServer(jsonObject);
//                appWideCallsService.sendToServer(jsonObject);
            }
        });
        footerPanel.add(editProfileButton, "1,1");
        footerPanel.add(saveProfileButton, "3,1");
        footerPanel.add(deactiveProfileButton, "5,1");
    }
    @Override
    public void addNotify() {
        super.addNotify();
        changeAddButtonState(false);
        changePoliceCoveredEditMode(false);
        changeFireCoveredEditMode(false);
        changeEmtCoveredEditMode(false);
        saveProfileButton.setEnabled(false);
        editProfileButton.setEnabled(true);
    }

    private void initData() {
        flagRowsField = true;
        policeStationList = appWideCallsService.getDispatchStationList(0);
        fireStationList = appWideCallsService.getDispatchStationList(1);
        emtStationList = appWideCallsService.getDispatchStationList(2);

        if(!dispatcherFlag) {
            jsonDispatch = appWideCallsService.getDispatcher();
            try {
                dispatcherID = jsonDispatch.getInt(KeyStrings.keyID);
            } catch (JSONException e) {
                JOptionPane.showConfirmDialog(this, "Try again after restart app.");
                System.exit(0);
            }
            dispatcherFlag = true;
        }
    }

    private void initComponents() {
        initData();
        initDispatcherInfoComponents();
        initDepartmentInfoComponents();
        initFooterComponents();
        showDispatcherAndCoverageUiDataModel();

        double[][] layoutSpecTopBottomFillSpec = new double[][] { {
                // Columns
                20, 3, TableLayout.FILL // stretch width from left to right
        }, {
                // Rows
                TableLayout.PREFERRED, // Dispatcher name, Email, ID
                10,
                TableLayout.PREFERRED, // State, City, Dispatch Station, Mailing address
                10,
                TableLayout.PREFERRED, // Has Police Departments Covered
                5,
                TableLayout.PREFERRED, // Has Fire Departments Covered
                5,
                TableLayout.PREFERRED, // Has Emergency Medical Technicians Covered
                TableLayout.FILL, // Bottom stretches, making everything above be preferred heights
                TableLayout.PREFERRED, // All fields must be filled in to ...
                TableLayout.PREFERRED, // Field is used to pair/match ...
                5,
                TableLayout.PREFERRED // Has the bottom buttons
        } };
        alertLabel = new JLabel("");
        alertLabel.setForeground(Color.RED);
        alertLabel.setFont(Roboto.BOLD.deriveFont(16.0f));
        alertLabel.setHorizontalAlignment(SwingConstants.CENTER);

        detailTopBottomFillPanel = new JPanel(new TableLayout(layoutSpecTopBottomFillSpec));
        detailTopBottomFillPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailTopBottomFillPanel.setBackground(AppWideStrings.panelBackgroundColor);
        detailTopBottomFillPanel.add(dispatcherInfoPanel, "0,0,2,0");
        detailTopBottomFillPanel.add(dispatcherUnitPanel, "0,2,2,2");
        detailTopBottomFillPanel.add(policeDepartmentsCoveredPanel, "0,4,2,4");
        detailTopBottomFillPanel.add(fireDepartmentsCoveredPanel, "0,6,2,6");
        detailTopBottomFillPanel.add(emtCoveredPanel, "0,8,2,8");
        detailTopBottomFillPanel.add(alertLabel, "2,10");
        getCenterPane().setLayout(new BorderLayout());
        getCenterPane().setBackground(AppWideStrings.panelBackgroundColor);
        getCenterPane().add(detailTopBottomFillPanel, BorderLayout.CENTER);
        getBottomPane().setLayout(new BorderLayout());
        getBottomPane().add(footerPanel, BorderLayout.CENTER);
    }

    public void addThisDispatcherCoveredFieldsToPoliceCoverage(DispatchStationModel model) {

        DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                new DispatchCoverageFieldsPanel(appWideCallsService, customizedUiWidgetsFactory,
                        policeStationsPanel);

        aDispatchCoverageFieldsPanel.setDispatcherCoveredFields(model);

        policeStationsPanel.add(aDispatchCoverageFieldsPanel);
        policeStationsPanel.revalidate();
    }

    public void addThisDispatcherCoveredFieldsToFireCoverage(DispatchStationModel model) {

        DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                new DispatchCoverageFieldsPanel(appWideCallsService, customizedUiWidgetsFactory,
                        fireStationsPanel);

        aDispatchCoverageFieldsPanel.setDispatcherCoveredFields(model);

        fireStationsPanel.add(aDispatchCoverageFieldsPanel);
        fireStationsPanel.revalidate();
    }

    public void addThisDispatcherCoveredFieldsToEmtCoverage(DispatchStationModel model) {

        DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                new DispatchCoverageFieldsPanel(appWideCallsService, customizedUiWidgetsFactory,
                        emtStationsPanel);

        aDispatchCoverageFieldsPanel.setDispatcherCoveredFields(model);

        emtStationsPanel.add(aDispatchCoverageFieldsPanel);
        emtStationsPanel.revalidate();
    }

    private void changeAddButtonState(boolean pEnabled) {
        coverageEmtAdd.setVisible(pEnabled);
        coverageFireAdd.setVisible(pEnabled);
        coveragePoliceAdd.setVisible(pEnabled);
    }

    private void changePoliceCoveredEditMode(boolean pEditable) {
        for (int index = 0; index < policeStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) policeStationsPanel
                            .getComponent(index);

            aDispatchCoverageFieldsPanel.setFieldsEditable(pEditable);
        }
    }

    private void changeFireCoveredEditMode(boolean pEditable) {
        for (int index = 0; index < fireStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) fireStationsPanel
                            .getComponent(index);

            aDispatchCoverageFieldsPanel.setFieldsEditable(pEditable);
        }
    }

    private void changeEmtCoveredEditMode(boolean pEditable) {
        for (int index = 0; index < emtStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) emtStationsPanel
                            .getComponent(index);

            aDispatchCoverageFieldsPanel.setFieldsEditable(pEditable);
        }
    }

    private void handleEditButtonClicked() {
        changeAddButtonState(true);
        changePoliceCoveredEditMode(true);
        changeFireCoveredEditMode(true);
        changeEmtCoveredEditMode(true);
        saveProfileButton.setEnabled(true);
        alertLabel.setText("");
        editProfileButton.setEnabled(false);
    }

    private void handleSaveButtonClicked() {
        flagRowsField = true;
        checkPoliceRowsField();
        checkFireRowsField();
        checkEmtRowsField();

        if (flagRowsField) {
            if (hasDuplicateCoverageRows()) {
                alertLabel.setText(AppWideStrings.dispatchOperatorThereAreDuplicateRows);
            } else {
                alertLabel.setText("");
                saveDepartInfos();
            }

        } else {
            alertLabel.setText(
                    AppWideStrings.dispatchOperatorAllFieldsMustBeFilledIn);
        }
    }

    private void saveDepartInfos() {
        unRedAllCoverageRowsFields();
//        putDispatcherFieldsInEditMode(false);
        changeAddButtonState(false);
        changePoliceCoveredEditMode(false);
        changeFireCoveredEditMode(false);
        changeEmtCoveredEditMode(false);
        saveProfileButton.setEnabled(false);
        editProfileButton.setEnabled(true);
        //TODO write to server
        ArrayList<DispatchStationModel> newList = new ArrayList<DispatchStationModel>();
        newList.addAll(policeStationList);
        newList.addAll(fireStationList);
        newList.addAll(emtStationList);
        appWideCallsService.setDispatchStation(newList);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KeyStrings.keyAction, KeyStrings.keySetDispatchStation);
            jsonObject.put(KeyStrings.keyDispatcherID, dispatcherID);
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < newList.size(); i++)
            {
                DispatchStationModel model = newList.get(i);
                JSONObject object = new JSONObject();
                object.put(KeyStrings.keyStationID, model.getStation_id());
                object.put(KeyStrings.keyType, model.getType());
                jsonArray.put(object);
            }
            jsonObject.put(KeyStrings.keyValues, jsonArray);
            appWideCallsService.sendToServer(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showDispatcherAndCoverageUiDataModel() {
//        try {
//            dispatcherFullNameTextField.setText(jsonDispatch.getString("first_name") + "  " + jsonDispatch.getString("last_name"));
//            dispatcherYourEmailTextField.setText(jsonDispatch.getString("user_email"));
//            dispatcherYourIDTextField.setText(jsonDispatch.getString("user_id"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        policeStationList = appWideCallsService.getDispatchStationList(0);
        fireStationList = appWideCallsService.getDispatchStationList(1);
        emtStationList = appWideCallsService.getDispatchStationList(2);
//        allDispatcherFieldsFilledIn = false;
        flagRowsField = true;


        policeStationsPanel.removeAll();
        fireStationsPanel.removeAll();
        emtStationsPanel.removeAll();

        if (policeStationList.size() != 0) {
            for (int index = 0; index < policeStationList.size(); index++) {
                DispatchStationModel eachDispatcherCoveredFields = policeStationList.get(index);
                addThisDispatcherCoveredFieldsToPoliceCoverage(eachDispatcherCoveredFields);
            }
        }

        if (fireStationList.size() != 0) {
            for (int index = 0; index < fireStationList.size(); index++) {
                DispatchStationModel eachDispatcherCoveredFields = fireStationList.get(index);
                addThisDispatcherCoveredFieldsToFireCoverage(eachDispatcherCoveredFields);
            }
        }

        if (emtStationList.size() != 0) {
            for (int index = 0; index < emtStationList.size(); index++) {
                DispatchStationModel eachDispatcherCoveredFields = emtStationList.get(index);
                addThisDispatcherCoveredFieldsToEmtCoverage(eachDispatcherCoveredFields);
            }
        }
//        putDispatcherFieldsInEditMode(false);
        changeAddButtonState(false);
        changePoliceCoveredEditMode(false);
        changeFireCoveredEditMode(false);
        changeEmtCoveredEditMode(false);
    }

    private void checkPoliceRowsField() {
        policeStationList.clear();
        for (int index = 0; index < policeStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) policeStationsPanel
                            .getComponent(index);

            DispatchStationModel aCoverageUiDataModel =
                    aDispatchCoverageFieldsPanel.getDispatcherCoveredFields(0);
            if(aCoverageUiDataModel == null) flagRowsField = false;
            policeStationList.add(aCoverageUiDataModel);
        }
    }

    private void checkFireRowsField() {
        fireStationList.clear();
        for (int index = 0; index < fireStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) fireStationsPanel
                            .getComponent(index);

            DispatchStationModel aCoverageUiDataModel =
                    aDispatchCoverageFieldsPanel.getDispatcherCoveredFields(1);
            if(aCoverageUiDataModel == null) flagRowsField = false;
            fireStationList.add(aCoverageUiDataModel);
        }
    }

    private void checkEmtRowsField() {
        emtStationList.clear();
        for (int index = 0; index < emtStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) emtStationsPanel
                            .getComponent(index);

            DispatchStationModel aCoverageUiDataModel =
                    aDispatchCoverageFieldsPanel.getDispatcherCoveredFields(2);
            if(aCoverageUiDataModel == null) flagRowsField = false;
            emtStationList.add(aCoverageUiDataModel);
        }
    }

    private boolean hasDuplicateCoverageRows() {
        boolean theresDuplicateCoverageRows = false;
        int policeRowsCount = policeStationList.size();
        if(policeRowsCount >= 2)
        {
            for(int indexOuter= 0; indexOuter < policeRowsCount; indexOuter++)
            {
                DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel1 =
                        (DispatchCoverageFieldsPanel) policeStationsPanel
                                .getComponent(indexOuter);
                DispatchStationModel model1 = policeStationList.get(indexOuter);
                for(int indexInner = indexOuter + 1; indexInner < policeRowsCount; indexInner++)
                {
                    DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel2 =
                            (DispatchCoverageFieldsPanel) policeStationsPanel
                                    .getComponent(indexInner);
                    DispatchStationModel model2 = policeStationList.get(indexInner);
                    if(model1.getStation_id() == model2.getStation_id())
                    {
                        theresDuplicateCoverageRows = true;
                        aDispatchCoverageFieldsPanel1.setAllEditableFieldsRed(true);
                        aDispatchCoverageFieldsPanel2.setAllEditableFieldsRed(true);
                    }
                }
            }
        }

        int fireRowsCount = fireStationList.size();
        if(fireRowsCount >= 2)
        {
            for(int indexOuter= 0; indexOuter < fireRowsCount; indexOuter++)
            {
                DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel1 =
                        (DispatchCoverageFieldsPanel) fireStationsPanel
                                .getComponent(indexOuter);
                DispatchStationModel model1 = fireStationList.get(indexOuter);
                for(int indexInner = indexOuter + 1; indexInner < fireRowsCount; indexInner++)
                {
                    DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel2 =
                            (DispatchCoverageFieldsPanel) policeStationsPanel
                                    .getComponent(indexInner);
                    DispatchStationModel model2 = fireStationList.get(indexInner);
                    if(model1.getStation_id() == model2.getStation_id())
                    {
                        theresDuplicateCoverageRows = true;
                        aDispatchCoverageFieldsPanel1.setAllEditableFieldsRed(true);
                        aDispatchCoverageFieldsPanel2.setAllEditableFieldsRed(true);
                    }
                }
            }
        }

        int emtRowsCount = emtStationList.size();
        if(emtRowsCount >= 2)
        {
            for(int indexOuter= 0; indexOuter < emtRowsCount; indexOuter++)
            {
                DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel1 =
                        (DispatchCoverageFieldsPanel) emtStationsPanel
                                .getComponent(indexOuter);
                DispatchStationModel model1 = emtStationList.get(indexOuter);
                for(int indexInner = indexOuter + 1; indexInner < emtRowsCount; indexInner++)
                {
                    DispatchCoverageFieldsPanel aDispatchCoverageFieldsPanel2 =
                            (DispatchCoverageFieldsPanel) emtStationsPanel
                                    .getComponent(indexInner);
                    DispatchStationModel model2 = emtStationList.get(indexInner);
                    if(model1.getStation_id() == model2.getStation_id())
                    {
                        theresDuplicateCoverageRows = true;
                        aDispatchCoverageFieldsPanel1.setAllEditableFieldsRed(true);
                        aDispatchCoverageFieldsPanel2.setAllEditableFieldsRed(true);
                    }
                }
            }
        }

        return theresDuplicateCoverageRows;
    }

    private void unRedAllCoverageRowsFields() {
        for (int index = 0; index < policeStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel policeDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) policeStationsPanel
                            .getComponent(index);
            policeDispatchCoverageFieldsPanel.setAllEditableFieldsRed(false);
        }
        for (int index = 0; index < fireStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel fireDispatchCoverageFieldsPanel =
                    (DispatchCoverageFieldsPanel) fireStationsPanel
                            .getComponent(index);
            fireDispatchCoverageFieldsPanel.setAllEditableFieldsRed(false);
        }
        for (int index = 0; index < emtStationsPanel.getComponentCount(); index++) {
            DispatchCoverageFieldsPanel emtDispatchCoverageFieldsPane =
                    (DispatchCoverageFieldsPanel) emtStationsPanel
                            .getComponent(index);
            emtDispatchCoverageFieldsPane.setAllEditableFieldsRed(false);
        }
    }
}
