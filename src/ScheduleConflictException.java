/**
 * Created by mhan on 10/16/2016.
 * A ScheduleConflictException object is a runtime exception
 * that indicates the client has attempted to introduce a conflict into a course schedule
 */
public class ScheduleConflictException extends RuntimeException {
    /**
     * Constructor that takes two Course objects. Assumes both of them are not null
     * Creates an appropriate error message
     * @param c1 course 1
     * @param c2 course 2
     */
    public ScheduleConflictException(Course c1, Course c2){
        super(c1.toString()+ " and " + c2.toString() + " have day and time conflict");
    }
}
