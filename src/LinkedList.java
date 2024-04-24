// Written by Logan Kinsella, kinse124

public class LinkedList<T extends Comparable<T>> implements List<T> {
    private Node<T> firstNode;                              // Points to the header node in the headed linked list
    private Node<T> currentNode;                            // Points to the node we are working with - Initialised to firstNode or firstNode.getNext() at the start of required methods
    private Node<T> lastNode;                               // Points to the last node in the List
    private boolean isSorted = true;
    private int numEle;                                     // Keeps track of number of elements in the list
    public LinkedList() {                                   // Constructor
        firstNode = new Node<T>(null);
        currentNode = firstNode;
        lastNode = firstNode;
        numEle = 0;
    }

    public boolean add(T element) {
        if (element == null) {
            return false;
        }
        lastNode.setNext(new Node<T>(element, null));                           // Adds element to the end of the list
        numEle ++;
        if (numEle > 1 && isSorted) {                                                // Checks for isSorted only if list was previously sorted
            isSorted = lastNode.getData().compareTo(element) <= 0;                   // Compares previous lastNode to added element
        }
        lastNode = lastNode.getNext();                                               // Updates lastNode
        return true;
    }

    public boolean add(int index, T element) {
        currentNode = firstNode;
        if (element == null || index > numEle) {
            return false;
        }
        int ind = 0;                                                                                    // Keeps track of index while traversing the list
        while (ind++ < index) {
            currentNode = currentNode.getNext();
        }
        currentNode.setNext(new Node<T>(element, currentNode.getNext()));                               // Adds element at given index

        numEle ++;

        if (numEle > 1 && isSorted) {                                                                   // Checks for isSorted only if list was previously sorted
            isSorted = (currentNode.getData() == null || currentNode.getData().compareTo(element) <= 0) && (currentNode.getNext().getNext() == null || element.compareTo(currentNode.getNext().getNext().getData()) <= 0);      // Compares added value with the previous and the next values (also takes care of null if added at first or last index)
        }

        if (lastNode.getNext() != null) {                                                               // Updates lastNode
            lastNode = lastNode.getNext();
        }
        return true;
    }

    public void clear() {
        firstNode.setNext(null);                                            // Sets the header node to null to clear the list
        numEle = 0;                                                         // Accordingly changes the instance variables
        isSorted = true;
        lastNode = firstNode;
    }

    public T get(int index) {
        currentNode = firstNode;
        if (index >= numEle || index < 0) {                                 // Returns null for an out-of-bounds index
            return null;
        }
        int ind = 0;                                                        // Keeps track of the index
        while (ind++ <= index && currentNode.getNext()!=null) {             // Traverses the list until index is reached
            currentNode = currentNode.getNext();
        }
        return (T) currentNode.getData();                                   // Returns the value at given index
    }

    public int indexOf(T element) {
        currentNode = firstNode;
        if (element != null && !isEmpty()) {
            int index = 0;
            while (currentNode.getNext() != null) {                                         // Traverses through the list to find the given element
                if (element.equals(currentNode.getNext().getData())) {
                    return index;                                                           // Returns the first index at which element is found (if found)
                } else {
                    if (isSorted) {
                        if (currentNode.getNext().getData().compareTo(element) > 0) {       // If list is sorted and a value greater than the element is encountered, the traversal stops and -1 is returned
                            return -1;
                        }
                    }
                    currentNode = currentNode.getNext();
                    index ++;
                }
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        if (lastNode.equals(firstNode)) {                  // if lastNode points to firstNode, which is the header node, the linked list is empty
            return true;
        }
        return false;
    }

    public int size() {
        return numEle;
    }                   // Returns the number of elements in the linked list (excluding the header node)

    public void sort() {
        if (!isSorted && (numEle != 0 || numEle != 1)) {                                            // Sorts list only if not already sorted
            currentNode = firstNode.getNext();
            Node<T> sorted = new Node<>(null);
            while (currentNode != null) {                                                           // Takes all values in the linked list, adds them to the new list in a sorted manner and then sets the current list to the sorted list
                T data = currentNode.getData();                                                     // Stores the data of the current node to compare to previous nodes
                Node<T> ptr, trailer;
                ptr = sorted.getNext();
                trailer = sorted;
                while (ptr != null && ptr.getData().compareTo(data) < 0) {                          // Traverses through the previous nodes to find where to insert the data. Loop breaks at point of insertion
                    trailer = ptr;
                    ptr = ptr.getNext();
                }

                trailer.setNext(new Node<T> (data, ptr));                                           // Inserts the new node at the point where the loop breaks, i.e. at correct position of insertion. Inserts at 0th index if list is null
                currentNode = currentNode.getNext();
            }
            firstNode = sorted;                                                                     // Sets this list to the sorted list (header of this list to header of sorted list)
        }
        if (lastNode.getNext() != null) {                                                           // Sets the last node
            while (lastNode.getNext() != null) {
                lastNode = lastNode.getNext();
            }
        }
        isSorted = true;
    }

    public T remove(int index) {
        currentNode = firstNode;
        if (index >= numEle || index < 0) {                                                         // Fall through case
            return null;
        }
        int ind = 0;
        while (ind++ < index) {                                                                     // Iterates to the node to be removed
            currentNode = currentNode.getNext();
        }
        T removedEle = (T) currentNode.getNext().getData();                                         // Gets to-be removed node
        currentNode.setNext(currentNode.getNext().getNext());                                       // Skips the to-be removed node to remove it
        numEle --;                                                                                  // Reduces the counter for number of elements in the list

        lastNode = firstNode;
        while (lastNode.getNext() != null) {                                                        // Updates lastNode
            lastNode = lastNode.getNext();
        }

        isSorted = true;
        if (numEle > 1) {
            currentNode = firstNode.getNext();
            while (isSorted && currentNode.getNext() != null) {
                isSorted = currentNode.getData().compareTo(currentNode.getNext().getData()) <= 0;   // Updates isSorted
                currentNode = currentNode.getNext();
            }
        }
        return removedEle;
    }

    public void removeDuplicates() {
        if (isEmpty() || numEle == 1) {
            return; // If the list is empty or has only one element, there are no duplicates to remove
        }

        Node<T> current = firstNode.getNext(); // Start from the first element

        // Iterate through the list
        while (current != null && current.getNext() != null) {
            Node<T> runner = current; // Use a runner pointer to compare with subsequent elements

            // Iterate with the runner pointer to check for duplicates
            while (runner.getNext() != null) {
                if (current.getData().compareTo(runner.getNext().getData()) == 0) {
                    runner.setNext(runner.getNext().getNext()); // Remove the duplicate element by skipping it
                    numEle--; // Decrease the number of elements
                } else {
                    runner = runner.getNext(); // Move the runner pointer to the next element
                }
            }

            current = current.getNext(); // Move to the next element
        }
    }

    public void reverse() {
        if (numEle != 0 || numEle != 1) {
            currentNode = firstNode.getNext();
            lastNode = firstNode.getNext();                                                         // Updates lastNode
            Node<T> prevNode = null;
            while (currentNode != null) {
                Node<T> temp = currentNode;                                                         // Stores a copy of current pointer in temp to reverse its direction
                currentNode = currentNode.getNext();                                                // Moves ahead current pointer
                temp.setNext(prevNode);                                                             // Reverses the direction of the temp
                prevNode = temp;                                                                    // Updates previous node
            }
            firstNode.setNext(prevNode);

            isSorted = true;
            currentNode = firstNode.getNext();
            while (isSorted && currentNode.getNext() != null) {                                     // Updates isSorted
                isSorted = currentNode.getData().compareTo(currentNode.getNext().getData()) <= 0;
                currentNode = currentNode.getNext();
            }
        }
    }

    public void exclusiveOr(List<T> otherList) {
        if (otherList == null || otherList.isEmpty()) {
            return; // If the other list is null or empty, no modification needed
        }

        ArrayList<T> result = new ArrayList<>(); // Resulting list for exclusive OR operation

        // Track indices for the current list and other list
        int currentIndex = 0;
        int otherCurrentIndex = 0;

        // Iterate through both lists simultaneously
        while (currentIndex < size() || otherCurrentIndex < otherList.size()) {
            if (currentIndex < size() && otherCurrentIndex < otherList.size()) {
                // Compare elements in both lists
                T currentElement = get(currentIndex);
                T otherCurrentElement = otherList.get(otherCurrentIndex);
                int compareResult = currentElement.compareTo(otherCurrentElement);
                if (compareResult < 0) {
                    // If element in the current list is smaller, add it to the result
                    result.add(currentElement);
                    currentIndex++;
                } else if (compareResult > 0) {
                    // If element in the other list is smaller, add it to the result
                    result.add(otherCurrentElement);
                    otherCurrentIndex++;
                } else {
                    // If elements are equal, move both pointers to the next elements
                    currentIndex++;
                    otherCurrentIndex++;
                }
            } else if (currentIndex < size()) {
                // If other list is exhausted, add remaining elements from the current list to the result
                result.add(get(currentIndex));
                currentIndex++;
            } else {
                // If current list is exhausted, add remaining elements from the other list to the result
                result.add(otherList.get(otherCurrentIndex));
                otherCurrentIndex++;
            }
        }

        // Clear the current list and copy elements from the resulting list
        clear();
        for (int i = 0; i < result.size(); i++) {
            add(result.get(i));
        }
    }




    public T getMin() {
        if (isEmpty()) {
            return null; // Return null if the list is empty
        }

        Node<T> current = firstNode.getNext(); // Start from the first element
        T min = current.getData(); // Initialize min with the data of the first element

        // Iterate through the list to find the minimum element
        while (current != null) {
            if (current.getData().compareTo(min) < 0) { // If current element is smaller than min
                min = current.getData(); // Update min
            }
            current = current.getNext(); // Move to the next element
        }

        return min; // Return the minimum element
    }

    public T getMax() {
        if (isEmpty()) {
            return null; // Return null if the list is empty
        }

        Node<T> current = firstNode.getNext(); // Start from the first element
        T max = current.getData(); // Initialize max with the data of the first element

        // Iterate through the list to find the maximum element
        while (current != null) {
            if (current.getData().compareTo(max) > 0) { // If current element is larger than max
                max = current.getData(); // Update max
            }
            current = current.getNext(); // Move to the next element
        }

        return max; // Return the maximum element
    }

    public String toString() {
        currentNode = firstNode;
        String retStr = "";
        while (currentNode.getNext() != null) {                     // Creates a string representation of the linked list, with each element in a new line
            currentNode = currentNode.getNext();
            retStr = retStr + currentNode.getData() + "\n";
        }
        return retStr;
    }

    public boolean isSorted() {
        if (isEmpty() || numEle == 1) {
            return true; // If the list is empty or has only one element, it is considered sorted
        }

        Node<T> current = firstNode.getNext(); // Start from the first element

        // Iterate through the list
        while (current != null && current.getNext() != null) {
            // If any adjacent elements are out of order, the list is not sorted
            if (current.getData().compareTo(current.getNext().getData()) > 0) {
                return false; // Return false if out of order
            }

            current = current.getNext(); // Move to the next element
        }

        return true; // If all elements are in order, return true
    }

}