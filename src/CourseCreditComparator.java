import java.util.Comparator;

/**
 * Created by mhan on 10/16/2016.
 */
public class CourseCreditComparator implements Comparator<Course> {
    @Override
    public int compare(Course o1, Course o2) {
        int diffCredits = o1.getCredits() - o2.getCredits();
        if (diffCredits == 0) {
            return (new CourseNameComparator().compare(o1, o2));
        }else{
            return diffCredits;
        }
    }
}
