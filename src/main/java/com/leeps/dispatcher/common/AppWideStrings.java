package com.leeps.dispatcher.common;

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
    public String socketOfflineString = "OFFLINE";

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
    // Google Maps height above earth is a number 1 to 21
    public int googleMapsMinHeightNumber = 1;
    public int googleMapsMaxHeightNumber = 21;
    public int googleMapsDefaultHeightNumber = 16;
    public int googleMapsDefaultCityHeightNumber = 14;
    public int googleMapsReadBytesNeeded = 200 * 1024; // 200 k bytes
    public String googleMapsZoomLabelString = "Zoom(1 to 21) = ";

    // Google Maps amount to pan up/left/down/right
    public double googleMapsAmountToArrowPan = 0.0001;
    public double googleMapsAmountToControlArrowPan = 0.001;

    // Google Maps Request Map Size
    public int googleMapsRequestMapSizeWidth = 610;
    public int googleMapsRequestMapSizeHeight = 330;

    public String mapAddressOfficerLocationString = "Officer Location";
    public String mapAddressMapAboveAddressButtonString = "Map this Address";
    public String mapAddressMapAboveAddressHoverString = "Show the map of this Address";
    public String mapAddressCenterOnOfficerButtonString = "Center On Officer";
    public String mapAddressCenterOnOfficerHoverString = "Show where the officer is";
    public String mapAddressCrossStreetString = "Cross Street";
    public String mapAddress3rdAddressString = "3rd Address";
    public String mapAddressGeocodePointString = "GeoCode Point";
    public String mapAddressMapAboveGeocodeButtonString = "Map this GeoCode";
    public String mapAddressMapAboveGeocodeHoverString = "Show the map of this GeoCode point";
    public String mapAddressCenterOfMapIconUrlString =
            "http://maps.google.com/mapfiles/kml/pal3/icon52.png";

    public String icon_zoom_in = "zoom_in.png";
    public String icon_zoom_in_HoverHelpText = "Zoom the map IN";

    public String icon_zoom_out = "zoom_out.png";
    public String icon_zoom_out_HoverHelpText = "Zoom the map OUT";

    public String icon_arrow_up = "arrow_up.png";
    public String icon_arrow_up_HoverHelpText = "Move the map down (Pan UP)";

    public String icon_arrow_left = "arrow_left.png";
    public String icon_arrow_left_HoverHelpText = "Move the map right (Pan LEFT)";

    public String icon_arrow_down = "arrow_down.png";
    public String icon_arrow_down_HoverHelpText = "Move the map up (Pan DOWN)";

    public String icon_arrow_right = "arrow_right.png";
    public String icon_arrow_rightHoverHelpText = "Move the map left (Pan RIGHT)";

    public Point2D.Double[] gpsSampleXYPoints = new Point2D.Double[] {
            // 100 W 1st St, Los Angeles, CA 90012 (LAPD Headquarters)
            new Point2D.Double(34.052003, -118.244601),
            // 746 S Duncan Ave, East Los Angeles, CA 90022
            new Point2D.Double(34.0239244, -118.1696491),
            // 968 Marietta St, Los Angeles, CA 90023
            new Point2D.Double(34.0304348, -118.2120673),
            // 2005 De La Cruz Blvd, Santa Clara, CA 95050
            new Point2D.Double(37.361002, -121.940770),
            // 1610 7th St, Los Angeles, CA 90017 (possible rent office)
            new Point2D.Double(34.053962, -118.271661)
    };
    public String googleMapsRequestStaticMapUrlString =
            "https://maps.googleapis.com/maps/api/staticmap?center=";
    public String googleMapsRequestCrossStreetsUrlString =
            "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    public String googleMapsGetXyFromAddressUrlString =
            "https://maps.googleapis.com/maps/api/geocode/json?address=";
}
