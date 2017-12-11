package com.leeps.dispatcher.common;

import info.monitorenter.util.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public interface AppWideStrings {
    //System Property
    public String appSettingsPropertiesFileNameString = "appSettings.properties";
    public String appTitle = "LEEPS Inc. Platform Beta";

    //App Icons
    public String appIcon1Loc = "../../../Leeps-Icon-1.png";
    public String appIcon2Loc = "../../../RedBlinkingIcon.png";

    //Connection Images
    public String connectedImage = "../../../connected.png";
    public String offlineImage = "../../../offline.png";

    //Color
    public Color primaryColor = new Color(0x33, 0x33, 0x33);
    public Color panelBackgroundColor = new Color(240, 240, 240);
    public Color whiteColor = new Color(255, 255, 255);
    public Color whiteWithTransparentColor = new Color(255, 255, 255, 0);
    public Color profileBackgroundColor = new Color(0x50, 0x50, 0x50);
    public Color lightGray = new Color(0xD3, 0xD3, 0xD3);
    //Font
    public String robotoFontBold = "../../../../Roboto-Bold.ttf";
    public String robotoFontRegular = "../../../../Roboto-Regular.ttf";

    // FamFamFamSilkIcons
    public String appFam3IconsLoc = "../../../../FamFamFamSilkIcons/icons/";
    public String icon_magic_wand = "wand.png";
    public String icon_magic_wand_HoverHelpText = "Click to auto log in";
    public String icon_generic_user_green = "user_green.png";

    public String icon_delete_row = "delete.png";
    public String icon_delete_row_HoverHelpText = "Delete row";

    public String icon_add_new_row = "add.png";
    public String icon_add_new_row_HoverHelpText = "Add new row";
    // Generic Officer Picture
    public String emptyOfficerProfileImg = "../../../GenericOfficerPic.png";

    // Menu Bar
    public String menuBarDispatcherString = "DISPATCHER";
    public String menuBarWindowString = "WINDOW";
    public String menuBarHelpString = "HELP";
    public String alarmPendingButtonString = "ALARMS PENDING: ";

    //Status String
    public String applicationStatusString = "APPLICATION STATUS:";
    public String applicationHandleOfficerString = "HANDLING OFFICER";
    public String applicationOfficerNeedAssistanceString = "OFFICERS NEEDS ASSISTANCE";
    public String applicationAwaitingOfficers = "AWAITING OFFICERS IN NEED OF ASSISTANCE";
    public String socketConnectionString = "CONNECTED";
    public String socketDisconnectionString = "DISCONNECTED";

    // DispatchCoverageFieldsPanel
    public String defaultComboString = "SELECT";
    public String stateString = "STATE";
    public String cityString = "CITY";
    public String stationString = "DEPT/STATION";
    public String streetString = "STREET ADDRESS";

    //Dispatcher Profile String
    public String dispatchOperatorInfoString = "DISPATCHER INFO";
    public String dispatchOperatorNameString = "NAME";
    public String dispatchOperatorEmailString = "EMAIL";
    public String dispatchOperatorIDString = "DISPATCHER ID";
    public String icon_MatchPair = "information.png";
    public String icon_MatchPair_HoverHelpText =
            "Field is used to pair/match a police officer to the right dispatch room";
    public String dispatchOperatorDispatchUnitLabelString = "DISPATCH UNIT";
    public String dispatchOperatorPoliceDepartmentsCoveredLabelString =
            "POLICE DEPARTMENT COVERED";
    public String dispatchOperatorFireDepartmentsCoveredLabelString =
            "FIRE DEPARTMENT COVERED";
    public String dispatchOperatorEMTCoveredLabelString =
            "EMERGENCY MEDICAL TECHNICIAN COVERED";

    public String dispatchOperatorThereAreDuplicateRows =
            "There are duplicate rows. Delete a duplicate row or make them not duplicate.";
    public String dispatchOperatorAllFieldsMustBeFilledIn =
            "All fields must be filled in to be able to Save.";
    public String buttonTextSaveString = "SAVE";
    public String buttonTextEditString = "EDIT";
    public String buttonTextDeactivateProfileString = "DEACTIVATE PROFILE";
    public String buttonTextActivateProfileString = "ACTIVATE PROFILE";


    //Change Password
    public String changePassword = "CHANGE PASSWORD";

    //Main Panel
    public String centerOfficerPanelCardLayoutKey =
            "centerOfficerPanel";

    // Officer Profile Panel
    public String officerNameString = "NAME";
    public String officerPhoneString = "PHONE";
    public String officerRankString = "RANK";
    public String officerBadgeNumberString = "BADGE";
    public String officerGenderString = "GENDER";
    public String officerAgeString = "AGE";
    public String officerRaceString = "RACE";
    public String officerHeightString = "HEIGHT";
    public String officerWeightString = "WEIGHT";
    public String officerEmailString = "EMAIL";
    public String officerBloodTypeString = "BLOOD";
    public String officerAllergiesString = "ALLERGIES";
    public String officerEmergencyContactNumberString = "PHONE";
    public String officerContactRelationshipString = "RELATIONSHIP";
    public String officerCurrentPoliceDepartmentString = "CURRENT DEPT";
    public String officerDepartmentPhoneDataString = "DEPT PHONE";

    // Officer Location Map
    public String mapGotoImage = "../../../../map-goto.png";
    public String mapLocationImage = "../../../../map-location.png";
    public String googleMapsRequestCrossStreetsUrlString =
            "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    public String crossStreet = "CROSS STREETS:";
    //Pending Dialog
    public String alarmsPendingDialogNoAlarmsString = "Currently no alarms";
    public String youAreOnlyAllowedToHandleTheFirstOfficerString =
            "You are only allowed to handle the first officer.";
    public String alarmsAreTakenInOrderString =
            "<html><strong>Important:</strong> Alarms are taken in order.</html>";
    public String buttonTextCloseString = "Close";
    public String handleOfficerButtonStirng = "Handle";
    public String elapsedTime = "ELAPSED TIME:";
    // Chart Panel

    public Range liveDataGraphOutsideTempAtRestAverageRange = new Range(65.0, 85.0);
    public Color liveDataGraphOutsideTempLineColor = new Color(165, 42, 42); // brown

    public String liveDataAllGraphLinesCardLayoutKey = "liveDataAllGraphLinesPanel";
    public String liveDataGraphHeartRateCardLayoutKey = "liveDataGraphHeartRatePanel";
    public String liveDataGraphMotionCardLayoutKey = "liveDataGraphMotionPanel";
    public String liveDataGraphPerspirationCardLayoutKey = "liveDataGraphPerspirationPanel";
    public String liveDataGraphSkinTempCardLayoutKey = "liveDataGraphSkinTempPanel";

    public String liveDataGraphBiometricsString = "BIOMETRICS";
    public String liveDataGraphTitleString = " GRAPH";
    public String liveDataMinutesString = "Minutes";

    public String liveDataAllGraphLinesString = "ALL GRAPH LINES";
    public String liveDataAllGraphLinesTitleString = "  All Graph Lines  ";
    public String liveDataGraphHeartRateString = "HEART RATE";
    public String liveDataGraphHeartRateTitleString = "  Heart Rate Graph  ";
    public String liveDataGraphHeartRateBpmString = " bpm)";
    public String liveDataGraphHeartRateBpmAxisString = "(bpm)";
    public String liveDataGraphHeartRateRangeString = "Range 0 to 240";
    public String liveDataGraphHeartRateHoverHelpString = "As measured from wrist sensor";
    public Range liveDataGraphHeartRateRange = new Range(0.0, 240.0);
    public Range liveDataGraphHeartRateAtRestAverageRange = new Range(66.0, 81.0);

    public String liveDataGraphMotionString = "MOTION";
    public String liveDataGraphMotionTitleString = "  Motion Graph  ";
    public String liveDataGraphMotionMs2String = " m / s\u00B2)";
    public String liveDataGraphMotionMs2AxisString = "(m / s\u00B2)";
    public String liveDataGraphMotionRangeString = "Range 0 to 3.7";
    public String liveDataGraphMotionHoverHelpString = "Meters per second squared";
    public Range liveDataGraphMotionRange = new Range(0.0, 3.7);
    public Range liveDataGraphMotionAtRestAverageRange = new Range(0.02, 0.06);

    public String liveDataGraphPerspirationString = "PERSPIRATION";
    public String liveDataGraphPerspirationTitleString = "  Perspiration Graph  ";
    public String liveDataGraphPerspirationcm2String = " cm\u00B2)";
    public String liveDataGraphPerspirationcm2AxisString = "(cm\u00B2)";
    public String liveDataGraphPerspirationRangeString = "Range 200 to 600";
    public String liveDataGraphPerspHoverHelpString = "Galvanic Skin Response Sensors";
    public Range liveDataGraphPerspirationRange = new Range(200.0, 600.0);
    public Range liveDataGraphPerspirationAtRestAverageRange = new Range(210, 260);

    public String liveDataGraphSkinOutsideTempString = "SKIN/OUTSIDE TEMP";
    public String liveDataGraphSkinTempString = "SKIN TEMP";
    public String liveDataGraphOutsideTempString = "OUTSIDE TEMP";
    public String liveDataGraphSkinTempTitleString = "  Skin/Outside Temperature Graph  ";
    public String liveDataGraphSkinTempFString = " F)";
    public String liveDataGraphSkinTempFAxisString = "(F)";
    public String liveDataGraphSkinTempRangeString = "Range -32 to 120";
    public String liveDataGraphSkinTempHoverHelpString = "As measured from wrist sensor";
    public Range liveDataGraphSkinTempRange = new Range(-32.0, 120.0);
    public Range liveDataGraphSkipTempAtRestAverageRange = new Range(91.0, 93.0);

    public String legalItemsTitleDisclaimerString = "  DISCLAIMER  ";
    public String legalItemsTitlePrivacyString = "  Privacy Policy  ";
    public String legalItemsTitleTermsString = "  Terms of Service  ";
    public String legalItemsTitleFAQString = "  FAQ  ";

    public String resourcesTopdownReadFileLoc =
            "./resources/";
    public String legalItemsDisclaimerTextFilename = "DisclaimerText.txt";
    public String legalItemsPrivacyTextFilename = "PrivacyPolicy.txt";
    public String legalItemsTermsTextFilename = "TermsOfService.txt";
    public String legalItemsFAQTextFilename = "FAQ.txt";

    public String centerPanelLegalItemsDisclaimerCardLayoutKey =
            "centerPanelLegalItemsDisclaimerCardLayoutKey";
    public String centerPanelLegalItemsPrivacyCardLayoutKey =
            "centerPanelLegalItemsPrivacyCardLayoutKey";
    public String centerPanelLegalItemsTermsCardLayoutKey =
            "centerPanelLegalItemsTermsCardLayoutKey";
    public String centerPanelLegalItemsFAQCardLayoutKey =
            "centerPanelLegalItemsFAQCardLayoutKey";
}
