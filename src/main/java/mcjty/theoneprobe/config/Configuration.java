package mcjty.theoneprobe.config;

// @todo temporary to make porting easier
public class Configuration {

    public int getInt(String name, String category, int def, int minimum, int maximum, String description) {
        return def;
    }

    public float getFloat(String name, String category, float def, float minimum, float maximum, String description) {
        return def;
    }

    public boolean getBoolean(String name, String category, boolean def, String description) {
        return def;
    }

    public String getString(String name, String category, String def, String description) {
        return def;
    }

    public String[] getStringList(String name, String category, String[] def, String description) {
        return def;
    }
}
