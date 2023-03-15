import java.util.Objects;

/**
 * Dataclass for a sorted linked list
 */
class SortedLinkedList {
    Node head;
    private int currentPosition = 0;

    class Node {
        String data;
        int position;
        Node next;

        Node(String data) {
            currentPosition++;
            this.data = data;
            this.position = currentPosition;
            next = null;
        }
    }

    /**
     * Inserts a string to the linked list
     * @param data string to be inserted
     */
    void sortedInsert(String data) {
        if (findPosition(data) != -1) return;

        Node new_node = newNode(data);
        Node current;

        if (head == null || head.data.compareTo(new_node.data) >= 0) {
            new_node.next = head;
            head = new_node;
        } else {
            current = head;

            while (current.next != null && current.next.data.compareTo(new_node.data) < 0) {
                current = current.next;
            }

            new_node.next = current.next;
            current.next = new_node;
        }
    }

    /**
     * Finds the position of an element
     * @param item the string to be found
     * @return returns the position, or -1 if the string is not found
     */
    int findPosition(String item) {
        Node current = head;
        while (current != null) {
            if (Objects.equals(current.data, item)) {
                return current.position;
            }
            current = current.next;
        }
        return -1;
    }

    /**
     * Print function for linked list
     */
    void printList() {
        Node current = head;
        System.out.println("Poz \tAtom");
        while (current != null) {
            System.out.println(current.position + "\t\t" + current.data);
            current = current.next;
        }
        System.out.println("\n\n");
    }

    /**
     * Creates a new node with the given data
     * @param data string data of the node
     * @return the new Node
     */
    Node newNode(String data) {
        return new Node(data);
    }
}