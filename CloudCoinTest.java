

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class CloudCoinTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class CloudCoinTest
{
    /**
     * Default constructor for test class CloudCoinTest
     */
    public CloudCoinTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testgetDenomination()
    {
        CloudCoin cloudCoi1 = new CloudCoin("w:/Code/Java/CloudCoinCore2/bank/1.CloudCoin.1.127002.test");
        assertEquals(1, cloudCoi1.getDenomination());
    }
}

