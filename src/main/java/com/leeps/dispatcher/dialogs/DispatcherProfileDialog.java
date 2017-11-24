package com.leeps.dispatcher.dialogs;

import com.leeps.dispatcher.service.*;

import java.awt.event.ActionListener;

public class DispatcherProfileDialog extends BaseDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;
    public DispatcherProfileDialog(int width, int height, AppWideCallsService pAppWideCallsService) {
        super(width, height);
        setBackButtonVisible(false);
        setBottomHidden(false);
        setTitle("LOG IN");
        setUndecorated(true);
        initComponents();

        appWideCallsService = pAppWideCallsService;

        setModal(true);
    }

    private void initComponents() {

    }
}
