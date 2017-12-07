package com.leeps.dispatcher.dialogs;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.service.*;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AlarmsPendingDialog extends BaseDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private AppWideCallsService appWideCallsService;
    private JPanel rowsNameListPanel;
    private JPanel pendingNameListPanel;
    private ArrayList<JSONObject> alarmDataList;
    private JPanel buttomButtonPanel;
    private JButton closeButton;

    public AlarmsPendingDialog(JFrame pParentFrame, int width, int height,
                               CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory,
                               AppWideCallsService pAppWideCallsService) {
        super(pParentFrame, width, height);

        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        appWideCallsService = pAppWideCallsService;
        setBackButtonVisible(true);
        setBottomVisible(false);
        setTitle("ALARMS PENDING");
        setUndecorated(true);
        initComponents();
    }

    private void initComponents() {
        buildPendingNameListPanel();
        buildBottomButtonPanel();
        buildContentPane();
    }

    private void buildPendingNameListPanel() {
        rowsNameListPanel = new JPanel();
        rowsNameListPanel.setLayout(new GridLayout(0, 1));
        rowsNameListPanel.setBorder(BorderFactory.createEmptyBorder());
        rowsNameListPanel.setBackground(AppWideStrings.panelBackgroundColor);

        replaceRowsNameListPanel(null);

        JScrollPane pendingNameListScrollPane = new JScrollPane(rowsNameListPanel);
        pendingNameListScrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pendingNameListScrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        pendingNameListScrollPane.setBorder(BorderFactory.createEmptyBorder());
        double scrollLayoutSpec[][] = {{
                // columns
                40,
                TableLayout.FILL,
                30
        }, {
                // rows
                25,
                140,
                25
        }};
        pendingNameListPanel = new JPanel(new TableLayout(scrollLayoutSpec));
        pendingNameListPanel.setOpaque(false);
        pendingNameListPanel.add(pendingNameListScrollPane, "1, 1");
    }

    public void replaceRowsNameListPanel(ArrayList<JSONObject> pAlarmDataList) {

        alarmDataList = pAlarmDataList;
        rowsNameListPanel.removeAll();
        if(alarmDataList == null || alarmDataList.size() == 0)
        {
            rowsNameListPanel.add(makeNeedsAssistanceNamePanel(
                    AppWideStrings.alarmsPendingDialogNoAlarmsString, "",-1));
            return;
        }
        for (int index = 0; index < alarmDataList.size(); index++) {
            String eachName = "", badgeNumber = "";
            JSONObject jsonObject = alarmDataList.get(index);
            try {
                eachName = jsonObject.getString(KeyStrings.keyFirstName) + " " + jsonObject.getString(KeyStrings.keyLastName);
                badgeNumber = jsonObject.getString(KeyStrings.keyBadgeNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rowsNameListPanel.add(makeNeedsAssistanceNamePanel(eachName, badgeNumber, index));
        }
    }

    private JPanel makeNeedsAssistanceNamePanel(String officerName, String badgeNumber, int pRowNumber) {
        double officerLayoutSpec[][] = {{
                // columns
                TableLayout.FILL,
                30,
                TableLayout.FILL,
                20,
                120,
                10
        }, {
                0,
                40
        }};
        JPanel needsAssistanceNamePanel = new JPanel(new TableLayout(officerLayoutSpec));
        needsAssistanceNamePanel.setOpaque(false);

        JLabel nameLabel = new JLabel();
        nameLabel.setFont(Roboto.MEDIUM.deriveFont(20.0f));
        nameLabel.setText(officerName);
        needsAssistanceNamePanel.add(nameLabel, "0, 1");

        JLabel badgeNumberLabel = new JLabel();
        badgeNumberLabel.setText(badgeNumber);
        badgeNumberLabel.setFont(Roboto.MEDIUM.deriveFont(20.0f));
        needsAssistanceNamePanel.add(badgeNumberLabel, "2, 1");

        if (pRowNumber == 0) {
            MaterialButton handleButton = new MaterialButton(AppWideStrings.handleOfficerButtonStirng, new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
            handleButton.setFont(Roboto.BOLD.deriveFont(20.0f));
            needsAssistanceNamePanel.add(handleButton, "4, 1");
            if(!appWideCallsService.isHandled()) {
                handleButton.setConfiguration(new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
                handleButton.setEnabled(true);
            }
            else {
                handleButton.setConfiguration(new Color(0x4F, 0x4F, 0x4F), Color.WHITE, new Color(0x4F, 0x4F, 0x4F));
                handleButton.setEnabled(false);
            }
            handleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent pE) {
                    JSONObject handledObject = alarmDataList.get(0);
                    appWideCallsService.setHandled(true);
                    alarmDataList.remove(0);
                    appWideCallsService.removeWaitingOfficer(handledObject);
                    try {
                        appWideCallsService.setLat(handledObject.getDouble(KeyStrings.keyLatitude));
                        appWideCallsService.setLon(handledObject.getDouble(KeyStrings.keyLongitude));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    appWideCallsService.blinkAppIcon(false);
                    try {
                        handledObject.put(KeyStrings.keyAction, KeyStrings.keyOfficerHandle);
                        handledObject.put(KeyStrings.keyDispatcherID, appWideCallsService.getDispatcherID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    appWideCallsService.sendToServer(handledObject);
//                    appWideCallsService.initOfficerGraph(true);
                    setVisible(false);
                    appWideCallsService.showHandledOfficer();
                }
            });

        }
        return needsAssistanceNamePanel;
    }

    public void buildBottomButtonPanel() {
        double bottomLayoutSpec[][] = {{
                // columns
                40,
                TableLayout.FILL,
                30
        }, {
                10,
                TableLayout.PREFERRED,
                5,
                TableLayout.PREFERRED,
                15
        }};
        buttomButtonPanel = new JPanel(new TableLayout(bottomLayoutSpec));

        JLabel infoLabel1 = new JLabel(
                AppWideStrings.alarmsAreTakenInOrderString);
        JLabel infoLabel2 = new JLabel(
                AppWideStrings.youAreOnlyAllowedToHandleTheFirstOfficerString);

        infoLabel1.setFont(Roboto.REGULAR.deriveFont(14.0f));
        infoLabel2.setFont(Roboto.REGULAR.deriveFont(14.0f));

        buttomButtonPanel.add(infoLabel1, "1, 1");
        buttomButtonPanel.add(infoLabel2, "1, 3");
        buttomButtonPanel.setOpaque(false);
    }

    public void buildContentPane() {
        getCenterPane().setLayout(new BorderLayout());
        getCenterPane().setBackground(AppWideStrings.panelBackgroundColor);
        getCenterPane().add(pendingNameListPanel, BorderLayout.CENTER);
        getCenterPane().add(buttomButtonPanel, BorderLayout.SOUTH);
        pack();
    }
}
