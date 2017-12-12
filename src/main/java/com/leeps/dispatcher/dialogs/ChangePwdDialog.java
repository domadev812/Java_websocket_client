package com.leeps.dispatcher.dialogs;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.service.*;
import de.craften.ui.swingmaterial.MaterialPanel;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePwdDialog extends BaseDialog implements ActionListener {
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;

    private JPanel contentPane;
    private MaterialPanel bodyPanel;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel errorMessage;
    private MaterialButton savePwdButton;

    public ChangePwdDialog(JFrame parentFrame, int width, int height, AppWideCallsService pAppWideCallsService) {
        super(parentFrame, width, height);
        setBackButtonVisible(true);
        setBottomVisible(false);
        setTitle(AppWideStrings.changePassword);
        setUndecorated(true);
        setResizable(false);
        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = new CustomizedUiWidgetsFactory();

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        MaterialPanel currentPasswordFieldPanel = new MaterialPanel();
        MaterialPanel newPasswordFieldPanel = new MaterialPanel();
        MaterialPanel confirmPasswordFieldPanel = new MaterialPanel();
        DropShadowBorder passFieldBorder = new DropShadowBorder(new Color(224, 224, 224),  2, 1.0f, 1, false, false, true, false);

        currentPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        currentPasswordFieldPanel.setLayout(new GridLayout(0, 1));
        currentPasswordFieldPanel.add(currentPasswordField);
        currentPasswordFieldPanel.setBackground(Color.WHITE);
        currentPasswordField.setBorder(new EmptyBorder(1, 20, 1, 20));
        currentPasswordField.setFont(Roboto.BOLD.deriveFont(16.0f));
        currentPasswordField.setForeground(Color.BLACK);
        currentPasswordField.setHorizontalAlignment(SwingConstants.CENTER);
        currentPasswordField.setColumns(10);
        currentPasswordField.setBorder(passFieldBorder);

        newPasswordFieldPanel.setLayout(new GridLayout(0, 1));
        newPasswordFieldPanel.add(currentPasswordField);
        newPasswordFieldPanel.setBackground(Color.WHITE);
        newPasswordField.setBorder(new EmptyBorder(1, 20, 1, 20));
        newPasswordField.setFont(Roboto.BOLD.deriveFont(16.0f));
        newPasswordField.setForeground(Color.BLACK);
        newPasswordField.setHorizontalAlignment(SwingConstants.CENTER);
        newPasswordField.setColumns(10);
        newPasswordField.setBorder(passFieldBorder);

        confirmPasswordFieldPanel.setLayout(new GridLayout(0, 1));
        confirmPasswordFieldPanel.add(currentPasswordField);
        confirmPasswordFieldPanel.setBackground(Color.WHITE);
        confirmPasswordField.setBorder(new EmptyBorder(1, 20, 1, 20));
        confirmPasswordField.setFont(Roboto.BOLD.deriveFont(16.0f));
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setHorizontalAlignment(SwingConstants.CENTER);
        confirmPasswordField.setColumns(10);
        confirmPasswordField.setBorder(passFieldBorder);

        PromptSupport.setPrompt("CURRENT PASSWORD", currentPasswordField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, currentPasswordField);
        PromptSupport.setFontStyle(Font.BOLD, currentPasswordField);

        PromptSupport.setPrompt("NEW PASSWORD", newPasswordField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, newPasswordField);
        PromptSupport.setFontStyle(Font.BOLD, newPasswordField);

        PromptSupport.setPrompt("CONFIRM PASSWORD", confirmPasswordField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, confirmPasswordField);
        PromptSupport.setFontStyle(Font.BOLD, confirmPasswordField);

        double[][] changePwdComponentLayoutSpec = new double[][] { {
                // Columns
                30, // 0 no info icon for this one
                TableLayout.FILL, // 3 TextField full name
                30
        }, {
                20,
                50,
                20,
                50,
                20,
                50,
                TableLayout.FILL,
                40,
                10
        } };
        bodyPanel = new MaterialPanel();
        bodyPanel.setLayout(new TableLayout(changePwdComponentLayoutSpec));
        bodyPanel.setBackground(Color.WHITE);

        bodyPanel.add(currentPasswordField, "1,1");
        bodyPanel.add(newPasswordField, "1,3");
        bodyPanel.add(confirmPasswordField, "1,5");

        double[][] changeButtonLayoutSpec = new double[][] { {
                // Columns
                70,
                TableLayout.FILL,
                70
        }, {
                TableLayout.FILL
        } };
        savePwdButton = new MaterialButton(AppWideStrings.buttonTextSaveString, new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
        savePwdButton.setFont(Roboto.BOLD.deriveFont(16.0f));
        savePwdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
        JPanel panButton = new JPanel(new TableLayout(changeButtonLayoutSpec));
        panButton.add(savePwdButton, "1, 0");
        panButton.setBackground(Color.WHITE);
        bodyPanel.add(panButton, "1,7");
        double[][] changePwdLayoutSpec = new double[][] { {
                // Columns
                50,
                TableLayout.FILL,
                50
        }, {
                10,
                TableLayout.FILL,
                10,
                TableLayout.PREFERRED,
                10
        } };

        contentPane = new JPanel(new TableLayout(changePwdLayoutSpec));
        contentPane.add(bodyPanel, "1, 1");

        errorMessage = new JLabel("");
        errorMessage.setFont(Roboto.BOLD.deriveFont(16.0f));
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setForeground(Color.RED);
        contentPane.add(errorMessage, "1, 3");

        getCenterPane().setLayout(new BorderLayout());
        getCenterPane().setBackground(AppWideStrings.panelBackgroundColor);
        getCenterPane().add(contentPane, BorderLayout.CENTER);
    }
    
    private void changePassword() {
        JSONObject jsonObject = appWideCallsService.getDispatcher();
        String currentPassword = String.valueOf(currentPasswordField.getPassword());
        String newPassword = String.valueOf(newPasswordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());

        try {
            if(currentPassword.equals("")) errorMessage.setText("Input Current Password");
            else if(newPassword.equals("")) errorMessage.setText("Input New Password");
            else if(confirmPassword.equals("")) errorMessage.setText("Input Confirmt Password");
            else if(!currentPassword.equals(jsonObject.getString(KeyStrings.keyUserPassword))) {
                errorMessage.setText("Current Password is Incorrrect.");
            } else if(!newPassword.equals(confirmPassword)) {
                errorMessage.setText("New Password and Conform Password must be same");
            } else {
                appWideCallsService.updatePassword(newPassword);
                JSONObject sendObject = new JSONObject();
                sendObject.put(KeyStrings.keyAction, KeyStrings.keyChangePassword);
                sendObject.put(KeyStrings.keyID, jsonObject.getString(KeyStrings.keyID));
                sendObject.put(KeyStrings.keyPassword, newPassword);
                appWideCallsService.sendToServer(sendObject);
                dispose();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
