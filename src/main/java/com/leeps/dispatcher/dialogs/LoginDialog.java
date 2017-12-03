package com.leeps.dispatcher.dialogs;

import com.leeps.dispatcher.common.KeyStrings;
import com.leeps.dispatcher.material.*;
import com.leeps.dispatcher.service.AppWideCallsService;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends BaseDialog implements ActionListener{
    JPanel contentPane = new JPanel();
    JPanel panBody = new JPanel();
    JTextField txtEmail = new JTextField("a1@gmail.com");
    JPasswordField txtPassword = new JPasswordField("test");
    JLabel lblForgotPassword = new JLabel("FORGOT PASSWORD");
    JLabel lblCreateAccount = new JLabel("CREATE ACCOUNT");
    JLabel lblErrorMessage = new JLabel("");

    MaterialButton btnLogin = new MaterialButton("LOG IN", new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));
    MaterialButton btnExit = new MaterialButton("EXIT", new Color(41, 117, 234), Color.WHITE, new Color(30, 110, 230));

    private AppWideCallsService appWideCallsService;
    public LoginDialog(JFrame parentFrame) {
        super(parentFrame, 640, 390);
        setBackButtonVisible(false);
        setBottomVisible(false);
        setTitle("Log In");
        setModal(true);
    }

    public LoginDialog(JFrame parentFrame, int width, int height, AppWideCallsService pAppWideCallsService) {
        super(parentFrame, width, height);
        setBackButtonVisible(false);
        setBottomVisible(false);
        setTitle("LOG IN");
        setUndecorated(true);
        initComponents();

        appWideCallsService = pAppWideCallsService;

        setModal(true);
    }

    private void initComponents() {
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.window);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] {0, 1};
        gbl_contentPane.rowHeights = new int[] {60, 60, 90, 20, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        panBody.setBackground(Color.WHITE);
        GridBagConstraints gbc_panBody = new GridBagConstraints();
        gbc_panBody.insets = new Insets(0, 130, 5, 130);
        gbc_panBody.gridheight = 3;
        gbc_panBody.fill = GridBagConstraints.BOTH;
        gbc_panBody.gridx = 0;
        gbc_panBody.gridy = 0;
        contentPane.add(panBody, gbc_panBody);
        GridBagLayout gbl_panBody = new GridBagLayout();
        gbl_panBody.columnWidths = new int[]{0, 0};
        gbl_panBody.rowHeights = new int[] {0, 0, 0, 0, 0, 4};
        gbl_panBody.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_panBody.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        panBody.setLayout(gbl_panBody);

        txtEmail.setToolTipText("");
        GridBagConstraints gbc_txtEmail = new GridBagConstraints();
        gbc_txtEmail.ipady = 30;
        gbc_txtEmail.insets = new Insets(20, 45, 5, 45);
        gbc_txtEmail.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtEmail.gridx = 0;
        gbc_txtEmail.gridy = 0;
        panBody.add(txtEmail, gbc_txtEmail);
        txtEmail.setColumns(10);
        txtEmail.setFont(common.getRobotoFont().deriveFont(18.0f));
        txtEmail.setForeground(new Color(130, 130, 130));
        PromptSupport.setPrompt("Email", txtEmail);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, txtEmail);
        PromptSupport.setFontStyle(Font.BOLD, txtEmail);

        GridBagConstraints gbc_txtPassword = new GridBagConstraints();
        gbc_txtPassword.ipady = 30;
        gbc_txtPassword.insets = new Insets(5, 45, 0, 45);
        gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtPassword.gridx = 0;
        gbc_txtPassword.gridy = 1;
        panBody.add(txtPassword, gbc_txtPassword);
        txtPassword.setColumns(10);
        txtPassword.setFont(common.getRobotoFont().deriveFont(18.0f));
        txtPassword.setForeground(new Color(130, 130, 130));
        PromptSupport.setPrompt("Password", txtPassword);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, txtPassword);
        PromptSupport.setFontStyle(Font.BOLD, txtPassword);

        JPanel panForgot = new JPanel();
        panForgot.setBackground(Color.WHITE);
        FlowLayout flowLayout = (FlowLayout) panForgot.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        GridBagConstraints gbc_pan_forgot= new GridBagConstraints();
        gbc_pan_forgot.anchor = GridBagConstraints.WEST;
        gbc_pan_forgot.insets = new Insets(20, 45, 5, 45);
        gbc_pan_forgot.gridx = 0;
        gbc_pan_forgot.gridy = 2;
        panBody.add(panForgot, gbc_pan_forgot);
        lblForgotPassword.setFont(common.getRobotoRegularFontUnderline(10.0f));
        lblForgotPassword.setForeground(Color.BLACK);
        panForgot.add(lblForgotPassword);

        JXPanel panButton = new JXPanel();
        panButton.setBackground(Color.WHITE);
        GridBagConstraints gbc_pane_button = new GridBagConstraints();
        gbc_pane_button.ipady = 15;
        gbc_pane_button.insets = new Insets(10, 45, 5, 45);
        gbc_pane_button.fill = GridBagConstraints.HORIZONTAL;
        gbc_pane_button.gridx = 0;
        gbc_pane_button.gridy = 3;
        panBody.add(panButton, gbc_pane_button);
        panButton.setLayout(new GridLayout(1, 2, 20, 0));

        btnLogin.setFont(common.getRobotoFont().deriveFont(20.0f));
        btnExit.setFont(common.getRobotoFont().deriveFont(20.0f));

        btnLogin.addActionListener(this);
        btnExit.addActionListener(this);
        panButton.add(btnLogin);
        panButton.add(btnExit);

        JPanel panError = new JPanel();
        panError.setBackground(Color.WHITE);
        GridBagConstraints gbc_pane_error = new GridBagConstraints();
        gbc_pane_error.ipady = 10;
        gbc_pane_error.fill = GridBagConstraints.HORIZONTAL;
        gbc_pane_error.gridx = 0;
        gbc_pane_error.gridy = 4;
        panBody.add(panError, gbc_pane_error);

        panError.add(lblErrorMessage);
        lblErrorMessage.setFont(common.getRobotoBoldFont(14.0f));
        lblErrorMessage.setForeground(Color.RED);

        JPanel panBottom = new JPanel();
        GridBagConstraints gbc_panBottom = new GridBagConstraints();
        gbc_panBottom.ipady = 50;
        gbc_panBottom.fill = GridBagConstraints.BOTH;
        gbc_panBottom.gridx = 0;
        gbc_panBottom.gridy = 3;
        contentPane.add(panBottom, gbc_panBottom);
        GridBagLayout gbl_panBottom = new GridBagLayout();
        gbl_panBottom.columnWidths = new int[] {1};
        gbl_panBottom.rowHeights = new int[] {1};
        gbl_panBottom.columnWeights = new double[]{0.0};
        gbl_panBottom.rowWeights = new double[]{0.0};
        panBottom.setLayout(gbl_panBottom);

        lblCreateAccount.setFont(common.getRobotoBoldFontUnderline(14.0f));
        lblCreateAccount.setForeground(new Color(41, 117, 234));

        GridBagConstraints gbc_lbl_create = new GridBagConstraints();
        gbc_lbl_create.gridx = 0;
        gbc_lbl_create.gridy = 0;
        panBottom.add(lblCreateAccount, gbc_lbl_create);
        panBottom.setBackground(SystemColor.window);

        DropShadowBorder border1 = new DropShadowBorder(Color.BLACK,  3, 0.2f, 1, true, true, true, true);
        panBody.setBorder(border1);

        DropShadowBorder border2 = new DropShadowBorder(new Color(224, 224, 224),  2, 1.0f, 1, false, false, true, false);
        txtEmail.setBorder(border2);
        txtPassword.setBorder(border2);
        getCenterPane().setLayout(new BorderLayout());
        getCenterPane().add(contentPane, BorderLayout.CENTER);

//        panError.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MaterialButton button = (MaterialButton)e.getSource();
        if(button.equals(btnLogin)) {
            login();
        } else if(button.equals(btnExit))
            System.exit(0);
    }

    private void login() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KeyStrings.keyAction, KeyStrings.actionLogin);
            jsonObject.put(KeyStrings.keyEmail, txtEmail.getText());
            jsonObject.put(KeyStrings.keyPassword, String.valueOf(txtPassword.getPassword()));
            appWideCallsService.sendToServer(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        dispose();
    }

    public void showErrorMessage(String errorMessage) {
        lblErrorMessage.setText(errorMessage);
    }
}
