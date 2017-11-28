package com.leeps.dispatcher.dialogs;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.material.MaterialComboBox;
import com.leeps.dispatcher.material.MaterialTextField;
import com.leeps.dispatcher.service.*;
import com.leeps.dispatcher.uidatamodel.*;
import layout.TableLayout;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DispatcherProfileDialog extends BaseDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;

    private JPanel detailTopBottomFillPanel;

    private JLabel infoMatchDispatchStationLabel;
    private JLabel infoMatchPoliceCoveredLabel;
    private JLabel infoMatchFireCoveredLabel;
    private JLabel infoMatchEmtCoveredLabel;

    private JTextField dispatcherFullNameTextField;
    private JTextField dispatcherEmailTextField;
    private JTextField dispatcherIDTextField;
    private JPanel dispatcherInfoPanel;

    private JComboBox<String> dispatcherStateComboBox;
    private JLabel dispatcherStateNameLabel;
    private JComboBox<String> dispatcherCityComboBox;
    private JComboBox<String> dispatcherStationNameComboBox;
    private JLabel dispatchStreetAddressLabel;
    private JPanel dispatcherUnitPanel;

    private ArrayList<DispatchStationModel> policeRowsModelList;
    private ArrayList<DispatchStationModel> fireRowsModelList;
    private ArrayList<DispatchStationModel> medicalRowsModelList;

    public DispatcherProfileDialog(JFrame parentFrame, int width, int height, AppWideCallsService pAppWideCallsService) {
        super(parentFrame, width, height);
        setBackButtonVisible(true);
        setBottomVisible(true);
        setTitle("DISPATCHER PROFILE");
        setUndecorated(false);

        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = new CustomizedUiWidgetsFactory();

        initComponents();
        setVisible(true);
    }

    private void initDispatcherInfoComponents() {
        JPanel dispatchNamePanel = new JPanel(new GridLayout(0, 1));
        JPanel dispatchEmailPanel = new JPanel(new GridLayout(0, 1));
        JPanel dispatchIDPanel = new JPanel(new GridLayout(0, 1));

        DropShadowBorder border = new DropShadowBorder(Color.BLACK, 2,
                0.3f, 2, false,
                false, true, true);

        dispatcherFullNameTextField = new JTextField("FIRSTNAME LASTNAME");
        dispatcherEmailTextField = new JTextField("EMAIL@MAIL.COM");
        dispatcherIDTextField = new JTextField("123456789");

        dispatchNamePanel.add(dispatcherFullNameTextField);
        dispatcherFullNameTextField.setBorder(new EmptyBorder(1, 20, 1, 20));
        dispatcherFullNameTextField.setFont(common.getRobotoFont(12.0f));
        dispatcherFullNameTextField.setForeground(Color.BLACK);
        dispatchNamePanel.setBackground(Color.WHITE);
        dispatchNamePanel.setBorder(border);

        dispatchEmailPanel.add(dispatcherEmailTextField);
        dispatcherEmailTextField.setBorder(new EmptyBorder(1, 20, 1, 20));
        dispatcherEmailTextField.setFont(common.getRobotoFont(12.0f));
        dispatcherEmailTextField.setForeground(Color.BLACK);
        dispatchEmailPanel.setBackground(AppWideStrings.panelBackgroundColor);
        dispatchEmailPanel.setBorder(border);

        dispatchIDPanel.add(dispatcherIDTextField);
        dispatcherIDTextField.setBorder(new EmptyBorder(1, 10, 1, 10));
        dispatcherIDTextField.setFont(common.getRobotoFont(12.0f));
        dispatcherIDTextField.setForeground(Color.BLACK);
        dispatchIDPanel.setBackground(AppWideStrings.panelBackgroundColor);
        dispatchIDPanel.setBorder(border);

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
                90, // 11 TextField your id
                22 // 12 spacing on far right
        }, {
                // Rows
                TableLayout.PREFERRED, // 0 "Dispatcher Info"
                10,
                36, // 1 has Dispatcher Full name, Email, dispatcher ID
                10
        } };

        dispatcherInfoPanel = new JPanel(new TableLayout(dispatcherInfoLayoutSpec));
        JLabel lblInfoString = new JLabel(AppWideStrings.dispatchOperatorInfoString);
        lblInfoString.setFont(common.getRobotoFont(12.0f));
        dispatcherInfoPanel.add(lblInfoString, "1,0,11,0");
        //
        JLabel lblNameString = new JLabel(AppWideStrings.dispatchOperatorNameString);
        lblNameString.setFont(common.getRobotoFont(12.0f));
        lblNameString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherInfoPanel.add(lblNameString, "1,2");
        dispatcherInfoPanel.add(dispatchNamePanel, "3,2");
        //
        JLabel lblEmailString = new JLabel(AppWideStrings.dispatchOperatorEmailString);
        lblEmailString.setFont(common.getRobotoFont(12.0f));
        lblEmailString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherInfoPanel.add(lblEmailString, "5,2");
        dispatcherInfoPanel.add(dispatchEmailPanel, "7,2");
        //
        JLabel lblIDString = new JLabel(AppWideStrings.dispatchOperatorIDString);
        lblIDString.setFont(common.getRobotoFont(12.0f));
        lblIDString.setHorizontalAlignment(SwingConstants.RIGHT);
        dispatcherInfoPanel.add(lblIDString, "9,2");
        dispatcherInfoPanel.add(dispatchIDPanel, "11,2");
        dispatcherInfoPanel.setBackground(AppWideStrings.panelBackgroundColor);

        dispatcherStateComboBox = new JComboBox<String>();
        dispatcherStateComboBox.setEditable(false);
        dispatcherStateComboBox.setBackground(Color.WHITE);
        dispatcherStateComboBox.setUI(new BasicComboBoxUI());

        dispatcherStateNameLabel = new JLabel();
        dispatcherCityComboBox = new JComboBox<String>();
        dispatcherCityComboBox.setEditable(false);
        dispatcherStationNameComboBox = new JComboBox<String>();
        dispatcherStationNameComboBox.setEditable(false);

        dispatchStreetAddressLabel = new JLabel("Street Address");
        dispatchStreetAddressLabel.setFont(common.getRobotoFont(12.0f));
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

        JPanel dispatchStatePanel = new JPanel(new GridLayout(0, 1));
        JPanel dispatchCityPanel = new JPanel(new GridLayout(0, 1));
        JPanel dispatchStationNamePanel = new JPanel(new GridLayout(0, 1));
        JPanel dispatchStreetAddressPanel = new JPanel(new GridLayout(0, 1));

        //
        dispatchStatePanel.setBackground(AppWideStrings.panelBackgroundColor);
        dispatchCityPanel.setBackground(AppWideStrings.panelBackgroundColor);
        dispatchStationNamePanel.setBackground(AppWideStrings.panelBackgroundColor);
        dispatchStreetAddressPanel.setBackground(AppWideStrings.panelBackgroundColor);
        //
        dispatchStatePanel.add(dispatcherStateComboBox);
        dispatchStatePanel.setBorder(border);
        dispatchCityPanel.add(dispatcherCityComboBox);
        dispatchStationNamePanel.add(dispatcherStationNameComboBox);
        dispatchStreetAddressPanel.add(dispatchStreetAddressLabel);
        dispatchStreetAddressPanel.setBorder(border);

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
                10,
                36, // 1 State, City, Dispatch Unit Name, Street Address
                10
        } };

        dispatcherUnitPanel = new JPanel(new TableLayout(dispatchStationLayoutSpec));
        dispatcherUnitPanel.setBackground(AppWideStrings.panelBackgroundColor);
        infoMatchDispatchStationLabel = customizedUiWidgetsFactory.makeFam3IconLabel(
                AppWideStrings.icon_MatchPair, AppWideStrings.icon_MatchPair_HoverHelpText, 20, 20);
        dispatcherUnitPanel.add(infoMatchDispatchStationLabel, "0,0");

        JLabel lblDispatchInfoString = new JLabel(AppWideStrings.dispatchOperatorDispatchUnitLabelString);
        lblDispatchInfoString.setFont(common.getRobotoFont(12.0f));
        dispatcherUnitPanel.add(lblDispatchInfoString, "2,0,18,0");
        //
        dispatcherUnitPanel.add(new JLabel(
                AppWideStrings.stateString), "2,2");
        dispatcherUnitPanel.add(dispatchStatePanel, "4,2");
        //
        dispatcherUnitPanel.add(new JLabel(
                AppWideStrings.cityString), "7,2");
        dispatcherUnitPanel.add(dispatchCityPanel, "9,2");
        //
        dispatcherUnitPanel.add(new JLabel(
                AppWideStrings.stationString), "11,2");
        dispatcherUnitPanel.add(dispatchStationNamePanel, "13,2");
        //
        dispatcherUnitPanel.add(new JLabel(
                AppWideStrings.streetString), "15,2");
        dispatcherUnitPanel.add(dispatchStreetAddressPanel, "17,2");

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
                20,
                TableLayout.PREFERRED, // Has Fire Departments Covered
                20,
                TableLayout.PREFERRED, // Has Emergency Medical Technicians Covered
                TableLayout.FILL, // Bottom stretches, making everything above be preferred heights
                TableLayout.PREFERRED, // All fields must be filled in to ...
                TableLayout.PREFERRED, // Field is used to pair/match ...
                5,
                TableLayout.PREFERRED // Has the bottom buttons
        } };

        detailTopBottomFillPanel = new JPanel(new TableLayout(layoutSpecTopBottomFillSpec));
        detailTopBottomFillPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailTopBottomFillPanel.setBackground(AppWideStrings.panelBackgroundColor);
        detailTopBottomFillPanel.add(dispatcherInfoPanel, "0,0,2,0");
        detailTopBottomFillPanel.add(dispatcherUnitPanel, "0,2,2,2");
    }
    private void initComponents() {
        initDispatcherInfoComponents();
        getCenterPane().setLayout(new BorderLayout());
        getCenterPane().setBackground(AppWideStrings.panelBackgroundColor);
        getCenterPane().add(detailTopBottomFillPanel, BorderLayout.CENTER);
    }

    static class ColorArrowUI extends BasicComboBoxUI {
        public static ColorArrowUI createUI(JComponent c) {
            return new ColorArrowUI();
        }

        @Override protected JButton createArrowButton() {
            return new BasicArrowButton(
                    BasicArrowButton.SOUTH,
                    Color.WHITE, Color.WHITE,
                    Color.BLACK, Color.WHITE);
        }
    }
}
