/**
 * Copyright 2017 LEEPS.Inc
 */
package com.leeps.dispatcher.panels;


import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.service.*;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class LegalItemsTermsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    private JPanel viewDetailPanel;
    private JPanel titleLabelPanel;
    private JPanel centerDetailWordingPanel;
    private JTextArea centerDetailTextArea;
    // private SwingController aSwingController;
    // private SwingViewBuilder aSwingViewBuilder;
    // private DocumentViewController aDocumentViewController;

    public LegalItemsTermsPanel(AppWideCallsService pAppWideCallsService,
                                CustomizedUiWidgetsFactory pCustomizedUiWidgetsFactory) {

        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = pCustomizedUiWidgetsFactory;
        viewDetailPanel = new JPanel(new BorderLayout());
        viewDetailPanel.setBorder(BorderFactory.createEmptyBorder());

        titleLabelPanel = new JPanel(new BorderLayout());
        titleLabelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        JLabel title = new JLabel(AppWideStrings.legalItemsTitleTermsString);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(Roboto.BOLD.deriveFont(24.0f));
        titleLabelPanel.add(title, BorderLayout.CENTER);
        titleLabelPanel.setOpaque(false);

        centerDetailWordingPanel = new JPanel(new GridLayout(0, 1));

        centerDetailWordingPanel.setBorder(BorderFactory.createEmptyBorder(5, 7, 7, 7));
        centerDetailWordingPanel.setOpaque(false);

        centerDetailTextArea = customizedUiWidgetsFactory.makeWordWrapTextArea(20, 1, "", 12);
        centerDetailTextArea.setEditable(false);
        centerDetailTextArea.setFocusable(false);
        centerDetailTextArea.setFont(Roboto.MEDIUM.deriveFont(16.0f));
        centerDetailTextArea.setForeground(AppWideStrings.whiteColor);
        centerDetailTextArea.setBackground(AppWideStrings.profileBackgroundColor);
        centerDetailTextArea.setText(customizedUiWidgetsFactory.readTextFileIntoString(
             AppWideStrings.resourcesTopdownReadFileLoc
                 + AppWideStrings.legalItemsTermsTextFilename));

        JScrollPane centerDetailScrollPane = new JScrollPane(centerDetailTextArea);
        centerDetailScrollPane.setHorizontalScrollBarPolicy(
             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        centerDetailScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerDetailScrollPane.setBorder(BorderFactory.createEmptyBorder());

        centerDetailWordingPanel.add(centerDetailScrollPane);
        viewDetailPanel.add(titleLabelPanel, BorderLayout.NORTH);
        viewDetailPanel.add(centerDetailWordingPanel, BorderLayout.CENTER);

        viewDetailPanel.setOpaque(false);

        double[][] layoutSpec = new double[][] { {
                120,
                TableLayout.FILL,
                120
        }, {
                50,
                TableLayout.FILL,
                20
        } };

        setLayout(new TableLayout(layoutSpec));
        add(viewDetailPanel, "1,1");
        setBackground(AppWideStrings.profileBackgroundColor);
    }

    public void scrollToTop() {
        centerDetailTextArea.scrollRectToVisible(new Rectangle(0, 0, 10, 10));
        // aSwingController.showPage(0);
    }
}
