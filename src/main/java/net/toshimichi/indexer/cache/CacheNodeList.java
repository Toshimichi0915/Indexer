package net.toshimichi.indexer.cache;

class CacheNodeList<E> implements CacheManipulator<E> {

    private CacheNode<E> head;
    private CacheNode<E> tail;
    private int size;

    public CacheNode<E> head() {
        return head;
    }

    public CacheNode<E> tail() {
        return tail;
    }

    public int size() {
        return size;
    }

    public void addLast(CacheNode<E> node) {
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }

        tail = node;
        size++;
    }

    public CacheNode<E> addLast(E value) {
        CacheNode<E> node = new CacheNode<>(value);
        addLast(node);
        return node;
    }

    public void addFirst(CacheNode<E> node) {
        if (tail == null) {
            tail = node;
        } else {
            head.prev = node;
            node.next = head;
        }

        head = node;
        size++;
    }

    public CacheNode<E> addFirst(E value) {
        CacheNode<E> node = new CacheNode<>(value);
        addFirst(node);
        return node;
    }

    public void addAfter(CacheNode<E> node, CacheNode<E> prev) {
        node.next = prev.next;
        node.prev = prev;
        prev.next = node;

        if (node.next == null) {
            tail = node;
        } else {
            node.next.prev = node;
        }

        size++;
    }

    public CacheNode<E> addAfter(E value, CacheNode<E> prev) {
        CacheNode<E> node = new CacheNode<>(value);
        addAfter(node, prev);
        return node;
    }

    public void addBefore(CacheNode<E> node, CacheNode<E> next) {
        node.prev = next.prev;
        node.next = next;
        next.prev = node;

        if (node.prev == null) {
            head = node;
        } else {
            node.prev.next = node;
        }

        size++;
    }

    public CacheNode<E> addBefore(E value, CacheNode<E> next) {
        CacheNode<E> node = new CacheNode<>(value);
        addBefore(node, next);
        return node;
    }

    @Override
    public void swap(CacheNode<E> n1, CacheNode<E> n2) {
        if (n1 == n2) {
            return;
        }

        if (n1.next == n2) {
            moveAfter(n1, n2);
        } else if (n1.prev == n2) {
            moveBefore(n1, n2);
        } else {
            CacheNode<E> n1p = n1.prev;
            CacheNode<E> n1n = n1.next;
            CacheNode<E> n2p = n2.prev;
            CacheNode<E> n2n = n2.next;

            n1.prev = n2p;
            n1.next = n2n;
            n2.prev = n1p;
            n2.next = n1n;

            if (n1p != null) {
                n1p.next = n2;
            } else {
                head = n2;
            }

            if (n1n != null) {
                n1n.prev = n2;
            } else {
                tail = n2;
            }

            if (n2p != null) {
                n2p.next = n1;
            } else {
                head = n1;
            }

            if (n2n != null) {
                n2n.prev = n1;
            } else {
                tail = n1;
            }
        }
    }

    @Override
    public void moveAfter(CacheNode<E> node, CacheNode<E> dest) {
        if (node == dest) {
            throw new IllegalArgumentException("node and dest are the same");
        }
        remove(node);
        addAfter(node, dest);
    }

    @Override
    public void moveBefore(CacheNode<E> node, CacheNode<E> dest) {
        if (node == dest) {
            throw new IllegalArgumentException("node and dest are the same");
        }
        remove(node);
        addBefore(node, dest);
    }

    @Override
    public void moveToTail(CacheNode<E> node) {
        if (node == tail) return;
        remove(node);
        addLast(node);
    }

    @Override
    public void moveToHead(CacheNode<E> node) {
        if (node == head) return;
        remove(node);
        addFirst(node);
    }

    public void remove(CacheNode<E> node) {
        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        node.prev = null;
        node.next = null;
        size--;
    }
}
