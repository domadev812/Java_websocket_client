package com.leeps.dispatcher.panels;

import com.leeps.dispatcher.service.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class OfficerStatusGraphPanel extends JPanel{
    private static final long serialVersionUID = -1513706401003116887L;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private AppWideCallsService appWideCallsService;
    public OfficerStatusGraphPanel(CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory,
                                   AppWideCallsService pAppWideCallsService) {

        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        appWideCallsService = pAppWideCallsService;

        setLayout(new GridLayout(0, 1));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));

//        currentOfficerBioLinksAndStatusGraphPanel = new JPanel(new BorderLayout());
//        currentOfficerBioLinksAndStatusGraphPanel.setMinimumSize(new Dimension(80, 80));
//        buildBiometricsClickLinksPanel();
//        buildCurrentOfficerStatusGraphPanel();
//        currentOfficerBioLinksAndStatusGraphPanel.add(biometricsClickLinksTopBottomPanel,
//                BorderLayout.WEST);
//        currentOfficerBioLinksAndStatusGraphPanel.add(liveDataGraphBiometricsPanel,
//                BorderLayout.CENTER);
//        add(currentOfficerBioLinksAndStatusGraphPanel);
        setBackground(Color.WHITE);
    }
}
