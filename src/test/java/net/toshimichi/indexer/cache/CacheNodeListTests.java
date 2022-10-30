package net.toshimichi.indexer.cache;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CacheNodeListTests {

    public <T> void assertList(CacheNodeList<T> list, List<T> values) {
        CacheNode<T> node = list.head();
        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i), node.value());

            if (i == values.size() - 1) {
                assertEquals(list.tail(), node);
            }
            node = node.next;
        }
    }

    @Test
    public void testSwap() {
        CacheNodeList<String> list = new CacheNodeList<>();
        CacheNode<String> s1 = list.addLast("1");
        CacheNode<String> s2 = list.addLast("2");
        list.swap(s1, s2);
        assertList(list, List.of("2", "1"));

        CacheNode<String> s3 = list.addLast("3");
        CacheNode<String> s4 = list.addLast("4");
        CacheNode<String> s5 = list.addAfter("5", s1);
        list.swap(s1, s5);
        assertList(list, List.of("2", "5", "1", "3", "4"));

        list.swap(s1, s5);
        assertList(list, List.of("2", "1", "5", "3", "4"));

        list.swap(s1, s3);
        assertList(list, List.of("2", "3", "5", "1", "4"));

        list.swap(s1, s3);
        assertList(list, List.of("2", "1", "5", "3", "4"));

        list.swap(s2, s4);
        assertList(list, List.of("4", "1", "5", "3", "2"));

        list.swap(s2, s4);
        assertList(list, List.of("2", "1", "5", "3", "4"));
    }

    @Test
    public void testAdd() {
        CacheNodeList<String> list = new CacheNodeList<>();
        CacheNode<String> s1 = list.addLast("1");
        CacheNode<String> s2 = list.addFirst("2");
        assertList(list, List.of("2", "1"));

        CacheNode<String> s3 = list.addAfter("3", s2);
        assertList(list, List.of("2", "3", "1"));

        CacheNode<String> s4 = list.addAfter("4", s1);
        assertList(list, List.of("2", "3", "1", "4"));
    }

    @Test
    public void testAdd0() {
        CacheNodeList<String> list = new CacheNodeList<>();
        CacheNode<String> s2 = list.addFirst("2");
        CacheNode<String> s1 = list.addLast("1");
        assertList(list, List.of("2", "1"));

        CacheNode<String> s3 = list.addBefore("3", s1);
        assertList(list, List.of("2", "3", "1"));

        CacheNode<String> s4 = list.addBefore("4", s2);
        assertList(list, List.of("4", "2", "3", "1"));
    }

    @Test
    public void testMoveTo() {
        CacheNodeList<String> list = new CacheNodeList<>();
        CacheNode<String> s1 = list.addLast("1");
        CacheNode<String> s2 = list.addLast("2");
        CacheNode<String> s3 = list.addLast("3");

        list.moveToHead(s2);
        assertList(list, List.of("2", "1", "3"));

        list.moveToHead(s3);
        assertList(list, List.of("3", "2", "1"));

        list.moveToTail(s2);
        assertList(list, List.of("3", "1", "2"));

        list.moveToTail(s3);
        assertList(list, List.of("1", "2", "3"));
    }
}
