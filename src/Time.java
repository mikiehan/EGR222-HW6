import java.util.Comparator;

/**
 * This class defines the Time object, which is displayed in HH:MM AM/PM format
 * and which can be mutated to and from a String object. Time can also be
 * "shifted" by a given number of minutes, as well as compared to other
 * Time objects.
 *
 * @author Dr. Mikyung Han
 * @version EGR326 SP19 v2 with Javadoc
 */
public class Time implements Cloneable , Comparable<Time> {
    private int hour;
    private int minute;
    private boolean isPM;

    /**
     * Constructor for TIime
     * @param hour: int of the hour, between 1 and 12
     * @param minute: int of the minute, between 0 and 59
     * @param isPM: boolean for time of day, true = AM, false = PM
     * @throws IllegalArgumentException for invalid hour, minute, isPM values
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
     * checkFormat checks the format of the given time String and
     * returns true if it is PM otherwise return false
     * @param tStr
     * @return true if @code{tStr} is PM, false otherwise
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
     * fromString constructs Time object given @code{tStr}
     * @param tStr String representation of Time
     * @return Time instance represented by @code{tStr}
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
     * Clones the Time object
     * @return a new Time object cloned from this object which is independent (deep copy)
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
     * @param obj the instance to compare against this (current instance)
     * @return true if @code{obj} is equal to this(current instance)
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
        return 65531 * (isPM ? 0 : 1)
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
     * advances the time of current instance by @code{minutesToAdd} minutes
     * @param minutesToAdd number of minutes to advance the current time
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

    /**
     * Overrides compareTo method
     * @param other Time object that is being compared with this object
     * @return negative value when this object is smaller than other object,
     *         positive value when this object is greater than other object,
     *         0 when this object is equal to other object
     */
    @Override
    public int compareTo(Time other) {
        return toMinute() - other.toMinute();
    }
}
