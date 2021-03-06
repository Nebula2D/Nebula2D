/*
 * Nebula2D is a cross-platform, 2D game engine for PC, Mac, & Linux
 * Copyright (c) 2014 Jon Bonazza
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.suiton2d.editor.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Enumeration;

/**
 * custom toolbar implementation
 */
public class SuitonToolbar extends JToolBar {
    public static final int RENDERER_WIDGET_TRANSLATE = 0;
    public static final int RENDERER_WIDGET_SCALE = 1;
    public static final int RENDERER_WIDGET_ROTATE = 2;

    private int selectedRendererWidget;
    private ButtonGroup renderWidgets;

    public SuitonToolbar() {
        selectedRendererWidget = 0;
        addButtons();

        Border innerBorder = new MatteBorder(1, 0, 0, 0, new Color(80, 80, 80));
        Border outterBorder = new MatteBorder(1, 0, 0, 0, new Color(40, 40, 40));
        setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
        setRendererWidgetsEnabled(false);
    }

    public int getSelectedRendererWidget() {
        return selectedRendererWidget;
    }


    private void addButtons() {
        renderWidgets = forgeRendererWidgetButtons();

        Enumeration buttons = renderWidgets.getElements();

        while(buttons.hasMoreElements()) {
            add((JToggleButton)buttons.nextElement());
        }
    }

    private ButtonGroup forgeRendererWidgetButtons() {
        ButtonGroup group = new ButtonGroup();
        JToggleButton translateWidget = new JToggleButton("Translate");
        translateWidget.addItemListener(e -> selectedRendererWidget = RENDERER_WIDGET_TRANSLATE);
        translateWidget.setSelected(true);
        JToggleButton scaleWidget = new JToggleButton("Scale");
        scaleWidget.addItemListener(e -> selectedRendererWidget = RENDERER_WIDGET_SCALE);
        JToggleButton rotateWidget = new JToggleButton("Rotate");
        rotateWidget.addItemListener(e -> selectedRendererWidget = RENDERER_WIDGET_ROTATE);
        group.add(translateWidget);
        group.add(scaleWidget);
        group.add(rotateWidget);

        return group;
    }

    public void setRendererWidgetsEnabled(boolean enable) {
        Enumeration buttons = renderWidgets.getElements();
        while (buttons.hasMoreElements()) {
            JToggleButton button = (JToggleButton) buttons.nextElement();
            button.setEnabled(enable);
        }
    }
}

