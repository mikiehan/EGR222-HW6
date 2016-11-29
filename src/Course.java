import java.util.EnumSet;
import java.util.Set;

/**
 * Created by mhan on 10/15/2016.
 * Time object stores information about a particular university course
 */
public class Course {

    private String name;
    private int numCredits;
    private Set<Weekday> daysOffered;
    private Time startTime;
    private int durationInMinutes;

    /**
     * Constructor for Course class
     * @param name course name. Shouldn't be null or an empty string. otherwise, IllegalArgumentException is thrown
     * @param numCredits the number of credits. should be [1-5] otherwise, IllegalArgumentException is thrown
     * @param daysOffered Set of Weekday enum is passed. shouldn't be null or empty.
     *                    otherwise, IllegalArgumentException is thrown.
     *                    We can assume each Weekday element is in order. Each element is not null
     * @param startTime the start time of the course which is Time object. shouldn't be null
     *                  otherwise, IllegalArgumentException is thrown
     * @param durationInMinutes integer representing the duration of class in minutes
     *                          should be strictly greater than 0. otherwise, IllegalArgumentException is thrown
     *                          We can assume no class wraps from one day to the next
     */
    public Course(String name,
                  int numCredits,
                  Set<Weekday> daysOffered,
                  Time startTime,
                  int durationInMinutes){

        if( name == null || name.isEmpty() || !name.contains(" ")||
                numCredits < 1 || numCredits > 5 ||
                daysOffered == null || daysOffered.isEmpty() ||
                startTime == null ||
                durationInMinutes <= 0) throw new IllegalArgumentException();

        this.name = name.toUpperCase();
        this.numCredits = numCredits;
        this.daysOffered = EnumSet.copyOf(daysOffered);
        this.startTime = startTime.clone();
        this.durationInMinutes = durationInMinutes;
    }

    /**
     * This method returns non-inclusive end time of the course
     * @return Time object that refers to the end time (non-inclusive)
     */
    public Time getEndTime(){
        Time endTime = startTime.clone();
        endTime.shift(durationInMinutes);
        return endTime;
    }

    /**
     * This method returns the duration of the course in minutes
     * @return duration of the course in minutes
     */
    public int getDuration() {
        return durationInMinutes;
    }

    /**
     * This method returns the name of the course
     * @return the name of the course
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the number of credits of the course
     * @return the number of credits
     */
    public int getCredits(){
        return numCredits;
    }

    /**
     * This method returns the start time of the course (inclusive)
     * @return the start time of the course
     */
    public Time getStartTime(){
        //prevents from the caller to change the startTime accidentally
        return startTime.clone();
    }

    /**
     * This method returns set of Weekdays which the course is offered
     * @return set of Weekdays the course is offered
     */
    public Set<Weekday> getDaysOffered(){
        return EnumSet.copyOf(daysOffered);
    }

    /**
     * Determines whether other course has at least a minute overlap with this course
     * Runs O(duration) since hasTimeOverlap is O(duration) and hasWeekdayOverlap is O(1)
     * @param other the other course
     * @return true of this course is in session during any days and times that overlap with other course
     */
    public boolean conflictsWith(Course other){
        return hasWeekdayOverlap(other) && hasTimeOverlap(other);
    }

    /**
     * Determines whether other course has at least a minute overlap with this course not considering day
     * Runs O(duration) since shift is O(duration)
     * @param other the other course
     * @return true of this course is in session during any days and times that overlap with other course
     */
    private boolean hasTimeOverlap(Course other){
        Time endTime = getEndTime();

        Time otherStartTime = other.startTime;
        Time otherEndTime = other.getEndTime();

        return (endTime.compareTo(otherStartTime) > 0 && startTime.compareTo(otherEndTime) < 0) ||
                (startTime.compareTo(otherEndTime) < 0 && endTime.compareTo(otherStartTime) > 0);
    }

    /**
     * Determines whether other course has at least weekday overlap with this course not considering time
     * O(1) since daysOffered is length 5 for both this object and other object
     * @param other the other course
     * @return true of this course is is in session in any same day as the other course
     */
    private boolean hasWeekdayOverlap(Course other){
        for (Weekday day : daysOffered) {
            for (Weekday otherDay : other.daysOffered) {
                if (day == otherDay) {
                    return true; //return true, if it happens on at least one the same day
                }
            }
        }
        return false;
    }

    /**
     * Returns true if course is in session during any day and any time passed in as parameters
     * @param day
     * @param time
     * @return true if the course happens at day and time given. returns false otherwise
     */
    public boolean contains(Weekday day, Time time){
        if (!daysOffered.contains(day)) return false;
        Time endTime = getEndTime();
        return (startTime.compareTo(time) <= 0 && endTime.compareTo(time) > 0);
    }

    /**
     * Overrides equals from the Object superclass
     * @param obj the object that is being compared with
     * @return true of the obj should be considered the same as the current object. Return false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if( obj!= null && getClass() == obj.getClass()) {
            Course other = (Course) obj;
            return name.equals(other.name) &&
                    numCredits == other.numCredits &&
                    daysOffered.equals(other.daysOffered) &&
                    startTime.equals(other.startTime) &&
                    durationInMinutes == other.durationInMinutes;
        }
        else{
            return false;
        }
    }

    /**
     * Overrides hashCode from the Object superclass
     * Whenever overriding equals method we must also override hashCode method
     * @return an integer hash code for this object based on its state (hour, minute, isPM).
     */
    @Override
    public int hashCode() {
        //Algorithm for hashCode from Effective Java by Joshua Bloch
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + numCredits;
        result = 31 * result + durationInMinutes;
        result = 31 * result + startTime.hashCode();
        result = 31 * result + daysOffered.hashCode();
        return result;
    }

    /**
     * Overrides toString from the Object superclass
     * @return a string representation of this course
     *         For example, "EGR 222, 3, MWF, 05:00 PM, 60"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Weekday d : daysOffered){
            sb.append(d.toShortName());
        }
        return name +"," +  numCredits + "," + sb.toString() + "," + startTime.toString() + "," + durationInMinutes;
    }
}
