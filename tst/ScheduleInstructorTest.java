import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

/**
 * Created by mhan on 12/2/2016.
 */
public class ScheduleInstructorTest {

    public static final String[] courseStringsSave = {
            "EGR 111,2,T,11:00 AM,120",
            "EGR 777,2,F,11:00 AM,100",
            "EGR 222,3,MWF,05:00 PM,60",
            "EGR 333,4,MWF,04:30 PM,30",
            "EGR 444,1,R,12:00 PM,60",
            "EGR 555,2,RF,02:00 AM,15",
            "EGR 666,3,T,05:00 PM,60",
    };

    private Course buildCourseHelper(String courseString){
        String[] tokens = courseString.split(",");
        return new Course(tokens[0],
                Integer.parseInt(tokens[1]),
                CourseInstructorTest.toEnumSet(tokens[2]),
                Time.fromString(tokens[3]), Integer.parseInt(tokens[4]));
    }

    private Schedule buildScheduleHelper(String[] courseStrings){
        Schedule s = new Schedule();
        for(String str : courseStrings){
            try{
                s.add(buildCourseHelper(str));
            }catch(ScheduleConflictException e){
                Assert.fail("Bug in the test case, courses in courseStrings should not conflict");
            }
        }
        return s;
    }

    @Test
    public void constructorTest(){
        Schedule s = new Schedule();
        Collection<Course> courses = s.getAllCourses();
        Assert.assertTrue(courses != null);
        Assert.assertEquals(0, courses.size());
    }

    @Test
    public void addTest(){
        String[] courseStrings = {"EGR 222,3,MWF,05:00 PM,60",
                                  "EGR 333,3,MWF,04:30 PM,30",
                                  "EGR 444,3,R,05:00 PM,60"
        };
        Course c1 = buildCourseHelper(courseStrings[0]);
        Course c2 = buildCourseHelper(courseStrings[1]);
        Course c3 = buildCourseHelper(courseStrings[2]);

        Schedule s = buildScheduleHelper(courseStrings);
        Collection<Course> courses = s.getAllCourses();
        Assert.assertEquals(3, courses.size());
        Assert.assertTrue(courses.contains(c1));
        Assert.assertTrue(courses.contains(c2));
        Assert.assertTrue(courses.contains(c3));
    }

    private void addNegativeTestHelper(Schedule s, Course conflictCourse){
        try{
            s.add(conflictCourse);
            Assert.fail();
        }catch(ScheduleConflictException e){
        }
    }

    @Test
    public void addNegativeTest(){
        String[] courseStrings = {
                "EGR 222,3,MWF,05:00 PM,60",
                "EGR 333,3,MWF,04:30 PM,30",
                "EGR 444,1,R,12:00 PM,60",
                "EGR 555,2,F,02:00 AM,15"
        };

        Schedule s = buildScheduleHelper(courseStrings);
        Course c3 = buildCourseHelper("EGR 105,3,M,03:30 PM,90");
        Course c4 = buildCourseHelper("EGR 106,3,TR,11:00 AM,90");
        Course c5 = buildCourseHelper("EGR 107,1,WF,12:00 AM,121");
        addNegativeTestHelper(s, c3);
        addNegativeTestHelper(s, c4);
        addNegativeTestHelper(s, c5);
    }

    @Test
    public void cloneTest(){
        Schedule s = new Schedule();
        Course c1 = new Course("EGR 111", 3,
                CourseInstructorTest.toEnumSet("MWF"),
                Time.fromString("05:00 PM"), 60);
        Course c2 = new Course("EGR 222", 3,
                CourseInstructorTest.toEnumSet("T"),
                Time.fromString("05:00 PM"), 60);

        Schedule clone = s.clone();
        Assert.assertFalse(s == clone);
        Course c3 = new Course("EGR 333", 3,
                CourseInstructorTest.toEnumSet("W"),
                Time.fromString("04:00 PM"), 20);
        s.add(c3);

        Collection<Course> courses = clone.getAllCourses();
        Assert.assertFalse(courses.contains(c3));
    }

    @Test
    public void getCourseTest(){
        String[] courseStrings = {
                "EGR 222,3,MWF,05:00 PM,60",
                "EGR 333,3,MWF,04:30 PM,30",
                "EGR 444,1,R,12:00 PM,60",
                "EGR 555,2,RF,02:00 AM,15"
        };

        Schedule s = buildScheduleHelper(courseStrings);
        Course c1 = s.getCourse(Weekday.WEDNESDAY, new Time(5, 15, true));
        Course c2 = buildCourseHelper("EGR 222,3,MWF,05:00 PM,60");
        Assert.assertTrue(c1.equals(c2));

        Course c3 = s.getCourse(Weekday.FRIDAY, new Time(2, 0, false));
        Course c4 = buildCourseHelper("EGR 555,2,RF,02:00 AM,15");
        Assert.assertTrue(c3.equals(c4));

        Course c5 = s.getCourse(Weekday.TUESDAY, new Time(12, 0, false));
        Assert.assertEquals(null, c5);

        Course c6 = s.getCourse(Weekday.THURSDAY, new Time(2, 15, false));
        Assert.assertEquals(null, c6);
    }

    @Test
    public void removeTest(){
        String[] courseStrings = {
                "EGR 222,3,MWF,05:00 PM,60",
                "EGR 333,3,MWF,04:30 PM,30",
                "EGR 444,1,R,12:00 PM,60",
                "EGR 555,2,RF,02:00 AM,15"
        };

        Schedule s = buildScheduleHelper(courseStrings);
        s.remove(Weekday.WEDNESDAY, new Time(5, 15, true));
        Course c1 = buildCourseHelper("EGR 222,3,MWF,05:00 PM,60");
        Assert.assertFalse(s.getAllCourses().contains(c1));

        s.remove(Weekday.THURSDAY, new Time(2, 15, false));
        Course c2 = buildCourseHelper("EGR 555,2,RF,02:00 AM,15");
        Assert.assertTrue(s.getAllCourses().contains(c2));
    }

    @Test
    public void totalCreditTest(){
        String[] courseStrings = {
                "EGR 222,3,MWF,05:00 PM,60",
                "EGR 333,4,MWF,04:30 PM,30",
                "EGR 444,1,R,12:00 PM,60",
                "EGR 555,2,RF,02:00 AM,15"
        };
        Schedule s = buildScheduleHelper(courseStrings);
        Assert.assertEquals(10, s.totalCredits());
    }

    private void saveTestHelper(String[] courseStrings, String[] expectedSortedResult,
                                Comparator<Course> c, String filename) throws FileNotFoundException{
        Schedule s = buildScheduleHelper(courseStrings);

        Collection<Course> courseCollection = s.getAllCourses();
        List<Course> shuffledCourses = new LinkedList<>();
        shuffledCourses.addAll(courseCollection);
        Collections.shuffle(shuffledCourses); //shuffle the order randomly

        PrintStream fileOutput = new PrintStream(new File(filename));
        s.save(fileOutput, c);

        Scanner fileInput = new Scanner(new File(filename));
        int i = 0;
        while(fileInput.hasNextLine()){
            String actual = fileInput.nextLine();
            Assert.assertTrue(expectedSortedResult[i++].equals(actual));
        }
        fileInput.close();
        fileOutput.close();
    }

    @Test
    public void saveTestWithNameComparator(){


        String[] expectedCourseStrings = {
                "EGR 111,2,T,11:00 AM,120",
                "EGR 222,3,MWF,05:00 PM,60",
                "EGR 333,4,MWF,04:30 PM,30",
                "EGR 444,1,R,12:00 PM,60",
                "EGR 555,2,RF,02:00 AM,15",
                "EGR 666,3,T,05:00 PM,60",
                "EGR 777,2,F,11:00 AM,100",
        };
        try {
            saveTestHelper(courseStringsSave, expectedCourseStrings, new CourseNameComparator(), "tmp1.txt");
        }catch(FileNotFoundException e){
            Assert.fail("Bug in testcode! Should not throw FileNotFoundException");
        }
    }

    @Test
    public void saveTestWithCreditComparator(){

        String[] expectedCourseStrings = {
                "EGR 444,1,R,12:00 PM,60",
                "EGR 111,2,T,11:00 AM,120",
                "EGR 555,2,RF,02:00 AM,15",
                "EGR 777,2,F,11:00 AM,100",
                "EGR 222,3,MWF,05:00 PM,60",
                "EGR 666,3,T,05:00 PM,60",
                "EGR 333,4,MWF,04:30 PM,30",
        };
        try {
            saveTestHelper(courseStringsSave, expectedCourseStrings, new CourseCreditComparator(), "tmp2.txt");
        }catch(FileNotFoundException e){
            Assert.fail("Bug in testcode! Should not throw FileNotFoundException");
        }
    }

    @Test
    public void saveTestWithTimeComparator(){

        String[] expectedCourseStrings = {
                "EGR 555,2,RF,02:00 AM,15",
                "EGR 777,2,F,11:00 AM,100",
                "EGR 111,2,T,11:00 AM,120",
                "EGR 444,1,R,12:00 PM,60",
                "EGR 333,4,MWF,04:30 PM,30",
                "EGR 222,3,MWF,05:00 PM,60",
                "EGR 666,3,T,05:00 PM,60",
        };
        try {
            saveTestHelper(courseStringsSave, expectedCourseStrings, new CourseTimeComparator(), "tmp3.txt");
        }catch(FileNotFoundException e){
            Assert.fail("Bug in testcode! Should not throw FileNotFoundException");
        }
    }
}
