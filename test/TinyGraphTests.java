import com.tiny.graphlib.Graph;
import com.tiny.graphlib.ItemExistsException;
import com.tiny.graphlib.ItemNotFoundException;
import com.tiny.graphlib.iGraph;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class TinyGraphTests {

    @Test
    public void vertexes() throws ItemExistsException {
        iGraph gt = new Graph<String>("Test");

        gt.addVertex("v1");
        gt.addVertex("V1");
        gt.addVertex("");

        assertEquals(gt.getVertices().size(), 3);
        assertTrue(gt.getVertices().contains("v1"));
        assertTrue(gt.getVertices().contains("V1"));
        assertTrue(gt.getVertices().contains(""));
        assertThrows(() -> gt.addVertex("v1"));

        gt.addVertex("v2");

        assertThrows(() -> gt.addVertex("V1"));
    }

    @Test
    public void edges() throws ItemExistsException, ItemNotFoundException {
        iGraph gt = new Graph<String>("Fest");

        gt.addVertex("A");
        gt.addVertex("B");
        gt.addVertex("C");

        assertEquals(gt.getVertices().size(), 3);

        gt.addEdge("A", "A");
        gt.addEdge("A", "B");
        gt.addEdge("A", "C");

        assertThrows(ItemExistsException.class, () -> gt.addEdge("A", "A"));
        assertThrows(ItemExistsException.class, () -> gt.addEdge("B", "A"));
        assertThrows(ItemExistsException.class, () -> gt.addEdge("C", "A"));
        assertThrows(ItemNotFoundException.class, () -> gt.addEdge("A", ""));

        gt.addEdge("B", "C");
        gt.addEdge("B", "B");
        gt.addEdge("C", "C");

        assertEquals(gt.getVertices().size(), 3);
    }

    @Test
    public void paths() throws ItemExistsException, ItemNotFoundException {
        iGraph gt = new Graph<String>("Best");

        gt.addVertex("v1");

        assertNull(gt.getPath("v1", "v1"));
        assertThrows(ItemNotFoundException.class, () -> gt.getPath("v1", "v2"));

        gt.addEdge("v1", "v1");

        assertNotNull(gt.getPath("v1", "v1"));

        gt.addVertex("v2");

        assertNull(gt.getPath("v1", "v2"));

        gt.addEdge("v1", "v2");
        gt.addVertex("v2.1");
        gt.addEdge("v1", "v2.1");
        gt.addEdge("v2.1", "v2");

        List path = gt.getPath("v1", "v2");

        assertNotNull(path);
        assertEquals(path.size(), 2);
        assertEquals(path.get(0), "v1");
        assertEquals(path.get(1), "v2");

        assertEquals(gt.getVertices().size(), 3);
    }

    @Test
    public void cycles() throws ItemExistsException, ItemNotFoundException {
        iGraph gt = new Graph<String>("Cycling");

        gt.addVertex("v1");
        gt.addVertex("v2");
        gt.addVertex("v2.1");
        gt.addVertex("v2.2");
        gt.addVertex("v3");

        gt.addEdge("v1", "v2");
        gt.addEdge("v2", "v3");
        gt.addEdge("v2", "v2.1");
        gt.addEdge("v2.1", "v2.2");
        gt.addEdge("v2.2", "v2");
        gt.addEdge("v3", "v1");

        List path = gt.getPath("v1", "v3");

        assertNotNull(path);
        assertEquals(path.size(), 2);
        assertEquals(path.get(0), "v1");
        assertEquals(path.get(1), "v3");

        path = gt.getPath("v3", "v1");

        assertNotNull(path);
        assertEquals(path.size(), 2);
        assertEquals(path.get(0), "v3");
        assertEquals(path.get(1), "v1");

        path = gt.getPath("v3", "v2.1");

        assertNotNull(path);
        assertEquals(path.size(), 3);
        assertEquals(path.get(0), "v3");
        assertEquals(path.get(1), "v2");
        assertEquals(path.get(2), "v2.1");

        path = gt.getPath("v1", "v1");
        assertNotNull(path);
        assertEquals(path.size(), 3);

        assertEquals(gt.getVertices().size(), 5);
    }

    @Test
    public void helpers() throws ItemExistsException, ItemNotFoundException {
        iGraph gt = new Graph<String>("Random");

        gt.addVertex("A");
        gt.addVertex("B");
        gt.addVertex("C");
        gt.addVertex("D");

        gt.addEdge("A", "D");
        gt.addEdge("C", "A");
        gt.addEdge("B", "D");
        gt.addEdge("B", "A");

        assertNotNull(gt.toString());
        assertNotNull(gt.getPath("A", "D").toString());
    }

    @Test
    public void directed() throws ItemExistsException, ItemNotFoundException {
        iGraph gt = new Graph<String>("Dir", true);

        gt.addVertex("A");
        gt.addVertex("B");

        gt.addEdge("A", "B");

        assertNotNull(gt.getPath("A", "B"));
        assertNull(gt.getPath("B", "A"));

        gt.addEdge("B", "A");

        List path = gt.getPath("A", "A");

        assertNotNull(path);
        assertEquals(path.size(), 3);
        assertEquals(path.get(0), "A");
        assertEquals(path.get(1), "B");
        assertEquals(path.get(2), "A");
    }
}
