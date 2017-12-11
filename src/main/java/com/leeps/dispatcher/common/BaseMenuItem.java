package com.leeps.dispatcher.common;

import de.craften.ui.swingmaterial.fonts.Roboto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BaseMenuItem extends JMenuItem{
    public BaseMenuItem (String title) {
        super(title);
        setBackground(AppWideStrings.primaryColor);
        setOpaque(true);
        setForeground(Color.WHITE);
        setFont(Roboto.BOLD.deriveFont(14.0f));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.width = Math.max(d.width, d.width); // set minimums
        d.height = Math.max(d.height, 40);
        return d;
    }
}
