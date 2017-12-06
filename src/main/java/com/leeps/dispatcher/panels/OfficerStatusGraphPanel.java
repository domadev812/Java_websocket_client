package com.leeps.dispatcher.panels;

import com.leeps.dispatcher.common.*;
import com.leeps.dispatcher.service.*;
import de.craften.ui.swingmaterial.fonts.Roboto;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterDate;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import layout.TableLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OfficerStatusGraphPanel extends JPanel {
    private static final long serialVersionUID = -1513706401003116887L;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private AppWideCallsService appWideCallsService;
    private CardLayout liveDataGraphBiometricsCardLayout;

    private JPanel currentOfficerBioLinksAndStatusGraphPanel;
    private JPanel biometricsClickLinksTopBottomPanel;
    private JPanel biometricsClickLinksPanel;
    private JPanel emptySpaceBelowBiometricClickLinksPanel;
    private JPanel liveDataGraphBiometricsPanel;
    private JPanel liveDataAllGraphLinesPanel;
    private JPanel liveDataGraphHeartRatePanel;
    private JPanel liveDataGraphMotionPanel;
    private JPanel liveDataGraphPerspirationPanel;
    private JPanel liveDataGraphSkinTempPanel;

    private JPanel allGraphHolderPanel;
    private JPanel heartRateGraphHolderPanel;
    private JPanel motionGraphHolderPanel;
    private JPanel perspirationGraphHolderPanel;
    private JPanel skinTempGraphHolderPanel;

    private Chart2D liveDataAllGraphLinesChart;
    private Chart2D liveDataGraphHeartRateChart;
    private Chart2D liveDataGraphMotionChart;
    private Chart2D liveDataGraphPerspirationChart;
    private Chart2D liveDataGraphSkinTempChart;

    private ITrace2D liveDataAllGraphLinesHeartRateTrace;
    private ITrace2D liveDataAllGraphLinesMotionTrace;
    private ITrace2D liveDataAllGraphLinesPerspirationTrace;
    private ITrace2D liveDataAllGraphLinesSkinTempTrace;
    private ITrace2D liveDataAllGraphLinesOutsideTempTrace;

    private ITrace2D liveDataGraphHeartRateTrace;
    private ITrace2D liveDataGraphMotionTrace;
    private ITrace2D liveDataGraphPerspirationTrace;
    private ITrace2D liveDataGraphSkinTempTrace;
    private ITrace2D liveDataGraphOutsideTempTrace;

    private JButton allGraphLinesClickLink;
    private JButton heartRateClickLink;
    private JButton motionClickLink;
    private JButton perspirationClickLink;
    private JButton skinTempClickLink;

    private JLabel lastHeartRateValueLabel;
    private JLabel lastMotionValueLabel;
    private JLabel lastPerspirationValueLabel;
    private JLabel lastSkinTempValueLabel;
    private JLabel lastOutsideTempValueLabel;

    private SimpleDateFormat xAxisTicksLabelsSimpleDateFormat = new SimpleDateFormat("hh:mm:ssa");
    private SimpleDateFormat xAxisTitleLabelSimpleDateFormat =
            new SimpleDateFormat("MMMMM dd, yyyy");

    public OfficerStatusGraphPanel(CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory,
                                   AppWideCallsService pAppWideCallsService) {

        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        appWideCallsService = pAppWideCallsService;

        setLayout(new GridLayout(0, 1));
        setBorder(BorderFactory.createEmptyBorder());

        currentOfficerBioLinksAndStatusGraphPanel = new JPanel(new BorderLayout());
        currentOfficerBioLinksAndStatusGraphPanel.setMinimumSize(new Dimension(80, 80));
        buildBiometricsClickLinksPanel();
        buildCurrentOfficerStatusGraphPanel();
        currentOfficerBioLinksAndStatusGraphPanel.add(biometricsClickLinksTopBottomPanel,
                BorderLayout.WEST);
        currentOfficerBioLinksAndStatusGraphPanel.add(liveDataGraphBiometricsPanel,
                BorderLayout.CENTER);
        currentOfficerBioLinksAndStatusGraphPanel.setBackground(AppWideStrings.panelBackgroundColor);
        currentOfficerBioLinksAndStatusGraphPanel.setOpaque(true);
        add(currentOfficerBioLinksAndStatusGraphPanel);
        setBackground(AppWideStrings.panelBackgroundColor);
        setOpaque(true);
    }

    private void buildBiometricsClickLinksPanel() {
        biometricsClickLinksPanel = new JPanel(new GridLayout(0, 1));
        biometricsClickLinksPanel.setBackground(AppWideStrings.panelBackgroundColor);
        biometricsClickLinksPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        int LEFT_INDENT_PX = 20;

        // All Graph Lines ----------------------------------------------------------------------
        allGraphLinesClickLink = customizedUiWidgetsFactory.makeClickableTextLink(
                AppWideStrings.liveDataAllGraphLinesString, Color.BLUE,
                AppWideStrings.panelBackgroundColor);
        allGraphLinesClickLink.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        allGraphLinesClickLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleAllGraphLinesLinkClicked();
            }
        });

        allGraphLinesClickLink.setBorder(BorderFactory.createEmptyBorder());
        JPanel allGraphLinesClickPanel = customizedUiWidgetsFactory.makeIndentedJPanel(
                0, allGraphLinesClickLink);
        biometricsClickLinksPanel.add(allGraphLinesClickPanel);

        // Heart Rate ---------------------------------------------------------------------------
        heartRateClickLink = customizedUiWidgetsFactory.makeClickableTextLink(
                AppWideStrings.liveDataGraphHeartRateString, Color.BLUE,
                AppWideStrings.panelBackgroundColor);
        heartRateClickLink.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        heartRateClickLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleHeartRateLinkClicked();
            }
        });

        heartRateClickLink.setBorder(BorderFactory.createEmptyBorder());
        heartRateClickLink.setToolTipText(AppWideStrings.liveDataGraphHeartRateHoverHelpString);
        JPanel heartRateClickPanel = customizedUiWidgetsFactory.makeIndentedJPanel(
                0, heartRateClickLink);
        heartRateClickPanel.add(new JLabel(" ("));
        lastHeartRateValueLabel = new JLabel("");
        lastHeartRateValueLabel.setForeground(Color.RED);
        heartRateClickPanel.add(lastHeartRateValueLabel);
        heartRateClickPanel.add(new JLabel(AppWideStrings.liveDataGraphHeartRateBpmString));
        biometricsClickLinksPanel.add(heartRateClickPanel);
        biometricsClickLinksPanel.add(customizedUiWidgetsFactory.makeIndentedJPanel(
                LEFT_INDENT_PX, new JLabel(AppWideStrings.liveDataGraphHeartRateRangeString)));

        // Motion --------------------------------------------------------------------------------
        motionClickLink = customizedUiWidgetsFactory.makeClickableTextLink(
                AppWideStrings.liveDataGraphMotionString, Color.BLUE,
                AppWideStrings.panelBackgroundColor);
        motionClickLink.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        motionClickLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleMotionLinkClicked();
            }
        });

        motionClickLink.setBorder(BorderFactory.createEmptyBorder());
        motionClickLink.setToolTipText(AppWideStrings.liveDataGraphMotionHoverHelpString);
        JPanel motionClickPanel = customizedUiWidgetsFactory.makeIndentedJPanel(
                0, motionClickLink);
        motionClickPanel.add(new JLabel(" ("));
        lastMotionValueLabel = new JLabel("");
        lastMotionValueLabel.setForeground(Color.RED);
        motionClickPanel.add(lastMotionValueLabel);
        motionClickPanel.add(new JLabel(AppWideStrings.liveDataGraphMotionMs2String));
        biometricsClickLinksPanel.add(motionClickPanel);
        biometricsClickLinksPanel.add(customizedUiWidgetsFactory.makeIndentedJPanel(
                LEFT_INDENT_PX, new JLabel(AppWideStrings.liveDataGraphMotionRangeString)));

        // Perspiration -------------------------------------------------------------------------
        perspirationClickLink = customizedUiWidgetsFactory.makeClickableTextLink(
                AppWideStrings.liveDataGraphPerspirationString, Color.BLUE,
                AppWideStrings.panelBackgroundColor);
        perspirationClickLink.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        perspirationClickLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handlePerspirationLinkClicked();
            }
        });

        perspirationClickLink.setBorder(BorderFactory.createEmptyBorder());
        perspirationClickLink.setToolTipText(AppWideStrings.liveDataGraphPerspHoverHelpString);
        JPanel perspirationClickPanel = customizedUiWidgetsFactory.makeIndentedJPanel(
                0, perspirationClickLink);
        perspirationClickPanel.add(new JLabel(" ("));
        lastPerspirationValueLabel = new JLabel("");
        lastPerspirationValueLabel.setForeground(Color.RED);
        perspirationClickPanel.add(lastPerspirationValueLabel);
        perspirationClickPanel.add(new JLabel(AppWideStrings.liveDataGraphPerspirationcm2String));
        biometricsClickLinksPanel.add(perspirationClickPanel);
        biometricsClickLinksPanel.add(customizedUiWidgetsFactory.makeIndentedJPanel(
                LEFT_INDENT_PX, new JLabel(AppWideStrings.liveDataGraphPerspirationRangeString)));

        // Skin Temp ----------------------------------------------------------------------------
        skinTempClickLink = customizedUiWidgetsFactory.makeClickableTextLink(
                AppWideStrings.liveDataGraphSkinOutsideTempString, Color.BLUE,
                AppWideStrings.panelBackgroundColor);
        skinTempClickLink.setFont(Roboto.MEDIUM.deriveFont(12.0f));
        skinTempClickLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent pE) {
                handleSkinTempLinkClicked();
            }
        });

        skinTempClickLink.setBorder(BorderFactory.createEmptyBorder());
        skinTempClickLink.setToolTipText(AppWideStrings.liveDataGraphSkinTempHoverHelpString);
        biometricsClickLinksPanel.add(customizedUiWidgetsFactory.makeIndentedJPanel(
                0, skinTempClickLink));
        JPanel skinOutsideTempPanel = customizedUiWidgetsFactory.makeIndentedJPanel(
                LEFT_INDENT_PX, new JLabel(" ("));
        lastSkinTempValueLabel = new JLabel("");
        lastSkinTempValueLabel.setForeground(Color.RED);
        skinOutsideTempPanel.add(lastSkinTempValueLabel);
        skinOutsideTempPanel.add(new JLabel(" / "));
        lastOutsideTempValueLabel = new JLabel("");
        lastOutsideTempValueLabel.setForeground(Color.RED);
        skinOutsideTempPanel.add(lastOutsideTempValueLabel);
        skinOutsideTempPanel.add(new JLabel(AppWideStrings.liveDataGraphSkinTempFString));
        biometricsClickLinksPanel.add(skinOutsideTempPanel);
        biometricsClickLinksPanel.add(customizedUiWidgetsFactory.makeIndentedJPanel(
                LEFT_INDENT_PX, new JLabel(AppWideStrings.liveDataGraphSkinTempRangeString)));

        double[][] biometricsLayoutSpec = new double[][] { {
                TableLayout.PREFERRED
        }, {
                30,
                TableLayout.PREFERRED,
                0.07,
                0.83,
                0.10
        } };

        biometricsClickLinksTopBottomPanel = new JPanel(new TableLayout(biometricsLayoutSpec));
        biometricsClickLinksTopBottomPanel.setBackground(AppWideStrings.panelBackgroundColor);
        biometricsClickLinksTopBottomPanel.setOpaque(true);

        //biometricsClickLinksTopBottomPanel.setPreferredSize(new Dimension(250, 20));
        biometricsClickLinksTopBottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(19, 15,
                        7, 10),
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));

        JPanel biometricLabelPanel = customizedUiWidgetsFactory.applyFontSizeAlignBgColorToLabel(
                16.0f, new JLabel(AppWideStrings.liveDataGraphBiometricsString), SwingConstants.CENTER,
                AppWideStrings.panelBackgroundColor);
        biometricLabelPanel.setBackground(AppWideStrings.panelBackgroundColor);
        biometricsClickLinksTopBottomPanel.add(biometricLabelPanel, "0,1");
        biometricsClickLinksTopBottomPanel.add(customizedUiWidgetsFactory.makeIndentedJPanel(
                0, null), "0,2");
        biometricsClickLinksTopBottomPanel.add(biometricsClickLinksPanel, "0,3");
        biometricsClickLinksTopBottomPanel.add(customizedUiWidgetsFactory.makeIndentedJPanel(
                0, null), "0,4");
    }

    private void buildCurrentOfficerStatusGraphPanel() {
        liveDataGraphBiometricsCardLayout = new CardLayout(0, 0);
        liveDataGraphBiometricsPanel = new JPanel(liveDataGraphBiometricsCardLayout);
        liveDataGraphBiometricsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                7, 7));
        liveDataGraphBiometricsPanel.setBackground(AppWideStrings.panelBackgroundColor);
        liveDataGraphBiometricsPanel.setOpaque(true);

        liveDataAllGraphLinesPanel = new JPanel(new BorderLayout());
        liveDataGraphHeartRatePanel = new JPanel(new BorderLayout());
        liveDataGraphMotionPanel = new JPanel(new BorderLayout());
        liveDataGraphPerspirationPanel = new JPanel(new BorderLayout());
        liveDataGraphSkinTempPanel = new JPanel(new BorderLayout());


        double graphTitleLayout[][] = new double[][] { {
                TableLayout.FILL
        }, {
                10,
                TableLayout.PREFERRED,
                5
        } };
        JPanel allGraphPanelTitle = new JPanel(new TableLayout(graphTitleLayout));
        allGraphPanelTitle.setOpaque(false);
        JLabel allGraphTitle = new JLabel(AppWideStrings.liveDataAllGraphLinesTitleString.toUpperCase());
        allGraphTitle.setFont(Roboto.BOLD.deriveFont(16.0f));
        allGraphTitle.setForeground(AppWideStrings.primaryColor);
        allGraphPanelTitle.add(allGraphTitle, "0, 1");
        allGraphTitle.setHorizontalAlignment(SwingConstants.CENTER);

        liveDataAllGraphLinesPanel.add(allGraphPanelTitle,
                BorderLayout.NORTH);
        liveDataAllGraphLinesPanel.setBackground(AppWideStrings.panelBackgroundColor);
        liveDataAllGraphLinesPanel.setOpaque(true);

        JPanel heartGraphPanelTitle = new JPanel(new TableLayout(graphTitleLayout));
        heartGraphPanelTitle.setOpaque(false);
        JLabel heartGraphTitle = new JLabel(AppWideStrings.liveDataGraphHeartRateTitleString.toUpperCase());
        heartGraphTitle.setFont(Roboto.BOLD.deriveFont(16.0f));
        heartGraphTitle.setForeground(AppWideStrings.primaryColor);
        heartGraphPanelTitle.add(heartGraphTitle, "0, 1");
        heartGraphTitle.setHorizontalAlignment(SwingConstants.CENTER);

        liveDataGraphHeartRatePanel.add(heartGraphPanelTitle,
                BorderLayout.NORTH);
        liveDataGraphHeartRatePanel.setBackground(AppWideStrings.panelBackgroundColor);
        liveDataGraphHeartRatePanel.setOpaque(true);

        JPanel motionGraphPanelTitle = new JPanel(new TableLayout(graphTitleLayout));
        motionGraphPanelTitle.setOpaque(false);
        JLabel motionGraphTitle = new JLabel(AppWideStrings.liveDataGraphMotionTitleString.toUpperCase());
        motionGraphTitle.setFont(Roboto.BOLD.deriveFont(16.0f));
        motionGraphTitle.setForeground(AppWideStrings.primaryColor);
        motionGraphPanelTitle.add(motionGraphTitle, "0, 1");
        motionGraphTitle.setHorizontalAlignment(SwingConstants.CENTER);

        liveDataGraphMotionPanel.add(motionGraphPanelTitle,
                BorderLayout.NORTH);
        liveDataGraphMotionPanel.setBackground(AppWideStrings.panelBackgroundColor);
        liveDataGraphMotionPanel.setOpaque(true);

        JPanel perspirationGraphPanelTitle = new JPanel(new TableLayout(graphTitleLayout));
        perspirationGraphPanelTitle.setOpaque(false);
        JLabel perspirationGraphTitle = new JLabel(AppWideStrings.liveDataGraphPerspirationTitleString.toUpperCase());
        perspirationGraphTitle.setFont(Roboto.BOLD.deriveFont(16.0f));
        perspirationGraphTitle.setForeground(AppWideStrings.primaryColor);
        perspirationGraphPanelTitle.add(perspirationGraphTitle, "0, 1");
        perspirationGraphTitle.setHorizontalAlignment(SwingConstants.CENTER);

        liveDataGraphPerspirationPanel.add(perspirationGraphPanelTitle,
                BorderLayout.NORTH);
        liveDataGraphPerspirationPanel.setBackground(AppWideStrings.panelBackgroundColor);
        liveDataGraphPerspirationPanel.setOpaque(true);

        JPanel tempGraphPanelTitle = new JPanel(new TableLayout(graphTitleLayout));
        tempGraphPanelTitle.setOpaque(false);
        JLabel tempGraphTitle = new JLabel(AppWideStrings.liveDataGraphSkinTempTitleString.toUpperCase());
        tempGraphTitle.setFont(Roboto.BOLD.deriveFont(16.0f));
        tempGraphTitle.setForeground(AppWideStrings.primaryColor);
        tempGraphPanelTitle.add(tempGraphTitle, "0, 1");
        tempGraphTitle.setHorizontalAlignment(SwingConstants.CENTER);

        liveDataGraphSkinTempPanel.add(tempGraphPanelTitle,
                BorderLayout.NORTH);
        liveDataGraphSkinTempPanel.setBackground(AppWideStrings.panelBackgroundColor);
        liveDataGraphSkinTempPanel.setOpaque(true);

        initCharts();

        liveDataAllGraphLinesPanel.add(allGraphHolderPanel, BorderLayout.CENTER);
        liveDataGraphHeartRatePanel.add(heartRateGraphHolderPanel, BorderLayout.CENTER);
        liveDataGraphMotionPanel.add(motionGraphHolderPanel, BorderLayout.CENTER);
        liveDataGraphPerspirationPanel.add(perspirationGraphHolderPanel, BorderLayout.CENTER);
        liveDataGraphSkinTempPanel.add(skinTempGraphHolderPanel, BorderLayout.CENTER);

        liveDataGraphBiometricsPanel.add(liveDataAllGraphLinesPanel,
                AppWideStrings.liveDataAllGraphLinesCardLayoutKey);

        liveDataGraphBiometricsPanel.add(liveDataGraphHeartRatePanel,
                AppWideStrings.liveDataGraphHeartRateCardLayoutKey);

        liveDataGraphBiometricsPanel.add(liveDataGraphMotionPanel,
                AppWideStrings.liveDataGraphMotionCardLayoutKey);

        liveDataGraphBiometricsPanel.add(liveDataGraphPerspirationPanel,
                AppWideStrings.liveDataGraphPerspirationCardLayoutKey);

        liveDataGraphBiometricsPanel.add(liveDataGraphSkinTempPanel,
                AppWideStrings.liveDataGraphSkinTempCardLayoutKey);
    }


    public void initOfficerGraph(boolean flag){
        allGraphHolderPanel.setVisible(flag);
        heartRateGraphHolderPanel.setVisible(flag);
        motionGraphHolderPanel.setVisible(flag);
        perspirationGraphHolderPanel.setVisible(flag);
        skinTempGraphHolderPanel.setVisible(flag);
    }
    private void handleBiometricsClickLinkClicked(String pliveDataGraphCardLayoutKey) {
        liveDataGraphBiometricsCardLayout.show(
                liveDataGraphBiometricsPanel, pliveDataGraphCardLayoutKey);
    }

    private void handleAllGraphLinesLinkClicked() {
        handleBiometricsClickLinkClicked(AppWideStrings.liveDataAllGraphLinesCardLayoutKey);
    }

    private void handleHeartRateLinkClicked() {
        handleBiometricsClickLinkClicked(AppWideStrings.liveDataGraphHeartRateCardLayoutKey);
    }

    private void handleMotionLinkClicked() {
        handleBiometricsClickLinkClicked(AppWideStrings.liveDataGraphMotionCardLayoutKey);
    }

    private void handlePerspirationLinkClicked() {
        handleBiometricsClickLinkClicked(AppWideStrings.liveDataGraphPerspirationCardLayoutKey);
    }

    private void handleSkinTempLinkClicked() {
        handleBiometricsClickLinkClicked(AppWideStrings.liveDataGraphSkinTempCardLayoutKey);
    }

    public void removeAllGraph(){
        liveDataAllGraphLinesHeartRateTrace.removeAllPoints();
        liveDataGraphHeartRateTrace.removeAllPoints();
        liveDataGraphMotionTrace.removeAllPoints();
        liveDataGraphPerspirationTrace.removeAllPoints();
        liveDataGraphSkinTempTrace.removeAllPoints();
    }

    private void initCharts() {
        liveDataAllGraphLinesChart = new Chart2D();
        liveDataGraphHeartRateChart = new Chart2D();
        liveDataGraphMotionChart = new Chart2D();
        liveDataGraphPerspirationChart = new Chart2D();
        liveDataGraphSkinTempChart = new Chart2D();

        allGraphHolderPanel = new JPanel(new GridLayout(0, 1));
        heartRateGraphHolderPanel = new JPanel(new GridLayout(0, 1));
        motionGraphHolderPanel = new JPanel(new GridLayout(0, 1));
        perspirationGraphHolderPanel = new JPanel(new GridLayout(0, 1));
        skinTempGraphHolderPanel = new JPanel(new GridLayout(0, 1));

        allGraphHolderPanel.setBackground(Color.WHITE);
        heartRateGraphHolderPanel.setBackground(Color.WHITE);
        motionGraphHolderPanel.setBackground(Color.WHITE);
        perspirationGraphHolderPanel.setBackground(Color.WHITE);
        skinTempGraphHolderPanel.setBackground(Color.WHITE);

        allGraphHolderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 3, 0)));

        heartRateGraphHolderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 3, 0)));

        motionGraphHolderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 3, 0)));

        perspirationGraphHolderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 3, 0)));

        skinTempGraphHolderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 3, 0)));

        allGraphHolderPanel.add(liveDataAllGraphLinesChart);
        heartRateGraphHolderPanel.add(liveDataGraphHeartRateChart);
        motionGraphHolderPanel.add(liveDataGraphMotionChart);
        perspirationGraphHolderPanel.add(liveDataGraphPerspirationChart);
        skinTempGraphHolderPanel.add(liveDataGraphSkinTempChart);


        // ----- ALL GRAPH LINES the Heart Rate Graph -----
        liveDataAllGraphLinesHeartRateTrace = new Trace2DLtd(60);
        liveDataAllGraphLinesHeartRateTrace.setName(AppWideStrings.liveDataGraphHeartRateString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataAllGraphLinesHeartRateTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphHeartRateBpmAxisString);

        // ----- ALL GRAPH LINES the Motion Graph -----
        liveDataAllGraphLinesMotionTrace = new Trace2DLtd(60);
        liveDataAllGraphLinesMotionTrace.setName(AppWideStrings.liveDataGraphMotionString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataAllGraphLinesMotionTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphMotionMs2AxisString);

        // ----- ALL GRAPH LINES the Perspiration Graph -----
        liveDataAllGraphLinesPerspirationTrace = new Trace2DLtd(60);
        liveDataAllGraphLinesPerspirationTrace
                .setName(AppWideStrings.liveDataGraphPerspirationString
                        + AppWideStrings.liveDataGraphTitleString);

        liveDataAllGraphLinesPerspirationTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphPerspirationcm2AxisString);

        // ----- ALL GRAPH LINES the Skin Temperature Graph -----
        liveDataAllGraphLinesSkinTempTrace = new Trace2DLtd(60);
        liveDataAllGraphLinesSkinTempTrace.setName(AppWideStrings.liveDataGraphSkinTempString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataAllGraphLinesSkinTempTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphSkinTempFAxisString);

        // ----- ALL GRAPH LINES the Outside Temperature Graph -----
        liveDataAllGraphLinesOutsideTempTrace = new Trace2DLtd(60);
        liveDataAllGraphLinesOutsideTempTrace.setName(AppWideStrings.liveDataGraphOutsideTempString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataAllGraphLinesOutsideTempTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphSkinTempFAxisString);

        // ----- Only the Heart Rate Graph -----
        liveDataGraphHeartRateTrace = new Trace2DLtd(60);
        liveDataGraphHeartRateTrace.setName(AppWideStrings.liveDataGraphHeartRateString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataGraphHeartRateTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphHeartRateBpmAxisString);

        // ----- Only the Motion Graph -----
        liveDataGraphMotionTrace = new Trace2DLtd(60);
        liveDataGraphMotionTrace.setName(AppWideStrings.liveDataGraphMotionString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataGraphMotionTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphMotionMs2AxisString);

        // ----- Only the Perspiration Graph -----
        liveDataGraphPerspirationTrace = new Trace2DLtd(60);
        liveDataGraphPerspirationTrace.setName(AppWideStrings.liveDataGraphPerspirationString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataGraphPerspirationTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphPerspirationcm2AxisString);

        // ----- Only the Skin Temperature Graph -----
        liveDataGraphSkinTempTrace = new Trace2DLtd(60);
        liveDataGraphSkinTempTrace.setName(AppWideStrings.liveDataGraphSkinTempString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataGraphSkinTempTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphSkinTempFAxisString);

        // ----- Only the Outside Temperature Graph -----
        liveDataGraphOutsideTempTrace = new Trace2DLtd(60);
        liveDataGraphOutsideTempTrace.setName(AppWideStrings.liveDataGraphOutsideTempString
                + AppWideStrings.liveDataGraphTitleString);

        liveDataGraphOutsideTempTrace.setPhysicalUnits(
                AppWideStrings.liveDataMinutesString,
                AppWideStrings.liveDataGraphSkinTempFAxisString);

        // ----- Specify color of graph lines -----
        liveDataAllGraphLinesHeartRateTrace.setColor(Color.RED);
        liveDataAllGraphLinesMotionTrace.setColor(Color.GREEN);
        liveDataAllGraphLinesPerspirationTrace.setColor(Color.BLUE);
        liveDataAllGraphLinesSkinTempTrace.setColor(Color.MAGENTA);
        liveDataAllGraphLinesOutsideTempTrace.setColor(
                AppWideStrings.liveDataGraphOutsideTempLineColor);

        liveDataGraphHeartRateTrace.setColor(Color.RED);
        liveDataGraphMotionTrace.setColor(Color.GREEN);
        liveDataGraphPerspirationTrace.setColor(Color.BLUE);
        liveDataGraphSkinTempTrace.setColor(Color.MAGENTA);
        liveDataGraphOutsideTempTrace.setColor(AppWideStrings.liveDataGraphOutsideTempLineColor);

        // ----- Add the traces to the charts. Do before adding points (deadlock prevention)
        liveDataGraphHeartRateChart.addTrace(liveDataGraphHeartRateTrace);
        liveDataGraphMotionChart.addTrace(liveDataGraphMotionTrace);
        liveDataGraphPerspirationChart.addTrace(liveDataGraphPerspirationTrace);
        liveDataGraphSkinTempChart.addTrace(liveDataGraphSkinTempTrace);
        liveDataGraphSkinTempChart.addTrace(liveDataGraphOutsideTempTrace);

        // ----- Low and High for Y Axis -----
        liveDataAllGraphLinesChart.getAxisY().setRangePolicy(
                new RangePolicyFixedViewport(AppWideStrings.liveDataGraphHeartRateRange));
        liveDataGraphHeartRateChart.getAxisY().setRangePolicy(
                new RangePolicyFixedViewport(AppWideStrings.liveDataGraphHeartRateRange));
        liveDataGraphMotionChart.getAxisY().setRangePolicy(
                new RangePolicyFixedViewport(AppWideStrings.liveDataGraphMotionRange));
        liveDataGraphPerspirationChart.getAxisY().setRangePolicy(
                new RangePolicyFixedViewport(AppWideStrings.liveDataGraphPerspirationRange));
        liveDataGraphSkinTempChart.getAxisY().setRangePolicy(
                new RangePolicyFixedViewport(AppWideStrings.liveDataGraphSkinTempRange));

        // ----- X Axis tick marks custom text -----
        liveDataAllGraphLinesChart.getAxisX().setFormatter(
                new LabelFormatterDate(xAxisTicksLabelsSimpleDateFormat));
        liveDataGraphHeartRateChart.getAxisX().setFormatter(
                new LabelFormatterDate(xAxisTicksLabelsSimpleDateFormat));
        liveDataGraphMotionChart.getAxisX().setFormatter(
                new LabelFormatterDate(xAxisTicksLabelsSimpleDateFormat));
        liveDataGraphPerspirationChart.getAxisX().setFormatter(
                new LabelFormatterDate(xAxisTicksLabelsSimpleDateFormat));
        liveDataGraphSkinTempChart.getAxisX().setFormatter(
                new LabelFormatterDate(xAxisTicksLabelsSimpleDateFormat));

        // ----- Y Axis Title -----
        liveDataAllGraphLinesChart.getAxisY().getAxisTitle().setTitle(
                AppWideStrings.liveDataGraphHeartRateBpmAxisString);
        liveDataGraphHeartRateChart.getAxisY().getAxisTitle().setTitle(
                AppWideStrings.liveDataGraphHeartRateBpmAxisString);
        liveDataGraphMotionChart.getAxisY().getAxisTitle().setTitle(
                AppWideStrings.liveDataGraphMotionMs2AxisString);
        liveDataGraphPerspirationChart.getAxisY().getAxisTitle().setTitle(
                AppWideStrings.liveDataGraphPerspirationcm2AxisString);
        liveDataGraphSkinTempChart.getAxisY().getAxisTitle().setTitle(
                AppWideStrings.liveDataGraphSkinTempFAxisString);

        // Add the Y Axis from other charts to the "All Chart"
        liveDataAllGraphLinesChart.removeAxisYLeft(liveDataAllGraphLinesChart.getAxisY());
        AAxis<?> heartRateYAxis = (AAxis<?>) liveDataGraphHeartRateChart.getAxisY();
        liveDataAllGraphLinesChart.addAxisYLeft(heartRateYAxis);

        AAxis<?> motionYAxis = (AAxis<?>) liveDataGraphMotionChart.getAxisY();
        liveDataAllGraphLinesChart.addAxisYLeft(motionYAxis);

        AAxis<?> perspirationYAxis = (AAxis<?>) liveDataGraphPerspirationChart.getAxisY();
        liveDataAllGraphLinesChart.addAxisYLeft(perspirationYAxis);

        AAxis<?> skinTempYAxis = (AAxis<?>) liveDataGraphSkinTempChart.getAxisY();
        liveDataAllGraphLinesChart.addAxisYLeft(skinTempYAxis);

        // ----- Add the traces to the charts. Do before adding points (deadlock prevention)
        liveDataAllGraphLinesChart.addTrace(liveDataAllGraphLinesHeartRateTrace,
                liveDataAllGraphLinesChart.getAxisX(), heartRateYAxis);

        liveDataAllGraphLinesChart.addTrace(liveDataAllGraphLinesMotionTrace,
                liveDataAllGraphLinesChart.getAxisX(), motionYAxis);

        liveDataAllGraphLinesChart.addTrace(liveDataAllGraphLinesPerspirationTrace,
                liveDataAllGraphLinesChart.getAxisX(), perspirationYAxis);

        liveDataAllGraphLinesChart.addTrace(liveDataAllGraphLinesSkinTempTrace,
                liveDataAllGraphLinesChart.getAxisX(), skinTempYAxis);

        liveDataAllGraphLinesChart.addTrace(liveDataAllGraphLinesOutsideTempTrace,
                liveDataAllGraphLinesChart.getAxisX(), skinTempYAxis);
    }

    public void addGraphPointHeartRate(long pDateMillis, double pYvalue) {
        liveDataAllGraphLinesHeartRateTrace.addPoint(pDateMillis, pYvalue);
        liveDataAllGraphLinesChart.getAxisX().getAxisTitle().setTitle(
                xAxisTitleLabelSimpleDateFormat.format(new Date(pDateMillis)));
        liveDataGraphHeartRateTrace.addPoint((double) pDateMillis, pYvalue);
        liveDataGraphHeartRateChart.getAxisX().getAxisTitle().setTitle(
                xAxisTitleLabelSimpleDateFormat.format(new Date(pDateMillis)));

        lastHeartRateValueLabel.setText("" + Math.round(pYvalue));
    }

    public void addGraphPointMotion(long pDateMillis, double pYvalue) {
        liveDataAllGraphLinesMotionTrace.addPoint(pDateMillis, pYvalue);
        liveDataGraphMotionTrace.addPoint(pDateMillis, pYvalue);
        liveDataGraphMotionChart.getAxisX().getAxisTitle().setTitle(
                xAxisTitleLabelSimpleDateFormat.format(new Date(pDateMillis)));
        lastMotionValueLabel.setText("" + (Math.round(pYvalue * 100) / 100.0));
    }

    public void addGraphPointPerspiration(long pDateMillis, double pYvalue) {
        liveDataAllGraphLinesPerspirationTrace.addPoint(pDateMillis, pYvalue);
        liveDataGraphPerspirationTrace.addPoint(pDateMillis, pYvalue);
        liveDataGraphPerspirationChart.getAxisX().getAxisTitle().setTitle(
                xAxisTitleLabelSimpleDateFormat.format(new Date(pDateMillis)));
        lastPerspirationValueLabel.setText("" + Math.round(pYvalue));
    }

    public void addGraphPointSkinTemp(long pDateMillis, double pYvalue) {
        liveDataAllGraphLinesSkinTempTrace.addPoint(pDateMillis, pYvalue);
        liveDataGraphSkinTempTrace.addPoint(pDateMillis, pYvalue);
        liveDataGraphSkinTempChart.getAxisX().getAxisTitle().setTitle(
                xAxisTitleLabelSimpleDateFormat.format(new Date(pDateMillis)));
        lastSkinTempValueLabel.setText("" + Math.round(pYvalue));
    }

    public void addGraphPointOutsideTemp(long pDateMillis, double pYvalue) {
        liveDataAllGraphLinesOutsideTempTrace.addPoint(pDateMillis, pYvalue);
        liveDataGraphOutsideTempTrace.addPoint(pDateMillis, pYvalue);
        lastOutsideTempValueLabel.setText("" + Math.round(pYvalue));
    }
}

