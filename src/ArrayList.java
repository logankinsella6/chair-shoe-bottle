// Written by Logan Kinsella, kinse124

public class ArrayList<T extends Comparable<T>> implements List<T> {

    private T[] elements;
    private int count;
    private boolean isSorted;

    // Constructor: Initializes an empty ArrayList with a default capacity of 2,
    // sets the count to 0, and sets isSorted to true.
    public ArrayList() {
        elements = (T[]) new Comparable[2];
        count = 0;
        isSorted = true;
    }

    // Private helper method: Checks if the ArrayList is sorted in ascending order
    // and returns true if it is, otherwise returns false.
    private boolean checkIsSorted() {
        boolean check = true;
        for (int k = 1; k < count; k++) {
            check &= elements[k].compareTo(elements[k - 1]) >= 0;
        }
        return check;
    }

    // Private helper method: Grows the capacity of the ArrayList by doubling its
    // current size.
    private void grow() {
        T[] updatedElements = (T[]) new Comparable[elements.length * 2];
        for (int i = 0; i < count; i++) {
            updatedElements[i] = elements[i];
        }
        elements = updatedElements;
    }

    // Adds a new element to the end of the ArrayList. Returns true if the element
    // was added successfully, otherwise returns false
    public boolean add(T element) {
        if (element == null) {
            return false;
        }
        if (count == elements.length) {
            grow();
        }
        if (count > 0 && isSorted && element.compareTo(elements[count - 1]) < 0) {
            isSorted = false;
        }
        elements[count++] = element;
        isSorted = checkIsSorted();
        return true;
    }

    // Inserts a new element at the specified position in the ArrayList. Returns
    // true if the element was added successfully, otherwise returns false
    public boolean add(int position, T element) {
        if (element == null || position < 0 || position >= count) {
            return false;
        }
        if (count == elements.length) {
            grow();
        }

        for (int i = count; i > position; i--) {
            elements[i] = elements[i - 1];
        }
        elements[position] = element;
        count++;

        if (isSorted) {
            if ((position > 0 && element.compareTo(elements[position - 1]) < 0) ||
                    (position < count - 1 && element.compareTo(elements[position + 1]) > 0)) {
                isSorted = false;
            }
        }
        isSorted = checkIsSorted();
        return true;
    }

    // Removes all elements from the ArrayList, resets the capacity to the default
    // value, and sets isSorted to true.
    public void clear() {
        elements = (T[]) new Comparable[2];
        count = 0;
        isSorted = true;
    }

    // Retrieves the element at the specified position in the ArrayList. Returns the
    // element if the position is valid, otherwise returns null.
    public T get(int index) {
        if (index < 0 || index >= count) {
            return null;
        }
        return elements[index];
    }

    // Finds the index of the first occurrence of the specified value in the
    // ArrayList. Returns the index if the value is found, otherwise returns -1.
    public int indexOf(T element) {
        if (element == null) {
            return -1;
        }

        int index = -1;

        for (int i = 0; i < count; i++) {
            if (elements[i].equals(element)) {
                if (isSorted) {
                    if (i == 0 || !elements[i - 1].equals(element)) {
                        return i;
                    }
                } else {
                    return i;
                }
                index = i;
            }
        }

        return index;
    }

    // Checks if the ArrayList is empty. Returns true if the ArrayList has no
    // elements, otherwise returns false.
    public boolean isEmpty() {
        return count == 0;
    }

    // Returns the current number of elements in the ArrayList.
    public int size() {
        return count;
    }

    // Sorts the elements of the ArrayList in ascending order using the insertion
    // sort algorithm.
    public void sort() {
        if (!isSorted) {
            for (int i = 1; i < count; i++) {
                T currentElement = elements[i];
                int prevIndex = i - 1;
                while (prevIndex >= 0 && elements[prevIndex].compareTo(currentElement) > 0) {
                    elements[prevIndex + 1] = elements[prevIndex];
                    prevIndex--;
                }
                elements[prevIndex + 1] = currentElement;
            }
            isSorted = true;
        }
    }

    // Removes the element at the specified position in the ArrayList. Returns the
    // removed element if the position is valid, otherwise returns null.
    public T remove(int index) {
        if (index < 0 || index >= count) {
            return null;
        }
        T removedElement = elements[index];

        for (int i = index; i < count - 1; i++) {
            elements[i] = elements[i + 1];
        }

        count--;

        if (isSorted && index > 1 && index < count) {
            T prevElement = elements[index - 1];
            T currentElement = elements[index];

            if (currentElement.compareTo(prevElement) < 0) {
                isSorted = false;
            }
        }
        isSorted = checkIsSorted();
        return removedElement;
    }

    public void removeDuplicates() {
        // Create a temporary list to store unique elements
        ArrayList<T> uniqueList = new ArrayList<>();

        // Iterate over the elements in the current list
        for (int i = 0; i < count; i++) {
            T element = elements[i];
            boolean isDuplicate = false;

            // Check if the current element is already present in the unique list
            for (int j = 0; j < uniqueList.size(); j++) {
                if (element.equals(uniqueList.get(j))) {
                    isDuplicate = true;
                    break;
                }
            }

            // If the element is not a duplicate, add it to the unique list
            if (!isDuplicate) {
                uniqueList.add(element);
            }
        }

        // Clear the current list
        clear();

        // Add all unique elements back to the current list
        for (int i = 0; i < uniqueList.size(); i++) {
            add(uniqueList.get(i));
        }
    }

    // Reverses the order of the elements in the ArrayList.
    public void reverse() {
        int leftIndex = 0;
        int rightIndex = count - 1;

        while (leftIndex < rightIndex) {
            T temp = elements[leftIndex];
            elements[leftIndex] = elements[rightIndex];
            elements[rightIndex] = temp;

            leftIndex++;
            rightIndex--;
        }

        if (count > 1) {
            isSorted = false;
        }
        isSorted = checkIsSorted();
    }


    public void exclusiveOr(List<T> otherList) {
        if (otherList == null || otherList.isEmpty()) {
            return;
        }

        ArrayList<T> result = new ArrayList<>();

        int currentIndex = 0;
        int otherCurrentIndex = 0;

        while (currentIndex < size() || otherCurrentIndex < otherList.size()) {
            if (currentIndex < size() && otherCurrentIndex < otherList.size()) {
                T currentElement = get(currentIndex);
                T otherCurrentElement = otherList.get(otherCurrentIndex);
                int compareResult = currentElement.compareTo(otherCurrentElement);
                if (compareResult < 0) {
                    result.add(currentElement);
                    currentIndex++;
                } else if (compareResult > 0) {
                    result.add(otherCurrentElement);
                    otherCurrentIndex++;
                } else {
                    currentIndex++;
                    otherCurrentIndex++;
                }
            } else if (currentIndex < size()) {
                result.add(get(currentIndex));
                currentIndex++;
            } else {
                result.add(otherList.get(otherCurrentIndex));
                otherCurrentIndex++;
            }
        }

        clear();
        for (int i = 0; i < result.size(); i++) {
            add(result.get(i));
        }
    }

    // Gets minimum element
    public T getMin() {
        if (isEmpty()) {
            return null;
        }
        T min = elements[0];
        for (int i = 1; i < count; i++) {
            if (elements[i].compareTo(min) < 0) {
                min = elements[i];
            }
        }
        return min;
    }

    //Gets maximum element
    public T getMax() {
        if (isEmpty()) {
            return null;
        }
        T max = elements[0];
        for (int i = 1; i < count; i++) {
            if (elements[i].compareTo(max) > 0) {
                max = elements[i];
            }
        }
        return max;
    }

    // Returns true if the ArrayList is sorted in ascending order, otherwise returns
    // false.
    public boolean isSorted() {
        return isSorted;
    }

    // Creates a String representation of the array, with each element in a new line
    public String toString() {
        String retStr = "";
        for (int i = 0; i < count; i++) {
            retStr += elements[i] + "\n";
        }
        return retStr;
    }
}