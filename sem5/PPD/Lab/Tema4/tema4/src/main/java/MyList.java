import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MyList {
    private Node root;

    public MyList() {
        this.root = null;
    }

    synchronized public void add(Node node) {
        if (root == null || node.getExponent() > root.getExponent()) {
            node.setNext(root);
            root = node;
        } else {
            Node prev = null;
            Node crt = root;
            while (crt.getNext() != null && crt.getNext().getExponent() >= node.getExponent()) {
                prev = crt;
                crt = crt.getNext();
            }

            if (crt.getExponent() == node.getExponent()) {
                // there is a node with the same exponent
                crt.setCoefficient(crt.getCoefficient() + node.getCoefficient());

                if (crt.getCoefficient() == 0) {
                    // after calculation, coefficient is 0, so we should delete the node
                    if (prev == null) { // crt is root
                        root = crt.getNext();
                    } else {
                        prev.setNext(crt.getNext());
                    }
                }
            } else {
                // we should add a new node
                node.setNext(crt.getNext());
                crt.setNext(node);
            }
        }
    }

    synchronized public void printList(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Node crt = root;
            while (crt != null) {
                writer.write("exponent: " + crt.getExponent() + " - coefficient: " + crt.getCoefficient());
                writer.newLine();
                crt = crt.getNext();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
