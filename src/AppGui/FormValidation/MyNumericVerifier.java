package AppGui.FormValidation;

import AppGui.ComboItem;

import javax.swing.*;

/**
 * Created by angel on 3/17/17.
 */
public class MyNumericVerifier extends InputVerifier{

    private String errors = "";

    @Override
    public String toString() {
        return errors;
    }

    @Override
    public boolean verify(JComponent input) {
        String text = null;

        if (input instanceof JTextField) {
            text = ((JTextField) input).getText();
        } else if (input instanceof JComboBox) {
            text = ((ComboItem)((JComboBox) input).getSelectedItem()).getValue();
        }

        if (text.isEmpty())
        {
            errors = " is empty";
            return false;
        }

        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            errors = " should be integer";
            return false;
        }

        return true;
    }
}
