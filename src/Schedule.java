import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mhan on 10/15/2016.
 * A Schedule object stores information about the collection of courses
 * in which a student is enrolled
 */
public class Schedule implements Cloneable{
    private List<Course> courses;

    /**
     * Constructor of Schedule
     * Initializes the courses field
     */
    public Schedule(){
        courses = new LinkedList<>();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Add a new course to courses in this schedule, if there is no conflict with existing courses
     * When conflict, throws ScheduleConflictException
     * @param courseToAdd a course that is to be added
     */
    public void add(Course courseToAdd) {
        for(Course c : courses){
            if(c.conflictsWith(courseToAdd))
                throw new ScheduleConflictException(c, courseToAdd);
        }
        courses.add(courseToAdd);
    }

    /**
     * Implemented for testing purpose
     * Returns all courses in this schedule as a separate copy
     * @return all courses in this schedule
     */
    public List<Course> getAllCourses(){
        return new LinkedList<>(courses);
    }

    /**
     * Clone this following the contract of clone from the Java API
     * @return Returns a copy of the Schedule object
     */
    public Schedule clone() {
        try {
            //Need to do deep copying
            Schedule copy = (Schedule) super.clone();
            copy.courses = new LinkedList<>(courses);
            return copy;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * Gets the course happening in given day and time
     * @param day
     * @param time
     * @return the course that is in session
     */
    public Course getCourse(Weekday day, Time time){
        for(Course c: courses){
            if(c.contains(day, time)) return c;
        }
        return null;
    }

    /**
     * Removes all courses if it conflicts with given day and time
     * @param day
     * @param time
     */
    public void remove(Weekday day, Time time){
        for(Course c: courses){
            if(c.contains(day, time)){
                courses.remove(c);
                return;
            }
        }
    }

    /**
     * Calculates the total credit of all courses in this schedule
     * @return the total credits
     */
    public int totalCredits(){
        int totalCredits = 0;
        for(Course c: courses){
            totalCredits += c.getCredits();
        }
        return totalCredits;
    }

    /**
     * Saves all the courses sorted according to the comparator to an output stream
     * @param out
     * @param comparator
     */
    public void save(PrintStream out, Comparator<Course> comparator){
        Collections.sort(courses, comparator);
        for(Course c: courses){
            out.println(c);
        }
    }
}
