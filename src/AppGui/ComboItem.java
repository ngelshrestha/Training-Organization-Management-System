package AppGui;

/**
 * Created by angel on 3/6/17.
 */
public class ComboItem {

    private String value;
    private int label;

    public ComboItem(String value, int label)
    {
        this.value = value;
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
