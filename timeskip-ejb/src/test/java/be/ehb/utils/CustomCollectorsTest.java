package be.ehb.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Guillaume Vandecasteele/Christophe Devos
 * @since 2017
 */
public class CustomCollectorsTest {

    @Test
    public void getSingleResult() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(5);
        int singleResult = integerList.stream().collect(CustomCollectors.getSingleResult());
        assertEquals(5, singleResult);
    }

    @Test
    public void getSingleResultNull() {
        List<Integer> integerList = new ArrayList<>();

        Object singleResult = integerList.stream().collect(CustomCollectors.getSingleResult());
        assertEquals(null, singleResult);
    }


    @Test(expected = IllegalStateException.class)
    public void getSingleResultListToBig() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(5);
        integerList.add(6);
        integerList.stream().collect(CustomCollectors.getSingleResult());
    }


    @Test
    public void getFirstResult() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(5);
        int singleResult = integerList.stream().collect(CustomCollectors.getFirstResult());
        assertEquals(5, singleResult);
    }

    @Test
    public void getFirstResultEmptyList() {
        List<Integer> integerList = new ArrayList<>();
        Object singleResult = integerList.stream().collect(CustomCollectors.getFirstResult());
        assertEquals(null, singleResult);
    }


    @Test
    public void getFirstResultBiggerList() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(5);
        integerList.add(6);
        int singleResult = integerList.stream().collect(CustomCollectors.getFirstResult());
        assertEquals(5, singleResult);
    }

}