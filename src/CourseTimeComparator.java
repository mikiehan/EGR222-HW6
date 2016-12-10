import java.util.Comparator;

/**
 * Created by mhan on 10/16/2016.
 */
public class CourseTimeComparator implements Comparator<Course>{
    /**
     * startTime, endTime, name
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Course o1, Course o2) {
        int startTimeDiff = o1.getStartTime().compareTo(o2.getStartTime());
        if (startTimeDiff != 0) return startTimeDiff;
        int endTimeDiff = o1.getEndTime().compareTo(o2.getEndTime());
        if (endTimeDiff != 0) return endTimeDiff;
        return (new CourseNameComparator().compare(o1, o2));
    }
}
