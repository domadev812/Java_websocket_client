package com.leeps.dispatcher.panels;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.service.AppWideCallsService;
import com.leeps.dispatcher.uidatamodel.StationModel;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class OfficerProfilePanel extends JPanel {
    private static final long serialVersionUID = -7457509470292039648L;
    private AppWideCallsService appWideCallsService;

    private JPanel row1Panel;
    private JPanel row2Panel;

    private JLabel currentOfficerPicLabel;
    private JLabel officerNameLabel;
    private JLabel officerPhoneLabel;
    private JLabel officerRankLabel;
    private JLabel officerBadgeNumberLabel;
    private JLabel officerGenderLabel;
    private JLabel officerAgeLabel;
    private JLabel officerRaceLabel;
    private JLabel officerHeightLabel;
    private JLabel officerWeightLabel;
    private JLabel officerEmailLabel;
    private JLabel officerBloodTypeLabel;
    private JLabel officerAllergiesLabel;
    private JLabel officerEmergencyContactNumberLabel;
    private JLabel officerContactRelationshipLabel;
    private JLabel officerCurrentPoliceDepartmentLabel;
    private JLabel officerDepartmentPhoneLabel;

    private JLabel officerNameDataLabel;
    private JLabel officerPhoneDataLabel;
    private JLabel officerRankDataLabel;
    private JLabel officerBadgeNumberDataLabel;
    private JLabel officerGenderDataLabel;
    private JLabel officerAgeDataLabel;
    private JLabel officerRaceDataLabel;
    private JLabel officerHeightDataLabel;
    private JLabel officerWeightDataLabel;
    private JLabel officerEmailDataLabel;
    private JLabel officerBloodTypeDataLabel;
    private JLabel officerAllergiesDataLabel;
    private JLabel officerEmergencyContactNumberDataLabel;
    private JLabel officerContactRelationshipDataLabel;
    private JLabel officerCurrentPoliceDepartmentDataLabel;
    private JLabel officerDepartmentPhoneDataLabel;

    public OfficerProfilePanel(AppWideCallsService appWideCallsService) {
        double officerProfilePanelLayoutSpec[][] = {{
                // columns
                TableLayout.FILL
        }, {
                // rows
                TableLayout.PREFERRED,
                TableLayout.PREFERRED
        }};

        JPanel hasTwoRowsPanel = new JPanel(new TableLayout(officerProfilePanelLayoutSpec));
        hasTwoRowsPanel.setBorder(BorderFactory.createEmptyBorder());
        hasTwoRowsPanel.setBackground(AppWideStrings.whiteColor);
        hasTwoRowsPanel.setOpaque(true);
        buildRow1Panel();
        buildRow2Panel();

        hasTwoRowsPanel.add(row1Panel, "0,0");
        hasTwoRowsPanel.add(row2Panel, "0,1");

        setMinimumSize(new Dimension(400, 50));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new GridLayout(0, 1));
        add(hasTwoRowsPanel);
        this.appWideCallsService = appWideCallsService;
    }

    private void buildRow1Panel() {
        double leftColPanelLayoutSpec[][] = {{
                // columns
                0,
                75,      // Label
                20,
                TableLayout.FILL,           // Data
                30
        }, {
                // rows
                0,
                TableLayout.PREFERRED,      // Officer Name
                10,
                TableLayout.PREFERRED,      // Officer Gender
                10,
                TableLayout.PREFERRED,      // Officer Race
                10,
                TableLayout.PREFERRED,      // Officer Age
                10,
                TableLayout.PREFERRED      // Officer Height
        }};
        JPanel leftColPanel = new JPanel(new TableLayout(leftColPanelLayoutSpec));
        leftColPanel.setBorder(BorderFactory.createEmptyBorder());

        officerNameLabel = new JLabel(AppWideStrings.officerNameString);
        officerNameDataLabel = new JLabel("");
        officerNameLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerNameDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerNameLabel.setForeground(AppWideStrings.whiteColor);
        officerNameDataLabel.setForeground(AppWideStrings.whiteColor);

        officerGenderLabel = new JLabel(AppWideStrings.officerGenderString);
        officerGenderDataLabel = new JLabel("");
        officerGenderLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerGenderDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerGenderLabel.setForeground(AppWideStrings.whiteColor);
        officerGenderDataLabel.setForeground(AppWideStrings.whiteColor);

        officerRaceLabel = new JLabel(AppWideStrings.officerRaceString);
        officerRaceDataLabel = new JLabel("");
        officerRaceLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerRaceDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerRaceLabel.setForeground(AppWideStrings.whiteColor);
        officerRaceDataLabel.setForeground(AppWideStrings.whiteColor);

        officerAgeLabel = new JLabel(AppWideStrings.officerAgeString);
        officerAgeDataLabel = new JLabel("");
        officerAgeLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerAgeDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerAgeLabel.setForeground(AppWideStrings.whiteColor);
        officerAgeDataLabel.setForeground(AppWideStrings.whiteColor);

        officerHeightLabel = new JLabel(AppWideStrings.officerHeightString);
        officerHeightDataLabel = new JLabel("");
        officerHeightLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerHeightDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerHeightLabel.setForeground(AppWideStrings.whiteColor);
        officerHeightDataLabel.setForeground(AppWideStrings.whiteColor);

        leftColPanel.add(officerNameLabel, "1, 1");
        leftColPanel.add(officerNameDataLabel, "3, 1");

        leftColPanel.add(officerGenderLabel, "1, 3");
        leftColPanel.add(officerGenderDataLabel, "3, 3");

        leftColPanel.add(officerRaceLabel, "1, 5");
        leftColPanel.add(officerRaceDataLabel, "3, 5");

        leftColPanel.add(officerAgeLabel, "1, 7");
        leftColPanel.add(officerAgeDataLabel, "3, 7");

        leftColPanel.add(officerHeightLabel, "1, 9");
        leftColPanel.add(officerHeightDataLabel, "3, 9");
        leftColPanel.setOpaque(false);

        double rightColPanelLayoutSpec[][] = {{
                // columns
                0,
                100,      // Label
                10
        }, {
                100
        }};
        JPanel rightColPanel = new JPanel(new TableLayout(rightColPanelLayoutSpec));
        currentOfficerPicLabel = new JLabel();
        rightColPanel.add(currentOfficerPicLabel, "1, 0");
        rightColPanel.setOpaque(false);
        rightColPanel.setBorder(BorderFactory.createEmptyBorder());

        double bottomRowPanelLayoutSpec[][] = {{
                // columns
                0,
                75,      // Label
                20,
                TableLayout.FILL,           // Data
                30
        }, {
                // rows
                0,
                TableLayout.PREFERRED,      // Officer Weight
                10,
                TableLayout.PREFERRED,      // Officer Badge
                10,
                TableLayout.PREFERRED,      // Officer Blood
                10,
                TableLayout.PREFERRED,      // Officer Allergy
        }};

        officerWeightLabel = new JLabel(AppWideStrings.officerWeightString);
        officerWeightDataLabel = new JLabel("");
        officerWeightLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerWeightDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerWeightLabel.setForeground(AppWideStrings.whiteColor);
        officerWeightDataLabel.setForeground(AppWideStrings.whiteColor);

        officerBadgeNumberLabel = new JLabel(AppWideStrings.officerBadgeNumberString);
        officerBadgeNumberDataLabel = new JLabel("");
        officerBadgeNumberLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerBadgeNumberDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerBadgeNumberLabel.setForeground(AppWideStrings.whiteColor);
        officerBadgeNumberDataLabel.setForeground(AppWideStrings.whiteColor);

        officerBloodTypeLabel = new JLabel(AppWideStrings.officerBloodTypeString);
        officerBloodTypeDataLabel = new JLabel("");
        officerBloodTypeLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerBloodTypeDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerBloodTypeLabel.setForeground(AppWideStrings.whiteColor);
        officerBloodTypeDataLabel.setForeground(AppWideStrings.whiteColor);

        officerAllergiesLabel = new JLabel(AppWideStrings.officerAllergiesString);
        officerAllergiesDataLabel = new JLabel("");
        officerAllergiesLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerAllergiesDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerAllergiesLabel.setForeground(AppWideStrings.whiteColor);
        officerAllergiesDataLabel.setForeground(AppWideStrings.whiteColor);

        JPanel bottomRowPanel = new JPanel(new TableLayout(bottomRowPanelLayoutSpec));
        bottomRowPanel.setOpaque(false);
        bottomRowPanel.add(officerWeightLabel, "1, 1");
        bottomRowPanel.add(officerWeightDataLabel, "3, 1");

        bottomRowPanel.add(officerBadgeNumberLabel, "1, 3");
        bottomRowPanel.add(officerBadgeNumberDataLabel, "3, 3");

        bottomRowPanel.add(officerBloodTypeLabel, "1, 5");
        bottomRowPanel.add(officerBloodTypeDataLabel, "3, 5");

        bottomRowPanel.add(officerAllergiesLabel, "1, 7");
        bottomRowPanel.add(officerAllergiesDataLabel, "3, 7");
        bottomRowPanel.setBorder(BorderFactory.createEmptyBorder());

        double row1PanelLayoutSpec[][] = {{
                // columns
                40,
                TableLayout.PREFERRED,      // Left Column
                TableLayout.FILL,
                TableLayout.PREFERRED,           // Profile Image
                10
        }, {
                20,
                TableLayout.PREFERRED,      // Left and Right
                10,
                TableLayout.PREFERRED,
                15
        }};
        row1Panel = new JPanel(new TableLayout(row1PanelLayoutSpec));
        row1Panel.setBorder(BorderFactory.createEmptyBorder());
        row1Panel.setBackground(AppWideStrings.profileBackgroundColor);
        row1Panel.setOpaque(true);
        row1Panel.add(leftColPanel, "1, 1");
        row1Panel.add(rightColPanel, "3, 1");
        row1Panel.add(bottomRowPanel, "1, 3, 3, 3");
    }

    private void buildRow2Panel() {
        double row2PanelLayoutSpec[][] = {{
                // columns
                40,
                75,      // Label
                20,
                TableLayout.FILL,           // Data
                30
        }, {
                // rows
                15,
                TableLayout.PREFERRED,      // Officer Phone
                10,
                TableLayout.PREFERRED,      // Officer Email
                10,
                TableLayout.PREFERRED,      // Emergency Relationship
                10,
                TableLayout.PREFERRED,      // Emergency Contact Number
                10,
                TableLayout.PREFERRED,      // Officer Rank
                10,
                TableLayout.PREFERRED,      // Officer Department
                10,
                TableLayout.PREFERRED,      // Officer Department Phone
                15
        }};
        row2Panel = new JPanel(new TableLayout(row2PanelLayoutSpec));
        row2Panel.setBackground(AppWideStrings.whiteColor);
        row2Panel.setOpaque(true);
        officerPhoneLabel = new JLabel(AppWideStrings.officerPhoneString);
        officerPhoneDataLabel = new JLabel("");
        officerPhoneLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerPhoneDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerPhoneLabel.setForeground(AppWideStrings.primaryColor);
        officerPhoneDataLabel.setForeground(AppWideStrings.primaryColor);

        officerEmailLabel = new JLabel(AppWideStrings.officerEmailString);
        officerEmailDataLabel = new JLabel("");
        officerEmailLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerEmailDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerEmailLabel.setForeground(AppWideStrings.primaryColor);
        officerEmailDataLabel.setForeground(AppWideStrings.primaryColor);

        officerContactRelationshipLabel = new JLabel(
                AppWideStrings.officerContactRelationshipString);
        officerContactRelationshipDataLabel = new JLabel("");
        officerContactRelationshipLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerContactRelationshipDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerContactRelationshipLabel.setForeground(AppWideStrings.primaryColor);
        officerContactRelationshipDataLabel.setForeground(AppWideStrings.primaryColor);

        officerEmergencyContactNumberLabel = new JLabel(
                AppWideStrings.officerEmergencyContactNumberString);
        officerEmergencyContactNumberDataLabel = new JLabel("");
        officerEmergencyContactNumberLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerEmergencyContactNumberDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerEmergencyContactNumberLabel.setForeground(AppWideStrings.primaryColor);
        officerEmergencyContactNumberDataLabel.setForeground(AppWideStrings.primaryColor);

        officerRankLabel = new JLabel(AppWideStrings.officerRankString);
        officerRankDataLabel = new JLabel("");
        officerRankLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerRankDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerRankLabel.setForeground(AppWideStrings.primaryColor);
        officerRankDataLabel.setForeground(AppWideStrings.primaryColor);

        officerCurrentPoliceDepartmentLabel = new JLabel(
                AppWideStrings.officerCurrentPoliceDepartmentString);
        officerCurrentPoliceDepartmentDataLabel = new JLabel("");
        officerCurrentPoliceDepartmentLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerCurrentPoliceDepartmentDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerCurrentPoliceDepartmentLabel.setForeground(AppWideStrings.primaryColor);
        officerCurrentPoliceDepartmentDataLabel.setForeground(AppWideStrings.primaryColor);

        officerDepartmentPhoneLabel = new JLabel(
                AppWideStrings.officerDepartmentPhoneDataString);
        officerDepartmentPhoneDataLabel = new JLabel("");
        officerDepartmentPhoneLabel.setFont(Roboto.REGULAR.deriveFont(10.0f));
        officerDepartmentPhoneDataLabel.setFont(Roboto.MEDIUM.deriveFont(14.0f));
        officerDepartmentPhoneLabel.setForeground(AppWideStrings.primaryColor);
        officerDepartmentPhoneDataLabel.setForeground(AppWideStrings.primaryColor);

        row2Panel.add(officerPhoneLabel, "1, 1");
        row2Panel.add(officerPhoneDataLabel, "3, 1");

        row2Panel.add(officerEmailLabel, "1, 3");
        row2Panel.add(officerEmailDataLabel, "3, 3");

        row2Panel.add(officerContactRelationshipLabel, "1, 5");
        row2Panel.add(officerContactRelationshipDataLabel, "3, 5");

        row2Panel.add(officerEmergencyContactNumberLabel, "1, 7");
        row2Panel.add(officerEmergencyContactNumberDataLabel, "3, 7");

        row2Panel.add(officerRankLabel, "1, 9");
        row2Panel.add(officerRankDataLabel, "3, 9");

        row2Panel.add(officerCurrentPoliceDepartmentLabel, "1, 11");
        row2Panel.add(officerCurrentPoliceDepartmentDataLabel, "3, 11");

        row2Panel.add(officerDepartmentPhoneLabel, "1, 13");
        row2Panel.add(officerDepartmentPhoneDataLabel, "3, 13");
    }

    public void setCurrentOfficerPicBufferedImage(BufferedImage pBufferedImage) {
        currentOfficerPicLabel.setIcon(new ImageIcon(pBufferedImage));
    }

    public void showOfficerProfileUiDataModel(JSONObject handledOfficer) {
        if (handledOfficer == null) {
            initProfile();
            return;
        }

        try {
            officerNameDataLabel.setText(handledOfficer.getString(KeyStrings.keyFirstName) + " " + handledOfficer.getString(KeyStrings.keyLastName));
            officerNameDataLabel.setToolTipText(officerNameDataLabel.getText());

            officerPhoneDataLabel.setText(handledOfficer.getString(KeyStrings.keyPhoneNumber));
            officerRankDataLabel.setText(handledOfficer.getString(KeyStrings.keyRank));
            officerBadgeNumberDataLabel.setText(handledOfficer.getString(KeyStrings.keyBadgeNumber));
            officerGenderDataLabel.setText(handledOfficer.getString(KeyStrings.keyGender));
            officerAgeDataLabel.setText("" + 32);
            officerRaceDataLabel.setText(handledOfficer.getString(KeyStrings.keyRace));

            officerHeightDataLabel.setText(handledOfficer.getString(KeyStrings.keyHeight));
            officerWeightDataLabel.setText(handledOfficer.getString(KeyStrings.keyWeight));
            officerEmailDataLabel.setText(handledOfficer.getString(KeyStrings.keyEmail));
            officerEmailDataLabel.setToolTipText(officerEmailDataLabel.getText());

            officerBloodTypeDataLabel.setText(handledOfficer.getString(KeyStrings.keyBloodType));
            officerAllergiesDataLabel.setText(handledOfficer.getString(KeyStrings.keyAllergy));
            officerAllergiesDataLabel.setToolTipText(officerAllergiesDataLabel.getText());

            officerEmergencyContactNumberDataLabel.setText(handledOfficer.getString(KeyStrings.keyContactPhone));
            officerContactRelationshipDataLabel.setText(handledOfficer.getString(KeyStrings.keyContactRelation));

            ArrayList<StationModel> mapStation = appWideCallsService.getStationList(0);
            for (int i = 0; i < mapStation.size(); i++) {
                StationModel model = mapStation.get(i);
                if (model.getId() == handledOfficer.getInt(KeyStrings.keyStationID)) {
                    officerCurrentPoliceDepartmentDataLabel.setText(model.getStationName());
                    officerCurrentPoliceDepartmentDataLabel.setToolTipText("<html>" +
                            officerCurrentPoliceDepartmentDataLabel.getText() + "<br>" +
                            model.getFullAddress() +
                            "</html>");
                    officerDepartmentPhoneDataLabel.setText(model.getPhoneNumber1());
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initProfile() {
        officerNameDataLabel.setText("");
        officerPhoneDataLabel.setText("");
        officerRankDataLabel.setText("");
        officerBadgeNumberDataLabel.setText("");
        officerGenderDataLabel.setText("");
        officerAgeDataLabel.setText("");
        officerRaceDataLabel.setText("");
        officerHeightDataLabel.setText("");
        officerWeightDataLabel.setText("");
        officerEmailDataLabel.setText("");

        officerBloodTypeDataLabel.setText("");
        officerAllergiesDataLabel.setText("");

        officerEmergencyContactNumberDataLabel.setText("");
        officerContactRelationshipDataLabel.setText("");

        officerCurrentPoliceDepartmentDataLabel.setText("");
    }
}
