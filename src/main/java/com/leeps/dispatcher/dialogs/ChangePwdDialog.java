package com.leeps.dispatcher.dialogs;

import com.leeps.dispatcher.common.AppWideStrings;
import com.leeps.dispatcher.material.MaterialButton;
import com.leeps.dispatcher.service.*;
import de.craften.ui.swingmaterial.MaterialPanel;
import de.craften.ui.swingmaterial.fonts.Roboto;
import layout.TableLayout;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChangePwdDialog extends BaseDialog implements ActionListener {
    private AppWideCallsService appWideCallsService;
    private CustomizedUiWidgetsFactory customizedUiWidgetsFactory;

    private JPanel contentPane;
    private MaterialPanel bodyPanel;
    private JPasswordField oldPassword;
    private JPasswordField newPassword;
    private JPasswordField confirmPassword;
    private JLabel errorMessage;
    private MaterialButton savePwdButton;

    public ChangePwdDialog(JFrame parentFrame, int width, int height, AppWideCallsService pAppWideCallsService) {
        super(parentFrame, width, height);
        setBackButtonVisible(true);
        setBottomVisible(false);
        setTitle(AppWideStrings.changePassword);
        setUndecorated(false);
        setResizable(false);
        appWideCallsService = pAppWideCallsService;
        customizedUiWidgetsFactory = new CustomizedUiWidgetsFactory();

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        MaterialPanel oldPasswordPanel = new MaterialPanel();
        MaterialPanel newPasswordPanel = new MaterialPanel();
        MaterialPanel confirmPasswordPanel = new MaterialPanel();
        DropShadowBorder passFieldBorder = new DropShadowBorder(new Color(224, 224, 224),  2, 1.0f, 1, false, false, true, false);

        oldPassword = new JPasswordField();
        newPassword = new JPasswordField();
        confirmPassword = new JPasswordField();

        oldPasswordPanel.setLayout(new GridLayout(0, 1));
        oldPasswordPanel.add(oldPassword);
        oldPasswordPanel.setBackground(Color.WHITE);
        oldPassword.setBorder(new EmptyBorder(1, 20, 1, 20));
        oldPassword.setFont(Roboto.BOLD.deriveFont(16.0f));
        oldPassword.setForeground(Color.BLACK);
        oldPassword.setHorizontalAlignment(SwingConstants.CENTER);
        oldPassword.setColumns(10);
        oldPassword.setBorder(passFieldBorder);

        newPasswordPanel.setLayout(new GridLayout(0, 1));
        newPasswordPanel.add(oldPassword);
        newPasswordPanel.setBackground(Color.WHITE);
        newPassword.setBorder(new EmptyBorder(1, 20, 1, 20));
        newPassword.setFont(Roboto.BOLD.deriveFont(16.0f));
        newPassword.setForeground(Color.BLACK);
        newPassword.setHorizontalAlignment(SwingConstants.CENTER);
        newPassword.setColumns(10);
        newPassword.setBorder(passFieldBorder);

        confirmPasswordPanel.setLayout(new GridLayout(0, 1));
        confirmPasswordPanel.add(oldPassword);
        confirmPasswordPanel.setBackground(Color.WHITE);
        confirmPassword.setBorder(new EmptyBorder(1, 20, 1, 20));
        confirmPassword.setFont(Roboto.BOLD.deriveFont(16.0f));
        confirmPassword.setForeground(Color.BLACK);
        confirmPassword.setHorizontalAlignment(SwingConstants.CENTER);
        confirmPassword.setColumns(10);
        confirmPassword.setBorder(passFieldBorder);

        PromptSupport.setPrompt("OLD PASSWORD", oldPassword);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, oldPassword);
        PromptSupport.setFontStyle(Font.BOLD, oldPassword);

        PromptSupport.setPrompt("NEW PASSWORD", newPassword);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, newPassword);
        PromptSupport.setFontStyle(Font.BOLD, newPassword);

        PromptSupport.setPrompt("CONFIRM PASSWORD", confirmPassword);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, confirmPassword);
        PromptSupport.setFontStyle(Font.BOLD, confirmPassword);

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

        bodyPanel.add(oldPassword, "1,1");
        bodyPanel.add(newPassword, "1,3");
        bodyPanel.add(confirmPassword, "1,5");

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

        errorMessage = new JLabel("Err");
        errorMessage.setFont(Roboto.BOLD.deriveFont(16.0f));
        errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessage.setForeground(Color.RED);
        contentPane.add(errorMessage, "1, 3");

        getCenterPane().setLayout(new BorderLayout());
        getCenterPane().setBackground(AppWideStrings.panelBackgroundColor);
        getCenterPane().add(contentPane, BorderLayout.CENTER);
    }
}
