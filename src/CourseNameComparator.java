import java.util.Comparator;

/**
 * Created by mhan on 10/16/2016.
 */
public class CourseNameComparator implements Comparator<Course>{

    @Override
    public int compare(Course o1, Course o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
