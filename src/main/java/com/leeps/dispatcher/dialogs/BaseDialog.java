package com.leeps.dispatcher.dialogs;

import javax.swing.*;

import com.leeps.dispatcher.common.*;
import com.leeps.dispatcher.material.MaterialButton;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BaseDialog extends JDialog implements ActionListener{
    private JPanel contentPane = new JPanel(new BorderLayout());
    private JPanel topPane = new JPanel();
    private JPanel centerPane = new JPanel();
    private JPanel bottomPane = new JPanel();
    private MaterialButton btnBack;
    private JLabel lblTitle = new JLabel();

    AppCommon common = new AppCommon();
    public BaseDialog() {
    }

    public BaseDialog(JFrame parentFrame, int width, int height)
    {
        super(parentFrame, "", true);
        setContentPane(contentPane);
        setBounds(common.getScreenSize().width / 2 - width / 2, common.getScreenSize().height / 2 - height / 2, width, height);
        initComponents();
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.add(topPane, BorderLayout.NORTH);
        contentPane.add(centerPane, BorderLayout.CENTER);
        contentPane.add(bottomPane, BorderLayout.SOUTH);

        btnBack.addActionListener(this);
        setModal(true);
    }

    private void initComponents() {
        topPane.setPreferredSize(new Dimension(this.getWidth(), 50));
        topPane.setLayout(null);
        topPane.setBackground(AppWideStrings.primaryColor);
        centerPane.setBackground(new Color(240, 240, 240));
        bottomPane.setPreferredSize(new Dimension(this.getWidth(), 63));
        bottomPane.setBackground(AppWideStrings.primaryColor);

        btnBack = new MaterialButton("BACK", new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
        btnBack.setBounds(21, 8, 80, 36);
        btnBack.setFont(common.getRobotoFont().deriveFont(14.0f));
        btnBack.setOpaque(true);

        lblTitle.setBounds(0, 0, topPane.getPreferredSize().width, topPane.getPreferredSize().height);
        lblTitle.setFont(common.getRobotoFont().deriveFont(20.0f));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        topPane.add(lblTitle);
        topPane.add(btnBack);
    }

    private void onCancel() {

    }

    public void onBackPressed() {
        dispose();
    }

    public JPanel getCenterPane() {
        return centerPane;
    }
    public JPanel getBottomPane() {
        return bottomPane;
    }

    public void setBottomVisible(boolean flag) {
        bottomPane.setVisible(flag);
    }
    public void setTitle(String title) {
        lblTitle.setText(title.toUpperCase());
    }
    public void setBackButtonVisible(boolean flag) {
        btnBack.setVisible(flag);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MaterialButton button = (MaterialButton)e.getSource();
        if(button.equals(btnBack)) {
            onBackPressed();
        }
    }
}

