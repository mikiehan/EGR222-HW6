import java.util.Comparator;

/**
 * Created by mhan on 11/8/2016.
 */
public class Time implements Cloneable , Comparable<Time> {
    private int hour;
    private int minute;
    private boolean isPM;

    /**
     * Constructor
     * @param hour
     * @param minute
     * @param isPM
     */
    public Time(int hour, int minute, boolean isPM){
        if( hour < 1 || hour > 12 )
            throw new IllegalArgumentException("hour should be between 1-12 inclusive");

        if( minute < 0 || minute > 59)
            throw new IllegalArgumentException("minute should be between 0-59 inclusive");

        this.hour = hour;
        this.minute = minute;
        this.isPM = isPM;
    }

    /**
     * checkFormat
     * @param tStr
     * @return
     */
    private static boolean checkFormat(String tStr){
        if(tStr.length() != 8)
            throw new IllegalArgumentException("The length of the given string must be 8");
        if(tStr.charAt(2) != ':')
            throw new IllegalArgumentException("The string should have a colon at index 2");
        if(tStr.charAt(5) != ' ')
            throw new IllegalArgumentException("The string should have a space at index 5");

        String amPmStr = tStr.substring(6);

        if(amPmStr.equals("PM")) return true;
        if(amPmStr.equals("AM")) return false;

        throw new IllegalArgumentException("The string should have AM or PM as the last two characters");
    }

    /**
     * fromString
     * @param tStr
     * @return
     */
    public static Time fromString(String tStr){
        try {
            boolean isPM = checkFormat(tStr);
            int h = Integer.parseInt(tStr.substring(0,2));
            int m = Integer.parseInt(tStr.substring(3,5));
            return new Time(h,m,isPM);
        }catch(NumberFormatException e){
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * clone method
     * @return a new Time object cloned from this object which is independent
     */
    @Override
    public Time clone() {
        try{
            return (Time) super.clone();
        }catch(CloneNotSupportedException e){
            return null;
        }
    }

    /**
     * equals
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj != null && getClass() == obj.getClass()) {
            Time other = (Time) obj;
            return hour == other.hour && minute == other.minute && isPM == other.isPM;
        } else {
            return false;
        }
    }

    /**
     * hashCode
     * @return
     */
    @Override
    public int hashCode() {
        return 65531 * new Boolean(isPM).hashCode()
                + 67 * hour + minute;
    }

    /**
     * toString
     * @return
     */
    @Override
    public String toString() {
        return String.format("%02d", hour)+":"+String.format("%02d", minute)+" "+ (isPM? "PM" : "AM");
    }

    /**
     * getHour
     * @return
     */
    public int getHour() {
        return hour;
    }

    /**
     * getMinute
     * @return
     */
    public int getMinute() {
        return minute;
    }

    /**
     * isPM
     * @return
     */
    public boolean isPM() {
        return isPM;
    }

    /**
     *
     * @param minutesToAdd
     */
    public void shift(int minutesToAdd){
        if(minutesToAdd < 0)
            throw new IllegalArgumentException("minutes cannot be negative");

        //1) update minute accordingly
        minute += minutesToAdd;
        int hoursToAdd = minute/60 % 24; //save hours to add
        minute %= 60; //adjust value that it has valid range for minute

        if(hoursToAdd == 0) return; //no need to update am/pm or hour

        //2) update hour accordingly
        if (hour == 12) hour = 0; //adjust hour to 0 if it is 12 for calculation
        hour += hoursToAdd;
        int numFlip = hour/12; //save num of flip for am/pm
        hour %= 12; //adjust value so that it has valid range for hour
        if (hour == 0) hour = 12; //adjust value to 12 since 0 hour doesn't exist

        //3) update am/pm accordingly
        if (numFlip%2 == 1) isPM = !isPM; //if the flipping happens odd times then we need to flip am/pm
    }

    /**
     * convert this object to minute integer
     *  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11 (AM)
     * 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 (PM)
     * @return minutes
     */
    private int toMinute() {
        int h = hour;
        if (h == 12) h = 0;
        if (isPM) h += 12;

        return h * 60 + minute;
    }

    @Override
    public int compareTo(Time other) {
        return toMinute() - other.toMinute();
    }
}
