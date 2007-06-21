
package simple.xml.transform;

/**
 * Match the transform to the specified type.
 * 
 * @author Niall Gallagher
 */
public interface Matcher {

    public Transform match(Class type) throws Exception;
}
