package Homework.src.six.hard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SafeList<T> implements List<T> {
    private List<T> list;

    public SafeList() {
        list = new ArrayList<>();
    }

    @Override
    public boolean add(T element) {
        if (element == null || list.contains(element)) {
            return false;
        }
        return list.add(element);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(java.util.Collection<? extends T> c) {
        boolean modified = false;
        for (T element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, java.util.Collection<? extends T> c) {
        boolean modified = false;
        for (T element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T set(int index, T element) {
        if (element == null || list.contains(element)) {
            return null;
        }
        return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        if (element == null || list.contains(element)) {
            return;
        }
        list.add(index, element);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public java.util.ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public java.util.ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public java.util.List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
