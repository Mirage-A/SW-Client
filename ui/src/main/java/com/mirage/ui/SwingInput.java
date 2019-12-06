package com.mirage.ui;

import com.badlogic.gdx.Input;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class SwingInput {
    public static void getTextInput (final Input.TextInputListener listener, final String title, final String text, final String hint) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run () {
                JPanel panel = new JPanel(new FlowLayout());

                JPanel textPanel = new JPanel() {
                    public boolean isOptimizedDrawingEnabled () {
                        return false;
                    };
                };

                textPanel.setLayout(new OverlayLayout(textPanel));
                panel.add(textPanel);

                final JTextField textField = new JTextField(20);
                textField.setText(text);
                textField.setAlignmentX(0.0f);
                textPanel.add(textField);

                final JLabel placeholderLabel = new JLabel(hint);
                placeholderLabel.setForeground(Color.GRAY);
                placeholderLabel.setAlignmentX(0.0f);
                textPanel.add(placeholderLabel, 0);

                textField.getDocument().addDocumentListener(new DocumentListener() {

                    @Override
                    public void removeUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    @Override
                    public void insertUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    @Override
                    public void changedUpdate (DocumentEvent arg0) {
                        this.updated();
                    }

                    private void updated () {
                        if (textField.getText().length() == 0)
                            placeholderLabel.setVisible(true);
                        else
                            placeholderLabel.setVisible(false);
                    }
                });
                //JOptionPane.showInternalMessageDialog(null, "dsadsadadasd");
                //String ans = JOptionPane.showInternalInputDialog(null, "message");
                //System.out.println(ans);
                /*JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null,
                        null);

                pane.setInitialValue(null);
                pane.setComponentOrientation(JOptionPane.getRootFrame().getComponentOrientation());

                Border border = textField.getBorder();
                placeholderLabel.setBorder(new EmptyBorder(border.getBorderInsets(textField)));

                JDialog dialog = pane.createDialog(null, title);
                pane.selectInitialValue();

                dialog.addWindowFocusListener(new WindowFocusListener() {

                    @Override
                    public void windowLostFocus (WindowEvent arg0) {
                        System.out.println("lostfocus " + arg0.toString());
                        //dialog.requestFocus();
                        //pane.requestFocus();
                        //pane.grabFocus();
                    }

                    @Override
                    public void windowGainedFocus (WindowEvent arg0) {
                        System.out.println("gainedfocus " + arg0.toString());
                        arg0.getWindow().toFront();
                        //arg0.getWindow().setAlwaysOnTop(true);
                        textField.requestFocusInWindow();
                    }
                });

                System.out.println("1");

                //pane.transferFocus();
                //dialog.requestFocus();
                //pane.requestFocus();
                //pane.grabFocus();
                //dialog.requestFocusInWindow();

                System.out.println("1.25");
                dialog.setVisible(true);

                System.out.println("1.5");
                dialog.dispose();

                System.out.println("2");
                Object selectedValue = pane.getValue();

                System.out.println("3");
                if (selectedValue != null && (selectedValue instanceof Integer)
                        && ((Integer)selectedValue).intValue() == JOptionPane.OK_OPTION) {
                    listener.input(textField.getText());
                } else {
                    listener.canceled();
                }

                System.out.println("4");*/

            }
        });
    }
}
