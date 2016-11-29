/**
 * Created by mhan on 11/9/2016.
 */
public enum Weekday {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY;

    @Override
    public String toString() {
        String orgStr = super.toString();
        return orgStr.substring(0,1) + orgStr.substring(1).toLowerCase();
    }

    public String toShortName() {
        if(this  == THURSDAY) {
            return "R";
        }
        return super.toString().substring(0,1);
    }

    public static Weekday fromString(String str){
        if(str == null || str.length() < 1)
            throw new IllegalArgumentException();

        if(str.equalsIgnoreCase("Monday") || str.equalsIgnoreCase("M")){
            return MONDAY;
        } else if (str.equalsIgnoreCase("Tuesday") || str.equalsIgnoreCase("T")){
            return TUESDAY;
        } else if (str.equalsIgnoreCase("Wednesday") || str.equalsIgnoreCase("W")){
            return WEDNESDAY;
        } else if (str.equalsIgnoreCase("Thursday") || str.equalsIgnoreCase("R")){
            return THURSDAY;
        } else if (str.equalsIgnoreCase("Friday") || str.equalsIgnoreCase("F")){
            return FRIDAY;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
